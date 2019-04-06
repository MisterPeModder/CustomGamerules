package com.misterpemodder.customgamerules.impl.menu.widget;

import java.util.List;
import com.google.common.collect.ImmutableList;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.value.GameRuleValue;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.MathHelper;

public class SelectionGameRuleListItem<V> extends GameRuleListItem<V> {
  private final ButtonWidget selectButton;
  private final String[] values;
  private int selected;
  private final int initialIndex;
  private ButtonWidget focusedButton = null;

  public SelectionGameRuleListItem(MinecraftClient client, String ruleName, GameRuleKey<V> ruleKey,
      GameRuleValue<V> ruleValue, String[] values, int initialIndex) {
    super(client, ruleName, ruleKey, ruleValue);
    this.selected = MathHelper.clamp(initialIndex, 0, values.length);
    this.values = values;
    this.initialIndex = initialIndex;
    this.selectButton = new ButtonWidget(0, 0, 80, 20, this.values[initialIndex], b -> {
      if (++this.selected >= this.values.length)
        this.selected = 0;
      b.setMessage(this.values[this.selected]);
    }) {
      @Override
      public boolean isHovered() {
        return SelectionGameRuleListItem.this.focusedButton == this;
      }
    };
  }

  @SuppressWarnings("unchecked")
  public static SelectionGameRuleListItem<?> create(MinecraftClient client, String ruleName,
      GameRuleKey<?> ruleKey, GameRuleValue<?> ruleValue, String[] values, int initialIndex) {
    return new SelectionGameRuleListItem<Object>(client, ruleName,
        (GameRuleKey<Object>) (Object) ruleKey, (GameRuleValue<Object>) (Object) ruleValue, values,
        initialIndex);
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
  public int compareTo(GameRuleListItem<V> other) {
    return this.ruleName.compareTo(other.ruleName);
  }

  public List<? extends Element> children() {
    return ImmutableList.of(this.selectButton, this.resetButton);
  }
}
