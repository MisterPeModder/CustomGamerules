package com.misterpemodder.customgamerules.mixin.world;

import java.util.function.BiConsumer;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType.InvalidGameRuleValueException;
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
  private boolean cgr$initialized;
  private Object cgr$defaultValueObj;
  private String cgr$modId;
  private String cgr$ruleName;
  private GameRuleType<Object> cgr$type;
  private ValueUpdateHandler<Object> cgr$onUpdate;
  private ValueValidator<Object> cgr$validator;
  private boolean cgr$cancelUpdateNoServer;
  private String cgr$descriptionKey;

  @SuppressWarnings("unchecked")
  @Inject(at = @At("RETURN"),
      method = "<init>(Ljava/lang/String;Lnet/minecraft/world/GameRules$Type;Ljava/util/function/BiConsumer;)V")
  public void onInit(String defaultValue, GameRules.Type type,
      BiConsumer<MinecraftServer, GameRules.Value> onUpdate, CallbackInfo ci) {
    this.cgr$initialized = false;
    this.cgr$type = (GameRuleType<Object>) GameRuleExtensions.getCGType(type);
    try {
      this.cgr$defaultValueObj = this.cgr$type.parse(defaultValue);
    } catch (InvalidGameRuleValueException e) {
      this.cgr$defaultValueObj = this.cgr$type.getDefaultValue();
    }
    this.cgr$modId = Constants.UNKNOWN_MOD_ID;
    this.cgr$ruleName = "errored rule name - report as bug";
    this.cgr$onUpdate = GameRuleExtensions.DUMMY_UPDATE_HANDLER;
    this.cgr$validator = GameRuleExtensions.DUMMY_VALIDATOR;
    this.cgr$cancelUpdateNoServer = true;
    this.cgr$descriptionKey = "errored description - report as bug";
  }

  @Override
  public boolean cgr$isInit() {
    return this.cgr$initialized;
  }

  @Override
  public void cgr$initExtensions(String modId, String ruleName, GameRuleType<Object> type,
      Object defaultValue, ValueUpdateHandler<Object> onUpdate, ValueValidator<Object> validator,
      boolean cancelUpdateNoServer, String descriptionKey) {
    this.cgr$initialized = true;
    this.cgr$modId = modId;
    this.cgr$ruleName = ruleName;
    this.cgr$type = type;
    this.cgr$validator = validator;
    this.cgr$defaultValueObj = defaultValue;
    this.cgr$cancelUpdateNoServer = cancelUpdateNoServer;
    this.cgr$descriptionKey = descriptionKey;

    if (onUpdate == null) {
      this.cgr$onUpdate = GameRuleExtensions.getCGUpdateHandler(this.field_9204);
    } else {
      this.cgr$onUpdate = onUpdate;
      this.field_9204 = GameRuleExtensions.getVanillaUpdateHandler(onUpdate);
    }

    this.defaultValue = type.stringify(defaultValue);
    this.type = GameRuleExtensions.getVanillaType(type);
  }

  @Override
  public Object getDefaultValue() {
    return this.cgr$defaultValueObj;
  }

  @Override
  public String getDefaultValueAsString() {
    return this.defaultValue;
  }

  @Override
  public String getModId() {
    return this.cgr$modId;
  }

  @Override
  public String getName() {
    return this.cgr$ruleName;
  }

  @Override
  public GameRuleType<Object> getType() {
    return this.cgr$type;
  }

  @Override
  public ValueUpdateHandler<Object> getUpdateHandler() {
    return this.cgr$onUpdate;
  }

  @Override
  public ValueValidator<Object> getValidator() {
    return this.cgr$validator;
  }

  @Override
  public void onValueUpdate(MinecraftServer server, GameRuleValue<Object> value) {
    if (server != null || !this.cgr$cancelUpdateNoServer)
      this.cgr$onUpdate.after(server, value);
  }

  @Override
  public boolean isValidValue(Object value) {
    return this.cgr$validator.isValid(value);
  }

  @Override
  public GameRuleValue<Object> createValue() {
    return GameRuleExtensions.newValue(this.cgr$type, this);
  }

  @Override
  public String getDescription() {
    return this.cgr$descriptionKey;
  }

  @Inject(at = @At("HEAD"), method = "Lnet/minecraft/world/GameRules$Key;createValue()"
      + "Lnet/minecraft/world/GameRules$Value;", cancellable = true)
  public void onCreateValue(CallbackInfoReturnable<GameRules.Value> ci) {
    ci.setReturnValue((GameRules.Value) createValue());
  }
}
