package com.misterpemodder.customgamerules.api.registry;

import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.value.ValueUpdateHandler;
import com.misterpemodder.customgamerules.api.rule.value.ValueValidator;
import com.misterpemodder.customgamerules.impl.registry.GameRuleRegistryImpl;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface GameRuleRegistry {
  public static final GameRuleRegistry INSTANCE = new GameRuleRegistryImpl();
  public static final Identifier TYPE_REGISTRY_ID =
      new Identifier("custom-gamerules", "gamerule_type");
  public static final Registry<GameRuleType<?>> TYPE_REGISTRY =
      GameRuleRegistryImpl.createTypeRegistry();

  /**
  * Registers a custom game rule type.
  * 
  * @param <T>  The underlying value type.
  * @param id   The type id.
  * @param type The type to register.
  * @return the registered type.
  */
  public static <T> GameRuleType<T> registerType(Identifier id, GameRuleType<T> type) {
    return Registry.register(TYPE_REGISTRY, id, type);
  }

  /**
   * Registers a custom game rule.
   * The default value will be set to the type default.
   * 
   * @param <T>   The underlying value type.
   * @param modId The registering mod id.
   * @param name  The game rule name.
   * @param type  Its type, if not registered this method
   *                     will throw an {@link IllegalArgumentException}.
   * @return the registered type.
   */
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleType<T> type);

  /**
   * Registers a custom game rule.
   * 
   * @param <T>          The underlying value type.
   * @param modId        The registering mod id.
   * @param name         The game rule name.
   * @param type         Its type, if not registered this method
   *                     will throw an {@link IllegalArgumentException}.
   * @param defaultValue The default value.
   * @return the registered type.
   */
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleType<T> type,
      T defaultValue);

  /**
   * Registers a custom game rule.
   * 
   * @param <T>          The underlying value type.
   * @param modId        The registering mod id.
   * @param name         The game rule name.
   * @param type         Its type, if not registered this method
   *                     will throw an {@link IllegalArgumentException}.
   * @param defaultValue The default value.
   * @param onUpdate     Called after the value is set, the server argument may be {@code null}.
   * @return the registered type.
   * 
   * @throws IllegalArgumentException if {@code type} is not registered.
   */
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleType<T> type,
      T defaultValue, ValueUpdateHandler<T> onUpdate);

  /**
   * Registers a custom game rule.
   * 
   * @param <T>          The underlying value type.
   * @param modId        The registering mod id.
   * @param name         The game rule name.
   * @param type         Its type, if not registered this method
   *                     will throw an {@link IllegalArgumentException}.
   * @param validator    The validator
   * @param defaultValue The default value.
   * @param validator    The validator.
   * @return the registered type.
  */
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleType<T> type,
      T defaultValue, ValueValidator<T> validator);

  /**
   * Registers a custom game rule.
   * 
   * @param <T>          The underlying value type.
   * @param modId        The registering mod id.
   * @param name         The game rule name.
   * @param type         Its type, if not registered this method
   *                     will throw an {@link IllegalArgumentException}.
   * @param validator    The validator
   * @param defaultValue The default value.
   * @param onUpdate     Called after the value is set, the server argument may be {@code null}.
   * @return the registered type.
   */
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleType<T> type,
      T defaultValue, ValueUpdateHandler<T> onUpdate, ValueValidator<T> validator);
}
