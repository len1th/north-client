package dev.northclient.hud;

import dev.northclient.config.NorthClientSettings;
import net.minecraft.client.gui.DrawContext;

public abstract class AbstractHudElement implements HudElement {
  private final String id;
  private final String name;
  private final HudBounds defaultBounds;
  private final boolean defaultEnabled;
  private boolean enabled;
  private HudBounds bounds;
  private float scale = 1.0f;
  private final HudStyle style = new HudStyle();

  protected AbstractHudElement(String id, String name, float x, float y, float width, float height, boolean enabled) {
    this.id = id;
    this.name = name;
    this.bounds = new HudBounds(x, y, width, height);
    this.defaultBounds = this.bounds;
    this.defaultEnabled = enabled;
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
  public HudStyle style() {
    return style;
  }

  @Override
  public void resetDefault() {
    this.bounds = defaultBounds;
    this.enabled = defaultEnabled;
    this.scale = 1.0f;
    this.style.reset();
  }

  @Override
  public String settingsHint() {
    return "Renk, stil, scale ve gorunurluk ayarlari bu HUD icin kaydedilir.";
  }

  @Override
  public void renderPreview(DrawContext context, float tickDelta) {
    drawPanel(context, getName());
  }

  protected void drawPanel(DrawContext context, String text) {
    drawPanel(context, text, style.accentColor);
  }

  protected void drawPanel(DrawContext context, String text, int accent) {
    HudBounds b = getBounds();
    int x = (int) b.x();
    int y = (int) b.y();
    int width = (int) b.width();
    int height = (int) b.height();
    if (style.backgroundEnabled) {
      context.fill(x + 2, y + 2, x + width + 2, y + height + 2, 0x55000000);
      context.fill(x, y, x + width, y + height, style.backgroundColor);
    }
    context.fill(x, y, x + width, y + 2, accent);
    drawBorder(context, x, y, width, height, style.borderColor);
    if (style.labelsEnabled) {
      drawText(context, text, x + 8, y + 8, style.textColor);
    }
  }

  protected void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
    context.fill(x, y, x + width, y + 1, color);
    context.fill(x, y + height - 1, x + width, y + height, color);
    context.fill(x, y, x + 1, y + height, color);
    context.fill(x + width - 1, y, x + width, y + height, color);
  }

  protected void drawText(DrawContext context, String text, int x, int y, int color) {
    net.minecraft.client.MinecraftClient client = net.minecraft.client.MinecraftClient.getInstance();
    if (client != null && client.textRenderer != null) {
      context.drawTextWithShadow(client.textRenderer, text, x, y, color);
    }
  }

  protected void drawKey(DrawContext context, String label, int x, int y, int width, int height, boolean pressed) {
    int bg = pressed ? style.pressedColor : soften(style.backgroundColor, 0xAA);
    int border = pressed ? style.textColor : style.borderColor;
    int text = pressed ? style.readablePressedTextColor() : style.textColor;
    if (pressed && NorthClientSettings.hudAnimationsEnabled()) {
      int pulse = (int) ((System.currentTimeMillis() / 80L) % 3L);
      border = pulse == 0 ? 0xFFFFFFFF : border;
    }
    context.fill(x, y, x + width, y + height, bg);
    drawBorder(context, x, y, width, height, border);
    drawText(context, label, x + Math.max(4, (width - label.length() * 6) / 2), y + Math.max(4, (height - 8) / 2), text);
  }

  protected void drawBar(DrawContext context, int x, int y, int width, int height, float value, int color) {
    if (!style.barsEnabled) return;
    float clamped = Math.max(0.0f, Math.min(1.0f, value));
    context.fill(x, y, x + width, y + height, soften(style.borderColor, 0xAA));
    context.fill(x, y, x + Math.round(width * clamped), y + height, color);
  }

  protected int textColor() {
    return style.textColor;
  }

  protected int mutedColor() {
    return style.mutedTextColor;
  }

  protected int accentColor() {
    return style.accentColor;
  }

  protected int pressedColor() {
    return style.pressedColor;
  }

  protected int successColor() {
    return 0xFF35D07F;
  }

  protected int warningColor() {
    return 0xFFFFB84D;
  }

  protected int errorColor() {
    return 0xFFFF5A5A;
  }

  protected int soften(int color, int alpha) {
    return (alpha << 24) | (color & 0x00FFFFFF);
  }
}
