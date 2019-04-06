package com.misterpemodder.customgamerules.impl.menu.widget;

import java.util.List;
import com.google.common.collect.ImmutableList;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.value.GameRuleValue;
import com.misterpemodder.customgamerules.mixin.client.gui.widget.TextFieldPosAccessor;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class FieldGameRuleListItem<V> extends GameRuleListItem<V> {
  private final TextFieldWidget editField;
  private Element focusedElement = null;

  public FieldGameRuleListItem(MinecraftClient client, String ruleName, GameRuleKey<V> ruleKey,
      GameRuleValue<V> ruleValue) {
    super(client, ruleName, ruleKey, ruleValue);
    this.editField = new TextFieldWidget(this.client.textRenderer, 0, 0, 76, 20);
    this.editField.setText(this.ruleKey.getDefaultValueAsString());
  }

  @SuppressWarnings("unchecked")
  public static FieldGameRuleListItem<?> create(MinecraftClient client, String ruleName,
      GameRuleKey<?> ruleKey, GameRuleValue<?> ruleValue) {
    return new FieldGameRuleListItem<Object>(client, ruleName,
        (GameRuleKey<Object>) (Object) ruleKey, (GameRuleValue<Object>) (Object) ruleValue);
  }

  @Override
  public void setFocused(boolean focused) {
    this.editField.setFocused(focused);
    this.focusedElement = focused ? this.editField : null;
  }

  @Override
  protected Element getFocusedElement() {
    return this.focusedElement;
  }

  @Override
  public void tick() {
    this.editField.tick();
  }

  @Override
  public boolean keyPressed(int keyCode, int int_2, int int_3) {
    if (this.focusedElement == this.resetButton && keyCode == GLFW.GLFW_KEY_ENTER) {
      this.resetButton.onPress();
      this.resetButton.playDownSound(this.client.getSoundManager());
      this.focusedElement = this.editField;
      this.editField.setFocused(true);
      return true;
    } else if (keyCode == GLFW.GLFW_KEY_LEFT && this.focusedElement != this.editField) {
      this.focusedElement = editField;
      this.editField.setFocused(true);
      return true;
    } else if (keyCode == GLFW.GLFW_KEY_RIGHT
        && this.editField.getCursor() >= this.editField.getText().length()
        && this.resetButton.active) {
      this.focusedElement = this.resetButton;
      this.editField.setFocused(false);
      return true;
    }
    return super.keyPressed(keyCode, int_2, int_3)
        || this.editField.keyPressed(keyCode, int_2, int_3);
  }

  @Override
  public boolean keyReleased(int keyCode, int int_2, int int_3) {
    return this.editField.keyReleased(keyCode, int_2, int_3);
  }

  @Override
  public boolean charTyped(char chr, int keyCode) {
    return this.editField.charTyped(chr, keyCode);
  }

  @Override
  public void onReset() {
    this.editField.setText(this.ruleKey.getDefaultValueAsString());
  }

  @Override
  public void render(int listX, int listY, int width, int height, int x, int y, int integer_7,
      boolean selected, float partial) {
    this.resetButton.active = !this.editField.getText().equals(this.ruleKey.getDefaultValue());
    super.render(listX, listY, width, height, x, y, integer_7, selected, partial);

    this.editField.method_1868(0xe0e0e0);
    try {
      V parsedValue = this.ruleKey.getType().parse(this.editField.getText());
      if (!this.ruleKey.isValidValue(parsedValue))
        this.editField.method_1868(0xff5555);
    } catch (IllegalArgumentException e) {
      this.editField.method_1868(0xff5555);
    }

    ((TextFieldPosAccessor) this.editField).cg$setXPos(width + 107);
    ((TextFieldPosAccessor) this.editField).cg$setYPos(listY);
    this.editField.render(y, integer_7, partial);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return this.editField.mouseClicked(mouseX, mouseY, button)
        || super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    return this.editField.mouseReleased(mouseX, mouseY, button)
        || super.mouseReleased(mouseX, mouseY, button);
  }

  @Override
  public int compareTo(GameRuleListItem<V> other) {
    return this.ruleName.compareTo(other.ruleName);
  }

  public List<? extends Element> children() {
    return ImmutableList.of(this.editField, this.resetButton);
  }
}
