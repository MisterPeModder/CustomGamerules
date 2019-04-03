package com.misterpemodder.customgamerules.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import com.misterpemodder.customgamerules.api.GameRuleRegistry;
import com.misterpemodder.customgamerules.api.rule.GameRuleType;
import com.misterpemodder.customgamerules.impl.command.DynamicGameRuleCommand;
import com.misterpemodder.customgamerules.impl.hook.GameRulesKeyHook;
import com.misterpemodder.customgamerules.impl.rule.BaseGameRuleKey;
import com.misterpemodder.customgamerules.impl.rule.ExtendedGameRuleKey;
import com.misterpemodder.customgamerules.impl.rule.ModGameRuleKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Key;

public class GameRuleRegistryImpl implements GameRuleRegistry {
  public static final String UNKOWN_MOD_ID = "unknown";
  public static final String UNKOWN_MOD_NAME = "Unknown";
  public static final String UNKOWN_MOD_KEY = "customgamerules.mod.unknown";

  @Override
  public void registerGameRule(String modId, String name, GameRuleType<?> type) {
    GameRules.getKeys().put(name,
        new ExtendedGameRuleKey<>(StringUtil.defaulted(modId, UNKOWN_MOD_ID), type));
    DynamicGameRuleCommand.addGameRule(name, type.getMcType());
  }

  @Override
  public void registerGameRule(String modId, String name, String defaultValue,
      GameRules.Type type) {
    GameRules.getKeys().put(name,
        new ModGameRuleKey(StringUtil.defaulted(modId, UNKOWN_MOD_ID), defaultValue, type));
    DynamicGameRuleCommand.addGameRule(name, type);
  }

  @Override
  public void registerGameRule(String modId, String name, String defaultValue, GameRules.Type type,
      BiConsumer<MinecraftServer, GameRules.Value> onUpdate) {
    GameRules.getKeys().put(name, new ModGameRuleKey(StringUtil.defaulted(modId, UNKOWN_MOD_ID),
        defaultValue, type, onUpdate));
    DynamicGameRuleCommand.addGameRule(name, type);
  }

  @Override
  public Map<String, GameRules.Key> entries() {
    return Collections.unmodifiableMap(GameRules.getKeys());
  }

  public static class ExtraGameRulesKeyMap extends TreeMap<String, GameRules.Key> {
    private static final long serialVersionUID = 6560184692110613856L;

    private String defaultNamespace;

    public ExtraGameRulesKeyMap() {
      super();
      setDefaultNamespace("");
    }

    public void setDefaultNamespace(String defaultNamespace) {
      this.defaultNamespace = StringUtil.defaulted(defaultNamespace, UNKOWN_MOD_ID);

    }

    private GameRules.Key getExtendedKey(GameRules.Key key) {
      if (!(key instanceof BaseGameRuleKey)) {
        key = new ModGameRuleKey(this.defaultNamespace, ((GameRulesKeyHook) key).getDefaultValue(),
            key.getType());
      }
      return key;
    }

    @Override
    public Key put(String key, GameRules.Key value) {
      return super.put(key, getExtendedKey(value));
    }

    @Override
    public void putAll(Map<? extends String, ? extends GameRules.Key> map) {
      Map<String, GameRules.Key> rules = new HashMap<>();
      for (Map.Entry<? extends String, ? extends GameRules.Key> entry : map.entrySet())
        rules.put(entry.getKey(), getExtendedKey(entry.getValue()));
      super.putAll(rules);
    }

    @Override
    public Key putIfAbsent(String key, Key value) {
      return super.putIfAbsent(key, getExtendedKey(value));
    }
  }
}
