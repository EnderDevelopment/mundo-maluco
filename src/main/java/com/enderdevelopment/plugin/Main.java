package com.enderdevelopment.plugin;

import java.util.Objects;
import java.util.Random;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Main extends JavaPlugin implements Listener, CommandExecutor {
    private static final long EVENT_PERIOD_TICKS = 200L;
    private final Random random = new Random();
    private boolean enabled = true;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getCommand("mundomal")).setExecutor(this);
        Bukkit.getScheduler().runTaskTimer(this, this::runCrazyEvent, EVENT_PERIOD_TICKS, EVENT_PERIOD_TICKS);
        getLogger().info("Mundo Maluco enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Mundo Maluco disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("mundomal.admin")) {
            sender.sendMessage(Component.text("You do not have permission to control Mundo Maluco.", NamedTextColor.RED));
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(Component.text("Usage: /mundomal <on|off|status>", NamedTextColor.YELLOW));
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "on" -> {
                enabled = true;
                broadcast("Mundo Maluco is now active!", NamedTextColor.GREEN);
            }
            case "off" -> {
                enabled = false;
                broadcast("Mundo Maluco is now paused.", NamedTextColor.RED);
            }
            case "status" -> sender.sendMessage(Component.text(
                    "Mundo Maluco: " + (enabled ? "active" : "paused"),
                    enabled ? NamedTextColor.GREEN : NamedTextColor.RED));
            default -> sender.sendMessage(Component.text("Usage: /mundomal <on|off|status>", NamedTextColor.YELLOW));
        }
        return true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (enabled) {
            event.getPlayer().sendMessage(Component.text("Welcome to Mundo Maluco! Expect the unexpected.", NamedTextColor.LIGHT_PURPLE));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (enabled && event.getEntity() instanceof Player player
                && event.getCause() == EntityDamageEvent.DamageCause.FALL && random.nextInt(4) == 0) {
            event.setCancelled(true);
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 18, 0.4, 0.1, 0.4, 0.03);
            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_AMBIENT, 1.0f, 1.5f);
            player.sendMessage(Component.text("The ground caught you. For now.", NamedTextColor.AQUA));
        }
    }

    private void runCrazyEvent() {
        if (!enabled) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            triggerRandomEvent(player);
        }
    }

    private void triggerRandomEvent(Player player) {
        switch (random.nextInt(5)) {
            case 0 -> giveGift(player);
            case 1 -> launchFireworksWithoutFire(player);
            case 2 -> applyTemporaryPower(player);
            case 3 -> spawnFriendlySurprise(player);
            case 4 -> swapWeather(player);
            default -> throw new IllegalStateException("Unexpected random event");
        }
    }

    private void giveGift(Player player) {
        Material[] gifts = {Material.COOKED_BEEF, Material.GOLDEN_APPLE, Material.FIREWORK_ROCKET, Material.ENDER_PEARL};
        Material gift = gifts[random.nextInt(gifts.length)];
        player.getInventory().addItem(new ItemStack(gift, gift == Material.GOLDEN_APPLE ? 1 : 3));
        player.sendMessage(Component.text("A crazy gift appeared in your inventory: " + gift.name() + "!", NamedTextColor.GOLD));
    }

    private void launchFireworksWithoutFire(Player player) {
        Location location = player.getLocation().clone().add(0, 1, 0);
        player.getWorld().spawnParticle(Particle.FIREWORK, location, 35, 0.6, 0.8, 0.6, 0.15);
        player.playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0.8f, 1.2f);
        player.sendMessage(Component.text("A silent firework show just for you!", NamedTextColor.LIGHT_PURPLE));
    }

    private void applyTemporaryPower(Player player) {
        PotionEffectType[] effects = {PotionEffectType.SPEED, PotionEffectType.JUMP_BOOST, PotionEffectType.HASTE, PotionEffectType.NIGHT_VISION};
        PotionEffectType effect = effects[random.nextInt(effects.length)];
        player.addPotionEffect(new PotionEffect(effect, 20 * 15, 1, false, true, true));
        player.sendMessage(Component.text("You received 15 seconds of " + effect.getKey().getKey() + "!", NamedTextColor.AQUA));
    }

    private void spawnFriendlySurprise(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation().clone().add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
        location.setY(world.getHighestBlockYAt(location) + 1);
        EntityType[] types = {EntityType.CHICKEN, EntityType.RABBIT, EntityType.FROG};
        world.spawnEntity(location, types[random.nextInt(types.length)]);
        player.sendMessage(Component.text("A tiny visitor has arrived nearby.", NamedTextColor.GREEN));
    }

    private void swapWeather(Player player) {
        World world = player.getWorld();
        boolean storm = !world.hasStorm();
        world.setStorm(storm);
        world.setThundering(false);
        player.sendMessage(Component.text(storm ? "Rain mode activated!" : "The sky is clear again!", NamedTextColor.BLUE));
    }

    private void broadcast(String message, NamedTextColor color) {
        Bukkit.broadcast(Component.text(message, color));
    }
}
