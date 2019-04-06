package com.misterpemodder.customgamerules.mixin.world;

import java.util.function.BiConsumer;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.value.GameRuleValue;
import com.misterpemodder.customgamerules.impl.rule.GameRuleExtensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

@Mixin(GameRules.Value.class)
public class MixinGameRulesValue implements GameRuleValue<Object>, GameRuleExtensions.Value {
  @Shadow
  private String asString;
  @Shadow
  private boolean asBoolean;
  @Shadow
  private int asInteger;
  @Shadow
  private double asDouble;

  @Shadow
  @Final
  @Mutable
  private GameRules.Type type;

  @Shadow
  @Final
  @Mutable
  private BiConsumer<MinecraftServer, GameRules.Value> applyConsumer;

  // CustomGameRules Extensions
  private boolean cg$initialized;
  private GameRuleType<Object> cg$type;
  private GameRuleKey<Object> cg$key;
  private Object cg$value;
  private boolean cg$valueUpdated;

  @Inject(at = @At("RETURN"),
      method = "<init>(Ljava/lang/String;Lnet/minecraft/world/GameRules$Type;Ljava/util/function/BiConsumer;)V")
  public void onInit(CallbackInfo ci) {
    this.cg$initialized = false;
    this.cg$type = GameRuleExtensions.DUMMY_TYPE;
    this.cg$key = GameRuleExtensions.DUMMY_KEY;
    this.cg$value = this.cg$type.getDefaultValue();
    this.cg$valueUpdated = true;
  }

  @Override
  public void cg$initExtensions(GameRuleType<Object> type, GameRuleKey<Object> key) {
    this.cg$initialized = true;
    this.cg$type = type;
    this.cg$key = key;

    this.type = GameRuleExtensions.getVanillaType(type);
    this.applyConsumer = GameRuleExtensions.getVanillaUpdateHandler(this);
  }

  @Override
  public boolean cg$isInit() {
    return this.cg$initialized;
  }

  @Override
  public Object get() {
    return this.cg$value;
  }

  @Override
  public GameRuleKey<Object> getKey() {
    return this.cg$key;
  }

  @Override
  public GameRuleType<Object> getType() {
    return this.cg$type;
  }

  @Override
  public void set(Object value, MinecraftServer server) {
    this.cg$value = value;
    this.cg$valueUpdated = true;
    this.cg$key.onValueUpdate(server, this);
  }

  @Unique
  private void cg$updateCachedValues() {
    String str = this.cg$type.stringify(this.cg$value);
    this.asString = str;
    this.asBoolean = Boolean.parseBoolean(str);
    this.asInteger = this.asBoolean ? 1 : 0;

    try {
      this.asInteger = Integer.parseInt(str);
    } catch (NumberFormatException e) {
    }
    try {
      this.asDouble = Double.parseDouble(str);
    } catch (NumberFormatException e) {
    }
  }

  @Inject(at = @At("HEAD"),
      method = "Lnet/minecraft/world/GameRules$Value;getString()Ljava/lang/String;")
  public void onGetString(CallbackInfoReturnable<String> ci) {
    if (this.cg$valueUpdated)
      cg$updateCachedValues();
  }

  @Inject(at = @At("HEAD"), method = "Lnet/minecraft/world/GameRules$Value;getBoolean()Z")
  public void onGetBoolean(CallbackInfoReturnable<Boolean> ci) {
    if (this.cg$valueUpdated)
      cg$updateCachedValues();
  }

  @Inject(at = @At("HEAD"), method = "Lnet/minecraft/world/GameRules$Value;getInteger()I")
  public void onGetInteger(CallbackInfoReturnable<Integer> ci) {
    if (this.cg$valueUpdated)
      cg$updateCachedValues();
  }

  @Inject(at = @At("HEAD"),
      method = "Lnet/minecraft/world/GameRules$Value;set(Ljava/lang/String;Lnet/minecraft/server/MinecraftServer;)V",
      cancellable = true)
  public void onSet(String str, MinecraftServer server, CallbackInfo ci) {
    if (this.cg$initialized)
      set(this.cg$type.parse(str), server);
    ci.cancel();
  }
}
