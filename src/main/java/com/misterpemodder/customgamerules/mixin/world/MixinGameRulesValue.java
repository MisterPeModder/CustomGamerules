package com.misterpemodder.customgamerules.mixin.world;

import java.util.function.BiConsumer;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType.InvalidGameRuleValueException;
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
  private boolean cgr$initialized;
  private GameRuleType<Object> cgr$type;
  private GameRuleKey<Object> cgr$key;
  private Object cgr$value;
  private boolean cgr$valueUpdated;

  @Inject(at = @At("RETURN"),
      method = "<init>(Ljava/lang/String;Lnet/minecraft/world/GameRules$Type;Ljava/util/function/BiConsumer;)V")
  public void onInit(CallbackInfo ci) {
    this.cgr$initialized = false;
    this.cgr$type = GameRuleExtensions.DUMMY_TYPE;
    this.cgr$key = GameRuleExtensions.DUMMY_KEY;
    this.cgr$value = this.cgr$type.getDefaultValue();
    this.cgr$valueUpdated = true;
  }

  @Override
  public void cgr$initExtensions(GameRuleType<Object> type, GameRuleKey<Object> key) {
    this.cgr$initialized = true;
    this.cgr$type = type;
    this.cgr$key = key;
    this.cgr$value = type.getDefaultValue();

    this.type = GameRuleExtensions.getVanillaType(type);
    this.applyConsumer = GameRuleExtensions.getVanillaUpdateHandler(this);
  }

  @Override
  public boolean cgr$isInit() {
    return this.cgr$initialized;
  }

  @Override
  public Object get() {
    return this.cgr$value;
  }

  @Override
  public GameRuleKey<Object> getKey() {
    return this.cgr$key;
  }

  @Override
  public GameRuleType<Object> getType() {
    return this.cgr$type;
  }

  @Override
  public void set(Object value, MinecraftServer server) {
    this.cgr$value = value;
    this.cgr$valueUpdated = true;
    this.cgr$key.onValueUpdate(server, this);
  }

  @Unique
  private void cgr$updateCachedValues() {
    String str = this.cgr$type.stringify(this.cgr$value);
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
    if (this.cgr$valueUpdated)
      cgr$updateCachedValues();
  }

  @Inject(at = @At("HEAD"), method = "Lnet/minecraft/world/GameRules$Value;getBoolean()Z")
  public void onGetBoolean(CallbackInfoReturnable<Boolean> ci) {
    if (this.cgr$valueUpdated)
      cgr$updateCachedValues();
  }

  @Inject(at = @At("HEAD"), method = "Lnet/minecraft/world/GameRules$Value;getInteger()I")
  public void onGetInteger(CallbackInfoReturnable<Integer> ci) {
    if (this.cgr$valueUpdated)
      cgr$updateCachedValues();
  }

  @Inject(at = @At("HEAD"),
      method = "Lnet/minecraft/world/GameRules$Value;set(Ljava/lang/String;Lnet/minecraft/server/MinecraftServer;)V",
      cancellable = true)
  public void onSet(String str, MinecraftServer server, CallbackInfo ci) {
    if (this.cgr$initialized) {
      try {
        set(this.cgr$type.parse(str), server);
      } catch (InvalidGameRuleValueException e) {
        set(this.cgr$type.getDefaultValue(), server);
      }
    }
    ci.cancel();
  }
}
