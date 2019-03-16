package com.misterpemodder.customgamerules.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.misterpemodder.customgamerules.impl.screen.EditGameRulesButtonWidget;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.menu.BackupLevelScreen;
import net.minecraft.client.resource.language.I18n;


@Mixin(BackupLevelScreen.class)
public abstract class BackupLevelScreenMixin extends Screen {
  @Inject(at = @At("TAIL"), method = "onInitialized()V")
  protected void onOnInitialized(CallbackInfo ci) {
    this.addButton(new EditGameRulesButtonWidget(this, this.screenWidth / 2 - 100,
        this.screenHeight / 4 + 168 + 5, I18n.translate("customgamerules.edit")));
  }
}
