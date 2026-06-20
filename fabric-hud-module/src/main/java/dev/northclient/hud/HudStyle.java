package dev.northclient.hud;

public final class HudStyle {
  public static final int[] PALETTE = new int[] {
    0xFF22C7FF,
    0xFF4DA3FF,
    0xFF35D07F,
    0xFFFFB84D,
    0xFFFF5A5A,
    0xFFEAF4FF,
    0xFFB46CFF,
    0xFFFF6FD8
  };

  public int accentColor = 0xFF22C7FF;
  public int pressedColor = 0xFF22C7FF;
  public int textColor = 0xFFEAF4FF;
  public int mutedTextColor = 0xFF9FB4CC;
  public int backgroundColor = 0xCC0E1522;
  public int borderColor = 0x9922314A;
  public int barColor = 0xFF22C7FF;
  public boolean backgroundEnabled = true;
  public boolean labelsEnabled = true;
  public boolean barsEnabled = true;
  public boolean compact = false;

  private final int defaultAccentColor;
  private final int defaultPressedColor;
  private final int defaultTextColor;
  private final int defaultMutedTextColor;
  private final int defaultBackgroundColor;
  private final int defaultBorderColor;
  private final int defaultBarColor;
  private final boolean defaultBackgroundEnabled;
  private final boolean defaultLabelsEnabled;
  private final boolean defaultBarsEnabled;
  private final boolean defaultCompact;

  public HudStyle() {
    this.defaultAccentColor = accentColor;
    this.defaultPressedColor = pressedColor;
    this.defaultTextColor = textColor;
    this.defaultMutedTextColor = mutedTextColor;
    this.defaultBackgroundColor = backgroundColor;
    this.defaultBorderColor = borderColor;
    this.defaultBarColor = barColor;
    this.defaultBackgroundEnabled = backgroundEnabled;
    this.defaultLabelsEnabled = labelsEnabled;
    this.defaultBarsEnabled = barsEnabled;
    this.defaultCompact = compact;
  }

  public void reset() {
    accentColor = defaultAccentColor;
    pressedColor = defaultPressedColor;
    textColor = defaultTextColor;
    mutedTextColor = defaultMutedTextColor;
    backgroundColor = defaultBackgroundColor;
    borderColor = defaultBorderColor;
    barColor = defaultBarColor;
    backgroundEnabled = defaultBackgroundEnabled;
    labelsEnabled = defaultLabelsEnabled;
    barsEnabled = defaultBarsEnabled;
    compact = defaultCompact;
  }

  public int nextPaletteColor(int color) {
    for (int i = 0; i < PALETTE.length; i++) {
      if (PALETTE[i] == color) {
        return PALETTE[(i + 1) % PALETTE.length];
      }
    }
    return PALETTE[0];
  }

  public int readablePressedTextColor() {
    return pressedColor == 0xFFEAF4FF ? 0xFF07111D : 0xFF07111D;
  }
}
