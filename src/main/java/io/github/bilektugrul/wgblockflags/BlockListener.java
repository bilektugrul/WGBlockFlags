package io.github.bilektugrul.wgblockflags;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class BlockListener implements Listener {

    private final RegionDataContainer container;

    public BlockListener(WGBlockFlags plugin) {
        this.container = plugin.getContainer();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        if (e.getPlayer().hasPermission("wgbf.place")) {
            return;
        }

        List<String> regions = Utils.getRegionsAt(e.getBlockPlaced().getLocation());

        for (String region : regions) {
            List<String> placeable = container.getPlaceableBlocksOf(region);
            if (placeable == null) continue;

            if (!placeable.contains(e.getBlockPlaced().getType().name())) {
                e.setCancelled(true);
            }

            if (e.isCancelled() && placeable.contains(e.getBlock().getType().name())) {
                e.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.getPlayer().hasPermission("wgbf.break")) {
            return;
        }

        List<String> regions = Utils.getRegionsAt(e.getBlock().getLocation());

        for (String region : regions) {
            List<String> breakable = container.getBreakableBlocksOf(region);
            if (breakable == null) continue;

            if (!breakable.contains(e.getBlock().getType().name())) {
                e.setCancelled(true);
            }

            if (e.isCancelled() && breakable.contains(e.getBlock().getType().name())) {
                e.setCancelled(false);
            }

        }

    }

}
