package com.misterpemodder.customgamerules.impl.screen;

import com.misterpemodder.customgamerules.impl.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.InputListener;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.GameRules;

/**
 * WIP
 */
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
  public void update() {
    this.searchBox.tick();
  }

  @Override
  protected void onInitialized() {

    this.client.keyboard.enableRepeatEvents(true);
    this.saveButton = addButton(new ButtonWidget(this.screenWidth / 2 - 154, this.screenHeight - 28,
        150, 20, Util.translate("selectWorld.edit.save", "Save"),
        b -> this.client.openScreen(this.parent)));
    this.cancelButton = addButton(new ButtonWidget(this.screenWidth / 2 + 4, this.screenHeight - 28,
        150, 20, Util.translate("gui.cancel", "Cancel"), b -> this.client.openScreen(this.parent)));
    (this.searchBox =
        new TextFieldWidget(this.fontRenderer, this.screenWidth / 2 - 100, 22, 200, 20) {
          @Override
          public void setFocused(boolean focused) {
            super.setFocused(focused);
          }
        }).setChangedListener(text -> {
          this.gameRuleList.filter(() -> text);
          this.searchBox
              .method_1868(this.gameRuleList.getInputListeners().isEmpty() ? 0xff5555 : 0xe0e0e0);
        });
    this.gameRuleList = new GameRuleListWidget(this, this.client, this.screenWidth,
        this.screenHeight, 48, this.screenHeight - 36, 22, () -> this.searchBox.getText());
    this.gameRuleList.setRenderSelection(false);
    this.searchBox.setFocused(true);
    this.listeners.add(this.searchBox);
    this.listeners.add(this.gameRuleList);
    focusOn(this.searchBox);
    enableButtons(true);
  }

  @Override
  public void onScaleChanged(MinecraftClient client, int screenWidth, int screenHeight) {
    String text = this.searchBox.getText();
    this.initialize(client, screenWidth, screenHeight);
    this.searchBox.setText(text);
  }

  @Override
  public void onClosed() {
    this.client.keyboard.enableRepeatEvents(false);
  }

  @Override
  public boolean charTyped(final char chr, final int keyCode) {
    if (getFocused() == this.searchBox)
      return this.searchBox.charTyped(chr, keyCode);
    return super.charTyped(chr, keyCode);
  }

  @Override
  public void render(final int mouseX, final int mouseY, final float delta) {
    drawBackground();
    this.gameRuleList.render(mouseX, mouseY, delta);
    this.drawString(this.fontRenderer, Util.translate("customgamerules.search", "Search"),
        this.screenWidth / 2 - 100, 9, 0xa0a0a0);
    this.searchBox.render(mouseX, mouseY, delta);
    super.render(mouseX, mouseY, delta);
    // TODO Handle tooltips
    /*
    for (GameRuleListWidget.Entry entry : this.gameRuleList.getInputListeners()) {
      int x = entry.getX();
      int y = entry.getY();
      if (mouseX >= x && mouseY >= y && mouseX < x + this.gameRuleList.getEntryWidth()
          && mouseY < y + this.gameRuleList.getEntryHeight()) {
        drawTooltip(entry.getTooltip(), mouseX, mouseY);
        new ButtonWidget(0, 0, 0, 0, "yeet", w -> {
        });
      }
    }*/
  }

  @Override
  public void focusNext() {
    /*if (true) {
      List<GameRuleListEntryWidget> entries = this.gameRuleList.getInputListeners();
      int id = entries.indexOf(this.gameRuleList.selected) + 1;
      if (id < entries.size()) {
        this.gameRuleList.selected = entries.get(id);
      }
    }*/
    super.focusNext();
  }

  @Override
  public void focusPrevious() {
    super.focusPrevious();
  }

  @Override
  public void setFocused(InputListener inputListener) {
    this.searchBox.setFocused(inputListener == this.searchBox);
    super.setFocused(inputListener);
  }

  public void enableButtons(boolean enable) {
    this.saveButton.active = enable;
    this.cancelButton.active = enable;
  }
}
