package com.misterpemodder.customgamerules.mixin.client.gui.menu;

import com.misterpemodder.customgamerules.impl.StringUtil;
import com.misterpemodder.customgamerules.impl.menu.EditGameRulesButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.menu.EditLevelScreen;
import net.minecraft.text.TextComponent;

@Mixin(EditLevelScreen.class)
public abstract class MixinEditLevelScreen extends Screen {
  protected MixinEditLevelScreen(TextComponent name) {
    super(name);
  }

  @Inject(at = @At("TAIL"), method = "init()V")
  protected void onOnInitialized(CallbackInfo ci) {
    addButton(new EditGameRulesButton(this.width / 2 - 100, this.height / 4 + 144 + 5, 200, 20,
        StringUtil.translate("customgamerules.edit", "Edit Gamerules"), this));
  }
}
