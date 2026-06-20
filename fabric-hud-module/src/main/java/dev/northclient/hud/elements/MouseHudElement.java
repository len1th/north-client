package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.gui.DrawContext;

public final class MouseHudElement extends AbstractHudElement {
  public MouseHudElement() {
    super("mouse", "Mouse HUD", 12, 300, 128, 56, true);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    drawPanel(context, "Mouse");
    int x = (int) getBounds().x();
    int y = (int) getBounds().y();
    context.fill(x + 8, y + 24, x + 58, y + 46, cps.isLeftDown() ? 0xCC22C7FF : 0xAA131C2B);
    context.fill(x + 66, y + 24, x + 116, y + 46, cps.isRightDown() ? 0xCC22C7FF : 0xAA131C2B);
    context.drawTextWithShadow(net.minecraft.client.MinecraftClient.getInstance().textRenderer, "LMB", x + 20, y + 31, cps.isLeftDown() ? 0xFF070B12 : 0xFFEAF4FF);
    context.drawTextWithShadow(net.minecraft.client.MinecraftClient.getInstance().textRenderer, "RMB", x + 78, y + 31, cps.isRightDown() ? 0xFF070B12 : 0xFFEAF4FF);
  }
}
