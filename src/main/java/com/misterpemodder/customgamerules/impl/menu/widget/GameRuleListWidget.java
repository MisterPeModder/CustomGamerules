package com.misterpemodder.customgamerules.impl.menu.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.misterpemodder.customgamerules.api.CustomGameRules;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleTypes;
import com.misterpemodder.customgamerules.impl.Constants;
import com.misterpemodder.customgamerules.impl.StringUtil;
import com.misterpemodder.customgamerules.impl.menu.EditGameRulesScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ItemListWidget;
import net.minecraft.util.math.MathHelper;

public class GameRuleListWidget extends ItemListWidget<GameRuleListWidget.ListItem> {
  public final EditGameRulesScreen gui;
  private int selectedId;
  private int maxStringWidth;

  private final Map<String, Set<GameRuleListItem<?>>> gamerules;
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
    for (Set<GameRuleListItem<?>> rules : this.gamerules.values())
      for (GameRuleListItem<?> rule : rules)
        this.maxStringWidth =
            Math.max(this.maxStringWidth, this.client.textRenderer.getStringWidth(rule.ruleName));
  }

  private Map<String, Set<GameRuleListItem<?>>> getGamerules() {
    TreeMap<String, GameRuleKey<?>> rules = CustomGameRules.getKeys();
    if (rules == null)
      return new HashMap<>();
    Map<String, String> modIdToName =
        FabricLoader.getInstance().getAllMods().stream().map(m -> m.getMetadata())
            .collect(Collectors.toMap(ModMetadata::getId, ModMetadata::getName));
    modIdToName.put(Constants.MC_MOD_ID, Constants.MC_MOD_NAME);
    modIdToName.put(Constants.UNKNOWN_MOD_ID,
        StringUtil.translate(Constants.UNKNOWN_MOD_KEY, Constants.UNKNOWN_MOD_NAME));
    Map<String, Set<GameRuleListItem<?>>> ret = new TreeMap<>();
    for (Map.Entry<String, GameRuleKey<?>> entry : rules.entrySet()) {
      GameRuleKey<?> key = entry.getValue();
      String modId = StringUtil.defaulted(key.getModId(),
          StringUtil.translate(Constants.UNKNOWN_MOD_KEY, Constants.UNKNOWN_MOD_NAME));
      String modName = modIdToName.get(modId);
      if (modName == null)
        modName = modId;
      Set<GameRuleListItem<?>> entries = ret.get(modName);
      if (entries == null) {
        entries = new TreeSet<>();
        ret.put(modName, entries);
      }
      if (key.getType() == GameRuleTypes.BOOLEAN)
        entries.add(SelectionGameRuleListItem.create(this, this.client, modName, key,
            this.gui.rules.get(entry.getKey()), new String[] {"true", "false"},
            key.getDefaultValueAsString().equalsIgnoreCase("true") ? 0 : 1));
      else
        entries.add(FieldGameRuleListItem.create(this, this.client, modName, key,
            this.gui.rules.get(entry.getKey())));
    }
    return ret;
  }

  @Override
  protected void renderList(int int_1, int int_2, int int_3, int int_4, float float_1) {
    for (Set<GameRuleListItem<?>> values : this.gamerules.values())
      for (GameRuleListItem<?> ruleListItem : values)
        ruleListItem.setMaxStringWidth(this.maxStringWidth);
    super.renderList(int_1, int_2, int_3, int_4, float_1);
  }

  @Override
  public boolean keyPressed(int keyCode, int int_2, int int_3) {
    return mapOnSelected(i -> i.keyPressed(keyCode, int_2, int_3))
        || super.keyPressed(keyCode, int_2, int_3);
  }

  @Override
  public boolean keyReleased(int keyCode, int int_2, int int_3) {
    return mapOnSelected(i -> i.keyReleased(keyCode, int_2, int_3))
        || super.keyReleased(keyCode, int_2, int_3);
  }

  @Override
  public boolean charTyped(char chr, int keyCode) {
    return mapOnSelected(i -> i.charTyped(chr, keyCode)) || super.charTyped(chr, keyCode);
  }

  private boolean mapOnSelected(Function<ListItem, Boolean> map) {
    Optional<Boolean> result = getSelected().map(map);
    return result.isPresent() && result.get();
  }

  /**
   * @param filter The filter.
   * @return true if the filtered list item changed.
   */
  public boolean filter(final Supplier<String> filter) {
    this.clearItems();
    String text = filter.get();
    Matcher matcher = SEARCH_PATTERN.matcher((text == null ? "" : text).trim());
    final String mod;
    final String term;
    if (matcher.matches()) {
      mod = StringUtil.nonNull(matcher.group(2));
      term = StringUtil.nonNull(matcher.group(5));
    } else {
      mod = "";
      term = "";
    }

    this.gamerules.entrySet().stream().filter(e -> !e.getValue().isEmpty()
        && StringUtil.containsLowerCase(e.getKey().replaceAll("\\s", ""), mod)).forEach(e -> {
          List<GameRuleListItem<?>> toAdd = new ArrayList<>();
          e.getValue().stream().filter(rule -> StringUtil.containsLowerCase(rule.ruleName, term))
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
      if (item.mouseClicked(double_1, double_2, int_1)) {
        Optional<ListItem> current = this.getSelected();
        int index = children().indexOf(item);
        setFocused(item);
        if (current.isPresent() && index >= 0 && index < this.getItemCount()) {
          moveSelection(index - children().indexOf(current.get()));
        }
        return true;
      }
    return super.mouseClicked(double_1, double_2, int_1);
  }

  @Override
  protected void moveSelection(int distance) {
    Optional<ListItem> selected =
        setSelected(MathHelper.clamp(this.selectedId + distance, 0, this.getItemCount() - 1));
    selected.ifPresent(this::ensureVisible);
  }

  public Optional<ListItem> getSelected() {
    return (this.selectedId < 0 || this.selectedId >= this.getItemCount()) ? Optional.empty()
        : Optional.of(this.children().get(this.selectedId));
  }

  public EditGameRulesScreen getParent() {
    return this.gui;
  }

  @Override
  public boolean changeFocus(boolean shiftPressed) {
    if (this.gui.getFocused() != this) {
      setSelected(1);
      return true;
    } else {
      List<? extends Element> siblings = this.gui.children();
      int index = (siblings.indexOf(this) + 1) % siblings.size();
      setSelected(-1);
      this.gui.setFocused(siblings.get(index));
      this.gui.changeFocus(shiftPressed);
    }
    return true;
  }

  public abstract static class ListItem extends ItemListWidget.Item<ListItem> {
    public void setFocused(boolean focused) {
    }

    public void tick() {
    }
  }
}
