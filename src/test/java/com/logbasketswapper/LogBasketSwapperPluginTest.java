package com.logbasketswapper;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class LogBasketSwapperPluginTest
{
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(LogBasketSwapperPlugin.class);
		RuneLite.main(args);
	}
}
