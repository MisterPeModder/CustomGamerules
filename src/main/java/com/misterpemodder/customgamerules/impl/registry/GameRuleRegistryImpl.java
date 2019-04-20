package com.misterpemodder.customgamerules.impl.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import com.misterpemodder.customgamerules.api.CustomGameRules;
import com.misterpemodder.customgamerules.api.registry.GameRuleRegistry;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleTypes;
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
    return register(modId, name, GameRuleSettings.of(type));
  }

  @Override
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleSettings<T> settings) {
    GameRuleType<T> type = settings.getType();
    if (GameRuleRegistry.TYPE_REGISTRY.getId(type) == null)
      throw new IllegalArgumentException("Couldn't register gamerule " + modId + ":" + name
          + ", type " + type.getTypeName() + " is not registered.");
    String descriptionKey = settings.getDescription();
    GameRuleKey<T> key = GameRuleExtensions.newKey(modId, name, type, settings.getDefaultValue(),
        settings.getUpdateHandler(), settings.getValidator(),
        descriptionKey == null ? "gamerule." + modId + "." + name + ".desc" : descriptionKey);
    CustomGameRules.getKeys().put(name, key);
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

  public static class GameRuleSettingsImpl<T> implements GameRuleSettings<T> {
    private GameRuleType<T> type;
    private T defaultValue;
    private ValueUpdateHandler<T> onUpdate;
    private ValueValidator<T> validator;
    private String description;


    public GameRuleSettingsImpl(GameRuleType<T> type) {
      this.type = type;
    }

    @Override
    public GameRuleSettings<T> defaultValue(T defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    @Override
    public GameRuleSettings<T> onUpdate(ValueUpdateHandler<T> onUpdate) {
      this.onUpdate = onUpdate;
      return this;
    }

    @Override
    public GameRuleSettings<T> validator(ValueValidator<T> validator) {
      this.validator = validator;
      return this;
    }

    @Override
    public GameRuleSettings<T> description(String description) {
      this.description = description;
      return this;
    }

    public static <T> GameRuleSettings<T> copy(GameRuleSettings<T> o) {
      if (o instanceof GameRuleSettingsImpl) {
        GameRuleSettingsImpl<T> original = (GameRuleSettingsImpl<T>) o;
        GameRuleSettingsImpl<T> copy = new GameRuleSettingsImpl<>(original.type);
        copy.defaultValue = original.defaultValue;
        copy.onUpdate = original.onUpdate;
        copy.validator = original.validator;
        copy.description = original.description;
        return copy;
      } else {
        return GameRuleSettings.of(o.getType()).defaultValue(o.getDefaultValue())
            .onUpdate(o.getUpdateHandler()).validator(o.getValidator())
            .description(o.getDescription());
      }
    }

    public static <T> GameRuleSettings<T> copy(GameRuleKey<T> key) {
      return GameRuleSettings.of(key.getType()).defaultValue(key.getDefaultValue())
          .validator(key.getValidator()).description(key.getDescription());
    }

    private static <T> T defaulted(T value, T defaultValue) {
      return value == null ? defaultValue : value;
    }

    @Override
    public T getDefaultValue() {
      return defaulted(this.defaultValue, getType().getDefaultValue());
    }

    @Override
    public String getDescription() {
      return this.description;
    }

    @Override
    @SuppressWarnings("unchecked")
    public GameRuleType<T> getType() {
      return defaulted(this.type, (GameRuleType<T>) GameRuleTypes.STRING);
    }

    @Override
    public ValueUpdateHandler<T> getUpdateHandler() {
      return defaulted(this.onUpdate, ValueUpdateHandler.noUpdate());
    }

    @Override
    public ValueValidator<T> getValidator() {
      return defaulted(this.validator, ValueValidator.alwaysValid());
    }
  }
}
