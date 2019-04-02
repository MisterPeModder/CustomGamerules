package com.misterpemodder.customgamerules.mixin.client;

import com.misterpemodder.customgamerules.impl.gui.EditGameRulesButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.menu.BackupLevelScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;

@Mixin(Screen.class)
public class ScreenMixin {
  @Shadow
  public int height;

  @Inject(at = @At("HEAD"), method = "addButton")
  protected void onAddButton(AbstractButtonWidget button, CallbackInfoReturnable<ButtonWidget> ci) {
    if (((Object) this) instanceof BackupLevelScreen)
      EditGameRulesButton.changeButtonHeight((Screen) (Object) this, this.height, button);
  }
}
