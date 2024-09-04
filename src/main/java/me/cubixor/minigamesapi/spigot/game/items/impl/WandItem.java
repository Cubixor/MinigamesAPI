package me.cubixor.minigamesapi.spigot.game.items.impl;

import com.cryptomorin.xseries.XMaterial;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.game.items.ClickableItem;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import me.cubixor.minigamesapi.spigot.utils.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class WandItem extends ClickableItem {

    public WandItem() {
        super(XMaterial.BLAZE_ROD.parseItem(), "arena-setup.wand-item-name", "arena-setup.wand-item-lore");
    }

    @Override
    public void handleClick(ArenasManager arenasManager, LocalArena localArena, Player player, PlayerInteractEvent evt) {
        if (!Permissions.has(player, "setup.wand")) {
            Messages.send(player, "general.no-permission");
            return;
        }

        switch (evt.getAction()) {
            case LEFT_CLICK_BLOCK: {
                player.setMetadata("MGAPI-selMin", new FixedMetadataValue(MinigamesAPI.getPlugin(), evt.getClickedBlock()));
                Messages.send(player, "arena-setup.wand-select-min");
                break;
            }
            case RIGHT_CLICK_BLOCK: {
                player.setMetadata("MGAPI-selMax", new FixedMetadataValue(MinigamesAPI.getPlugin(), evt.getClickedBlock()));
                Messages.send(player, "arena-setup.wand-select-max");
                break;
            }
        }
    }

}
