package dev.northclient.mixin;

import dev.northclient.config.NorthClientSettings;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.PlayerLikeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
  @Inject(
    method = "updateRenderState(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
    at = @At("TAIL"),
    require = 0
  )
  private void northClient$applyCharacterAnimationPolish(PlayerLikeEntity entity, PlayerEntityRenderState state, float tickDelta, CallbackInfo ci) {
    if (!NorthClientSettings.characterAnimationEnabled() || state.spectator) return;
    if (state.limbSwingAmplitude < 0.04f) {
      float idle = 0.018f + (float) Math.abs(Math.sin((state.age + state.id) * 0.08f)) * 0.018f;
      state.limbSwingAmplitude = Math.max(state.limbSwingAmplitude, idle);
    }
  }
}
