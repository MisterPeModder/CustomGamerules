package com.misterpemodder.customgamerules.impl.menu;

import com.misterpemodder.customgamerules.api.menu.GameRulesMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;

public class EditGameRulesButton extends ButtonWidget {
  public EditGameRulesButton(int x, int y, int width, int height, String text, Screen parent) {
    super(x, y, width, height, text, b -> GameRulesMenu.INSTANCE
        .open((s, r) -> MinecraftClient.getInstance().openScreen(parent)));
  }

  public static void changeButtonHeight(Screen screen, int height, AbstractButtonWidget button) {
    if (button.y >= height / 4 + 144 + 5 && !(button instanceof EditGameRulesButton))
      button.y += 24;
    button.y -= 12;
  }
}
