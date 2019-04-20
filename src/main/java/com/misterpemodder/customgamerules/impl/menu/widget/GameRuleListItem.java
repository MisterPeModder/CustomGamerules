package com.misterpemodder.customgamerules.impl.menu.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.value.GameRuleValue;
import com.misterpemodder.customgamerules.impl.StringUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TextFormat;

public abstract class GameRuleListItem<V> extends GameRuleListWidget.ListItem
    implements Comparable<GameRuleListItem<V>> {
  protected final MinecraftClient client;
  protected final String ruleName;
  protected final GameRuleKey<V> ruleKey;
  protected final GameRuleValue<V> ruleValue;
  protected final ButtonWidget resetButton;
  private int maxStringWidth;
  private final GameRuleListWidget parent;
  protected final String modName;

  protected GameRuleListItem(GameRuleListWidget gui, MinecraftClient client, String modName,
      GameRuleKey<V> ruleKey, GameRuleValue<V> ruleValue) {
    this.parent = gui;
    this.client = client;
    this.ruleName = ruleKey.getName();
    this.ruleKey = ruleKey;
    this.ruleValue = ruleValue;
    this.modName = modName;
    this.resetButton = new ButtonWidget(0, 0, 50, 20,
        StringUtil.translate("controls.reset", "Reset"), b -> onReset()) {
      @Override
      public boolean isHovered() {
        return this.isHovered || GameRuleListItem.this.getFocusedElement() == this;
      }
    };
  }

  protected abstract Element getFocusedElement();

  public void setMaxStringWidth(int maxStringWidth) {
    this.maxStringWidth = maxStringWidth;
  }

  public int getMaxStringWidth() {
    return this.maxStringWidth;
  }

  public abstract void onReset();

  @Override
  public void render(int index, int rowTop, int rowLeft, int itemWidth, int itemHeight, int mouseX,
      int mouseY, boolean hovered, float partial) {
    int middle = rowTop + itemHeight / 2;
    int textLeft = rowLeft + 90 - this.maxStringWidth;
    this.client.textRenderer.draw(this.ruleName, textLeft, middle - 9 / 2, 0xFFFFFF);
    this.resetButton.x = rowLeft + 190;
    this.resetButton.y = rowTop;
    this.resetButton.render(mouseX, mouseY, partial);

    if (!this.parent.isMouseOver(mouseX, mouseY))
      return;
    if (isEditWidgetHovered(mouseX, mouseY)) {
      setTooltip(Arrays.asList(
          TextFormat.YELLOW + "Type: " + TextFormat.RESET + this.ruleKey.getType().getTypeName(),
          TextFormat.YELLOW + "Default value: " + TextFormat.RESET
              + this.ruleKey.getDefaultValueAsString()));
    } else if (mouseY >= rowTop - 1 && mouseY <= rowTop + itemHeight + 2 && mouseX >= textLeft
        && mouseX <= rowLeft + 105) {
      String key = "gamerule." + this.ruleKey.getModId() + "." + ruleName + ".desc";
      List<String> tooltip = new ArrayList<>();
      tooltip.add(this.ruleName);
      if (I18n.hasTranslation(key))
        tooltip.addAll(this.client.textRenderer
            .wrapStringToWidthAsList(TextFormat.GRAY + I18n.translate(key), 200));
      else
        tooltip.addAll(Arrays.asList("no description", "", key + " is undefined"));
      tooltip.add(TextFormat.BLUE + "" + TextFormat.ITALIC + this.modName);
      setTooltip(tooltip);
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return this.resetButton.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    return this.resetButton.mouseReleased(mouseX, mouseY, button);
  }

  protected abstract boolean isEditWidgetHovered(int mouseX, int mouseY);

  protected void setTooltip(List<String> tooltip) {
    this.parent.gui.setTooltip(tooltip);
  }
}
