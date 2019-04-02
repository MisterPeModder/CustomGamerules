package com.misterpemodder.customgamerules.impl.gui.widget;

import java.util.List;
import com.google.common.collect.ImmutableList;
import com.misterpemodder.customgamerules.mixin.client.TextFieldPosAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.world.GameRules;

public class FieldGameRuleListItem extends GameRuleListItem {
  private final TextFieldWidget editField;

  public FieldGameRuleListItem(MinecraftClient client, String ruleName, GameRules.Key ruleKey,
      GameRules.Value ruleValue) {
    super(client, ruleName, ruleKey, ruleValue);
    this.editField = new TextFieldWidget(this.client.textRenderer, 0, 0, 76, 20);
    this.editField.setText(this.ruleKey.getDefaultValue());
  }

  @Override
  public void setFocused(boolean focused) {
    this.editField.setFocused(focused);
  }

  @Override
  public void tick() {
    this.editField.tick();
  }

  @Override
  public boolean keyPressed(int keyCode, int int_2, int int_3) {
    return this.editField.keyPressed(keyCode, int_2, int_3);
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
    this.editField.setText(this.ruleKey.getDefaultValue());
  }

  @Override
  public void render(int listX, int listY, int width, int height, int x, int y, int integer_7,
      boolean selected, float partial) {
    this.resetButton.active = !this.editField.getText().equals(this.ruleKey.getDefaultValue());
    super.render(listX, listY, width, height, x, y, integer_7, selected, partial);

    // TODO Add better type validation to API
    //this.editField.method_1868(color);

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
  public int compareTo(GameRuleListItem other) {
    return this.ruleName.compareTo(other.ruleName);
  }

  public List<? extends Element> children() {
    return ImmutableList.of(this.editField, this.resetButton);
  }
}
