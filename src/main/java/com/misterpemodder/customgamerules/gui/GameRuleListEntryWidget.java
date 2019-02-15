package com.misterpemodder.customgamerules.gui;

import java.util.Arrays;
import java.util.List;
import com.google.common.collect.ImmutableMap;
import com.misterpemodder.customgamerules.hook.GameRulesKeyHook;
import net.fabricmc.loader.FabricLoader;
import net.fabricmc.loader.ModContainer;
import net.fabricmc.loader.ModInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.TextFormat;
import net.minecraft.world.GameRules;

@SuppressWarnings("deprecation")
public class GameRuleListEntryWidget extends EntryListWidget.Entry<GameRuleListEntryWidget> {
  private final String ruleName;
  private final GameRules.Key ruleKey;
  private final String modName;
  private final GameRuleListWidget list;
  private final MinecraftClient client;

  private static final ImmutableMap<String, String> MODID_TO_NAME;

  static {
    ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    // Using FabricLoader.INSTANCE because there is currently no way of querying mods using the API.
    for (ModContainer modContainer : FabricLoader.INSTANCE.getModContainers()) {
      ModInfo modInfo = modContainer.getInfo();
      builder.put(modInfo.getId(), modInfo.getName());
    }
    builder.put("minecraft", "Minecraft");
    MODID_TO_NAME = builder.build();
  }

  public GameRuleListEntryWidget(String ruleName, GameRules.Key ruleKey, MinecraftClient client,
      GameRuleListWidget list) {
    this.ruleName = ruleName;
    this.ruleKey = ruleKey;
    this.client = client;
    this.list = list;
    String modId = ((GameRulesKeyHook) this.ruleKey).getModId();
    String modName = MODID_TO_NAME.get(modId);
    this.modName = modName == null ? "unknown" : modName;
  }

  @Override
  public void draw(int width, int height, int var3, int var4, boolean selected, float delta) {
    if (this.list.selected == this) {
      int x = getX();
      int y = getY();
      Drawable.drawRect(x - 2, y - 2, x - 2 + width - 15, y - 2 + 18, 0xFF808080);
      Drawable.drawRect(x - 1, y - 1, x - 3 + width - 15, y - 3 + 18, 0xFF000000);
    }
    this.client.fontRenderer.draw(this.ruleName, this.getX() + 32 + 3, this.getY() + 1, 0xffffff);
  }

  @Override
  public int getX() {
    return super.getX();
  }

  @Override
  public int getY() {
    return super.getY();
  }

  @Override
  public boolean mouseClicked(double v, double v1, int i) {
    list.selected = this;
    return true;
  }

  public List<String> getTooltip() {
    return Arrays.asList(
        TextFormat.GOLD + "type: " + TextFormat.RESET
            + ((GameRulesKeyHook) this.ruleKey).getTypeName(),
        TextFormat.BLUE + "" + TextFormat.ITALIC + this.modName);
  }
}
