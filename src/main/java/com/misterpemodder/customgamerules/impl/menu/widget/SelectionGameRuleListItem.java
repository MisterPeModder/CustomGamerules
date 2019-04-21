package com.misterpemodder.customgamerules.impl.menu.widget;

import java.util.List;
import com.google.common.collect.ImmutableList;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType.InvalidGameRuleValueException;
import com.misterpemodder.customgamerules.api.rule.value.GameRuleValue;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.MathHelper;

public class SelectionGameRuleListItem<V> extends GameRuleListItem<V> {
  private final HoverButtonWidget selectButton;
  private final String[] values;
  private int selected;
  private final int defaultIndex;
  private ButtonWidget focusedButton = null;

  public SelectionGameRuleListItem(GameRuleListWidget parent, MinecraftClient client,
      String modName, GameRuleKey<V> ruleKey, GameRuleValue<V> ruleValue, String[] values,
      int initialIndex, int defaultIndex) {
    super(parent, client, modName, ruleKey, ruleValue);
    this.selected = MathHelper.clamp(initialIndex, 0, values.length);
    this.values = values;
    this.defaultIndex = defaultIndex;
    this.selectButton = new HoverButtonWidget(0, 0, 80, 20, this.values[initialIndex], b -> {
      if (++this.selected >= this.values.length)
        this.selected = 0;
      b.setMessage(this.values[this.selected]);
    });
  }

  @SuppressWarnings("unchecked")
  public static SelectionGameRuleListItem<?> create(GameRuleListWidget gui, MinecraftClient client,
      String modName, GameRuleKey<?> ruleKey, GameRuleValue<?> ruleValue, String[] values,
      int initialIndex, int defaultIndex) {
    return new SelectionGameRuleListItem<Object>(gui, client, modName,
        (GameRuleKey<Object>) (Object) ruleKey, (GameRuleValue<Object>) (Object) ruleValue, values,
        initialIndex, defaultIndex);
  }

  @Override
  public boolean keyPressed(int keyCode, int int_2, int int_3) {
    if (keyCode == GLFW.GLFW_KEY_ENTER) {
      if (this.focusedButton == null)
        return super.keyPressed(keyCode, int_2, int_3);
      this.focusedButton.onPress();
      this.resetButton.playDownSound(this.client.getSoundManager());
      this.focusedButton = this.selectButton;
    } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
      this.focusedButton = this.selectButton;
    } else if (keyCode == GLFW.GLFW_KEY_RIGHT && this.resetButton.active) {
      this.focusedButton = this.resetButton;
    } else {
      return super.keyPressed(keyCode, int_2, int_3);
    }
    return true;
  }

  @Override
  public void setFocused(boolean focused) {
    this.focusedButton = focused ? this.selectButton : null;
  }

  @Override
  protected Element getFocusedElement() {
    return this.focusedButton;
  }

  @Override
  public void onReset() {
    this.selected = this.defaultIndex;
    this.selectButton.setMessage(this.values[this.defaultIndex]);
  }

  @Override
  public void render(int index, int rowTop, int rowLeft, int itemWidth, int itemHeight, int mouseX,
      int mouseY, boolean hovered, float partial) {
    this.resetButton.active =
        !this.selectButton.getMessage().equals(this.ruleKey.getDefaultValueAsString());
    super.render(index, rowTop, rowLeft, itemWidth, itemHeight, mouseX, mouseY, hovered, partial);
    this.selectButton.x = rowLeft + 105;
    this.selectButton.y = rowTop;
    this.selectButton.render(mouseX, mouseY, partial);
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
  public int compareTo(GameRuleListItem<V> other) {
    return this.ruleName.compareTo(other.ruleName);
  }

  public List<? extends Element> children() {
    return ImmutableList.of(this.selectButton, this.resetButton);
  }

  @Override
  protected boolean isEditWidgetHovered(int mouseX, int mouseY) {
    return this.selectButton.getIsHovered();
  }

  @Override
  public void onSave() {
    try {
      this.ruleValue.set(this.ruleValue.getType().parse(this.values[this.selected]), null);
    } catch (InvalidGameRuleValueException e) {
      this.ruleValue.set(this.ruleKey.getDefaultValue(), null);
    }
  }

  protected class HoverButtonWidget extends ButtonWidget {
    public HoverButtonWidget(int x, int y, int width, int height, String message,
        ButtonWidget.PressAction pressAction) {
      super(x, y, width, height, message, pressAction);
    }

    public boolean getIsHovered() {
      return this.isHovered;
    }

    @Override
    public boolean isHovered() {
      return this.isHovered || SelectionGameRuleListItem.this.focusedButton == this;
    }
  }
}
