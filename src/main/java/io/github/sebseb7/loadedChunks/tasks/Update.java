package io.github.sebseb7.loadedchunks.tasks;

import io.github.sebseb7.loadedChunks.Loadedchunks;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;

public class Update {

  public static void updateTimed(Loadedchunks main, MarkerSet set) {
    List<World> worlds = main.getServer().getWorlds();
    for (int i = 0; i < worlds.size(); i++) {
      World world = worlds.get(i);

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
                + Integer.toString(chunk.getX() * 16)
                + " "
                + Integer.toString(chunk.getZ() * 16);

        AreaMarker m = main.markermap.get(markerid);

        if (m != null) {

          if (m.getDescription() != type) {
            m.setDescription(type);
            double opacity = main.getConfig().getDouble("opacity", 0.5);
            if (type == "BORDER") {
              m.setFillStyle(opacity, 0x0000ff);
            } else if (type == "ENTITY_TICKING") {
              m.setFillStyle(opacity, 0xff0000);
            } else if (type == "TICKING") {
              m.setFillStyle(opacity, 0xff00ff);
            }
          }
        }
      }
    }
  }

  public static void updateTimedFull(Loadedchunks main, MarkerSet set) {

    final Map<String, AreaMarker> newmap = new HashMap<>();

    List<World> worlds = main.getServer().getWorlds();
    for (int i = 0; i < worlds.size(); i++) {
      World world = worlds.get(i);

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
        } else {
          if (m.getDescription() != type) {
            m.deleteMarker();
            m = set.createAreaMarker(markerid, type, false, wname, x, z, false);
          }
        }
        if (m == null) {
          continue;
        }

        double opacity = main.getConfig().getDouble("opacity", 0.5);
        m.setLineStyle(1, opacity, 0x00ff00);
        if (chunk.getLoadLevel() == Chunk.LoadLevel.BORDER) {
          m.setFillStyle(opacity, 0x0000ff);
        } else if (chunk.getLoadLevel() == Chunk.LoadLevel.ENTITY_TICKING) {
          m.setFillStyle(opacity, 0xff0000);
        } else if (chunk.getLoadLevel() == Chunk.LoadLevel.TICKING) {
          m.setFillStyle(opacity, 0xff00ff);
        }
        newmap.put(markerid, m);
      }
    }
    if (main.markermap != null) {
      for (final AreaMarker oldm : main.markermap.values()) {
        oldm.deleteMarker();
      }
      main.markermap.clear();
      for (String i : newmap.keySet()) {
        main.markermap.put(i, newmap.get(i));
      }
    }
  }
}
