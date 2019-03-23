package com.misterpemodder.customgamerules.impl.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

public class OpenScreenButtonAction implements ButtonWidget.class_4241 {
  private final Screen toOpen;

  public OpenScreenButtonAction(Screen toOpen) {
    this.toOpen = toOpen;
  }

  @Override
  public void onPress(ButtonWidget button) {
    MinecraftClient.getInstance().openScreen(this.toOpen);
  }
}
