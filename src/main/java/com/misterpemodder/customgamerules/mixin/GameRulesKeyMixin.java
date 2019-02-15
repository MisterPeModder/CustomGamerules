package com.misterpemodder.customgamerules.mixin;

import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import com.misterpemodder.customgamerules.impl.hook.GameRulesKeyHook;
import net.minecraft.world.GameRules;

@Mixin(GameRules.Key.class)
@Interface(iface = GameRulesKeyHook.class, prefix = "extragamerules")
public final class GameRulesKeyMixin implements GameRulesKeyHook {
  @Shadow
  private String defaultValue;
  @Shadow
  private GameRules.Type type;
  @Unique
  private String typeName;

  public String extragamerules$getDefaultValue() {
    return this.getDefaultValue();
  }

  public String extragamerules$getModId() {
    return "minecraft";
  }

  public String extragamerules$getTypeName() {
    if (this.typeName != null)
      return this.typeName;
    switch (this.type) {
      case STRING:
        return "string";
      case INTEGER:
        return "integer";
      case BOOLEAN:
        return "boolean";
      default:
        return type.name().toLowerCase();
    }
  }
}
