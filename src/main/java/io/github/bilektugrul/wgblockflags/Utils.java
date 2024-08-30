package io.github.bilektugrul.wgblockflags;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {

    private static final WorldGuardPlatform platform = WorldGuard.getInstance().getPlatform();

    public static List<String> getRegionsAt(Location l) {
        final ArrayList<String> r = new ArrayList<>();

        if (l == null)
            return Collections.emptyList();
        if (l.getWorld() == null)
            return Collections.emptyList();

        RegionManager manager = platform.getRegionContainer().get(BukkitAdapter.adapt(l.getWorld()));

        if (manager == null) {
            return r;
        }

        ApplicableRegionSet applicable = manager.getApplicableRegions(BukkitAdapter.adapt(l).toVector().toBlockPoint());
        if (applicable.getRegions().isEmpty()) {
            return r;
        }

        for (ProtectedRegion region : applicable) {
            r.add(region.getId());
        }

        return r;
    }

    public static ProtectedRegion getRegionFromString(String name, World inWorld) {
        com.sk89q.worldedit.world.World wgWorld = BukkitAdapter.adapt(inWorld);
        RegionManager manager = platform.getRegionContainer().get(wgWorld);

        return manager.getRegion(name);
    }

}
