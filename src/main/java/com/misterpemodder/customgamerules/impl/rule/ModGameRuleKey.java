package com.misterpemodder.customgamerules.impl.rule;

import java.util.function.BiConsumer;
import com.misterpemodder.customgamerules.impl.hook.GameRulesKeyHook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

public class ModGameRuleKey extends BaseGameRuleKey implements GameRulesKeyHook {
  public final String modId;

  public ModGameRuleKey(String modId, String defaultValue, GameRules.Type type) {
    super(defaultValue, type);
    this.modId = modId;
  }

  public ModGameRuleKey(String modId, String defaultValue, GameRules.Type type,
      BiConsumer<MinecraftServer, GameRules.Value> onUpdate) {
    super(defaultValue, type, onUpdate);
    this.modId = modId;
  }

  @Override
  public String getModId() {
    return this.modId;
  }
}
