package com.misterpemodder.customgamerules.impl.gui;

import com.misterpemodder.customgamerules.impl.StringUtil;
import com.misterpemodder.customgamerules.impl.gui.widget.GameRuleListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.GameRules;

public class EditGameRulesScreen extends Screen {
  public final Screen parent;
  private TextFieldWidget searchBox;
  private GameRuleListWidget gameRuleList;
  private ButtonWidget saveButton;
  private ButtonWidget cancelButton;

  public final GameRules rules;

  public EditGameRulesScreen(GameRules rules, Screen parent) {
    super(new TranslatableTextComponent("customgamerules.edit.title"));
    this.parent = parent;
    this.rules = rules;
  }

  @Override
  public void tick() {
    this.searchBox.tick();
    this.gameRuleList.tick();
  }

  @Override
  protected void init() {
    this.minecraft.keyboard.enableRepeatEvents(true);
    this.saveButton = addButton(new ButtonWidget(this.width / 2 - 154, this.height - 28, 150, 20,
        StringUtil.translate("selectWorld.edit.save", "Save"),
        b -> this.minecraft.openScreen(this.parent)));
    this.cancelButton = addButton(new ButtonWidget(this.width / 2 + 4, this.height - 28, 150, 20,
        StringUtil.translate("gui.cancel", "Cancel"), b -> this.minecraft.openScreen(this.parent)));
    (this.searchBox = new TextFieldWidget(this.font, this.width / 2 - 100, 22, 200, 20))
        .setChangedListener(text -> {
          if (this.gameRuleList.filter(() -> text))
            this.gameRuleList.capYPosition(0.0);
          this.searchBox.method_1868(this.gameRuleList.children().isEmpty() ? 0xff5555 : 0xe0e0e0);
        });
    this.gameRuleList = new GameRuleListWidget(this, this.minecraft, this.width, this.height, 48,
        this.height - 36, 22, () -> this.searchBox.getText());
    this.gameRuleList.setRenderSelection(false);
    this.searchBox.setFocused(true);
    this.children.add(this.searchBox);
    this.children.add(this.gameRuleList);
    setFocused(this.searchBox);
    enableButtons(true);
  }

  @Override
  public void resize(MinecraftClient client, int width, int height) {
    String text = this.searchBox.getText();
    this.init(client, width, height);
    this.searchBox.setText(text);
  }

  @Override
  public void onClose() {
    this.minecraft.keyboard.enableRepeatEvents(false);
  }

  @Override
  public void render(final int mouseX, final int mouseY, final float delta) {
    renderBackground();
    this.gameRuleList.render(mouseX, mouseY, delta);
    this.drawString(this.font, StringUtil.translate("customgamerules.search", "Search"),
        this.width / 2 - 100, 9, 0xa0a0a0);
    this.searchBox.render(mouseX, mouseY, delta);
    super.render(mouseX, mouseY, delta);
  }

  @Override
  public void setFocused(Element inputListener) {
    this.searchBox.setFocused(inputListener == this.searchBox);
    super.setFocused(inputListener);
  }

  public void enableButtons(boolean enable) {
    this.saveButton.active = enable;
    this.cancelButton.active = enable;
  }
}
