package com.misterpemodder.customgamerules.api.rule.value;

import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ValueUpdateHandler<T> {
  static final ValueUpdateHandler<?> NO_UPDATE = (s, v) -> {
  };

  @SuppressWarnings("unchecked")
  static <T> ValueUpdateHandler<T> noUpdate() {
    return (ValueUpdateHandler<T>) NO_UPDATE;
  }

  void after(@Nullable MinecraftServer server, GameRuleValue<T> value);
}
