package nl.morrog.speak;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MorrogSpeakPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(MorrogSpeakPlugin.class);
		RuneLite.main(args);
	}
}