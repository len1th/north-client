package dev.northclient.hud;

import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.gui.DrawContext;

public interface HudElement {
  String getId();
  String getName();
  boolean isEnabled();
  void setEnabled(boolean enabled);
  HudBounds getBounds();
  void setPosition(float x, float y);
  float getScale();
  void setScale(float scale);
  HudStyle style();
  void resetDefault();
  String settingsHint();
  void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys);
  void renderPreview(DrawContext context, float tickDelta);
}
