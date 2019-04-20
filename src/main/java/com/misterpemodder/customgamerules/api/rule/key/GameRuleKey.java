package com.misterpemodder.customgamerules.api.rule.key;

import javax.annotation.Nullable;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.value.GameRuleValue;
import com.misterpemodder.customgamerules.api.rule.value.ValueUpdateHandler;
import com.misterpemodder.customgamerules.api.rule.value.ValueValidator;
import net.minecraft.server.MinecraftServer;

public interface GameRuleKey<V> {
  String getName();

  String getModId();

  GameRuleType<V> getType();

  String getDefaultValueAsString();

  V getDefaultValue();

  ValueUpdateHandler<V> getUpdateHandler();

  ValueValidator<V> getValidator();

  default void onValueUpdate(@Nullable MinecraftServer server, GameRuleValue<V> value) {
    getUpdateHandler().after(server, value);
  }

  default boolean isValidValue(V value) {
    return getValidator().isValid(value);
  }

  GameRuleValue<V> createValue();

  String getDescription();
}
