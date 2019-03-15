package com.misterpemodder.customgamerules.impl.proxy;

import javax.annotation.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

public interface SidedProxy {
  @Nullable
  MinecraftServer getServerInstance();

  static SidedProxy getInstance() {
    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
      return new ClientProxy();
    return new ServerProxy();
  }
}
