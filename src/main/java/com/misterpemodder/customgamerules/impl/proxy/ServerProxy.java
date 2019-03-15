package com.misterpemodder.customgamerules.impl.proxy;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

class ServerProxy implements SidedProxy {
  @Override
  public MinecraftServer getServerInstance() {
    return (MinecraftServer) FabricLoader.getInstance().getGameInstance();
  }
}
