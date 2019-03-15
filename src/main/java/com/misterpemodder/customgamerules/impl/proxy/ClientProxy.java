package com.misterpemodder.customgamerules.impl.proxy;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;

class ClientProxy implements SidedProxy {
  @Override
  public MinecraftServer getServerInstance() {
    return MinecraftClient.getInstance().getServer();
  }
}
