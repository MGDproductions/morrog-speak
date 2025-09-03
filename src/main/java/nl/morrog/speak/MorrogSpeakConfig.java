package nl.morrog.speak;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("morrog.speak")
public interface MorrogSpeakConfig extends Config
{
	@Range(min = 1, max = 100)
	@ConfigItem(
		keyName = "volume",
		name = "Volume",
		description = "Adjust the volume for quest text to speech.",
		position = 1)
	default int volume() {
		return 100;
	}

	@ConfigItem(
		keyName = "mute",
		name = "Mute",
		description = "Mute quest text to speech.",
		position = 2
	)
	default boolean mute()
	{
		return false;
	}
}
