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
    context.fill(0, 0, width, height, 0x66070B12);
    context.fill(8, 8, 158, height - 8, 0xDD0E1522);
    drawBorder(context, 8, 8, 150, height - 16, 0x8822314A);
    context.fill(width - 172, 8, width - 8, 112, 0xDD0E1522);
    drawBorder(context, width - 172, 8, 164, 104, 0x8822314A);
    context.drawCenteredTextWithShadow(textRenderer, "North HUD Editor", width / 2, 16, 0xFFEAF4FF);
    context.drawTextWithShadow(textRenderer, "HUD Listesi", 18, 18, 0xFF22C7FF);
    context.drawTextWithShadow(textRenderer, "ON/OFF   R", 18, 32, 0xFF9FB4CC);
    context.drawTextWithShadow(textRenderer, "Right Shift ile ac/kapat - HUD elemanlarini surukle", 170, 36, 0xFF9FB4CC);
    context.drawTextWithShadow(textRenderer, "Ayarlar", width - 160, 18, 0xFF22C7FF);
    context.drawTextWithShadow(textRenderer, "Sol: sec/surukle", width - 160, 36, 0xFF9FB4CC);
    context.drawTextWithShadow(textRenderer, "Liste: ac/kapat", width - 160, 52, 0xFF9FB4CC);
    context.drawTextWithShadow(textRenderer, "R: satiri sifirla", width - 160, 68, 0xFF9FB4CC);
    context.drawTextWithShadow(textRenderer, "Konumlar kaydedilir", width - 160, 84, 0xFF9FB4CC);

    int row = 0;
    for (HudElement element : hudManager.elements()) {
      int y = 50 + row * 16;
      int color = element.isEnabled() ? 0xFFEAF4FF : 0xFF56687D;
      context.fill(16, y - 2, 150, y + 12, element == selected ? 0x3322C7FF : 0x00000000);
      context.drawTextWithShadow(textRenderer, element.isEnabled() ? "ON " : "OFF", 18, y, element.isEnabled() ? 0xFF35D07F : 0xFFFF5A5A);
      context.drawTextWithShadow(textRenderer, trimName(element.getName()), 48, y, color);
      context.drawTextWithShadow(textRenderer, "R", 140, y, 0xFFFFB84D);
      row++;
    }

    int actionsY = height - 42;
    context.fill(18, actionsY, 74, actionsY + 22, 0xAA131C2B);
    drawBorder(context, 18, actionsY, 56, 22, 0xFF22314A);
    context.drawTextWithShadow(textRenderer, "Save", 32, actionsY + 7, 0xFFEAF4FF);
    context.fill(82, actionsY, 148, actionsY + 22, 0xAA131C2B);
    drawBorder(context, 82, actionsY, 66, 22, 0xFFFFB84D);
    context.drawTextWithShadow(textRenderer, "Default", 94, actionsY + 7, 0xFFFFB84D);

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
    int row = 0;
    for (HudElement element : hudManager.elements()) {
      int y = 50 + row * 16;
      if (click.x() >= 16 && click.x() <= 134 && click.y() >= y - 3 && click.y() <= y + 13) {
        element.setEnabled(!element.isEnabled());
        selected = element;
        hudManager.save();
        return true;
      }
      if (click.x() >= 136 && click.x() <= 152 && click.y() >= y - 3 && click.y() <= y + 13) {
        element.resetDefault();
        selected = element;
        hudManager.save();
        return true;
      }
      row++;
    }
    int actionsY = height - 42;
    if (click.x() >= 18 && click.x() <= 74 && click.y() >= actionsY && click.y() <= actionsY + 22) {
      hudManager.save();
      return true;
    }
    if (click.x() >= 82 && click.x() <= 148 && click.y() >= actionsY && click.y() <= actionsY + 22) {
      for (HudElement element : hudManager.elements()) {
        element.resetDefault();
      }
      selected = null;
      hudManager.save();
      return true;
    }
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

  private String trimName(String name) {
    return name.length() <= 13 ? name : name.substring(0, 12) + ".";
  }
}
