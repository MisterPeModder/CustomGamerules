package com.misterpemodder.customgamerules.api.registry;

import javax.annotation.Nullable;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.value.ValueUpdateHandler;
import com.misterpemodder.customgamerules.api.rule.value.ValueValidator;
import com.misterpemodder.customgamerules.impl.registry.GameRuleRegistryImpl;
import com.misterpemodder.customgamerules.impl.registry.GameRuleRegistryImpl.GameRuleSettingsImpl;
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
   * Same as {@link GameRuleRegistry#register(String, String, GameRuleSettings)} with
   * {@code settings = GameRuleSettings.of(type)}
   * 
   * @param <T>   The underlying value type.
   * @param modId The registering mod id.
   * @param name  The game rule name.
   * @param type  Its type, if not registered this method
   *                     will throw an {@link UnregisteredGameRuleTypeException}.
   * @return the registered rule.
   */
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleType<T> type);

  /**
   * Registers a custom game rule.
   * 
   * @param <T>          The underlying value type.
   * @param modId        The registering mod id.
   * @param name         The game rule name.
   * @param settings     The rule settings, see {@link GameRuleSettings} for more details.
   * @return the registered rule.
   */
  public <T> GameRuleKey<T> register(String modId, String name, GameRuleSettings<T> settings);

  public static interface GameRuleSettings<T> {
    public static <T> GameRuleSettings<T> of(GameRuleType<T> type) {
      return new GameRuleSettingsImpl<T>(type);
    }

    public static <T> GameRuleSettings<T> copy(GameRuleSettings<T> original) {
      return GameRuleSettingsImpl.copy(original);
    }

    public static <T> GameRuleSettings<T> copy(GameRuleKey<T> key) {
      return GameRuleSettingsImpl.copy(key);
    }

    public GameRuleSettings<T> defaultValue(T defaultValue);

    public GameRuleSettings<T> onUpdate(ValueUpdateHandler<T> onUpdate);

    public GameRuleSettings<T> validator(ValueValidator<T> validator);

    public GameRuleSettings<T> description(@Nullable String key);

    public GameRuleType<T> getType();

    public T getDefaultValue();

    public ValueUpdateHandler<T> getUpdateHandler();

    public ValueValidator<T> getValidator();

    @Nullable
    public String getDescription();
  }

  public static class UnregisteredGameRuleTypeException extends RuntimeException {
    private static final long serialVersionUID = 15264932501L;

    public UnregisteredGameRuleTypeException(String name, String modId, GameRuleType<?> type) {
      super("Couldn't register gamerule " + modId + ":" + name + ", type " + type.getTypeName()
          + " is not registered.");
    }
  }
}
