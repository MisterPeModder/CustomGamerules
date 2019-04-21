package com.misterpemodder.customgamerules.mixin.world;

import java.util.TreeMap;
import com.misterpemodder.customgamerules.api.CustomGameRules;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.value.GameRuleValue;
import com.misterpemodder.customgamerules.impl.Constants;
import com.misterpemodder.customgamerules.impl.registry.GameRuleRegistryImpl;
import com.misterpemodder.customgamerules.impl.registry.GameRuleRegistryImpl.ExtraGameRulesKeyMap;
import com.misterpemodder.customgamerules.impl.registry.GameRuleRegistryImpl.ExtraGameRulesValueMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.GameRules;

@Mixin(GameRules.class)
public class MixinGameRules implements CustomGameRules {
  @Shadow
  @Final
  @Mutable
  private static TreeMap<String, GameRules.Key> KEYS;
  @Shadow
  @Final
  @Mutable
  private TreeMap<String, GameRules.Value> rules;

  @Inject(at = @At("HEAD"), method = "<clinit>")
  private static void onClinitHead(CallbackInfo ci) {
    // In case someone would inject their game rules at the head...
    KEYS = new ExtraGameRulesKeyMap();
  }

  @Inject(at = @At("RETURN"), method = "<init>()V")
  private void oninit(CallbackInfo ci) {
    TreeMap<String, GameRules.Value> newRules = new ExtraGameRulesValueMap();
    newRules.putAll(this.rules);
    this.rules = newRules;
  }

  @ModifyArg(at = @At(value = "INVOKE",
      target = "Lnet/minecraft/util/SystemUtil;consume(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;",
      ordinal = 0), method = "<clinit>", index = 0)
  private static Object modifyVanillaKeyMap(Object map) {
    GameRuleRegistryImpl.setDefaultNamespace(Constants.MC_MOD_ID);
    return new ExtraGameRulesKeyMap();
  }

  @Inject(at = @At(value = "INVOKE",
      target = "Lnet/minecraft/util/SystemUtil;consume(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;",
      ordinal = 0, shift = Shift.AFTER), method = "<clinit>")
  private static void endVanillaNamespace(CallbackInfo ci) {
    GameRuleRegistryImpl.setDefaultNamespace(Constants.UNKNOWN_MOD_ID);
  }

  @Override
  public void copyTo(GameRules target) {
    copyTo((CustomGameRules) target);
  }

  @Override
  public void copyTo(CustomGameRules target) {
    target.getRules().putAll(getRules());
  }

  @Override
  @SuppressWarnings("unchecked")
  public TreeMap<String, GameRuleValue<?>> getRules() {
    return (TreeMap<String, GameRuleValue<?>>) (Object) rules;
  }

  @Override
  public GameRuleValue<?> get(String key) {
    return getRules().get(key);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> GameRuleValue<T> get(GameRuleType<T> type, String key) {
    GameRuleValue<?> value = getRules().get(key);
    if (value.getType() != type)
      return null;
    return (GameRuleValue<T>) value;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> GameRuleValue<T> get(GameRuleKey<T> key) {
    return (GameRuleValue<T>) getRules().get(key.getName());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getValue(GameRuleType<T> type, String key) {
    GameRuleValue<?> value = getRules().get(key);
    if (value.getType() != type)
      return type.getDefaultValue();
    return ((GameRuleValue<T>) value).get();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getValue(GameRuleKey<T> key) {
    return ((GameRuleValue<T>) getRules().get(key.getName())).get();
  }

  @Override
  public GameRules toVanilla() {
    return (GameRules) (Object) this;
  }
}
