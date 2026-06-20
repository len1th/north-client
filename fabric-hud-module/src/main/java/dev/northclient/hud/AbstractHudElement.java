package dev.northclient.hud;

import net.minecraft.client.gui.DrawContext;

public abstract class AbstractHudElement implements HudElement {
  private final String id;
  private final String name;
  private boolean enabled;
  private HudBounds bounds;
  private float scale = 1.0f;

  protected AbstractHudElement(String id, String name, float x, float y, float width, float height, boolean enabled) {
    this.id = id;
    this.name = name;
    this.bounds = new HudBounds(x, y, width, height);
    this.enabled = enabled;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public HudBounds getBounds() {
    return bounds;
  }

  @Override
  public void setPosition(float x, float y) {
    bounds = new HudBounds(x, y, bounds.width(), bounds.height());
  }

  @Override
  public float getScale() {
    return scale;
  }

  @Override
  public void setScale(float scale) {
    this.scale = Math.max(0.5f, Math.min(2.0f, scale));
  }

  @Override
  public void renderPreview(DrawContext context, float tickDelta) {
    drawPanel(context, getName());
  }

  protected void drawPanel(DrawContext context, String text) {
    HudBounds b = getBounds();
    int x = (int) b.x();
    int y = (int) b.y();
    int width = (int) b.width();
    int height = (int) b.height();
    context.fill(x, y, x + width, y + height, 0xAA0E1522);
    drawBorder(context, x, y, width, height, 0xFF22C7FF);
    context.drawTextWithShadow(net.minecraft.client.MinecraftClient.getInstance().textRenderer, text, x + 8, y + 8, 0xFFEAF4FF);
  }

  protected void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
    context.fill(x, y, x + width, y + 1, color);
    context.fill(x, y + height - 1, x + width, y + height, color);
    context.fill(x, y, x + 1, y + height, color);
    context.fill(x + width - 1, y, x + width, y + height, color);
  }
}
