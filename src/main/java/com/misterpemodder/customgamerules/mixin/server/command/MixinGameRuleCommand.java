package com.misterpemodder.customgamerules.mixin.server.command;

import com.misterpemodder.customgamerules.impl.command.DynamicGameRuleCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import net.minecraft.server.command.GameRuleCommand;
import net.minecraft.server.command.ServerCommandSource;

@Mixin(GameRuleCommand.class)
public final class MixinGameRuleCommand {
  @ModifyArg(
      at = @At(value = "INVOKE",
          target = "Lcom/mojang/brigadier/CommandDispatcher;register"
              + "(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)"
              + "Lcom/mojang/brigadier/tree/LiteralCommandNode;",
          ordinal = 0, remap = false),
      method = "Lnet/minecraft/server/command/GameRuleCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V")
  private static LiteralArgumentBuilder<ServerCommandSource> changeGameRuleCommand(
      LiteralArgumentBuilder<ServerCommandSource> original) {
    return DynamicGameRuleCommand.create();
  }
}
