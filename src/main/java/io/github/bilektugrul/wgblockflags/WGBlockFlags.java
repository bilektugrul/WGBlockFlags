package io.github.bilektugrul.wgblockflags;

import io.github.bilektugrul.butils.BUtilsLib;
import org.bukkit.plugin.java.JavaPlugin;

public class WGBlockFlags extends JavaPlugin {

    private RegionDataContainer container;

    @Override
    public void onEnable() {
        BUtilsLib.setUsingPlugin(this);
        saveDefaultConfig();

        container = new RegionDataContainer(this);
        CommandHandler.register(new WGBFCommand(this));
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
    }

    @Override
    public void onDisable() {
        container.save();
    }

    public RegionDataContainer getContainer() {
        return container;
    }

}