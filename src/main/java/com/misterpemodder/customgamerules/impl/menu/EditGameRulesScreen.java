package com.misterpemodder.customgamerules.impl.menu;

import java.util.function.BiConsumer;
import com.misterpemodder.customgamerules.api.CustomGameRules;
import com.misterpemodder.customgamerules.impl.StringUtil;
import com.misterpemodder.customgamerules.impl.menu.widget.GameRuleListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.TranslatableTextComponent;

public class EditGameRulesScreen extends Screen {
  public final BiConsumer<Boolean, CustomGameRules> onClose;
  private TextFieldWidget searchBox;
  private GameRuleListWidget gameRuleList;
  private ButtonWidget saveButton;
  private ButtonWidget cancelButton;

  public final CustomGameRules rules;

  public EditGameRulesScreen(CustomGameRules rules, BiConsumer<Boolean, CustomGameRules> onClose) {
    super(new TranslatableTextComponent("customgamerules.edit.title"));
    this.onClose = onClose;
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
        b -> this.onClose.accept(true, this.rules)));
    this.cancelButton = addButton(new ButtonWidget(this.width / 2 + 4, this.height - 28, 150, 20,
        StringUtil.translate("gui.cancel", "Cancel"), b -> this.onClose.accept(false, this.rules)));
    (this.searchBox = new TextFieldWidget(this.font, this.width / 2 - 100, 22, 200, 20))
        .setChangedListener(text -> {
          if (this.gameRuleList.filter(() -> text))
            this.gameRuleList.capYPosition(0.0);
          this.searchBox.method_1868(this.gameRuleList.children().isEmpty() ? 0xff5555 : 0xe0e0e0);
        });
    this.gameRuleList = new GameRuleListWidget(this, this.minecraft, this.width, this.height, 48,
        this.height - 36, 22, () -> this.searchBox.getText());
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
    if (this.searchBox != null)
      this.searchBox.setFocused(inputListener == this.searchBox);
    super.setFocused(inputListener);
  }

  public void enableButtons(boolean enable) {
    this.saveButton.active = enable;
    this.cancelButton.active = enable;
  }
}
