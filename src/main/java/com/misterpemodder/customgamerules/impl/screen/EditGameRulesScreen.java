package com.misterpemodder.customgamerules.impl.screen;

import java.util.List;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;

/**
 * WIP
 */
public class EditGameRulesScreen extends Screen {
  public final Screen parent;
  private TextFieldWidget textField;
  private GameRuleListWidget gameRuleList;

  public EditGameRulesScreen(Screen parent) {
    this.parent = parent;
  }

  @Override
  public void update() {
    this.textField.tick();
  }

  @Override
  protected void onInitialized() {
    this.client.keyboard.enableRepeatEvents(true);
    this.addButton(new ButtonWidget(this.screenWidth / 2 - 154, this.screenHeight - 28, 150, 20,
        I18n.translate("selectWorld.edit.save")) {
      @Override
      public void onPressed() {
        client.openScreen(EditGameRulesScreen.this.parent);
      }
    });
    this.addButton(new ButtonWidget(this.screenWidth / 2 + 4, this.screenHeight - 28, 150, 20,
        I18n.translate("gui.cancel")) {
      @Override
      public void onPressed() {
        client.openScreen(EditGameRulesScreen.this.parent);
      }
    });
    (this.textField =
        new TextFieldWidget(this.fontRenderer, this.screenWidth / 2 - 100, 22, 200, 20) {
          @Override
          public void setFocused(boolean focused) {
            super.setFocused(true);
          }
        }).setChangedListener(text -> {
          this.gameRuleList.filter(() -> text, false);
          this.textField
              .method_1868(this.gameRuleList.getInputListeners().isEmpty() ? 0xff5555 : 0xe0e0e0);
        });
    this.gameRuleList = new GameRuleListWidget(this, this.client, this.screenWidth,
        this.screenHeight, 48, this.screenHeight - 36, 18, () -> this.textField.getText());
    this.textField.setFocused(true);
    this.listeners.add(this.gameRuleList);
    this.listeners.add(this.textField);
  }

  @Override
  public void onScaleChanged(MinecraftClient client, int screenWidth, int screenHeight) {
    String text = this.textField.getText();
    this.initialize(client, screenWidth, screenHeight);
    this.textField.setText(text);
  }

  @Override
  public void onClosed() {
    this.client.keyboard.enableRepeatEvents(false);
  }

  @Override
  public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
    boolean down;

    if (!(down = keyCode == GLFW.GLFW_KEY_DOWN) && keyCode != GLFW.GLFW_KEY_UP)
      return this.textField.keyPressed(keyCode, scanCode, modifiers);
    if (this.gameRuleList.selected == null)
      return true;
    List<GameRuleListEntryWidget> entries = this.gameRuleList.getInputListeners();
    int id = entries.indexOf(this.gameRuleList.selected) + (down ? 1 : -1);
    if (id >= 0 && id < entries.size())
      this.gameRuleList.selected = entries.get(id);
    int y = this.gameRuleList.selected.getY();
    if (y < 40)
      this.gameRuleList.scroll(-18);
    else if (y > this.screenHeight - 50)
      this.gameRuleList.scroll(18);
    return true;
  }

  @Override
  public boolean charTyped(final char chr, final int keyCode) {
    return this.textField.charTyped(chr, keyCode);
  }

  @Override
  public void draw(final int mouseX, final int mouseY, final float delta) {
    drawBackground();
    this.gameRuleList.draw(mouseX, mouseY, delta);
    this.drawString(this.fontRenderer, I18n.translate("customgamerules.search"),
        this.screenWidth / 2 - 100, 9, 0xa0a0a0);
    this.textField.draw(mouseX, mouseY, delta);
    super.draw(mouseX, mouseY, delta);
    for (GameRuleListEntryWidget entry : this.gameRuleList.getInputListeners()) {
      int x = entry.getX();
      int y = entry.getY();
      if (mouseX >= x && mouseY >= y && mouseX < x + this.gameRuleList.getEntryWidth()
          && mouseY < y + this.gameRuleList.getEntryHeight()) {
        drawTooltip(entry.getTooltip(), mouseX, mouseY);
      }
    }
  }
}
