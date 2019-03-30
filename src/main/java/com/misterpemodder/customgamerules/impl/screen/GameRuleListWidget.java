package com.misterpemodder.customgamerules.impl.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.misterpemodder.customgamerules.impl.Util;
import com.misterpemodder.customgamerules.impl.hook.GameRulesKeyHook;
import com.misterpemodder.customgamerules.mixin.client.TextFieldPosAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ItemListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;

public class GameRuleListWidget extends ItemListWidget<GameRuleListWidget.Entry> {
  private EditGameRulesScreen gui;
  private int selectedId;
  private int maxStringWidth;

  private final Map<String, Set<GameRuleEntry>> gamerules;
  private static final Pattern SEARCH_PATTERN = Pattern.compile("^(@\\s*([^\\s]*))?((\\s*)(.*))?");

  public GameRuleListWidget(EditGameRulesScreen gui, MinecraftClient client, int width, int height,
      int y1, int y2, int entryHeight, Supplier<String> filter) {
    super(client, width, height, y1, y2, entryHeight);
    this.gui = gui;
    this.gamerules = getGamerules();
    this.selectedId = -1;
    filter(filter);
    this.maxStringWidth = 0;
    for (Set<GameRuleEntry> rules : this.gamerules.values())
      for (GameRuleEntry rule : rules)
        this.maxStringWidth =
            Math.max(this.maxStringWidth, this.client.textRenderer.getStringWidth(rule.ruleName));
  }

  private Map<String, Set<GameRuleEntry>> getGamerules() {
    TreeMap<String, GameRules.Key> rules = GameRules.getKeys();
    if (rules == null)
      return new HashMap<>();
    Map<String, String> modIdToName =
        FabricLoader.getInstance().getAllMods().stream().map(m -> m.getMetadata())
            .collect(Collectors.toMap(ModMetadata::getId, ModMetadata::getName));
    modIdToName.put("minecraft", "Minecraft");
    Map<String, Set<GameRuleEntry>> ret = new TreeMap<>();
    for (Map.Entry<String, GameRules.Key> rule : rules.entrySet()) {
      String modId = ((GameRulesKeyHook) rule.getValue()).getModId();
      if (modId == null || modId.isEmpty())
        modId = Util.translate("customgamerules.mod.unknown", "Unknown");
      String modName = modIdToName.get(modId);
      if (modName == null)
        modName = modId;
      Set<GameRuleEntry> entries = ret.get(modName);
      if (entries == null) {
        entries = new TreeSet<>();
        ret.put(modName, entries);
      }
      entries.add(
          new GameRuleEntry(rule.getKey(), rule.getValue(), this.gui.rules.get(rule.getKey())));
    }
    return ret;
  }

  @Override
  public void render(int mouseX, int mouseY, float delta) {
    super.render(mouseX, mouseY, delta);
  }

  public void filter(final Supplier<String> filter) {
    this.clearItems();
    String text = filter.get();
    Matcher matcher = SEARCH_PATTERN.matcher((text == null ? "" : text).trim());
    final String mod;
    final String term;
    if (matcher.matches()) {
      mod = matcher.group(2);
      term = matcher.group(5);
    } else {
      mod = "";
      term = "";
    }

    this.gamerules.entrySet().stream()
        .filter(e -> !e.getValue().isEmpty() && termMatches(mod, e.getKey().replaceAll("\\s", "")))
        .forEach(e -> {
          List<GameRuleEntry> toAdd = new ArrayList<>();
          e.getValue().stream().filter(rule -> termMatches(term, rule.ruleName))
              .forEachOrdered(toAdd::add);
          if (!toAdd.isEmpty()) {
            addItem(new CategoryEntry(e.getKey()));
            toAdd.forEach(this::addItem);
          }
        });
  }

  private static boolean termMatches(String term, String toMatch) {
    if (term == null || term.isEmpty())
      return true;
    return toMatch.toLowerCase().contains(term.toLowerCase());
  }

  @Override
  protected int getScrollbarPosition() {
    return super.getScrollbarPosition() + 20;
  }

  @Override
  protected boolean isFocused() {
    return this.gui.getFocused() == this;
  }

