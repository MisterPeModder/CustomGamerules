package com.misterpemodder.customgamerules.impl.rule;

import java.util.function.BiConsumer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

/**
 * Used as way to deferenciate between vanilla and EG GameRule keys.
 */
public abstract class BaseGameRuleKey extends GameRules.Key {
  public BaseGameRuleKey(String defaultValue, GameRules.Type type) {
    super(defaultValue, type);
  }

  public BaseGameRuleKey(String defaultValue, GameRules.Type type,
      BiConsumer<MinecraftServer, GameRules.Value> onUpdate) {
    super(defaultValue, type, onUpdate);
  }
}
