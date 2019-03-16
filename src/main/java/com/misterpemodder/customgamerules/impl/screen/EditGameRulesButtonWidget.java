package com.misterpemodder.customgamerules.impl.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

public class EditGameRulesButtonWidget extends ButtonWidget {
  public final Screen containingScreen;

  public EditGameRulesButtonWidget(Screen containingScreen, int x, int y, String text) {
    super(x, y, text);
    this.containingScreen = containingScreen;
  }

  @Override
  public void onPressed() {
    MinecraftClient.getInstance().openScreen(new EditGameRulesScreen(this.containingScreen));
  }
}
