package com.misterpemodder.customgamerules.impl.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

public class CategoryListItem extends GameRuleListWidget.ListItem
    implements Comparable<CategoryListItem> {
  private final String name;
  private final int nameWidth;
  private final MinecraftClient client;

  public CategoryListItem(MinecraftClient client, String name) {
    this.client = client;
    this.name = name;
    this.nameWidth = this.client.textRenderer.getStringWidth(this.name);
  }

  @Override
  public void render(int listX, int listY, int width, int height, int x, int y, int integer_7,
      boolean selected, float partial) {
    TextRenderer textRenderer = this.client.textRenderer;
    float str = (float) (this.client.currentScreen.width / 2 - this.nameWidth / 2);
    textRenderer.draw(this.name, str, x + listY - 10, 0xFFFFFF);
  }

  @Override
  public int compareTo(CategoryListItem other) {
    return this.name.compareToIgnoreCase(other.name);
  }
}
