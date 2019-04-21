package com.misterpemodder.customgamerules.impl.menu;

import com.misterpemodder.customgamerules.api.CustomGameRules;
import com.misterpemodder.customgamerules.api.menu.GameRulesMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.world.GameRules;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.LevelStorage;

public class EditGameRulesButton extends ButtonWidget {
  public EditGameRulesButton(int x, int y, int width, int height, String text, Screen parent,
      String levelName) {
    super(x, y, width, height, text,
        b -> GameRulesMenu.INSTANCE.open(getLevelRules(levelName), (shouldSave, rules) -> {
          if (shouldSave)
            setLevelRules(rules, levelName);
          MinecraftClient.getInstance().openScreen(parent);
        }));
  }

  private static CustomGameRules getLevelRules(String levelName) {
    LevelProperties properties =
        MinecraftClient.getInstance().getLevelStorage().getLevelProperties(levelName);
    return CustomGameRules.copy(properties == null ? new GameRules() : properties.getGameRules());
  }

  private static void setLevelRules(CustomGameRules rules, String levelName) {
    LevelStorage levelStorage = MinecraftClient.getInstance().getLevelStorage();
    LevelProperties properties = levelStorage.getLevelProperties(levelName);
    if (properties == null)
      return;
    rules.copyTo(properties.getGameRules());
    levelStorage.method_242(levelName, null).saveWorld(properties);
  }

  public static void changeButtonHeight(Screen screen, int height, AbstractButtonWidget button) {
    if (button.y >= height / 4 + 144 + 5 && !(button instanceof EditGameRulesButton))
      button.y += 24;
    button.y -= 12;
  }
}
