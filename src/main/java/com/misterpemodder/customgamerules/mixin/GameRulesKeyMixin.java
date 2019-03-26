package com.misterpemodder.customgamerules.mixin;

import com.misterpemodder.customgamerules.impl.hook.GameRulesKeyHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import net.minecraft.world.GameRules;

@Mixin(GameRules.Key.class)
public final class GameRulesKeyMixin implements GameRulesKeyHook {
  @Shadow
  private String defaultValue;
  @Shadow
  private GameRules.Type type;
  @Unique
  private String typeName;

  @Override
  public String getDefaultValue() {
    return this.defaultValue;
  }

  @Override
  public String getModId() {
    return "minecraft";
  }

  @Override
  public String getTypeName() {
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
