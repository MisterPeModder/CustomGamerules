package com.misterpemodder.customgamerules.gui;

import java.util.Locale;
import java.util.TreeMap;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.world.GameRules;

public class GameRuleListWidget extends EntryListWidget<GameRuleListEntryWidget> {
  public GameRuleListEntryWidget selected;

  public GameRuleListWidget(EditGameRulesScreen gui, MinecraftClient client, int width, int height,
      int y1, int y2, int entryHeight, Supplier<String> filter) {
    super(client, width, height, y1, y2, entryHeight);
    this.filter(filter, false);
    this.selected = null;
  }

  @Override
  public void draw(int mouseX, int mouseY, float delta) {
    super.draw(mouseX, mouseY, delta);
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
}
