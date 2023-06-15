package io.github.sebseb7.loadedchunks.commands;

import io.github.sebseb7.Loadedchunks;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.*;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;
import org.jetbrains.annotations.NotNull;

public class LoadedchunksCmd implements CommandExecutor {

  private final Loadedchunks main;
  private final DynmapAPI api;
  private final MarkerAPI markerapi;
  private final MarkerSet set;

  public LoadedchunksCmd(@NotNull Loadedchunks main, @NotNull DynmapAPI api) {
    this.main = main;
    this.api = api;
    this.markerapi = api.getMarkerAPI();
    MarkerSet set = markerapi.getMarkerSet("loadedchunks.markerset");
    if (set == null) {
      set = markerapi.createMarkerSet("loadedchunks.markerset", "Loaded Chunks", null, false);
    }
    this.set = set;
  }

  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String label,
      @NotNull String[] args) {

    final Map<String, AreaMarker> newmap = new HashMap<>();

    main.getLogger().info("updating loaded chunks on map");
    List<World> worlds = main.getServer().getWorlds();
    for (int i = 0; i < worlds.size(); i++) {
      World world = worlds.get(i);
      main.getLogger().info(world.getName());

      String wname = world.getName();

      Chunk[] loadedChunks = world.getLoadedChunks();

      for (int j = 0; j < loadedChunks.length; j++) {
        Chunk chunk = loadedChunks[j];
        String type = "";
        if (chunk.getLoadLevel() == Chunk.LoadLevel.BORDER) {
          type = "BORDER";
        } else if (chunk.getLoadLevel() == Chunk.LoadLevel.ENTITY_TICKING) {
          type = "ENTITY_TICKING";
        } else if (chunk.getLoadLevel() == Chunk.LoadLevel.TICKING) {
          type = "TICKING";
        }

        String markerid =
            wname
                + " "
                + type
                + " "
                + Integer.toString(chunk.getX() * 16)
                + " "
                + Integer.toString(chunk.getZ() * 16);

        double[] x = new double[4];
        double[] z = new double[4];
        x[0] = chunk.getX() * 16;
        z[0] = chunk.getZ() * 16;
        x[1] = chunk.getX() * 16;
        z[1] = (chunk.getZ() + 1) * 16;
        x[2] = (chunk.getX() + 1) * 16;
        z[2] = (chunk.getZ() + 1) * 16;
        x[3] = (chunk.getX() + 1) * 16;

        z[3] = chunk.getZ() * 16;

        AreaMarker m = main.markermap.remove(markerid);
        if (m == null) {
          m = set.createAreaMarker(markerid, type, false, wname, x, z, false);
          if (m == null) {
            continue;
          }
        }

        m.setLineStyle(1, 0.5, 0x00ff00);
        if (chunk.getLoadLevel() == Chunk.LoadLevel.BORDER) {
          m.setFillStyle(0.5, 0x0000ff);
        } else if (chunk.getLoadLevel() == Chunk.LoadLevel.ENTITY_TICKING) {
          m.setFillStyle(0.5, 0xff0000);
        } else if (chunk.getLoadLevel() == Chunk.LoadLevel.TICKING) {
          m.setFillStyle(0.5, 0xff00ff);
        }
        newmap.put(markerid, m);
      }
    }
    if (main.markermap != null) {
      for (final AreaMarker oldm : main.markermap.values()) {
        oldm.deleteMarker();
      }
    }
    main.markermap = newmap;

    return false;
  }
}
