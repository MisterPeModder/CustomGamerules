package com.misterpemodder.customgamerules.impl;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;
import com.misterpemodder.customgamerules.api.GameRuleRegistry;
import com.misterpemodder.customgamerules.api.rule.GameRuleType;
import com.misterpemodder.customgamerules.impl.rule.ExtendedGameRuleKey;
import com.misterpemodder.customgamerules.impl.rule.ModGameRuleKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

public class GameRuleRegistryImpl implements GameRuleRegistry {
  @Override
  public void registerGameRule(String modId, String name, GameRuleType<?> type) {
    GameRules.getKeys().put(name, new ExtendedGameRuleKey<>(modId, type));
  }

  @Override
  public void registerGameRule(String modId, String name, String defaultValue,
      GameRules.Type type) {
    GameRules.getKeys().put(name, new ModGameRuleKey(modId, defaultValue, type));
  }

  @Override
  public void registerGameRule(String modId, String name, String defaultValue, GameRules.Type type,
      BiConsumer<MinecraftServer, GameRules.Value> onUpdate) {
    GameRules.getKeys().put(name, new ModGameRuleKey(modId, defaultValue, type, onUpdate));
  }

  @Override
  public Map<String, GameRules.Key> entries() {
    return Collections.unmodifiableMap(GameRules.getKeys());
  }
}
