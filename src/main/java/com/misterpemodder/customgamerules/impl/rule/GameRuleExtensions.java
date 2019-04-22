package com.misterpemodder.customgamerules.impl.rule;

import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleTypes;
import com.misterpemodder.customgamerules.api.rule.value.GameRuleValue;
import com.misterpemodder.customgamerules.api.rule.value.ValueUpdateHandler;
import com.misterpemodder.customgamerules.api.rule.value.ValueValidator;
import com.misterpemodder.customgamerules.impl.Constants;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;


public final class GameRuleExtensions {
  @SuppressWarnings("unchecked")
  public static final GameRuleKey<Object> DUMMY_KEY =
      (GameRuleKey<Object>) (Object) newKey(Constants.UNKNOWN_MOD_ID, "dummy - report as bug",
          GameRuleTypes.STRING, "errored value - report as bug", ValueUpdateHandler.noUpdate(),
          ValueValidator.alwaysValid(), null);
  @SuppressWarnings("unchecked")
  public static final GameRuleType<Object> DUMMY_TYPE =
      (GameRuleType<Object>) (Object) GameRuleTypes.STRING;
  @SuppressWarnings("unchecked")
  public static final ValueUpdateHandler<Object> DUMMY_UPDATE_HANDLER =
      (ValueUpdateHandler<Object>) (Object) ValueUpdateHandler.NO_UPDATE;
  @SuppressWarnings("unchecked")
  public static final ValueValidator<Object> DUMMY_VALIDATOR =
      (ValueValidator<Object>) (Object) ValueValidator.ALWAYS_VALID;


  public static boolean isKeyInitialized(GameRules.Key key) {
    return ((GameRuleExtensions.Key) key).cgr$isInit();
  }

  @SuppressWarnings("unchecked")
  public static <T> GameRuleKey<T> newKey(String modId, String ruleName, GameRuleType<T> type,
      T defaultValue, ValueUpdateHandler<T> onUpdate, ValueValidator<T> validator,
      String descriptionKey) {
    GameRules.Key mcKey = new GameRules.Key(null, null, null);
    ((GameRuleExtensions.Key) mcKey).cgr$initExtensions(modId, ruleName,
        (GameRuleType<Object>) type, defaultValue, (ValueUpdateHandler<Object>) onUpdate,
        (ValueValidator<Object>) validator, false, descriptionKey);
    return (GameRuleKey<T>) mcKey;
  }

  @SuppressWarnings("unchecked")
  public static GameRuleKey<Object> initKeyDefault(String modId, String name, GameRules.Key key) {
    ((GameRuleExtensions.Key) key).cgr$initExtensions(modId, name,
        (GameRuleType<Object>) getCGType(key.getType()), ((GameRuleKey<?>) key).getDefaultValue(),
        null, ValueValidator.alwaysValid(), true, "gamerule." + modId + "." + name + ".desc");
    return (GameRuleKey<Object>) key;
  }

  public static boolean isValueInitialized(GameRules.Value key) {
    return ((GameRuleExtensions.Value) key).cgr$isInit();
  }

  @SuppressWarnings("unchecked")
  public static <T> GameRuleValue<T> newValue(GameRuleType<T> type, GameRuleKey<T> key) {
    GameRules.Value mcValue =
        new GameRules.Value(key.getDefaultValueAsString(), getVanillaType(type), null);
    ((GameRuleExtensions.Value) mcValue).cgr$initExtensions((GameRuleType<Object>) type,
        (GameRuleKey<Object>) key);
    return (GameRuleValue<T>) mcValue;
  }

  @SuppressWarnings("unchecked")
  public static GameRuleValue<Object> initValueDefault(GameRuleKey<?> key, GameRules.Value value) {
    ((GameRuleExtensions.Value) value).cgr$initExtensions((GameRuleType<Object>) key.getType(),
        (GameRuleKey<Object>) key);
    return (GameRuleValue<Object>) value;
  }

  public static GameRules.Type getVanillaType(GameRuleType<?> type) {
    if (type == GameRuleTypes.BOOLEAN)
      return GameRules.Type.BOOLEAN;
    if (type == GameRuleTypes.INTEGER)
      return GameRules.Type.INTEGER;
    return GameRules.Type.STRING;
  }

  public static GameRuleType<?> getCGType(GameRules.Type type) {
    if (type == null)
      return GameRuleTypes.STRING;
    switch (type) {
      case BOOLEAN:
        return GameRuleTypes.BOOLEAN;
      case INTEGER:
        return GameRuleTypes.INTEGER;
      default:
        return GameRuleTypes.STRING;
    }
  }

  @SuppressWarnings("unchecked")
  public static BiConsumer<MinecraftServer, GameRules.Value> getVanillaUpdateHandler(
      ValueUpdateHandler<Object> handler) {
    return (s, v) -> handler.after(s, (GameRuleValue<Object>) v);
  }

  @SuppressWarnings("unchecked")
  public static BiConsumer<MinecraftServer, GameRules.Value> getVanillaUpdateHandler(
      GameRuleValue<Object> value) {
    return (s, v) -> value.getKey().onValueUpdate(s, (GameRuleValue<Object>) v);
  }

  public static ValueUpdateHandler<Object> getCGUpdateHandler(
      BiConsumer<MinecraftServer, GameRules.Value> handler) {
    return (s, v) -> handler.accept(s, (GameRules.Value) v);
  }

  public interface Key {
    void cgr$initExtensions(String modId, String ruleName, GameRuleType<Object> type,
        Object defaultValue, @Nullable ValueUpdateHandler<Object> onUpdate,
        ValueValidator<Object> validator, boolean cancelUpdateNoServer, String descriptionKey);

    boolean cgr$isInit();
  }

  public interface Value {
    void cgr$initExtensions(GameRuleType<Object> type, GameRuleKey<Object> key);

    boolean cgr$isInit();
  }
}
