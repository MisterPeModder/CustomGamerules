package com.misterpemodder.customgamerules.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

public class EditGameRulesButtonWidget extends ButtonWidget {
  public final Screen containingScreen;

  public EditGameRulesButtonWidget(Screen containingScreen, int id, int x, int y, String text) {
    super(id, x, y, text);
    this.containingScreen = containingScreen;
  }

  @Override
  public void onPressed(double x, double y) {
    MinecraftClient.getInstance().openScreen(new EditGameRulesScreen(this.containingScreen));
  }
}
