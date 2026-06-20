package dev.northclient.screen;

import dev.northclient.config.NorthClientSettings;
import dev.northclient.hud.HudBounds;
import dev.northclient.hud.HudElement;
import dev.northclient.hud.HudManager;
import dev.northclient.hud.HudStyle;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class HudEditorScreen extends Screen {
  private static final int TAB_HUD = 0;
  private static final int TAB_CLIENT = 1;

  private final HudManager hudManager;
  private HudElement selected;
  private double dragOffsetX;
  private double dragOffsetY;
  private boolean dragging;
  private int tab = TAB_HUD;

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
    int leftX = 8;
    int leftWidth = 158;
    int rightWidth = Math.min(190, Math.max(164, width / 3));
    int rightX = width - rightWidth - 8;
    context.fill(0, 0, width, height, 0x66070B12);
    drawPanel(context, leftX, 8, leftWidth, height - 16, "HUD Listesi");
    drawPanel(context, rightX, 8, rightWidth, height - 16, "Ayarlar");

    context.drawCenteredTextWithShadow(textRenderer, "North HUD Editor", width / 2, 16, 0xFFEAF4FF);
    context.drawTextWithShadow(textRenderer, "Right Shift ile ac/kapat - HUD elemanlarini surukle", leftX + leftWidth + 10, 36, 0xFF9FB4CC);
    renderHudList(context, leftX, leftWidth);
    renderActions(context, leftX);
    renderTabs(context, rightX, rightWidth);
    if (tab == TAB_CLIENT) {
      renderClientSettings(context, rightX, rightWidth);
    } else {
      renderHudSettings(context, rightX, rightWidth);
    }

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
    int leftX = 8;
    int leftWidth = 158;
    int rightWidth = Math.min(190, Math.max(164, width / 3));
    int rightX = width - rightWidth - 8;

    if (handleTabs(click, rightX)) return true;
    if (handleActions(click, leftX)) return true;
    if (handleHudList(click, leftX, leftWidth)) return true;
    if (tab == TAB_CLIENT && handleClientSettings(click, rightX, rightWidth)) return true;
    if (tab == TAB_HUD && handleHudSettings(click, rightX, rightWidth)) return true;

    for (HudElement element : hudManager.elements()) {
      if (element.getBounds().contains(click.x(), click.y())) {
        selected = element;
        dragging = true;
        dragOffsetX = click.x() - element.getBounds().x();
        dragOffsetY = click.y() - element.getBounds().y();
        return true;
      }
    }
    return super.mouseClicked(click, doubled);
  }

  @Override
  public boolean mouseDragged(Click click, double offsetX, double offsetY) {
    if (selected != null && dragging) {
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
    if (dragging) {
      dragging = false;
      hudManager.save();
      return true;
    }
    return super.mouseReleased(click);
  }

  private void renderHudList(DrawContext context, int leftX, int leftWidth) {
    context.drawTextWithShadow(textRenderer, "ON   HUD                         R", leftX + 10, 32, 0xFF9FB4CC);
    int row = 0;
    for (HudElement element : hudManager.elements()) {
      int y = 50 + row * 16;
      int color = element.isEnabled() ? 0xFFEAF4FF : 0xFF56687D;
      context.fill(leftX + 8, y - 2, leftX + leftWidth - 8, y + 12, element == selected ? 0x3322C7FF : 0x00000000);
      context.drawTextWithShadow(textRenderer, element.isEnabled() ? "ON " : "OFF", leftX + 10, y, element.isEnabled() ? 0xFF35D07F : 0xFFFF5A5A);
      context.drawTextWithShadow(textRenderer, trimName(element.getName(), 14), leftX + 42, y, color);
      context.drawTextWithShadow(textRenderer, "R", leftX + leftWidth - 22, y, 0xFFFFB84D);
      row++;
    }
  }

  private void renderActions(DrawContext context, int leftX) {
    int actionsY = height - 42;
    drawButton(context, leftX + 10, actionsY, 58, 22, "Save", 0xFF22314A, 0xFFEAF4FF);
    drawButton(context, leftX + 76, actionsY, 78, 22, "Default", 0xFFFFB84D, 0xFFFFB84D);
  }

  private void renderTabs(DrawContext context, int rightX, int rightWidth) {
    drawButton(context, rightX + 10, 30, 64, 18, "HUD", tab == TAB_HUD ? 0xFF22C7FF : 0xFF22314A, tab == TAB_HUD ? 0xFF22C7FF : 0xFFEAF4FF);
    drawButton(context, rightX + 82, 30, Math.max(72, rightWidth - 92), 18, "Client", tab == TAB_CLIENT ? 0xFF22C7FF : 0xFF22314A, tab == TAB_CLIENT ? 0xFF22C7FF : 0xFFEAF4FF);
  }

  private void renderHudSettings(DrawContext context, int rightX, int rightWidth) {
    if (selected == null) {
      context.drawTextWithShadow(textRenderer, "Bir HUD sec", rightX + 12, 58, 0xFFEAF4FF);
      context.drawTextWithShadow(textRenderer, "Listeden veya ekrandan", rightX + 12, 74, 0xFF9FB4CC);
      context.drawTextWithShadow(textRenderer, "tiklayarak ayarla.", rightX + 12, 90, 0xFF9FB4CC);
      return;
    }
    HudStyle style = selected.style();
    int y = 56;
    context.drawTextWithShadow(textRenderer, trimName(selected.getName(), 19), rightX + 12, y, 0xFFEAF4FF);
    context.drawTextWithShadow(textRenderer, trimName(selected.settingsHint(), 26), rightX + 12, y + 14, 0xFF9FB4CC);
    y += 34;
    drawSetting(context, rightX, rightWidth, y, "Aktif", selected.isEnabled() ? "ON" : "OFF", selected.isEnabled() ? 0xFF35D07F : 0xFFFF5A5A);
    drawSetting(context, rightX, rightWidth, y += 18, "Scale", String.format("%.2f  - / +", selected.getScale()), 0xFFEAF4FF);
    drawColorSetting(context, rightX, rightWidth, y += 18, "Accent", style.accentColor);
    drawColorSetting(context, rightX, rightWidth, y += 18, "Basili", style.pressedColor);
    drawColorSetting(context, rightX, rightWidth, y += 18, "Yazi", style.textColor);
    drawSetting(context, rightX, rightWidth, y += 18, "Arka plan", style.backgroundEnabled ? "ON" : "OFF", style.backgroundEnabled ? 0xFF35D07F : 0xFFFF5A5A);
    drawSetting(context, rightX, rightWidth, y += 18, "Label", style.labelsEnabled ? "ON" : "OFF", style.labelsEnabled ? 0xFF35D07F : 0xFFFF5A5A);
    drawSetting(context, rightX, rightWidth, y += 18, "Bar", style.barsEnabled ? "ON" : "OFF", style.barsEnabled ? 0xFF35D07F : 0xFFFF5A5A);
    drawSetting(context, rightX, rightWidth, y += 18, "Compact", style.compact ? "ON" : "OFF", style.compact ? 0xFF35D07F : 0xFFFF5A5A);
    drawButton(context, rightX + 10, height - 42, rightWidth - 20, 22, "HUD Default", 0xFFFFB84D, 0xFFFFB84D);
  }

  private void renderClientSettings(DrawContext context, int rightX, int rightWidth) {
    int y = 58;
    context.drawTextWithShadow(textRenderer, "Client Kozmetik", rightX + 12, y, 0xFFEAF4FF);
    context.drawTextWithShadow(textRenderer, "Sadece client-side", rightX + 12, y + 14, 0xFF9FB4CC);
    y += 36;
    drawSetting(context, rightX, rightWidth, y, "Item physics", NorthClientSettings.itemPhysicsEnabled() ? "ON" : "OFF", NorthClientSettings.itemPhysicsEnabled() ? 0xFF35D07F : 0xFFFF5A5A);
    drawSetting(context, rightX, rightWidth, y += 20, "Karakter anim.", NorthClientSettings.characterAnimationEnabled() ? "ON" : "OFF", NorthClientSettings.characterAnimationEnabled() ? 0xFF35D07F : 0xFFFF5A5A);
    drawSetting(context, rightX, rightWidth, y += 20, "HUD anim.", NorthClientSettings.hudAnimationsEnabled() ? "ON" : "OFF", NorthClientSettings.hudAnimationsEnabled() ? 0xFF35D07F : 0xFFFF5A5A);
    context.drawTextWithShadow(textRenderer, "Item physics: dusen item", rightX + 12, y + 30, 0xFF9FB4CC);
    context.drawTextWithShadow(textRenderer, "renderini yere yatirir.", rightX + 12, y + 44, 0xFF9FB4CC);
    context.drawTextWithShadow(textRenderer, "Karakter animasyonu", rightX + 12, y + 64, 0xFF9FB4CC);
    context.drawTextWithShadow(textRenderer, "idle hareketi yumusatir.", rightX + 12, y + 78, 0xFF9FB4CC);
  }

  private boolean handleHudList(Click click, int leftX, int leftWidth) {
    int row = 0;
    for (HudElement element : hudManager.elements()) {
      int y = 50 + row * 16;
      if (inside(click, leftX + leftWidth - 28, y - 3, 22, 16)) {
        element.resetDefault();
        selected = element;
        hudManager.save();
        return true;
      }
      if (inside(click, leftX + 8, y - 3, 32, 16)) {
        element.setEnabled(!element.isEnabled());
        selected = element;
        hudManager.save();
        return true;
      }
      if (inside(click, leftX + 40, y - 3, leftWidth - 72, 16)) {
        selected = element;
        return true;
      }
      row++;
    }
    return false;
  }

  private boolean handleActions(Click click, int leftX) {
    int actionsY = height - 42;
    if (inside(click, leftX + 10, actionsY, 58, 22)) {
      hudManager.save();
      return true;
    }
    if (inside(click, leftX + 76, actionsY, 78, 22)) {
      for (HudElement element : hudManager.elements()) {
        element.resetDefault();
      }
      NorthClientSettings.resetDefaults();
      hudManager.save();
      return true;
    }
    return false;
  }

  private boolean handleTabs(Click click, int rightX) {
    if (inside(click, rightX + 10, 30, 64, 18)) {
      tab = TAB_HUD;
      return true;
    }
    if (inside(click, rightX + 82, 30, 88, 18)) {
      tab = TAB_CLIENT;
      return true;
    }
    return false;
  }

  private boolean handleHudSettings(Click click, int rightX, int rightWidth) {
    if (selected == null) return false;
    HudStyle style = selected.style();
    int y = 90;
    if (inside(click, rightX + 10, y, rightWidth - 20, 15)) {
      selected.setEnabled(!selected.isEnabled());
    } else if (inside(click, rightX + 10, y += 18, (rightWidth - 24) / 2, 15)) {
      selected.setScale(selected.getScale() - 0.05f);
    } else if (inside(click, rightX + 12 + (rightWidth - 24) / 2, y, (rightWidth - 24) / 2, 15)) {
      selected.setScale(selected.getScale() + 0.05f);
    } else if (inside(click, rightX + 10, y += 18, rightWidth - 20, 15)) {
      style.accentColor = style.nextPaletteColor(style.accentColor);
      style.barColor = style.accentColor;
    } else if (inside(click, rightX + 10, y += 18, rightWidth - 20, 15)) {
      style.pressedColor = style.nextPaletteColor(style.pressedColor);
    } else if (inside(click, rightX + 10, y += 18, rightWidth - 20, 15)) {
      style.textColor = style.nextPaletteColor(style.textColor);
    } else if (inside(click, rightX + 10, y += 18, rightWidth - 20, 15)) {
      style.backgroundEnabled = !style.backgroundEnabled;
    } else if (inside(click, rightX + 10, y += 18, rightWidth - 20, 15)) {
      style.labelsEnabled = !style.labelsEnabled;
    } else if (inside(click, rightX + 10, y += 18, rightWidth - 20, 15)) {
      style.barsEnabled = !style.barsEnabled;
    } else if (inside(click, rightX + 10, y += 18, rightWidth - 20, 15)) {
      style.compact = !style.compact;
    } else if (inside(click, rightX + 10, height - 42, rightWidth - 20, 22)) {
      selected.resetDefault();
    } else {
      return false;
    }
    hudManager.save();
    return true;
  }

  private boolean handleClientSettings(Click click, int rightX, int rightWidth) {
    int y = 94;
    if (inside(click, rightX + 10, y, rightWidth - 20, 15)) {
      NorthClientSettings.setItemPhysicsEnabled(!NorthClientSettings.itemPhysicsEnabled());
    } else if (inside(click, rightX + 10, y += 20, rightWidth - 20, 15)) {
      NorthClientSettings.setCharacterAnimationEnabled(!NorthClientSettings.characterAnimationEnabled());
    } else if (inside(click, rightX + 10, y += 20, rightWidth - 20, 15)) {
      NorthClientSettings.setHudAnimationsEnabled(!NorthClientSettings.hudAnimationsEnabled());
    } else {
      return false;
    }
    hudManager.save();
    return true;
  }

  private void drawPanel(DrawContext context, int x, int y, int width, int height, String title) {
    context.fill(x, y, x + width, y + height, 0xDD0E1522);
    drawBorder(context, x, y, width, height, 0x8822314A);
    context.fill(x, y, x + width, y + 2, 0xFF22C7FF);
    context.drawTextWithShadow(textRenderer, title, x + 10, y + 10, 0xFF22C7FF);
  }

  private void drawButton(DrawContext context, int x, int y, int width, int height, String text, int border, int textColor) {
    context.fill(x, y, x + width, y + height, 0xAA131C2B);
    drawBorder(context, x, y, width, height, border);
    context.drawTextWithShadow(textRenderer, text, x + 8, y + 7, textColor);
  }

  private void drawSetting(DrawContext context, int rightX, int rightWidth, int y, String label, String value, int valueColor) {
    context.fill(rightX + 10, y, rightX + rightWidth - 10, y + 15, 0x88131C2B);
    drawBorder(context, rightX + 10, y, rightWidth - 20, 15, 0x5522314A);
    context.drawTextWithShadow(textRenderer, label, rightX + 16, y + 4, 0xFF9FB4CC);
    context.drawTextWithShadow(textRenderer, value, rightX + rightWidth - 64, y + 4, valueColor);
  }

  private void drawColorSetting(DrawContext context, int rightX, int rightWidth, int y, String label, int color) {
    drawSetting(context, rightX, rightWidth, y, label, colorName(color), color);
    context.fill(rightX + rightWidth - 26, y + 3, rightX + rightWidth - 14, y + 12, color);
    drawBorder(context, rightX + rightWidth - 26, y + 3, 12, 9, 0xFFEAF4FF);
  }

  private void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
    context.fill(x, y, x + width, y + 1, color);
    context.fill(x, y + height - 1, x + width, y + height, color);
    context.fill(x, y, x + 1, y + height, color);
    context.fill(x + width - 1, y, x + width, y + height, color);
  }

  private boolean inside(Click click, int x, int y, int width, int height) {
    return click.x() >= x && click.x() <= x + width && click.y() >= y && click.y() <= y + height;
  }

  private String colorName(int color) {
    return switch (color) {
      case 0xFF22C7FF -> "cyan";
      case 0xFF4DA3FF -> "blue";
      case 0xFF35D07F -> "green";
      case 0xFFFFB84D -> "gold";
      case 0xFFFF5A5A -> "red";
      case 0xFFEAF4FF -> "white";
      case 0xFFB46CFF -> "violet";
      case 0xFFFF6FD8 -> "pink";
      default -> "custom";
    };
  }

  private String trimName(String name, int max) {
    return name.length() <= max ? name : name.substring(0, Math.max(1, max - 1)) + ".";
  }
}
