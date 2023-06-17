package io.github.sebseb7.loadedchunks.listener;

import io.github.sebseb7.loadedChunks.Loadedchunks;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;
import org.jetbrains.annotations.NotNull;

public class ChunkUnloadListener implements Listener {

  private final Loadedchunks main;
  private final MarkerSet set;

  public ChunkUnloadListener(@NotNull Loadedchunks main, @NotNull MarkerSet set) {
    this.main = main;
    this.set = set;
  }

  @EventHandler
  public void onChunkUnload(ChunkUnloadEvent event) {
    Chunk chunk = event.getChunk();

    String markerid =
        chunk.getWorld().getName()
            + " "
            + Integer.toString(chunk.getX() * 16)
            + " "
            + Integer.toString(chunk.getZ() * 16);

    AreaMarker m = main.markermap.remove(markerid);
    if (m != null) {
      m.deleteMarker();
    }
  }
}
