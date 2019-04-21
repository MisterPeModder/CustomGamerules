package com.misterpemodder.customgamerules.api.rule.type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;

/**
 * Represents a game rule type.
 * Also extended by {@link GameRules.Type}
 */
public interface GameRuleType<V> {
  /**
   * @return The type's class.
   */
  Class<V> getTypeClass();

  /**
   * Retrives the type's default value
   * For example the default value for {@link GameRuleTypes#INTEGER} is {@code 0}
   * 
   * @return the default value.
   */
  @Nonnull
  V getDefaultValue();

  /**
   * Attemps to parse a value of type V
   * 
   * @param source The source string.
   * @return The parsed value.
   * @throws InvalidGameRuleValueException If parsing failed.
   */
  @Nullable
  V parse(String source) throws InvalidGameRuleValueException;

  /**
   * Converts the given value to a string.
   * 
   * @param <T>   The value type.
   * @param value The value to stringify.
   * @return      A string representation of the value, may not be null.
   */
  <T extends V> String stringify(T value);

  /**
   * @return The string representation of the current type.
   */
  String getTypeName();

  /**
   * @return The argument type for use in commands.
   */
  default ArgumentType<?> getArgumentType() {
    return StringArgumentType.greedyString();
  }

  /**
   * Creates a {@link RequiredArgumentBuilder}.
   * 
   * @param name The argument name.
   * @return The argument builder.
   */
  default RequiredArgumentBuilder<ServerCommandSource, ?> createCommandArgument(String name) {
    return CommandManager.argument(name, getArgumentType());
  }

  public static class InvalidGameRuleValueException extends Exception {
    private static final long serialVersionUID = 7229312786905033556L;

    public InvalidGameRuleValueException(GameRuleType<?> type, String value) {
      this(type, value, null);
    }

    public InvalidGameRuleValueException(GameRuleType<?> type, String value, Throwable cause) {
      super("Failed to parse game rule value: Expected " + type.getTypeName() + ", got '" + value
          + "'", cause);
    }
  }
}
