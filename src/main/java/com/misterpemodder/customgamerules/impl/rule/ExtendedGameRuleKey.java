package com.misterpemodder.customgamerules.impl.rule;

import com.misterpemodder.customgamerules.api.rule.GameRuleType;
import com.misterpemodder.customgamerules.impl.hook.GameRulesKeyHook;
import net.minecraft.world.GameRules.Value;

public class ExtendedGameRuleKey<V> extends BaseGameRuleKey implements GameRulesKeyHook {
  public GameRuleType<V> type;
  public final String modId;

  public ExtendedGameRuleKey(String modId, GameRuleType<V> type) {
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
