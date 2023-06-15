package io.github.sebseb7;

import io.github.sebseb7.loadedchunks.commands.LoadedchunksCmd;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;

public final class Loadedchunks extends JavaPlugin {

  Plugin dynmap;
  DynmapAPI api;
  public Map<String, AreaMarker> markermap;

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
    getServer().getConsoleSender().sendMessage("[LoadedChunks] Loaded");
    getCommand("loadedchunks").setExecutor(new LoadedchunksCmd(this, api));
  }

  @Override
  public void onDisable() {
    getServer().getConsoleSender().sendMessage("[LoadedChunks] Unloaded");
  }
}
