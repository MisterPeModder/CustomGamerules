package com.misterpemodder.customgamerules.api.rule;

import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;

/**
 * Holds a GameRule's value.
 * 
 * @param <V> the value type.
 */
public interface GameRuleValue<V> {
  /**
   * @return This GameRule's current value.
   */
  V getValue();

  /**
   * @param value  the new value.
   * @param server the MinecraftServer instance, may be null.
   * @return The value that the GameRule will have.
   */
  <T extends V> void setValue(T value, @Nullable MinecraftServer server);

  /**
   * @return The GameRuleType.
   */
  GameRuleType<V> getGameRuleType();
}