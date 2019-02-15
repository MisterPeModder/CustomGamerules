package com.misterpemodder.customgamerules;

import java.util.Collections;
import java.util.Map;
import com.misterpemodder.customgamerules.impl.ExtendedGameRuleKey;
import com.misterpemodder.customgamerules.impl.ModGameRuleKey;
import com.misterpemodder.customgamerules.rule.IGameRuleType;
import net.minecraft.world.GameRules;

public class GameRuleRegistry {
  public static final GameRuleRegistry INSTANCE = new GameRuleRegistry();

  public void registerGameRule(String modId, String name, IGameRuleType<?> type) {
    GameRules.getKeys().put(name, new ExtendedGameRuleKey<>(modId, type));
  }

  public void registerGameRule(String modId, String name, String defaultValue,
      GameRules.Type type) {
    GameRules.getKeys().put(name, new ModGameRuleKey(modId, defaultValue, type));
  }

  public Map<String, GameRules.Key> entries() {
    return Collections.unmodifiableMap(GameRules.getKeys());
  }
}
