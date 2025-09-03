package nl.morrog.speak;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.WidgetClosed;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import jaco.mp3.player.MP3Player;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@PluginDescriptor(
	name = "Morrog Speak"
)
public class MorrogSpeakPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private MorrogSpeakConfig config;

	private String playerName = null;
	private String server = "https://rune.mgdproductions.com/audio";
	private MP3Player audioPlayer = new MP3Player();
	private int lastAudioStartTime = 0;

	@Override
	protected void startUp() throws Exception
	{
		log.info("MorrogSpeak started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("MorrogSpeak stopped!");
	}

	@Subscribe
	public void onChatMessage(ChatMessage message)
	{
		if (message.getType().equals(ChatMessageType.DIALOG)) {
			String characterName = message.getMessage().split("\\|", 2)[0];
			String text = message.getMessage().split("\\|", 2)[1];

			if (playerName == null){
				playerName = client.getLocalPlayer().getName();
			}

			if (characterName.equals(playerName)){
				characterName = "Player";
			}

			String urlString = server + "?character=" + URLEncoder.encode(characterName, StandardCharsets.UTF_8) + "&text=" + URLEncoder.encode(text, StandardCharsets.UTF_8);

			audioPlayer.clearPlayList();
			audioPlayer.stop();
			audioPlayer.setVolume(config.mute() ? 0 : config.volume());

			try {
				URL audioUrl = new URL(urlString);

				audioPlayer.add(audioUrl);
				audioPlayer.play();

				lastAudioStartTime = client.getTickCount();
			} catch (MalformedURLException e) {
				log.error("MorrogSpeak could not play audio file");
			}
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (event.getMenuOption().equals("Continue") && client.getTickCount() > lastAudioStartTime + 1)
		{
			audioPlayer.stop();
		}
	}

	@Subscribe
	public void onWidgetClosed(WidgetClosed widgetClosed)
	{
		if ((widgetClosed.getGroupId() == InterfaceID.DIALOG_NPC ||
				widgetClosed.getGroupId() == InterfaceID.DIALOG_PLAYER ||
				widgetClosed.getGroupId() == InterfaceID.DIALOG_OPTION
			) && client.getTickCount() > lastAudioStartTime + 1)
		{
			audioPlayer.stop();
		}
	}

	@Provides
	MorrogSpeakConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MorrogSpeakConfig.class);
	}
}
