package com.misterpemodder.customgamerules.impl;

import com.misterpemodder.customgamerules.hook.GameRulesKeyHook;
import com.misterpemodder.customgamerules.rule.IGameRuleType;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Value;

public class ExtendedGameRuleKey<V> extends GameRules.Key implements GameRulesKeyHook {
  public IGameRuleType<V> type;
  public final String modId;

  public ExtendedGameRuleKey(String modId, IGameRuleType<V> type) {
    super(type.stringify(type.getDefaultValue()), type.getMcType());
    this.type = type;
    this.modId = modId;
  }

  @Override
  public Value createValue() {
    return new ExtendedGameRuleValue<>(this.type.createValue());
  }

  @Override
  public String getModId() {
    return this.modId;
  }

  @Override
  public String getTypeName() {
    return this.type.getTypeName();
  }
}
