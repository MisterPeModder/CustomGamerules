package com.misterpemodder.customgamerules.mixin;

import com.misterpemodder.customgamerules.impl.screen.EditGameRulesScreen;
import com.misterpemodder.customgamerules.impl.screen.OpenScreenButtonAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.menu.BackupLevelScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TextComponent;


@Mixin(BackupLevelScreen.class)
public abstract class BackupLevelScreenMixin extends Screen {
  protected BackupLevelScreenMixin(TextComponent name) {
    super(name);
  }

  @Inject(at = @At("TAIL"), method = "onInitialized()V")
  protected void onOnInitialized(CallbackInfo ci) {
    this.addButton(new ButtonWidget(this.screenWidth / 2 - 100, this.screenHeight / 4 + 168 + 5,
        200, 20, I18n.translate("customgamerules.edit"),
        new OpenScreenButtonAction(new EditGameRulesScreen(this))));
  }
}
