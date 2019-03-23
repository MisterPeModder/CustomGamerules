package com.misterpemodder.customgamerules.impl.screen;

import java.util.Locale;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;

public class GameRuleListWidget extends EntryListWidget<GameRuleListEntryWidget> {
  private EditGameRulesScreen parent;
  private int selectedId;

  public GameRuleListWidget(EditGameRulesScreen gui, MinecraftClient client, int width, int height,
      int y1, int y2, int entryHeight, Supplier<String> filter) {
    super(client, width, height, y1, y2, entryHeight);
    this.filter(filter, false);
    this.parent = gui;
    this.selectedId = -1;
  }

  @Override
  public void render(int mouseX, int mouseY, float delta) {
    super.render(mouseX, mouseY, delta);
  }

  @Override
  public int getEntryHeight() {
    return super.getEntryHeight();
  }

  @Override
  public int getEntryWidth() {
    return super.getEntryWidth();
  }

  public void filter(final Supplier<String> filter, final boolean load) {
    this.clearEntries();
    TreeMap<String, GameRules.Key> gamerules = GameRules.getKeys();
    if (gamerules != null) {
      final String term = filter.get().toLowerCase(Locale.ROOT);
      gamerules.entrySet().stream()
          .filter(entry -> term.isEmpty() || entry.getKey().toLowerCase(Locale.ROOT).contains(term))
          .forEach(entry -> addEntry(
              new GameRuleListEntryWidget(entry.getKey(), entry.getValue(), client, this)));
    }
  }

  @Override
  protected int getScrollbarPosition() {
    return super.getScrollbarPosition() + 20;
  }

  @Override
  protected boolean isFocused() {
    return this.parent.getFocused() == this;
  }

  public void setSelected(int index) {
    this.selectedId = index;
    this.parent.enableButtons(this.getSelected().isPresent());
  }

  @Override
  protected void moveSelection(int index) {
    this.selectedId = MathHelper.clamp(this.selectedId + index, 0, this.getEntryCount() - 1);
    Optional<GameRuleListEntryWidget> selected = this.getSelected();
    //scrolls to 'selected'
    selected.ifPresent(this::method_19349);
    this.parent.enableButtons(selected.isPresent());
  }

  @Override
  public boolean isPartOfFocusCycle() {
    return true;
  }

  @Override
  protected boolean isSelectedEntry(int index) {
    return index == this.selectedId;
  }

  public Optional<GameRuleListEntryWidget> getSelected() {
    return (this.selectedId < 0 || this.selectedId >= this.getEntryCount()) ? Optional.empty()
        : Optional.of(this.getInputListeners().get(this.selectedId));
  }

  public EditGameRulesScreen getParent() {
    return this.parent;
  }

  @Override
  public void onFocusChanged(boolean hasFocus) {
    if (hasFocus && !this.getSelected().isPresent() && this.getEntryCount() > 0) {
      this.selectedId = 0;
      this.parent.enableButtons(false);
    }
  }
}
