package com.misterpemodder.customgamerules.impl.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import com.misterpemodder.customgamerules.api.CustomGameRules;
import com.misterpemodder.customgamerules.api.registry.GameRuleRegistry;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.value.ValueUpdateHandler;
import com.misterpemodder.customgamerules.api.rule.value.ValueValidator;
import com.misterpemodder.customgamerules.impl.Constants;
import com.misterpemodder.customgamerules.impl.command.DynamicGameRuleCommand;
import com.misterpemodder.customgamerules.impl.rule.GameRuleExtensions;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.GameRules;

public class GameRuleRegistryImpl implements GameRuleRegistry {
  private static boolean warnOnRegister = true;
  private static String defaultNamspace = Constants.UNKNOWN_MOD_ID;

  public static void setDefaultNamespace(String value) {
    defaultNamspace = value;
    warnOnRegister = value.equals(Constants.UNKNOWN_MOD_ID);
  }

  public static Registry<GameRuleType<?>> createTypeRegistry() {
    return new SimpleRegistry<>();
  }

  @Override
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleType<T> type) {
    return register(modId, name, type, type.getDefaultValue(), ValueUpdateHandler.noUpdate(),
        ValueValidator.alwaysValid());
  }

  @Override
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleType<T> type,
      T defaultValue) {
    return register(modId, name, type, defaultValue, ValueUpdateHandler.noUpdate(),
        ValueValidator.alwaysValid());
  }

  @Override
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleType<T> type,
      T defaultValue, ValueValidator<T> validator) {
    return register(modId, name, type, defaultValue, ValueUpdateHandler.noUpdate(), validator);
  }

  @Override
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleType<T> type,
      T defaultValue, ValueUpdateHandler<T> onUpdate) {
    return register(modId, name, type, defaultValue, onUpdate, ValueValidator.alwaysValid());
  }

  @Override
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleType<T> type,
      T defaultValue, ValueUpdateHandler<T> onUpdate, ValueValidator<T> validator) {
    if (GameRuleRegistry.TYPE_REGISTRY.getId(type) == null)
      throw new IllegalArgumentException("Couldn't register gamerule " + modId + ":" + name
          + ", type " + type.getTypeName() + " is not registered.");
    GameRuleKey<T> key =
        GameRuleExtensions.newKey(modId, name, type, defaultValue, onUpdate, validator);
    DynamicGameRuleCommand.addGameRule(name, type);
    return key;
  }

  public static class ExtraGameRulesKeyMap extends TreeMap<String, GameRules.Key> {
    private static final long serialVersionUID = 6560184692110613856L;

    private GameRules.Key getExtendedKey(String name, GameRules.Key key) {
      if (!GameRuleExtensions.isKeyInitialized(key)) {
        if (GameRuleRegistryImpl.warnOnRegister)
          Constants.LOGGER.warn("[CustomGameRules API]: game rule " + name
              + " does not have CustomGameRules metadata, registering as " + defaultNamspace + ":"
              + name);
        GameRuleExtensions.initKeyDefault(defaultNamspace, name, key);
      }
      return key;
    }

    @Override
    public GameRules.Key put(String key, GameRules.Key value) {
      return super.put(key, getExtendedKey(key, value));
    }

    @Override
    public void putAll(Map<? extends String, ? extends GameRules.Key> map) {
      Map<String, GameRules.Key> rules = new HashMap<>();
      for (Map.Entry<? extends String, ? extends GameRules.Key> entry : map.entrySet())
        rules.put(entry.getKey(), getExtendedKey(entry.getKey(), entry.getValue()));
      super.putAll(rules);
    }

    @Override
    public GameRules.Key putIfAbsent(String key, GameRules.Key value) {
      return super.putIfAbsent(key, getExtendedKey(key, value));
    }
  }

  public static class ExtraGameRulesValueMap extends TreeMap<String, GameRules.Value> {
    private static final long serialVersionUID = 9116761080481297083L;

    private GameRules.Value getExtendedKey(String name, GameRules.Value value) {
      if (!GameRuleExtensions.isValueInitialized(value)) {
        GameRuleKey<?> key = CustomGameRules.getKeys().get(name);
        if (key == null)
          throw new IllegalStateException(
              "trying to put a game rule value with the unregistered key '" + name + "'");
        GameRuleExtensions.initValueDefault(key, value);
      }
      return value;
    }

    @Override
    public GameRules.Value put(String key, GameRules.Value value) {
      return super.put(key, getExtendedKey(key, value));
    }

    @Override
    public void putAll(Map<? extends String, ? extends GameRules.Value> map) {
      Map<String, GameRules.Value> rules = new HashMap<>();
      for (Map.Entry<? extends String, ? extends GameRules.Value> entry : map.entrySet())
        rules.put(entry.getKey(), getExtendedKey(entry.getKey(), entry.getValue()));
      super.putAll(rules);
    }

    @Override
    public GameRules.Value putIfAbsent(String key, GameRules.Value value) {
      return super.putIfAbsent(key, getExtendedKey(key, value));
    }
  }
}
