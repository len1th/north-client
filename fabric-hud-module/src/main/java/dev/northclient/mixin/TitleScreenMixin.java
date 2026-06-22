package dev.northclient.mixin;

import dev.northclient.NorthClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
  protected TitleScreenMixin(MinecraftClient client, TextRenderer textRenderer, Text title) {
    super(client, textRenderer, title);
  }

  @Inject(method = "init", at = @At("TAIL"))
  private void northclient$addHudEditorButton(CallbackInfo ci) {
    addDrawableChild(ButtonWidget.builder(Text.literal("HUD Olustur"), button ->
      NorthClientMod.openHudEditor(MinecraftClient.getInstance())
    ).dimensions(width - 112, 8, 104, 20).build());
  }
}
