package com.misterpemodder.customgamerules.impl.rule.type;

import java.util.function.Supplier;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.mojang.brigadier.arguments.ArgumentType;

public class ArgumentTypeSupplier implements Supplier<ArgumentType<?>> {
  private final GameRuleType<?> type;

  public ArgumentTypeSupplier(GameRuleType<?> type) {
    this.type = type;
  }

  @Override
  public ArgumentType<?> get() {
    return this.type.getArgumentType();
  }
}
