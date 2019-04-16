package com.misterpemodder.customgamerules.mixin.client.gui.menu;

import com.misterpemodder.customgamerules.impl.menu.EditGameRulesButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.menu.EditLevelScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;

@Mixin(Screen.class)
public class MixinScreen {
  @Shadow
  public int height;

  @Inject(at = @At("HEAD"), method = "addButton")
  protected void onAddButton(AbstractButtonWidget button, CallbackInfoReturnable<ButtonWidget> ci) {
    if (((Object) this) instanceof EditLevelScreen)
      EditGameRulesButton.changeButtonHeight((Screen) (Object) this, this.height, button);
  }
}
