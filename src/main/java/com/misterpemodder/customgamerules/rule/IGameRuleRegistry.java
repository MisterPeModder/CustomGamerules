package com.misterpemodder.customgamerules.rule;

import java.util.List;
import net.minecraft.world.GameRules;

public interface IGameRuleRegistry {
  public void registerGameRule(String modId, String name, GameRules.Type type);

  public void registerGameRule(String modId, String name, IGameRuleType<?> type);

  public List<GameRules.Key> entries();
}
