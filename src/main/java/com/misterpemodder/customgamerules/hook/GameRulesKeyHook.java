package com.misterpemodder.customgamerules.hook;

import net.minecraft.world.GameRules;

/**
 * Implemented by {@link GameRules.Key}
 */
public interface GameRulesKeyHook {
  /**
   * Retrieve the default value associated with this Key.
   * 
   * @return the (stringified) default value.
   */
  default String getDefaultValue() {
    return "";
  }

  /**
   * Retrieve the mod that registered this gamerule.
   * 
   * @return the mod id.
   */
  default String getModId() {
    return "minecraft";
  }

  /**
   * Returns the string representation of the gamerule's type.
   */
  default String getTypeName() {
    return "";
  }
}
