package com.misterpemodder.customgamerules.mixin.world;

import java.util.TreeMap;
import com.misterpemodder.customgamerules.impl.GameRuleRegistryImpl.ExtraGameRulesKeyMap;
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
public class MixinGameRules {
  @Shadow
  @Final
  @Mutable
  private static TreeMap<String, GameRules.Key> KEYS;

  @Inject(at = @At("HEAD"), method = "<clinit>")
  private static void initKeyMap(CallbackInfo ci) {
    // In case someone would inject their game rules at the head...
    KEYS = new ExtraGameRulesKeyMap();
  }

  @ModifyArg(at = @At(value = "INVOKE",
      target = "Lnet/minecraft/util/SystemUtil;consume(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;",
      ordinal = 0), method = "<clinit>", index = 0)
  private static Object modifyVanillaKeyMap(Object map) {
    return new ExtraGameRulesKeyMap();
  }

  @Inject(at = @At("HEAD"),
      method = "Lnet/minecraft/world/GameRules;method_8353(Ljava/util/TreeMap;)V")
  private static void vanillaGameRulesStart(TreeMap<String, GameRules.Key> map, CallbackInfo ci) {
    ((ExtraGameRulesKeyMap) map).setDefaultNamespace("minecraft");
  }

  @Inject(at = @At("TAIL"),
      method = "Lnet/minecraft/world/GameRules;method_8353(Ljava/util/TreeMap;)V")
  private static void vanillaGameRulesEnd(TreeMap<String, GameRules.Key> map, CallbackInfo ci) {
    ((ExtraGameRulesKeyMap) map).setDefaultNamespace("");
  }
}
