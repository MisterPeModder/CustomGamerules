package com.misterpemodder.customgamerules.mixin.client.gui.widget;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.client.gui.widget.AbstractButtonWidget;

@Mixin(AbstractButtonWidget.class)
public interface ButtonFocusAccessor {
  @Invoker("setFocused")
  void cg$setFocused(boolean focused);
}
