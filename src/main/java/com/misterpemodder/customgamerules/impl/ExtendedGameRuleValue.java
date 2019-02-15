package com.misterpemodder.customgamerules.impl;

import java.util.function.BiConsumer;
import com.misterpemodder.customgamerules.rule.IGameRuleType;
import com.misterpemodder.customgamerules.rule.IGameRuleValue;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

public class ExtendedGameRuleValue<V> extends GameRules.Value {
  public IGameRuleValue<V> value;
  public IGameRuleType<V> type;

  public ExtendedGameRuleValue(IGameRuleValue<V> value) {
    this(value, (s, v) -> {
    });
  }

  public ExtendedGameRuleValue(IGameRuleValue<V> value,
      BiConsumer<MinecraftServer, GameRules.Value> onUpdate) {
    super(value.getGameRuleType().stringify(value.getValue()), value.getGameRuleType().getMcType(),
        onUpdate);
    this.value = value;
    this.type = value.getGameRuleType();
  }

  @Override
  public void set(String source, MinecraftServer server) {
    if (this.value == null) {
      super.set(source, server);
      return;
    }
    V value;
    try {
      value = this.type.parse(source);
    } catch (NumberFormatException e) {
      value = this.type.getDefaultValue();
    }
    if (!this.type.isValidValue(value))
      value = this.type.getDefaultValue();
    this.value.setValue(value, server);
    super.set(this.type.stringify(value), server);
  }
}
