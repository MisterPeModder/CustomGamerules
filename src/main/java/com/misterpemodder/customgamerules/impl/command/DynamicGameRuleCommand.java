package com.misterpemodder.customgamerules.impl.command;

import java.util.Map;
import com.misterpemodder.customgamerules.api.CustomGameRules;
import com.misterpemodder.customgamerules.api.rule.key.GameRuleKey;
import com.misterpemodder.customgamerules.api.rule.type.GameRuleType;
import com.misterpemodder.customgamerules.impl.proxy.SidedProxy;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.GameRuleCommand;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.GameRules;

/**
 * Replaces the vanilla {@link GameRuleCommand}.
 */
public final class DynamicGameRuleCommand {
  private static final SidedProxy PROXY = SidedProxy.getInstance();
  public static final String NAME = "gamerule";

  public static LiteralArgumentBuilder<ServerCommandSource> create() {
    LiteralArgumentBuilder<ServerCommandSource> builder =
        (LiteralArgumentBuilder<ServerCommandSource>) ServerCommandManager.literal(NAME)
            .requires(source -> source.hasPermissionLevel(2));
    for (Map.Entry<String, GameRuleKey<?>> entry : CustomGameRules.getKeys().entrySet())
      builder.then(createGameRuleNode(entry.getKey(), entry.getValue().getType()));
    return builder;
  }

  private static CommandNode<ServerCommandSource> createGameRuleNode(String name,
      GameRuleType<?> type) {
    return ServerCommandManager
        .literal(name).executes(ctx -> queryValue(ctx.getSource(), name)).then(type
            .createCommandArgument("value").executes(ctx -> setValue(ctx.getSource(), name, ctx)))
        .build();
  }

  public static void addGameRule(String name, GameRuleType<?> type) {
    MinecraftServer server = PROXY.getServerInstance();
    if (server == null)
      return;
    CommandNode<ServerCommandSource> cmdNode =
        server.getCommandManager().getDispatcher().getRoot().getChild(NAME);
    if (cmdNode != null)
      cmdNode.addChild(createGameRuleNode(name, type));
  }

  private static int setValue(ServerCommandSource source, String string,
      CommandContext<ServerCommandSource> ctx) {
    GameRules.Value value4 = source.getMinecraftServer().getGameRules().get(string);
    value4.getType().method_8370(ctx, "value", value4);
    source.sendFeedback(new TranslatableTextComponent("commands.gamerule.set",
        new Object[] {string, value4.getString()}), true);
    return value4.getInteger();
  }

  private static int queryValue(ServerCommandSource source, String string) {
    GameRules.Value value3 = source.getMinecraftServer().getGameRules().get(string);
    source.sendFeedback(new TranslatableTextComponent("commands.gamerule.query",
        new Object[] {string, value3.getString()}), false);
    return value3.getInteger();
  }
}
