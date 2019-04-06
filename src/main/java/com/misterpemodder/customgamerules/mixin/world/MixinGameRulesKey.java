package com.misterpemodder.customgamerules.mixin.world;

import java.util.function.BiConsumer;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.value.GameRuleValue;
import com.misterpemodder.customgamerules.api.rule.value.ValueUpdateHandler;
import com.misterpemodder.customgamerules.api.rule.value.ValueValidator;
import com.misterpemodder.customgamerules.impl.Constants;
import com.misterpemodder.customgamerules.impl.rule.GameRuleExtensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

@Mixin(GameRules.Key.class)
public final class MixinGameRulesKey implements GameRuleKey<Object>, GameRuleExtensions.Key {
  @Shadow
  @Final
  @Mutable
  private String defaultValue;

  @Shadow
  @Final
  @Mutable
  private GameRules.Type type;

  @Shadow
  @Final
  @Mutable
  private BiConsumer<MinecraftServer, GameRules.Value> field_9204;

  // CustomGameRules Extensions
  private boolean cg$initialized;
  private Object cg$defaultValueObj;
  private String cg$modId;
  private String cg$ruleName;
  private GameRuleType<Object> cg$type;
  private ValueUpdateHandler<Object> cg$onUpdate;
  private ValueValidator<Object> cg$validator;
  private boolean cg$cancelUpdateNoServer;

  @SuppressWarnings("unchecked")
  @Inject(at = @At("RETURN"),
      method = "<init>(Ljava/lang/String;Lnet/minecraft/world/GameRules$Type;Ljava/util/function/BiConsumer;)V")
  public void onInit(String defaultValue, GameRules.Type type,
      BiConsumer<MinecraftServer, GameRules.Value> onUpdate, CallbackInfo ci) {
    this.cg$initialized = false;
    this.cg$type = (GameRuleType<Object>) GameRuleExtensions.getCGType(type);
    this.cg$defaultValueObj = this.cg$type.parse(defaultValue);
    this.cg$modId = Constants.UNKNOWN_MOD_ID;
    this.cg$ruleName = "errored rule name - report as bug";
    this.cg$onUpdate = GameRuleExtensions.DUMMY_UPDATE_HANDLER;
    this.cg$validator = GameRuleExtensions.DUMMY_VALIDATOR;
    this.cg$cancelUpdateNoServer = true;
  }

  @Override
  public boolean cg$isInit() {
    return this.cg$initialized;
  }

  @Override
  public void cg$initExtensions(String modId, String ruleName, GameRuleType<Object> type,
      Object defaultValue, ValueUpdateHandler<Object> onUpdate, ValueValidator<Object> validator,
      boolean cancelUpdateNoServer) {
    this.cg$initialized = true;
    this.cg$modId = modId;
    this.cg$ruleName = ruleName;
    this.cg$type = type;
    this.cg$validator = validator;
    this.cg$defaultValueObj = defaultValue;
    this.cg$cancelUpdateNoServer = cancelUpdateNoServer;

    if (onUpdate == null) {
      this.cg$onUpdate = GameRuleExtensions.getCGUpdateHandler(this.field_9204);
    } else {
      this.cg$onUpdate = onUpdate;
      this.field_9204 = GameRuleExtensions.getVanillaUpdateHandler(onUpdate);
    }

    this.defaultValue = type.stringify(defaultValue);
    this.type = GameRuleExtensions.getVanillaType(type);
  }

  @Override
  public Object getDefaultValue() {
    return this.cg$defaultValueObj;
  }

  @Override
  public String getDefaultValueAsString() {
    return this.defaultValue;
  }

  @Override
  public String getModId() {
    return this.cg$modId;
  }

  @Override
  public String getName() {
    return this.cg$ruleName;
  }

  @Override
  public GameRuleType<Object> getType() {
    return this.cg$type;
  }

  @Override
  public void onValueUpdate(MinecraftServer server, GameRuleValue<Object> value) {
    if (server != null || !this.cg$cancelUpdateNoServer)
      this.cg$onUpdate.after(server, value);
  }

  @Override
  public boolean isValidValue(Object value) {
    return this.cg$validator.isValid(value);
  }

  @Override
  public GameRuleValue<Object> createValue() {
    return GameRuleExtensions.newValue(this.cg$type, this);
  }

  @Inject(at = @At("HEAD"), method = "Lnet/minecraft/world/GameRules$Key;createValue()"
      + "Lnet/minecraft/world/GameRules$Value;", cancellable = true)
  public void onCreateValue(CallbackInfoReturnable<GameRules.Value> ci) {
    ci.setReturnValue((GameRules.Value) createValue());
  }
}
