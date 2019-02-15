package com.misterpemodder.customgamerules.api;

import java.util.Map;
import java.util.function.BiConsumer;
import com.misterpemodder.customgamerules.api.rule.GameRuleType;
import com.misterpemodder.customgamerules.impl.GameRuleRegistryImpl;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

public interface GameRuleRegistry {
  public static final GameRuleRegistry INSTANCE = new GameRuleRegistryImpl();

  /**
   * Register a custom gamerule.
   * 
   * @param modId the registering mod id.
   * @param name  the gamerule name.
   * @param type  its type.
   */
  public void registerGameRule(String modId, String name, GameRuleType<?> type);

  /**
   * Register a vanilla-like gamerule
   * 
   * @param modId        the registering mod id.
   * @param name         the gamerule name.
   * @param defaultValue the default (string) value.
   * @param type         its type.
   */
  public void registerGameRule(String modId, String name, String defaultValue, GameRules.Type type);

  /**
   * Register a vanilla-like gamerule
   * 
   * @param modId        the registering mod id.
   * @param name         the gamerule name.
   * @param defaultValue the default (string) value.
   * @param type         its type.
   * @param onUpdate     called when the gamerule value is updated.
   */
  public void registerGameRule(String modId, String name, String defaultValue, GameRules.Type type,
      BiConsumer<MinecraftServer, GameRules.Value> onUpdate);

  public Map<String, GameRules.Key> entries();
}
