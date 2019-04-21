package com.misterpemodder.customgamerules.impl.rule.type;

import java.util.function.Function;
import java.util.function.Supplier;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

public class GameRuleTypeImpl<V> implements GameRuleType<V> {
  protected final String typeName;
  protected final V defaultValue;
  protected final Class<V> typeClass;
  private final Function<String, V> parseFunction;
  private final Function<V, String> stringifyFunction;
  private final Supplier<ArgumentType<?>> argumentTypeSupplier;

  protected GameRuleTypeImpl(Class<V> typeClass, String typeName, V defaultValue,
      Function<String, V> parseFunction, Function<V, String> stringifyFunction,
      Supplier<ArgumentType<?>> argumentTypeSupplier) {
    this.typeClass = typeClass;
    this.typeName = typeName;
    this.defaultValue = defaultValue;
    this.parseFunction = parseFunction;
    this.stringifyFunction = stringifyFunction;
    this.argumentTypeSupplier = argumentTypeSupplier;
  }

  public static GameRuleTypeImpl<String> stringType() {
    return new GameRuleTypeImpl<String>(String.class, "string", "", Function.identity(),
        Function.identity(), StringArgumentType::string);
  }

  public static GameRuleTypeImpl<Boolean> booleanType() {
    return new GameRuleTypeImpl<Boolean>(Boolean.class, "boolean", false, Boolean::valueOf,
        Object::toString, BoolArgumentType::bool);
  }

  public static GameRuleTypeImpl<Integer> integerType() {
    return new GameRuleTypeImpl<Integer>(Integer.class, "integer", 0, Integer::valueOf,
        Object::toString, IntegerArgumentType::integer);
  }

  public static GameRuleTypeImpl<Float> floatType() {
    return new GameRuleTypeImpl<Float>(Float.class, "float", 0.0F, Float::valueOf, Object::toString,
        FloatArgumentType::floatArg);
  }

  public static GameRuleTypeImpl<Double> doubleType() {
    return new GameRuleTypeImpl<Double>(Double.class, "double", 0.0, Double::valueOf,
        d -> d.toString(), DoubleArgumentType::doubleArg);
  }

  public static GameRuleTypeImpl<Long> longType() {
    return new GameRuleTypeImpl<Long>(Long.class, "long", 0L, Long::valueOf, l -> l.toString(),
        LongArgumentType::longArg);
  }

  @Override
  public Class<V> getTypeClass() {
    return this.typeClass;
  }

  @Override
  public V getDefaultValue() {
    return this.defaultValue;
  }

  @Override
  public V parse(String source) throws InvalidGameRuleValueException {
    try {
      return this.parseFunction.apply(source);
    } catch (RuntimeException e) {
      throw new InvalidGameRuleValueException(this, source, e);
    }
  }

  @Override
  public <T extends V> String stringify(T value) {
    return this.stringifyFunction.apply(value);
  }

  @Override
  public String getTypeName() {
    return this.typeName;
  }

  @Override
  public ArgumentType<?> getArgumentType() {
    return argumentTypeSupplier.get();
  }
}
