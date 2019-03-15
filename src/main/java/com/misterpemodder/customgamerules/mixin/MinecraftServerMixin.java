package com.misterpemodder.customgamerules.mixin;

import java.util.Map;
import com.misterpemodder.customgamerules.impl.rule.ExtendedGameRuleValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
  @Shadow
  private Map<DimensionType, ServerWorld> worlds;

  @Inject(at = @At("RETURN"),
      method = "Lnet/minecraft/server/MinecraftServer;createWorlds"
          + "(Lnet/minecraft/world/WorldSaveHandler;Lnet/minecraft/world/level/LevelProperties;"
          + "Lnet/minecraft/world/level/LevelInfo;"
          + "Lnet/minecraft/server/WorldGenerationProgressListener;)V")
  private void onCreateWorlds(WorldSaveHandler saveHandler, LevelProperties levelProperties,
      LevelInfo levelInfo, WorldGenerationProgressListener worldGenerationProgressListener,
      CallbackInfo ci) {
    GameRules rules = levelProperties.getGameRules();
    for (String key : GameRules.getKeys().keySet()) {
      GameRules.Value value = rules.get(key);
      if (value instanceof ExtendedGameRuleValue)
        value.set(value.getString(), (MinecraftServer) (Object) this);
    }
  }
}
