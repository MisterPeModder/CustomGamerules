package com.misterpemodder.customgamerules.api.rule.type;

import com.misterpemodder.customgamerules.api.registry.GameRuleRegistry;
import com.misterpemodder.customgamerules.impl.rule.type.GameRuleTypeImpl;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Provides standard game rules types and utility.
 */
public final class GameRuleTypes {
    public static final GameRuleType<String> STRING =
            register(new Identifier("minecraft", "string"), GameRuleTypeImpl.stringType());
    public static final GameRuleType<Boolean> BOOLEAN =
            register(new Identifier("minecraft", "boolean"), GameRuleTypeImpl.booleanType());
    public static final GameRuleType<Integer> INTEGER =
            register(new Identifier("minecraft", "integer"), GameRuleTypeImpl.integerType());
    public static final GameRuleType<Long> LONG =
            register(new Identifier("custom-gamerules", "long"), GameRuleTypeImpl.longType());
    public static final GameRuleType<Float> FLOAT =
            register(new Identifier("custom-gamerules", "float"), GameRuleTypeImpl.floatType());
    public static final GameRuleType<Double> DOUBLE =
            register(new Identifier("custom-gamerules", "double"), GameRuleTypeImpl.doubleType());

    private static <T> GameRuleType<T> register(Identifier id, GameRuleType<T> type) {
        Registry.register(GameRuleRegistry.TYPE_REGISTRY, id, type);
        return type;
    }
}
