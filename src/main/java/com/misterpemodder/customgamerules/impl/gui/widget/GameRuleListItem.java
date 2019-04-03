package com.misterpemodder.customgamerules.impl.gui.widget;

import com.misterpemodder.customgamerules.impl.StringUtil;
import com.misterpemodder.customgamerules.impl.hook.GameRulesKeyHook;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.world.GameRules;

public abstract class GameRuleListItem extends GameRuleListWidget.ListItem
    implements Comparable<GameRuleListItem> {
  protected final MinecraftClient client;
  protected final String ruleName;
  protected final GameRulesKeyHook ruleKey;
  protected final GameRules.Value ruleValue;
  protected final ButtonWidget resetButton;
  private int maxStringWidth;

  protected GameRuleListItem(MinecraftClient client, String ruleName, GameRules.Key ruleKey,
      GameRules.Value ruleValue) {
    this.client = client;
    this.ruleName = ruleName;
    this.ruleKey = ((GameRulesKeyHook) ruleKey);
    this.ruleValue = ruleValue;
    this.resetButton = new ButtonWidget(0, 0, 50, 20,
        StringUtil.translate("controls.reset", "Reset"), b -> onReset());
  }

  public void setMaxStringWidth(int maxStringWidth) {
    this.maxStringWidth = maxStringWidth;
  }

  public int getMaxStringWidth() {
    return this.maxStringWidth;
  }

  public abstract void onReset();

  @Override
  public void render(int listX, int listY, int width, int height, int x, int y, int integer_7,
      boolean selected, float partial) {
    int n2 = listY + x / 2;
    this.client.textRenderer.draw(this.ruleName, width + 90 - this.maxStringWidth, n2 - 9 / 2,
        0xFFFFFF);
    this.resetButton.x = width + 190;
    this.resetButton.y = listY;
    this.resetButton.render(y, integer_7, partial);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return this.resetButton.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    return this.resetButton.mouseReleased(mouseX, mouseY, button);
  }
}
