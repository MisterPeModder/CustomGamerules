package com.misterpemodder.customgamerules.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.language.I18n;

public final class StringUtil {
  public static final boolean HAS_FABRIC_API;

  public static String translate(String key, String defaultString, Object... args) {
    return HAS_FABRIC_API ? I18n.translate(key, args) : String.format(defaultString, args);
  }

  public static String nonNull(String str) {
    return str == null ? "" : str;
  }

  public static String defaulted(@Nullable String str, String defaultString) {
    if (str == null || str.isEmpty())
      return defaultString;
    return str;
  }

  public static boolean containsLowerCase(@Nonnull String haystack, @Nonnull String needle) {
    return needle.isEmpty() || haystack.toLowerCase().contains(needle.toLowerCase());
  }

  static {
    HAS_FABRIC_API = FabricLoader.getInstance().isModLoaded("fabric");
  }
}