  public Optional<Entry> setSelected(int index) {
    Optional<Entry> previous = this.getSelected();
    if (previous.isPresent()) {
      Entry previousEntry = previous.get();
      if (previousEntry instanceof GameRuleEntry) {
        ((GameRuleEntry) previousEntry).editField.setFocused(false);
      }
    }
    this.selectedId = index;
    Optional<Entry> current = this.getSelected();
    if (current.isPresent()) {
      Entry currentEntry = current.get();
      if (currentEntry instanceof GameRuleEntry) {
        ((GameRuleEntry) currentEntry).editField.setFocused(true);
      }
    }
    return current;
    //this.gui.enableButtons(this.getSelected().isPresent());
  }

  // moveSelection()
  @Override
  protected void method_20069(int index) {
    Optional<Entry> selected =
        setSelected(MathHelper.clamp(this.selectedId + index, 0, this.getItemCount() - 1));
    //scrolls to 'selected'
    selected.ifPresent(this::method_20072);
    //this.gui.enableButtons(selected.isPresent());
  }

  @Override
  public boolean isPartOfFocusCycle() {
    return true;
  }

  @Override
  protected boolean isSelected(int index) {
    return index == this.selectedId;
  }

  public Optional<Entry> getSelected() {
    return (this.selectedId < 0 || this.selectedId >= this.getItemCount()) ? Optional.empty()
        : Optional.of(this.children().get(this.selectedId));
  }

  public EditGameRulesScreen getParent() {
    return this.gui;
  }

  @Override
  public void onFocusChanged(boolean boolean_1, boolean hasFocus) {
    if (hasFocus && !this.getSelected().isPresent() && this.getItemCount() > 0)
      setSelected(0);
  }

  public abstract static class Entry extends ItemListWidget.Item<Entry> {
  }

  public class CategoryEntry extends Entry implements Comparable<CategoryEntry> {
    private final String name;
    private final int nameWidth;

    public CategoryEntry(String name) {
      this.name = name;
      this.nameWidth = GameRuleListWidget.this.client.textRenderer.getStringWidth(this.name);
    }

    @Override
    public void render(int listX, int listY, int width, int height, int x, int y, int integer_7,
        boolean selected, float partial) {
      TextRenderer textRenderer = GameRuleListWidget.this.client.textRenderer;
      float str =
          (float) (GameRuleListWidget.this.client.currentScreen.width / 2 - this.nameWidth / 2);
      textRenderer.draw(this.name, str, x + listY - 10, 0xFFFFFF);
    }

    @Override
    public int compareTo(CategoryEntry other) {
      return this.name.compareTo(other.name);
    }
  }

  public class GameRuleEntry extends Entry implements Comparable<GameRuleEntry> {
    private final String ruleName;
    private final GameRulesKeyHook ruleKey;
    private final GameRules.Value ruleValue;

    private final TextFieldWidget editField;
    private final ButtonWidget resetButton;

    private GameRuleEntry(String ruleName, GameRules.Key ruleKey, GameRules.Value ruleValue) {
      this.ruleName = ruleName;
      this.ruleKey = ((GameRulesKeyHook) ruleKey);
      this.ruleValue = ruleValue;
      this.editField =
          new TextFieldWidget(GameRuleListWidget.this.client.textRenderer, 0, 0, 75, 16);
      this.editField.setText(this.ruleKey.getDefaultValue());
      this.resetButton = new ButtonWidget(0, 0, 50, 20, Util.translate("controls.reset", "Reset"),
          buttonWidget -> {
            this.ruleValue.set(this.ruleKey.getDefaultValue(), null);
          });
    }

    @Override
    public void render(int listX, int listY, int width, int height, int x, int y, int integer_7,
        boolean selected, float partial) {
      int n2 = listY + x / 2;
      GameRuleListWidget.this.client.textRenderer.draw(this.ruleName,
          width + 90 - GameRuleListWidget.this.maxStringWidth, n2 - 9 / 2, 0xFFFFFF);
      this.resetButton.x = width + 190;
      this.resetButton.y = listY - 2;
      this.resetButton.active = this.editField.getText().equals(this.ruleValue.getString());
      this.resetButton.render(y, integer_7, partial);
      ((TextFieldPosAccessor) this.editField).cg$setXPos(width + 105);
      ((TextFieldPosAccessor) this.editField).cg$setYPos(listY);
      this.editField.render(y, integer_7, partial);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
      return this.editField.mouseClicked(mouseX, mouseY, button)
          || this.resetButton.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
      return this.editField.mouseReleased(mouseX, mouseY, button)
          || this.resetButton.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public int compareTo(GameRuleEntry other) {
      return this.ruleName.compareTo(other.ruleName);
    }
  }
}
