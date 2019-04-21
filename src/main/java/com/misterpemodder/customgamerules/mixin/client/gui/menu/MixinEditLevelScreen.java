package com.misterpemodder.customgamerules.mixin.client.gui.menu;

import com.misterpemodder.customgamerules.impl.StringUtil;
import com.misterpemodder.customgamerules.impl.menu.EditGameRulesButton;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.menu.EditLevelScreen;
import net.minecraft.text.TextComponent;

@Mixin(EditLevelScreen.class)
public abstract class MixinEditLevelScreen extends Screen {
  @Shadow
  @Final
  private String levelName;

  protected MixinEditLevelScreen(TextComponent name) {
    super(name);
  }

  @Inject(at = @At(value = "INVOKE",
      target = "Lnet/minecraft/client/gui/menu/EditLevelScreen;addButton", shift = Shift.AFTER,
      ordinal = 4), method = "init()V")
  protected void onOnInitialized(CallbackInfo ci) {
    addButton(new EditGameRulesButton(this.width / 2 - 100, this.height / 4 + 144 + 5, 200, 20,
        StringUtil.translate("custom-gamerules.menu.edit", "Edit Gamerules"), this,
        this.levelName));
  }
}
