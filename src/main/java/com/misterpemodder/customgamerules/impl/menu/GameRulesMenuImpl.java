package com.misterpemodder.customgamerules.impl.menu;

import java.util.function.BiConsumer;
import com.misterpemodder.customgamerules.api.CustomGameRules;
import com.misterpemodder.customgamerules.api.menu.GameRulesMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.GameRules;

public class GameRulesMenuImpl implements GameRulesMenu {

  @Override
  public void open(BiConsumer<Boolean, CustomGameRules> onClose) {
    MinecraftClient.getInstance()
        .openScreen(new EditGameRulesScreen(CustomGameRules.of(new GameRules()), onClose));
  }

  @Override
  public void open(CustomGameRules rules, BiConsumer<Boolean, CustomGameRules> onClose) {
    MinecraftClient.getInstance().openScreen(new EditGameRulesScreen(rules, onClose));
  }
}
