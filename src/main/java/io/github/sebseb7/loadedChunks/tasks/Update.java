package io.github.sebseb7.loadedchunks.tasks;

import io.github.sebseb7.loadedChunks.Loadedchunks;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;

public class Update {

  public static void updateTimed(Loadedchunks main, MarkerSet set) {
    double opacity = main.getConfig().getDouble("opacity", 0.5);
    main.entitycountmap.clear();
    List<World> worlds = main.getServer().getWorlds();
    for (int i = 0; i < worlds.size(); i++) {
      World world = worlds.get(i);

      String wname = world.getName();

      Chunk[] loadedChunks = world.getLoadedChunks();

      for (int j = 0; j < loadedChunks.length; j++) {
        Chunk chunk = loadedChunks[j];
        if (chunk.getLoadLevel() == Chunk.LoadLevel.ENTITY_TICKING) {
          String countid =
              wname
                  + " "
                  + Integer.toString(chunk.getX() - (chunk.getX() % 16))
                  + " "
                  + Integer.toString(chunk.getX() - (chunk.getX() % 16));
          int count = main.entitycountmap.getOrDefault(countid, 0);
          if (chunk.getEntities().length > count) {
            main.entitycountmap.put(countid, chunk.getEntities().length);
          }
          ;
        }
      }

      for (int j = 0; j < loadedChunks.length; j++) {
        Chunk chunk = loadedChunks[j];
        String desc = "";
        if (chunk.getLoadLevel() == Chunk.LoadLevel.BORDER) {
          desc = "BORDER";
        } else if (chunk.getLoadLevel() == Chunk.LoadLevel.ENTITY_TICKING) {
          desc = "ENTITY_TICKING";
        } else if (chunk.getLoadLevel() == Chunk.LoadLevel.TICKING) {
          desc = "TICKING";
        }

        int entity_count = 0;
        int with_ai_count = 0;
        Entity[] entities = chunk.getEntities();
        for (int q = 0; q < entities.length; q++) {
          entity_count++;
          if (entities[q] instanceof LivingEntity && ((LivingEntity) entities[q]).hasAI()) {
            with_ai_count++;
          }
        }

        desc += "<br>Entites: " + entity_count + "<br/> WithAI: " + with_ai_count;

        String markerid =
            wname
                + " "
                + Integer.toString(chunk.getX() * 16)
                + " "
                + Integer.toString(chunk.getZ() * 16);

        AreaMarker m = main.markermap.get(markerid);

        if (m != null) {

          if (m.getDescription() != desc) {
            m.setDescription(desc);
          }

          int fillColor = 0;
          if (chunk.getLoadLevel() == Chunk.LoadLevel.BORDER) {
            fillColor = 0x0000ff;
          } else if (chunk.getLoadLevel() == Chunk.LoadLevel.ENTITY_TICKING) {
            String countid =
                wname
                    + " "
                    + Integer.toString(chunk.getX() - (chunk.getX() % 16))
                    + " "
                    + Integer.toString(chunk.getX() - (chunk.getX() % 16));
            int max = main.entitycountmap.getOrDefault(countid, 0);
            double fraction = entity_count / (double) max;
            if (fraction > 1.0) fraction = 1.0;
            int yellow = (int) Math.round(fraction * 255.0);
            fillColor = 0xff0000 + yellow * 0x0100;
          } else if (chunk.getLoadLevel() == Chunk.LoadLevel.TICKING) {
            fillColor = 0xff00ff;
          }
          if (m.getFillColor() != fillColor) {
            m.setFillStyle(opacity, fillColor);
          }
        }
      }
    }
  }

  public static void updateTimedFull(Loadedchunks main, MarkerSet set) {

    double opacity = main.getConfig().getDouble("opacity", 0.5);
    main.entitycountmap.clear();
    final Map<String, AreaMarker> newmap = new HashMap<>();

    List<World> worlds = main.getServer().getWorlds();
    for (int i = 0; i < worlds.size(); i++) {
      World world = worlds.get(i);

      String wname = world.getName();

      Chunk[] loadedChunks = world.getLoadedChunks();

      for (int j = 0; j < loadedChunks.length; j++) {
        Chunk chunk = loadedChunks[j];
        String countid =
            wname
                + " "
                + Integer.toString(chunk.getX() - (chunk.getX() % 16))
                + " "
                + Integer.toString(chunk.getX() - (chunk.getX() % 16));
        int count = main.entitycountmap.getOrDefault(countid, 0);
        if (chunk.getEntities().length > count) {
          main.entitycountmap.put(countid, chunk.getEntities().length);
        }
        ;
      }

      for (int j = 0; j < loadedChunks.length; j++) {
        Chunk chunk = loadedChunks[j];
        String desc = "";
        if (chunk.getLoadLevel() == Chunk.LoadLevel.BORDER) {
          desc = "BORDER";
        } else if (chunk.getLoadLevel() == Chunk.LoadLevel.ENTITY_TICKING) {
          desc = "ENTITY_TICKING";
        } else if (chunk.getLoadLevel() == Chunk.LoadLevel.TICKING) {
          desc = "TICKING";
        }

        int entity_count = 0;
        int with_ai_count = 0;
        Entity[] entities = chunk.getEntities();
        for (int q = 0; q < entities.length; q++) {
          entity_count++;
          if (entities[q] instanceof LivingEntity && ((LivingEntity) entities[q]).hasAI()) {
            with_ai_count++;
          }
        }

        desc += "<br>Entites: " + entity_count + "<br/> WithAI: " + with_ai_count;

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
          m = set.createAreaMarker(markerid, desc, false, wname, x, z, false);
        } else {
          if (m.getDescription() != desc) {
            m.setDescription(desc);
          }
        }
        if (m == null) {
          continue;
        }

        m.setLineStyle(1, opacity, 0x00ff00);
        if (chunk.getLoadLevel() == Chunk.LoadLevel.BORDER) {
          m.setFillStyle(opacity, 0x0000ff);
        } else if (chunk.getLoadLevel() == Chunk.LoadLevel.ENTITY_TICKING) {
          String countid =
              wname
                  + " "
                  + Integer.toString(chunk.getX() - (chunk.getX() % 16))
                  + " "
                  + Integer.toString(chunk.getX() - (chunk.getX() % 16));
          int max = main.entitycountmap.getOrDefault(countid, 0);
          double fraction = entity_count / (double) max;
          if (fraction > 1.0) fraction = 1.0;
          int yellow = (int) Math.round(fraction * 255.0);
          m.setFillStyle(opacity, 0xff0000 + yellow * 0x100);
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
