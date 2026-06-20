package dev.northclient.config;

public final class NorthClientSettings {
  private static boolean itemPhysicsEnabled;
  private static boolean characterAnimationEnabled;
  private static boolean hudAnimationsEnabled = true;

  private NorthClientSettings() {
  }

  public static boolean itemPhysicsEnabled() {
    return itemPhysicsEnabled;
  }

  public static void setItemPhysicsEnabled(boolean enabled) {
    itemPhysicsEnabled = enabled;
  }

  public static boolean characterAnimationEnabled() {
    return characterAnimationEnabled;
  }

  public static void setCharacterAnimationEnabled(boolean enabled) {
    characterAnimationEnabled = enabled;
  }

  public static boolean hudAnimationsEnabled() {
    return hudAnimationsEnabled;
  }

  public static void setHudAnimationsEnabled(boolean enabled) {
    hudAnimationsEnabled = enabled;
  }

  public static void resetDefaults() {
    itemPhysicsEnabled = false;
    characterAnimationEnabled = false;
    hudAnimationsEnabled = true;
  }
}
