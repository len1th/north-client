package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.gui.DrawContext;

public final class KeystrokesHudElement extends AbstractHudElement {
  public KeystrokesHudElement() {
    super("keystrokes", "Klavye HUD", 12, 420, 128, 116, true);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    drawPanel(context, "W:" + keys.forward + " A:" + keys.left + " S:" + keys.back + " D:" + keys.right);
  }
}

