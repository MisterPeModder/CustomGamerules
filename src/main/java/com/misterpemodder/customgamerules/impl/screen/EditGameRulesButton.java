package com.misterpemodder.customgamerules.impl.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.world.GameRules;

public class EditGameRulesButton extends ButtonWidget {
  public EditGameRulesButton(int x, int y, int width, int height, String text, Screen parent) {
    super(x, y, width, height, text, b -> MinecraftClient.getInstance()
        .openScreen(new EditGameRulesScreen(new GameRules(), parent)));
  }

  public static void changeButtonHeight(Screen screen, int screenHeight,
      AbstractButtonWidget button) {
    if (button.y >= screenHeight / 4 + 144 + 5 && !(button instanceof EditGameRulesButton))
      button.y += 24;
    button.y -= 12;
  }
}
