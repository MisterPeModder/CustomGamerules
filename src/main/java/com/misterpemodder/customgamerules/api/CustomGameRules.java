package com.misterpemodder.customgamerules.api;

import java.util.TreeMap;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.value.GameRuleValue;
import net.minecraft.world.GameRules;

public interface CustomGameRules {
  static CustomGameRules of(GameRules rules) {
    return (CustomGameRules) rules;
  }

  static CustomGameRules copy(GameRules original) {
    return copy(of(original));
  }

  static CustomGameRules copy(CustomGameRules original) {
    CustomGameRules copy = CustomGameRules.of(new GameRules());
    copy.getRules().putAll(original.getRules());
    return copy;
  }

  @SuppressWarnings("unchecked")
  static TreeMap<String, GameRuleKey<?>> getKeys() {
    return (TreeMap<String, GameRuleKey<?>>) (Object) GameRules.getKeys();
  }

  void copyTo(GameRules target);

  void copyTo(CustomGameRules target);

  TreeMap<String, GameRuleValue<?>> getRules();

  GameRuleValue<?> get(String key);

  <T> GameRuleValue<T> get(GameRuleType<T> type, String key);

  <T> GameRuleValue<T> get(GameRuleKey<T> key);

  <T> T getValue(GameRuleType<T> type, String key);

  <T> T getValue(GameRuleKey<T> key);

  GameRules toVanilla();
}
