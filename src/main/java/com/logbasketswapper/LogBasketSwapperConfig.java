package com.logbasketswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("log-basket-swapper")
public interface LogBasketSwapperConfig extends Config
{
  @ConfigSection(
    name = "Sawmill Locations",
    description = "Enable/disable the plugin at specific sawmill locations",
    position = 0
  )
  String sawmillSection = "sawmillSection";

	@ConfigItem(
		keyName = "lumberYard",
		name = "Lumber Yard",
		description = "Enable at Lumber Yard sawmill",
	   position = 1,
     section = sawmillSection
	)
	default boolean lumberYard() {
	   return true;
	}

	@ConfigItem(
		keyName = "woodcuttingGuild",
		name = "Woodcutting Guild",
		description = "Enable at Woodcutting Guild sawmill",
	   position = 2,
     section = sawmillSection
	)
	default boolean woodcuttingGuild() {
	   return true;
	}

	@ConfigItem(
		keyName = "prifddinas",
		name = "Prifddinas",
		description = "Enable at Prifddinas sawmill",
	   position = 3,
     section = sawmillSection
	)
	default boolean prifddinas() {
	   return true;
	}

	@ConfigItem(
		keyName = "auburnvale",
		name = "Auburnvale",
		description = "Enable at Auburnvale sawmill",
	   position = 4,
     section = sawmillSection
	)
	default boolean auburnvale() {
	   return true;
	}
}
