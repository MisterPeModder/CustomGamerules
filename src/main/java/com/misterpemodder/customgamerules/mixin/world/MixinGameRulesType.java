package com.misterpemodder.customgamerules.mixin.world;

import java.util.function.Supplier;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;

@Mixin(GameRules.Type.class)
public class MixinGameRulesType {
  @Shadow
  @Final
  @Mutable
  private Supplier<ArgumentType<?>> field_9208;
  //private ThreadLocal<Supplier<ArgumentType<?>>> cg$argumentTypeSupplier = new ThreadLocal<>();

  @Inject(at = @At(value = "HEAD"),
      method = "Lnet/minecraft/world/GameRules$Type;method_8370"
          + "(Lcom/mojang/brigadier/context/CommandContext;"
          + "Ljava/lang/String;Lnet/minecraft/world/GameRules$Value;)V")
  private void changeArgumentTypeSupplier(CommandContext<ServerCommandSource> ctx, String name,
      GameRules.Value value, CallbackInfo ci) {
    // TODO Finsh custom types handling in commands.
    //gameRules$Value_1.set(this.field_9207.apply(commandContext_1, string_1), ((ServerCommandSource)commandContext_1.getSource()).getMinecraftServer());
    //this.cg$argumentTypeSupplier
    //    .set(new ArgumentTypeSupplier(((GameRuleValue<?>) value).getType()));
  }

  /*
  @Redirect(
      at = @At(value = "FIELD",
          target = "Lnet/minecraft/world/GameRules$Type;field_9208:Ljava/util/function/Supplier;"),
      method = "Lnet/minecraft/world/GameRules$Type;method_8370"
          + "(Lcom/mojang/brigadier/context/CommandContext;"
          + "Ljava/lang/String;Lnet/minecraft/world/GameRules$Value;)V")
  private Supplier<ArgumentType<?>> changeArgumentTypeSupplier(GameRules.Type owner) {
    return this.cg$argumentTypeSupplier.get();
  }*/
}
