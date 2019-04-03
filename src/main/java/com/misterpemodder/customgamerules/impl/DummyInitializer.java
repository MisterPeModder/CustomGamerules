package com.misterpemodder.customgamerules.impl;

import java.util.Map;
import java.util.TreeMap;
import com.misterpemodder.customgamerules.api.GameRuleRegistry;
import com.misterpemodder.customgamerules.impl.hook.GameRulesKeyHook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.GameRules;

public class DummyInitializer implements ModInitializer {
  @Override
  public void onInitialize() {
    // register the vanilla way
    GameRules.getKeys().put("doSomething", new GameRules.Key("whatever", GameRules.Type.STRING));
    GameRuleRegistry.INSTANCE.registerGameRule("custom-gamerules", "yeet", "false",
        GameRules.Type.BOOLEAN);
    GameRuleRegistry.INSTANCE.registerGameRule("", "thonk", ":thonk:", GameRules.Type.STRING);

    TreeMap<String, GameRules.Key> rules = GameRules.getKeys();
    Logger logger = LogManager.getLogger();
    for (Map.Entry<String, GameRules.Key> entry : rules.entrySet())
      logger.info("[CustomGameRules]: " + ((GameRulesKeyHook) entry.getValue()).getModId() + ":"
          + entry.getKey() + " = " + entry.getValue().createValue().getString());
  }
}
