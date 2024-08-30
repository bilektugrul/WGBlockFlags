package io.github.bilektugrul.wgblockflags;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.github.bilektugrul.butils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WGBFCommand extends Command {

    private final WGBlockFlags plugin;
    private final RegionDataContainer container;

    public WGBFCommand(WGBlockFlags plugin) {
        super("wgbf", "WG Block Flags command", "", List.of());
        this.plugin = plugin;
        this.container = plugin.getContainer();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }

        if (!player.hasPermission("wgbf.admin")){
            Utils.noPermission(player);
            return true;
        }

        if (args.length == 0) {
            Utils.sendMessage("usage", player);
            return true;
        }

        String arg = args[0];
        if (arg.equalsIgnoreCase("name")) {
            if (args.length == 2) {
                Block block = player.getTargetBlockExact(5);
                if (block != null) {
                    player.sendMessage(block.getType().name());
                }
            } else {
                Material material = player.getInventory().getItem(EquipmentSlot.HAND).getType();
                player.sendMessage(material.name());
            }
        }

        if (arg.equalsIgnoreCase("reload")) {
            if (args.length == 2 && args[1].equalsIgnoreCase("true")) {
                plugin.reloadConfig();
                plugin.getContainer().load();
                Utils.sendMessage("reloaded", player);
            } else {
                player.sendMessage(Component
                        .text(Utils.getMessage("confirm-reload", player))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/wgbf reload true")));
            }
            return true;
        }

        if (arg.equalsIgnoreCase("add-placeable")) {

            if (args.length < 3) {
                player.sendMessage(Utils.getMessage("wrong-usage", player).replace("%type%", "add-placeable"));
                return true;
            }

            String regionName = args[1];
            String material = args[2];
            container.addPlaceable(regionName, material);
            player.sendMessage(Utils.getMessage("added-placeable", player)
                    .replace("%block%", material)
                    .replace("%region%", regionName));
            return true;
        }

        if (arg.equalsIgnoreCase("add-breakable")) {

            if (args.length < 3) {
                player.sendMessage(Utils.getMessage("wrong-usage", player).replace("%type%", "add-breakable"));
                return true;
            }

            String regionName = args[1];
            String material = args[2];
            container.addBreakable(regionName, material);
            player.sendMessage(Utils.getMessage("added-breakable", player)
                    .replace("%block%", material)
                    .replace("%region%", regionName));
            return true;
        }

        if (arg.equalsIgnoreCase("remove-breakable")) {

            if (args.length < 3) {
                player.sendMessage(Utils.getMessage("wrong-usage", player).replace("%type%", "remove-breakable"));
                return true;
            }

            String regionName = args[1];
            String material = args[2];
            container.removeBreakable(regionName, material);
            player.sendMessage(Utils.getMessage("removed-breakable", player)
                    .replace("%block%", material)
                    .replace("%region%", regionName));
            return true;
        }

        if (arg.equalsIgnoreCase("remove-placeable")) {

            if (args.length < 3) {
                player.sendMessage(Utils.getMessage("wrong-usage", player).replace("%type%", "remove-placeable"));
                return true;
            }

            String regionName = args[1];
            String material = args[2];
            container.removePlaceable(regionName, material);
            player.sendMessage(Utils.getMessage("removed-placeable", player)
                    .replace("%block%", material)
                    .replace("%region%", regionName));
            return true;
        }

        return true;
    }

}
