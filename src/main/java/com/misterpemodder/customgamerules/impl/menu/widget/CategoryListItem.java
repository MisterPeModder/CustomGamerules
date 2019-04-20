package com.misterpemodder.customgamerules.impl.menu.widget;

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
  public void render(int index, int rowTop, int rowLeft, int itemWidth, int itemHeight, int mouseX,
      int mouseY, boolean hovered, float partial) {
    TextRenderer textRenderer = this.client.textRenderer;
    float str = (float) (this.client.currentScreen.width / 2 - this.nameWidth / 2);
    textRenderer.draw(this.name, str, itemHeight + rowTop - 10, 0xFFFFFF);
  }

  @Override
  public int compareTo(CategoryListItem other) {
    return this.name.compareToIgnoreCase(other.name);
  }

  @Override
  public boolean changeFocus(boolean boolean_1) {
    return true;
  }
}
