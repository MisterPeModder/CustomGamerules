package com.misterpemodder.customgamerules.impl.hook;

import com.misterpemodder.customgamerules.impl.GameRuleRegistryImpl;
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
    return GameRuleRegistryImpl.UNKOWN_MOD_ID;
  }

  /**
   * @return The string representation of the gamerule's type.
   */
  default String getTypeName() {
    return "";
  }
}
