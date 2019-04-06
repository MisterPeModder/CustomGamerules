package com.misterpemodder.customgamerules.api.rule.key;

import javax.annotation.Nullable;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.value.GameRuleValue;
import net.minecraft.server.MinecraftServer;

public interface GameRuleKey<V> {
  String getName();

  String getModId();

  GameRuleType<V> getType();

  String getDefaultValueAsString();

  V getDefaultValue();

  void onValueUpdate(@Nullable MinecraftServer server, GameRuleValue<V> value);

  boolean isValidValue(V value);

  GameRuleValue<V> createValue();
}
