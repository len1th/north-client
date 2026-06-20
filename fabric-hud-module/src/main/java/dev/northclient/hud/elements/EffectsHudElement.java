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
    super("effects", "Buyu/Efekt HUD", 1680, 80, 188, 112, true);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    drawPanel(context, "Effects");
    MinecraftClient client = MinecraftClient.getInstance();
    if (client == null || client.player == null) return;
    Collection<StatusEffectInstance> effects = client.player.getStatusEffects();
    int y = (int) getBounds().y() + 24;
    if (effects == null || effects.isEmpty()) {
      context.drawTextWithShadow(client.textRenderer, "No active effects", (int) getBounds().x() + 8, y, 0xFF9FB4CC);
      return;
    }
    int index = 0;
    for (StatusEffectInstance effect : effects) {
      if (index >= 5) break;
      String name = effect.getTranslationKey().replace("effect.minecraft.", "");
      String line = name + " " + roman(effect.getAmplifier() + 1) + " " + duration(effect.getDuration());
      context.drawTextWithShadow(client.textRenderer, line, (int) getBounds().x() + 8, y + index * 16, 0xFFEAF4FF);
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
