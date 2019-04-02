package com.misterpemodder.customgamerules.impl.gui.widget;

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
import com.misterpemodder.customgamerules.impl.gui.EditGameRulesScreen;
import com.misterpemodder.customgamerules.impl.hook.GameRulesKeyHook;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ItemListWidget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;

public class GameRuleListWidget extends ItemListWidget<GameRuleListWidget.ListItem> {
  private EditGameRulesScreen gui;
  private int selectedId;
  private int maxStringWidth;

  private final Map<String, Set<GameRuleListItem>> gamerules;
  private static final Pattern SEARCH_PATTERN = Pattern.compile("^(@\\s*([^\\s]*))?((\\s*)(.*))?");

  private String previousModTerm = "";
  private String previousSearchTerm = "";

  public GameRuleListWidget(EditGameRulesScreen gui, MinecraftClient client, int width, int height,
      int y1, int y2, int entryHeight, Supplier<String> filter) {
    super(client, width, height, y1, y2, entryHeight);
    this.gui = gui;
    this.gamerules = getGamerules();
    this.selectedId = -1;
    filter(filter);
    this.maxStringWidth = 0;
    for (Set<GameRuleListItem> rules : this.gamerules.values())
      for (GameRuleListItem rule : rules)
        this.maxStringWidth =
            Math.max(this.maxStringWidth, this.client.textRenderer.getStringWidth(rule.ruleName));
  }

  private Map<String, Set<GameRuleListItem>> getGamerules() {
    TreeMap<String, GameRules.Key> rules = GameRules.getKeys();
    if (rules == null)
      return new HashMap<>();
    Map<String, String> modIdToName =
        FabricLoader.getInstance().getAllMods().stream().map(m -> m.getMetadata())
            .collect(Collectors.toMap(ModMetadata::getId, ModMetadata::getName));
    modIdToName.put("minecraft", "Minecraft");
    Map<String, Set<GameRuleListItem>> ret = new TreeMap<>();
    for (Map.Entry<String, GameRules.Key> rule : rules.entrySet()) {
      GameRules.Key key = rule.getValue();
      String modId = ((GameRulesKeyHook) key).getModId();
      if (modId == null || modId.isEmpty())
        modId = Util.translate("customgamerules.mod.unknown", "Unknown");
      String modName = modIdToName.get(modId);
      if (modName == null)
        modName = modId;
      Set<GameRuleListItem> entries = ret.get(modName);
      if (entries == null) {
        entries = new TreeSet<>();
        ret.put(modName, entries);
      }
      if (key.getType() == GameRules.Type.BOOLEAN)
        entries.add(new SelectionGameRuleListItem(this.client, rule.getKey(), key,
            this.gui.rules.get(rule.getKey()), new String[] {"true", "false"},
            ((GameRulesKeyHook) key).getDefaultValue().equalsIgnoreCase("true") ? 0 : 1));
      else
        entries.add(new FieldGameRuleListItem(client, rule.getKey(), key,
            this.gui.rules.get(rule.getKey())));
    }
    return ret;
  }

  public void setRenderSelection(boolean renderSelection) {
    this.renderSelection = renderSelection;
  }

  @Override
  protected void renderList(int int_1, int int_2, int int_3, int int_4, float float_1) {
    for (Set<GameRuleListItem> values : this.gamerules.values())
      for (GameRuleListItem ruleListItem : values)
        ruleListItem.setMaxStringWidth(this.maxStringWidth);
    super.renderList(int_1, int_2, int_3, int_4, float_1);
  }

  @Override
  public boolean keyPressed(int keyCode, int int_2, int int_3) {
    return getSelected().map(i -> i.keyPressed(keyCode, int_2, int_3)).get()
        || super.keyPressed(keyCode, int_2, int_3);
  }

  @Override
  public boolean keyReleased(int keyCode, int int_2, int int_3) {
    return getSelected().map(i -> i.keyReleased(keyCode, int_2, int_3)).get()
        || super.keyReleased(keyCode, int_2, int_3);
  }

  @Override
  public boolean charTyped(char chr, int keyCode) {
    return getSelected().map(i -> i.charTyped(chr, keyCode)).get() || super.charTyped(chr, keyCode);
  }

  /**
   * @return true if the filtered list item changed.
   */
  public boolean filter(final Supplier<String> filter) {
    this.clearItems();
    String text = filter.get();
    Matcher matcher = SEARCH_PATTERN.matcher((text == null ? "" : text).trim());
    final String mod;
    final String term;
    if (matcher.matches()) {
      mod = Util.nonNullString(matcher.group(2));
      term = Util.nonNullString(matcher.group(5));
    } else {
      mod = "";
      term = "";
    }

    this.gamerules.entrySet().stream().filter(e -> !e.getValue().isEmpty()
        && Util.containsLowerCase(e.getKey().replaceAll("\\s", ""), mod)).forEach(e -> {
          List<GameRuleListItem> toAdd = new ArrayList<>();
          e.getValue().stream().filter(rule -> Util.containsLowerCase(rule.ruleName, term))
              .forEachOrdered(toAdd::add);
          if (!toAdd.isEmpty()) {
            addItem(new CategoryListItem(this.client, e.getKey()));
            toAdd.forEach(this::addItem);
          }
        });
    boolean changed = !(this.previousModTerm.equalsIgnoreCase(mod)
        && this.previousSearchTerm.equalsIgnoreCase(term));
    this.previousModTerm = mod;
    this.previousSearchTerm = term;
    return changed;
  }

  @Override
  protected int getScrollbarPosition() {
    return super.getScrollbarPosition() + 20;
  }

  @Override
  protected boolean isFocused() {
    return this.gui.getFocused() == this;
  }

  public void tick() {
    children().forEach(ListItem::tick);
  }

  public Optional<ListItem> setSelected(int index) {
    Optional<ListItem> previous = this.getSelected();
    if (previous.isPresent())
      previous.get().setFocused(false);
    this.selectedId = index;
    Optional<ListItem> current = this.getSelected();
    if (current.isPresent()) {
      children().forEach(l -> l.setFocused(false));
      current.get().setFocused(true);
    }
    return current;
  }

  @Override
  public boolean mouseClicked(double double_1, double double_2, int int_1) {
    for (ListItem item : children())
      if (item.mouseClicked(double_1, double_2, int_1))
        return true;
    return super.mouseClicked(double_1, double_2, int_1);
  }

  // moveSelection()
  @Override
  protected void method_20069(int index) {
    Optional<ListItem> selected =
        setSelected(MathHelper.clamp(this.selectedId + index, 0, this.getItemCount() - 1));
    //scrolls to 'selected'
    selected.ifPresent(this::method_20072);
  }

  @Override
  public boolean isPartOfFocusCycle() {
    return true;
  }

  public Optional<ListItem> getSelected() {
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

  public abstract static class ListItem extends ItemListWidget.Item<ListItem> {
    public void setFocused(boolean focused) {
    }

    public void tick() {
    }
  }
}
