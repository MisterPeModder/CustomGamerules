package com.misterpemodder.customgamerules.impl;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.language.I18n;

public final class Util {
  public static final boolean HAS_FABRIC_API;

  public static String translate(String key, String defaultString, Object... args) {
    return HAS_FABRIC_API ? I18n.translate(key, args) : String.format(defaultString, args);
  }

  static {
    HAS_FABRIC_API = FabricLoader.getInstance().isModLoaded("fabric");
  }
}
