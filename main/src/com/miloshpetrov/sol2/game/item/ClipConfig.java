package com.miloshpetrov.sol2.game.item;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.miloshpetrov.sol2.SolFiles;
import com.miloshpetrov.sol2.TexMan;
import com.miloshpetrov.sol2.game.projectile.ProjectileConfig;

public class ClipConfig {
  public final int price;
  public final String displayName;
  public final String desc;
  public final int size;
  public final ClipItem example;
  public final TextureAtlas.AtlasRegion icon;
  public final ProjectileConfig projConfig;
  public final boolean infinite;
  public final int projectilesPerShot;
  public final SolItemType itemType;
  public final String plural;

  public ClipConfig(ProjectileConfig projConfig, boolean infinite, int price, String displayName, int size,
    String plural, TextureAtlas.AtlasRegion icon, int projectilesPerShot, SolItemType itemType)
  {
    this.projConfig = projConfig;
    this.infinite = infinite;
    this.price = price;
    this.displayName = displayName;
    this.size = size;
    this.icon = icon;
    this.projectilesPerShot = projectilesPerShot;
    this.itemType = itemType;
    this.plural = plural;
    this.desc = size + " " + this.plural;
    this.example = new ClipItem(this);
  }

  public static void load(ItemMan itemMan, TexMan texMan, SolItemTypes types) {
    JsonReader r = new JsonReader();
    FileHandle configFile = SolFiles.readOnly(ItemMan.ITEM_CONFIGS_DIR + "clips.json");
    JsonValue parsed = r.parse(configFile);
    for (JsonValue sh : parsed) {
      String projectileName = sh.getString("projectile");
      ProjectileConfig projConfig = itemMan.projConfigs.find(projectileName);
      boolean infinite = sh.getBoolean("infinite", false);
      int size = sh.getInt("size");
      int projectilesPerShot = sh.getInt("projectilesPerShot", 1);
      if (projectilesPerShot < 1) throw new AssertionError("projectiles per shot");

      int price = 0;
      String displayName = "";
      String plural = "";
      TextureAtlas.AtlasRegion icon = null;
      if (!infinite) {
        String iconName = sh.getString("iconName");
        price = sh.getInt("price");
        displayName = sh.getString("displayName");
        plural = sh.getString("plural");
        icon = texMan.getTex(TexMan.ICONS_DIR + iconName, configFile);
      }
      ClipConfig config = new ClipConfig(projConfig, infinite, price, displayName, size, plural, icon, projectilesPerShot, types.clip);
      itemMan.registerItem(sh.name(), config.example);
    }
  }
}
