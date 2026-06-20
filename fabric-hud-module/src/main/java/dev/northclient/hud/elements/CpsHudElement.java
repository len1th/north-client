package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.gui.DrawContext;

public final class CpsHudElement extends AbstractHudElement {
  public CpsHudElement() {
    super("cps", "CPS HUD", 12, 360, 128, 52, true);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    drawPanel(context, "LMB: " + cps.leftCps() + " CPS  RMB: " + cps.rightCps() + " CPS");
  }
}

