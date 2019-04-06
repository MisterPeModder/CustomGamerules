package com.misterpemodder.customgamerules.api.rule.value;

import javax.annotation.Nullable;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import net.minecraft.server.MinecraftServer;

/**
 * Holds a GameRule's value.
 * 
 * @param <V> the value type.
 */
public interface GameRuleValue<V> {
  GameRuleKey<V> getKey();

  /**
   * @return This GameRule's current value.
   */
  V get();

  /**
   * @param value  the new value.
   * @param server the MinecraftServer instance, may be null.
   * @param <T>    the value type.
   */
  <T extends V> void set(T value, @Nullable MinecraftServer server);

  /**
   * @return Its type.
   */
  GameRuleType<V> getType();
}
