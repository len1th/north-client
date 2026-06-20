package dev.northclient.screen;

import dev.northclient.hud.HudBounds;
import dev.northclient.hud.HudElement;
import dev.northclient.hud.HudManager;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class HudEditorScreen extends Screen {
  private final HudManager hudManager;
  private HudElement selected;
  private double dragOffsetX;
  private double dragOffsetY;

  public HudEditorScreen(HudManager hudManager) {
    super(Text.literal("North HUD Editor"));
    this.hudManager = hudManager;
  }

  @Override
  public boolean shouldPause() {
    return false;
  }

  @Override
  public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    renderBackground(context, mouseX, mouseY, delta);
    context.drawCenteredTextWithShadow(textRenderer, "North HUD Editor", width / 2, 16, 0xFFEAF4FF);
    context.drawTextWithShadow(textRenderer, "Right Shift ile ac/kapat - HUD elemanlarini surukle", 16, 36, 0xFF9FB4CC);

    for (HudElement element : hudManager.elements()) {
      element.renderPreview(context, delta);
      HudBounds b = element.getBounds();
      int color = element == selected ? 0xFF22C7FF : 0x6622C7FF;
      drawBorder(context, (int) b.x(), (int) b.y(), (int) b.width(), (int) b.height(), color);
    }
    super.render(context, mouseX, mouseY, delta);
  }

  @Override
  public boolean mouseClicked(Click click, boolean doubled) {
    for (HudElement element : hudManager.elements()) {
      if (element.getBounds().contains(click.x(), click.y())) {
        selected = element;
        dragOffsetX = click.x() - element.getBounds().x();
        dragOffsetY = click.y() - element.getBounds().y();
        return true;
      }
    }
    return super.mouseClicked(click, doubled);
  }

  @Override
  public boolean mouseDragged(Click click, double offsetX, double offsetY) {
    if (selected != null) {
      HudBounds b = selected.getBounds();
      float x = (float) Math.max(0, Math.min(width - b.width(), click.x() - dragOffsetX));
      float y = (float) Math.max(0, Math.min(height - b.height(), click.y() - dragOffsetY));
      selected.setPosition(Math.round(x / 8f) * 8f, Math.round(y / 8f) * 8f);
      return true;
    }
    return super.mouseDragged(click, offsetX, offsetY);
  }

  @Override
  public boolean mouseReleased(Click click) {
    if (selected != null) {
      hudManager.save();
      selected = null;
      return true;
    }
    return super.mouseReleased(click);
  }

  private void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
    context.fill(x, y, x + width, y + 1, color);
    context.fill(x, y + height - 1, x + width, y + height, color);
    context.fill(x, y, x + 1, y + height, color);
    context.fill(x + width - 1, y, x + width, y + height, color);
  }
}
