package com.logbasketswapper;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;

import com.google.inject.Provides;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.KeyCode;
import net.runelite.api.Menu;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.PostMenuSort;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
  name = "Log Basket Swapper",
  description = "Swaps the left-click option on the Log Basket to \"Empty\" when near a sawmill",
  tags = {"sawmill", "log", "basket", "swap", "swapper", "menu", "auburnvale", "prifddinas", "lumber", "yard", "woodcutting", "guild", "planks", "foresty"}
)
public class LogBasketSwapperPlugin extends Plugin {
  private static final int LUMBER_YARD_REGION = 13110;
  private static final int WOODCUTTING_GUILD_REGION = 6454;
  private static final int PRIFDDINAS_REGION = 13151;
  private static final int AUBURNVALE_REGION = 5428;

  private static final int[] LOG_BASKETS = {
    ItemID.LOG_BASKET_OPEN,
    ItemID.LOG_BASKET_CLOSED,
  };

  private static final int[] FORESTRY_BASKETS = {
    ItemID.FORESTRY_BASKET_OPEN,
    ItemID.FORESTRY_BASKET_CLOSED,
  };

  @Inject
  private Client client;

  @Inject
  private LogBasketSwapperConfig config;

  @Override
  protected void startUp() throws Exception {
    log.debug("Log Basket Swapper started!");
  }

  @Override
  protected void shutDown() throws Exception {
    log.debug("Log Basket Swapper stopped!");
  }

  private boolean isAtEnabledSawmill(int regionID) {
    switch (regionID) {
      case LUMBER_YARD_REGION:
        return config.lumberYard();
      case WOODCUTTING_GUILD_REGION:
        return config.woodcuttingGuild();
      case PRIFDDINAS_REGION:
        return config.prifddinas();
      case AUBURNVALE_REGION:
        return config.auburnvale();
      default:
        return false;
    }
  }

  private MenuEntry getMenuEntry(MenuEntry[] menuEntries, String option, String subOption) {
    for (MenuEntry menuEntry : menuEntries) {
      String menuEntryOption = Text.removeTags(menuEntry.getOption());

      if (!menuEntryOption.equals(option)) {
        continue;
      }

      for (MenuEntry subMenuEntry : menuEntry.getSubMenu().getMenuEntries()) {
        String subMenuEntryOption = Text.removeTags(subMenuEntry.getOption());

        if (!subMenuEntryOption.equals(subOption)) {
          continue;
        }

        return subMenuEntry;
      }
    }

    return null;
  }

  @Subscribe(priority = -100)
  public void onPostMenuSort(PostMenuSort event) {
    if (client.getGameState() != GameState.LOGGED_IN || client.isMenuOpen() || client.isKeyPressed(KeyCode.KC_SHIFT)) {
      return;
    }

    int playerRegionID = client.getLocalPlayer().getWorldLocation().getRegionID();

    if (!isAtEnabledSawmill(playerRegionID)) {
      return;
    }

    Menu menu = client.getMenu();
    MenuEntry[] menuEntries = menu.getMenuEntries();
    MenuEntry emptyMenuEntry = null;

    int hoveredItemId = menuEntries[menuEntries.length - 1].getItemId();

    if (ArrayUtils.contains(LOG_BASKETS, hoveredItemId)) {
      emptyMenuEntry = getMenuEntry(menuEntries, "Check", "Empty");
    } else if (ArrayUtils.contains(FORESTRY_BASKETS, hoveredItemId)) {
      emptyMenuEntry = getMenuEntry(menuEntries, "View", "Empty basket");
    }

    if (emptyMenuEntry == null) {
      return;
    }

    for (MenuEntry menuEntry : menuEntries) {
      menuEntry.setDeprioritized(true);
    }

    menu.createMenuEntry(-1)
      .setOption(emptyMenuEntry.getOption())
      .setTarget(emptyMenuEntry.getTarget())
      .setIdentifier(emptyMenuEntry.getIdentifier())
      .setDeprioritized(false)
      .setType(emptyMenuEntry.getType())
      .setItemId(emptyMenuEntry.getItemId())
      .setParam0(emptyMenuEntry.getParam0())
      .setParam1(emptyMenuEntry.getParam1())
      .onClick(emptyMenuEntry.onClick());
  }

  @Provides
  LogBasketSwapperConfig provideConfig(ConfigManager configManager) {
    return configManager.getConfig(LogBasketSwapperConfig.class);
  }
}
