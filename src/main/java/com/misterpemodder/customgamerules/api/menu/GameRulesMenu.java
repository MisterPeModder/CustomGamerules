package com.misterpemodder.customgamerules.api.menu;

import java.util.function.BiConsumer;
import com.misterpemodder.customgamerules.api.CustomGameRules;
import com.misterpemodder.customgamerules.impl.menu.GameRulesMenuImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface GameRulesMenu {
  static GameRulesMenu INSTANCE = new GameRulesMenuImpl();

  /**
   * Opens the game rules menu with the default values.
   * 
   * @param onClose Called when the menu is closed.
   *                The first argument is true when the rules changed.
   *                The (maybe) modified rules.
   */
  void open(BiConsumer<Boolean, CustomGameRules> onClose);

  /**
   * Opens the game rules menu with the default values.
   * 
   * @param rules   The rule values
   * @param onClose Called when the menu is closed.
   *                The first argument is true when the rules changed.
   *                The (maybe) modified rules.
   */
  void open(CustomGameRules rules, BiConsumer<Boolean, CustomGameRules> onClose);
}
