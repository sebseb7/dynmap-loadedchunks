package io.github.sebseb7.loadedchunks.listener;

import io.github.sebseb7.loadedChunks.Loadedchunks;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;
import org.jetbrains.annotations.NotNull;

public class ChunkLoadListener implements Listener {

  private final Loadedchunks main;
  private final MarkerSet set;

  public ChunkLoadListener(@NotNull Loadedchunks main, @NotNull MarkerSet set) {
    this.main = main;
    this.set = set;
  }

  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    Chunk chunk = event.getChunk();

    String markerid =
        chunk.getWorld().getName()
            + " "
            + Integer.toString(chunk.getX() * 16)
            + " "
            + Integer.toString(chunk.getZ() * 16);

    String type = "";
    if (chunk.getLoadLevel() == Chunk.LoadLevel.BORDER) {
      type = "BORDER";
    } else if (chunk.getLoadLevel() == Chunk.LoadLevel.ENTITY_TICKING) {
      type = "ENTITY_TICKING";
    } else if (chunk.getLoadLevel() == Chunk.LoadLevel.TICKING) {
      type = "TICKING";
    }

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

    AreaMarker m =
        set.createAreaMarker(markerid, type, false, chunk.getWorld().getName(), x, z, false);
    if (m == null) {
      return;
    }

    m.setLineStyle(1, 0.5, 0x00ff00);
    if (chunk.getLoadLevel() == Chunk.LoadLevel.BORDER) {
      m.setFillStyle(0.5, 0x0000ff);
    } else if (chunk.getLoadLevel() == Chunk.LoadLevel.ENTITY_TICKING) {
      m.setFillStyle(0.5, 0xff0000);
    } else if (chunk.getLoadLevel() == Chunk.LoadLevel.TICKING) {
      m.setFillStyle(0.5, 0xff00ff);
    }
    main.markermap.put(markerid, m);
  }
}
