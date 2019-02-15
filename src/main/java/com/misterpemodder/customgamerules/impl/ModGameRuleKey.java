package com.misterpemodder.customgamerules.impl;

import com.misterpemodder.customgamerules.hook.GameRulesKeyHook;
import net.minecraft.world.GameRules;

public class ModGameRuleKey extends GameRules.Key implements GameRulesKeyHook {
  public final String modId;

  public ModGameRuleKey(String modId, String defaultValue, GameRules.Type type) {
    super(defaultValue, type);
    this.modId = modId;
  }

  @Override
  public String getModId() {
    return this.modId;
  }
}
