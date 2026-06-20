package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.Collection;

public final class EffectsHudElement extends AbstractHudElement {
  public EffectsHudElement() {
    super("effects", "Efekt HUD", 1660, 80, 216, 126, true);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    drawPanel(context, "Active effects", 0xFFFFB84D);
    MinecraftClient client = MinecraftClient.getInstance();
    if (client == null || client.player == null) return;
    Collection<StatusEffectInstance> effects = client.player.getStatusEffects();
    int y = (int) getBounds().y() + 24;
    if (effects == null || effects.isEmpty()) {
      drawText(context, "No active effects", (int) getBounds().x() + 8, y, 0xFF9FB4CC);
      return;
    }
    int index = 0;
    for (StatusEffectInstance effect : effects) {
      if (index >= 5) break;
      String name = effect.getTranslationKey().replace("effect.minecraft.", "");
      String line = name + " " + roman(effect.getAmplifier() + 1) + " " + duration(effect.getDuration());
      int rowY = y + index * 18;
      int color = effect.getDuration() <= 200 ? 0xFFFF5A5A : 0xFFEAF4FF;
      drawText(context, line, (int) getBounds().x() + 8, rowY, color);
      drawBar(context, (int) getBounds().x() + 132, rowY + 5, 64, 4, Math.min(1.0f, effect.getDuration() / 2400.0f), color);
      index++;
    }
  }

  private String roman(int value) {
    return switch (value) {
      case 1 -> "I";
      case 2 -> "II";
      case 3 -> "III";
      case 4 -> "IV";
      default -> String.valueOf(value);
    };
  }

  private String duration(int ticks) {
    int seconds = Math.max(0, ticks / 20);
    return String.format("%02d:%02d", seconds / 60, seconds % 60);
  }
}
