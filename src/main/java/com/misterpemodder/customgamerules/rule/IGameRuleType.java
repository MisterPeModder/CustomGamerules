package com.misterpemodder.customgamerules.rule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.GameRules.Type;

public interface IGameRuleType<V> {
  /**
   * Retrives the GameRule's default value. This value may mary.
   * 
   * @return the default value, {@code isValidValue(getDefaultValue())} must always return true.
   */
  @Nonnull
  V getDefaultValue();

  IGameRuleValue<V> createValue();

  /**
   * Returns the vanilla GameRule type. If you use a custom type, return {@link Type#STRING}.
   * 
   * @return The vanilla gameruleType.
   */
  Type getMcType();

  /**
   * Attemps to parse a value of type V
   * 
   * @param source
   * @return
   * @throws NumberFormatException
   */
  @Nullable
  V parse(String source) throws NumberFormatException;

  <T extends V> String stringify(T value);

  default <T extends V> boolean isValidValue(T value) {
    return true;
  }

  /**
   * Returns the string representation of the current type.
   */
  String getTypeName();
}
