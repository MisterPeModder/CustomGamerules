package com.misterpemodder.customgamerules.mixin.client.gui.widget;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.gui.widget.TextFieldWidget;

@Mixin(TextFieldWidget.class)
public interface TextFieldPosAccessor {
  @Accessor("x")
  void cg$setXPos(int x);

  @Accessor("y")
  void cg$setYPos(int y);
}
