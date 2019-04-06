package com.misterpemodder.customgamerules.mixin.world;

import java.util.Map;
import java.util.TreeMap;
import com.misterpemodder.customgamerules.api.CustomGameRules;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.value.GameRuleValue;
import com.misterpemodder.customgamerules.impl.Constants;
import com.misterpemodder.customgamerules.impl.registry.GameRuleRegistryImpl;
import com.misterpemodder.customgamerules.impl.registry.GameRuleRegistryImpl.ExtraGameRulesKeyMap;
import com.misterpemodder.customgamerules.impl.registry.GameRuleRegistryImpl.ExtraGameRulesValueMap;
import com.misterpemodder.customgamerules.impl.rule.GameRuleExtensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
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
    return new ExtraGameRulesKeyMap();
  }

  @Inject(at = @At("HEAD"),
      method = "Lnet/minecraft/world/GameRules;method_8353(Ljava/util/TreeMap;)V")
  private static void vanillaGameRuleStart(TreeMap<String, GameRules.Key> map, CallbackInfo ci) {
    GameRuleRegistryImpl.setWarnOnRegister(false);
  }

  @Inject(at = @At("TAIL"),
      method = "Lnet/minecraft/world/GameRules;method_8353(Ljava/util/TreeMap;)V")
  private static void vanillaGameRulesEnd(TreeMap<String, GameRules.Key> map, CallbackInfo ci) {
    GameRuleRegistryImpl.setWarnOnRegister(true);
    for (Map.Entry<String, GameRules.Key> entry : map.entrySet())
      GameRuleExtensions.initKeyDefault(Constants.MC_MOD_ID, entry.getKey(), entry.getValue());
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
  public <T> T getValue(GameRuleType<T> type, String key) {
    GameRuleValue<?> value = getRules().get(key);
    if (value.getType() != type)
      return type.getDefaultValue();
    return ((GameRuleValue<T>) value).get();
  }

  @Override
  public GameRules toVanilla() {
    return (GameRules) (Object) this;
  }
}
