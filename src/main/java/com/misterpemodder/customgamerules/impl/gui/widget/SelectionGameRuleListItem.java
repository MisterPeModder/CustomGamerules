package com.misterpemodder.customgamerules.impl.gui.widget;

import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;

public class SelectionGameRuleListItem extends GameRuleListItem {
  private final ButtonWidget selectButton;
  private final String[] values;
  private int selected;
  private final int initialIndex;

  public SelectionGameRuleListItem(MinecraftClient client, String ruleName, GameRules.Key ruleKey,
      GameRules.Value ruleValue, String[] values, int initialIndex) {
    super(client, ruleName, ruleKey, ruleValue);
    this.selected = MathHelper.clamp(initialIndex, 0, values.length);
    this.values = values;
    this.initialIndex = initialIndex;
    this.selectButton = new ButtonWidget(0, 0, 80, 20, this.values[initialIndex], b -> {
      if (++this.selected >= this.values.length)
        this.selected = 0;
      b.setMessage(this.values[this.selected]);
    });
  }

  @Override
  public void onReset() {
    this.selected = this.initialIndex;
    this.selectButton.setMessage(this.values[this.initialIndex]);
  }

  @Override
  public void render(int listX, int listY, int width, int height, int x, int y, int integer_7,
      boolean selected, float partial) {
    this.resetButton.active =
        !this.selectButton.getMessage().equals(this.ruleKey.getDefaultValue());
    super.render(listX, listY, width, height, x, y, integer_7, selected, partial);
    this.selectButton.x = width + 105;
    this.selectButton.y = listY;
    this.selectButton.render(y, integer_7, partial);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return this.selectButton.mouseClicked(mouseX, mouseY, button)
        || super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    return this.selectButton.mouseReleased(mouseX, mouseY, button)
        || super.mouseReleased(mouseX, mouseY, button);
  }

  @Override
  public int compareTo(GameRuleListItem other) {
    return this.ruleName.compareTo(other.ruleName);
  }

  public List<? extends Element> children() {
    return ImmutableList.of(this.selectButton, this.resetButton);
  }
}
