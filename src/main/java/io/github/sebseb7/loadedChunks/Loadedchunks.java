package io.github.sebseb7.loadedChunks;

import io.github.sebseb7.loadedchunks.listener.ChunkLoadListener;
import io.github.sebseb7.loadedchunks.listener.ChunkUnloadListener;
import io.github.sebseb7.loadedchunks.tasks.Update;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

public final class Loadedchunks extends JavaPlugin {

  Plugin dynmap;
  DynmapAPI api;
  MarkerSet set;
  public Map<String, AreaMarker> markermap;
  private static Loadedchunks instance;

  public Loadedchunks() {
    instance = this;
  }

  @Override
  public void onEnable() {
    dynmap = getServer().getPluginManager().getPlugin("dynmap");
    if (dynmap == null || !dynmap.isEnabled()) {
      getLogger().severe("Unable to find Dynmap! The plugin will shut down.");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }
    markermap = new HashMap<>();
    api = (DynmapAPI) dynmap;
    getServer().getConsoleSender().sendMessage("Loaded");

    MarkerAPI markerapi = api.getMarkerAPI();
    set = markerapi.getMarkerSet("loadedchunks.markerset");
    if (set == null) {
      set = markerapi.createMarkerSet("loadedchunks.markerset", "Loaded Chunks", null, false);
    }

    getServer().getPluginManager().registerEvents(new ChunkLoadListener(this, set), instance);
    getServer().getPluginManager().registerEvents(new ChunkUnloadListener(this, set), instance);

    startUpdateTask();
  }

  @Override
  public void onDisable() {
    getServer().getConsoleSender().sendMessage("Unloaded");
    getServer().getScheduler().cancelTasks(this);
    if (set != null) {
      set.deleteMarkerSet();
      set = null;
    }
    markermap.clear();
  }

  private void startUpdateTask() {

    Loadedchunks main = this;

    new BukkitRunnable() {
      @Override
      public void run() {
        Update.updateTimed(main, set);
      }
    }.runTaskTimerAsynchronously(this, 20L * 4L, 20L * 2L);
    new BukkitRunnable() {
      @Override
      public void run() {
        Update.updateTimedFull(main, set);
      }
    }.runTaskTimerAsynchronously(this, 20L * 2L, 20L * 20L);
  }
}
