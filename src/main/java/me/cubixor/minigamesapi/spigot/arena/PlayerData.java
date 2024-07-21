package me.cubixor.minigamesapi.spigot.arena;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

public class PlayerData {

    private final Player player;

    private final ItemStack[] inventory;
    private final ItemStack[] armorContents;
    private final Location location;
    private final Collection<PotionEffect> potionEffects;
    private final GameMode gameMode;
    private final double health;
    private final int food;
    private final float exp;
    private final int level;
    private final boolean fly;
    private final boolean flying;
    private final boolean invulnerable;

    public PlayerData(Player player) {
        this.player = player;
        this.inventory = player.getInventory().getContents();
        this.armorContents = player.getInventory().getArmorContents();
        this.location = player.getLocation();
        this.potionEffects = player.getActivePotionEffects();
        this.gameMode = player.getGameMode();
        this.health = player.getHealth();
        this.food = player.getFoodLevel();
        this.exp = player.getExp();
        this.level = player.getLevel();
        this.fly = player.getAllowFlight();
        this.flying = player.isFlying();
        this.invulnerable = player.isInvulnerable();
    }

    public void clearPlayerData() {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setInvulnerable(true);
        player.getActivePotionEffects().forEach(pe -> player.removePotionEffect(pe.getType()));
    }

    public void restorePlayerData() {
        clearGameData();

        player.getInventory().setContents(inventory);
        player.getInventory().setArmorContents(armorContents);
        player.updateInventory();
        potionEffects.forEach(player::addPotionEffect);
        player.setGameMode(gameMode);
        player.setHealth(health);
        player.setFoodLevel(food);
        player.setExp(exp);
        player.setLevel(level);
        player.setAllowFlight(fly);
        player.setFlying(flying);
        player.setInvulnerable(invulnerable);
    }

    private void clearGameData() {
        player.getActivePotionEffects().forEach(pe -> player.removePotionEffect(pe.getType()));
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        Titles.clearTitle(player);
        ActionBar.clearActionBar(player);
    }

    public Location getLocation() {
        return location;
    }
}
