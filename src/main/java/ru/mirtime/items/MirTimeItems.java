/*
 *    __  __ _        _____ _                
 *   |  \/  (_)_ __  |_   _(_)_ __ ___   ___ 
 *   | |\/| | | '__|   | | | | '_ ` _ \ / _ \
 *   | |  | | | |      | | | | | | | | |  __/
 *   |_|  |_|_|_|      |_| |_|_| |_| |_|\___|
 *                                           
 *   Project: Mir Time | Author: Ars3nBar
 */
package ru.mirtime.items;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MirTimeItems extends JavaPlugin implements CommandExecutor, TabCompleter, Listener {

    private NamespacedKey itemKey;
    private final Map<String, Long> cooldowns = new HashMap<>();
    private final Set<UUID> summonedBosses = new HashSet<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        itemKey = new NamespacedKey(this, "mt_custom_item");

        if (getCommand("mtitems") != null) {
            getCommand("mtitems").setExecutor(this);
            getCommand("mtitems").setTabCompleter(this);
        }
        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getConsoleSender().sendMessage(color("&#0055ff   __  __ _        _____ _                "));
        Bukkit.getConsoleSender().sendMessage(color("&#0055ff  |  \\/  (_)_ __  |_   _(_)_ __ ___   ___ "));
        Bukkit.getConsoleSender().sendMessage(color("&#00E5FF  | |\\/| | | '__|   | | | | '_ ` _ \\ / _ \\"));
        Bukkit.getConsoleSender().sendMessage(color("&#00E5FF  | |  | | | |      | | | | | | | | |  __/"));
        Bukkit.getConsoleSender().sendMessage(color("&#00FF66  |_|  |_|_|_|      |_| |_|_| |_| |_|\\___|"));
        Bukkit.getConsoleSender().sendMessage(color("&#00E5FF  MirTimeItems v1.0.0 успешно запущен!"));
    }

    public ItemStack createCustomItem(String type, int amount) {
        ItemStack is;
        String name;
        List<String> lore;
        switch (type.toLowerCase()) {
            case "trapka" -> {
                is = new ItemStack(Material.NETHERITE_SCRAP, amount);
                name = "&#0055ff&l[📦] &#00E5FF&lТрапка Бога";
                lore = List.of(
                        "&7Мгновенно возводит вокруг вас",
                        "&7неразрушимую клетку из обсидиана",
                        "&7на &#00FF66" + getConfig().getInt("trapka.duration-seconds", 18) + " сек.&7!",
                        "",
                        "&#00FF66➥ Нажмите ПКМ&f, чтобы активировать"
                );
            }
            case "cataclysm" -> {
                is = new ItemStack(Material.TNT, amount);
                name = "&#0055ff&l[💥] &c&lЯдерный Схлоп";
                lore = List.of(
                        "&7Стягивает всех врагов в радиусе &#00E5FF10 блоков",
                        "&7в одну точку и вызывает мощный взрыв",
                        "&7без разрушения построек!",
                        "",
                        "&#00FF66➥ Нажмите ПКМ&f, чтобы активировать"
                );
            }
            case "totem" -> {
                is = new ItemStack(Material.TOTEM_OF_UNDYING, amount);
                name = "&#0055ff&l[🔥] &#00FF66&lПламенный Тотем Призыва";
                lore = List.of(
                        "&7Проводит 20-секундный ритуал и призывает",
                        "&7с небес могущественного босса на &#00E5FF1 минуту&7!",
                        "&cАтакует всех игроков, не ломает блоки.",
                        "",
                        "&#00FF66➥ Нажмите ПКМ&f, чтобы начать ритуал"
                );
            }
            default -> { return null; }
        }

        ItemMeta meta = is.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(color(name));
            List<String> coloredLore = new ArrayList<>();
            for (String s : lore) coloredLore.add(color(s));
            meta.setLore(coloredLore);
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
            meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, type.toLowerCase());
            is.setItemMeta(meta);
        }
        return is;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = color(getConfig().getString("prefix", "&#0055ff&l⚡ &#00E5FF&lMIR-TIME &8» "));
        if (args.length > 0 && List.of(args).get(0).equalsIgnoreCase("reload")) {
            reloadConfig();
            sender.sendMessage(prefix + color("&#00FF66Конфиг перезагружен!"));
            return true;
        }
        if (args.length < 3 || !List.of(args).get(0).equalsIgnoreCase("give")) {
            sender.sendMessage(prefix + color("&fИспользование: &#00FF66/mtitems give <ник> <trapka|cataclysm|totem> [кол-во]"));
            return true;
        }
        Player target = Bukkit.getPlayer(List.of(args).get(1));
        if (target == null) {
            sender.sendMessage(prefix + color("&cИгрок не в сети."));
            return true;
        }
        String type = List.of(args).get(2).toLowerCase();
        int amount = 1;
        if (args.length >= 4) {
            try { amount = Math.max(1, Integer.parseInt(List.of(args).get(3))); } catch (Exception ignored) {}
        }
        ItemStack item = createCustomItem(type, amount);
        if (item == null) {
            sender.sendMessage(prefix + color("&cДоступные предметы: trapka, cataclysm, totem"));
            return true;
        }
        target.getInventory().addItem(item);
        sender.sendMessage(prefix + color("&#00FF66Выдан предмет &f" + type + " &7(x" + amount + ") &#00FF66игроку &f" + target.getName()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) return List.of("give", "reload");
        if (args.length == 2 && List.of(args).get(0).equalsIgnoreCase("give")) return null;
        if (args.length == 3 && List.of(args).get(0).equalsIgnoreCase("give")) return List.of("trapka", "cataclysm", "totem");
        return List.of();
    }
    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack is = e.getItem();
        if (is == null || !is.hasItemMeta()) return;

        String type = is.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
        if (type == null) return;
        e.setCancelled(true);

        Player p = e.getPlayer();
        String prefix = color(getConfig().getString("prefix", "&#0055ff&l⚡ &#00E5FF&lMIR-TIME &8» "));

        String mode = getConfig().getString(type + ".world-mode", "BLOCKED").toUpperCase();
        List<String> worldList = getConfig().getStringList(type + ".worlds");
        String currentWorld = p.getWorld().getName();
        boolean inList = worldList.stream().anyMatch(w -> w.equalsIgnoreCase(currentWorld));

        boolean denied = mode.equals("ALLOWED") ? !inList : inList;
        if (denied) {
            p.sendMessage(prefix + color("&cЭтот предмет нельзя использовать в мире &e" + currentWorld + "&c!"));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        String cdKey = p.getUniqueId() + ":" + type;
        long now = System.currentTimeMillis();
        long cdEnd = cooldowns.getOrDefault(cdKey, 0L);
        if (now < cdEnd) {
            long rem = (cdEnd - now) / 1000 + 1;
            p.sendMessage(prefix + color("&cПодождите ещё &#00E5FF" + rem + " сек. &cперед повторным использованием!"));
            return;
        }

        int cdSec = getConfig().getInt(type + ".cooldown-seconds", 30);
        cooldowns.put(cdKey, now + cdSec * 1000L);
        is.setAmount(is.getAmount() - 1);

        switch (type) {
            case "trapka" -> activateTrapka(p);
            case "cataclysm" -> activateCataclysm(p);
            case "totem" -> activateTotem(p, e.getClickedBlock() != null ? e.getClickedBlock().getLocation().add(0.5, 1.0, 0.5) : p.getLocation());
        }
    }


        String cdKey = p.getUniqueId() + ":" + type;
        long now = System.currentTimeMillis();
        long cdEnd = cooldowns.getOrDefault(cdKey, 0L);
        if (now < cdEnd) {
            long rem = (cdEnd - now) / 1000 + 1;
            p.sendMessage(prefix + color("&cПодождите ещё &#00E5FF" + rem + " сек. &cперед повторным использованием!"));
            return;
        }

        int cdSec = getConfig().getInt(type + ".cooldown-seconds", 30);
        cooldowns.put(cdKey, now + cdSec * 1000L);
        is.setAmount(is.getAmount() - 1);

        switch (type) {
            case "trapka" -> activateTrapka(p);
            case "cataclysm" -> activateCataclysm(p);
            case "totem" -> activateTotem(p, e.getClickedBlock() != null ? e.getClickedBlock().getLocation().add(0.5, 1.0, 0.5) : p.getLocation());
        }
    }

    private void activateTrapka(Player p) {
        Location center = p.getLocation().getBlock().getLocation();
        Material mat = Material.matchMaterial(getConfig().getString("trapka.material", "OBSIDIAN"));
        if (mat == null) mat = Material.OBSIDIAN;
        int duration = getConfig().getInt("trapka.duration-seconds", 18);

        List<Block> placed = new ArrayList<>();
        for (int x = -2; x <= 2; x++) {
            for (int y = -1; y <= 3; y++) {
                for (int z = -2; z <= 2; z++) {
                    boolean isWall = Math.abs(x) == 2 || Math.abs(z) == 2 || y == -1 || y == 3;
                    if (!isWall) continue;
                    Block b = center.clone().add(x, y, z).getBlock();
                    if (b.getType().isAir() || b.isLiquid()) {
                        b.setType(mat);
                        placed.add(b);
                    }
                }
            }
        }

        p.getWorld().playSound(center, Sound.BLOCK_ANVIL_LAND, 1f, 0.8f);
        p.sendMessage(color(getConfig().getString("prefix") + "&#00FF66Трапка Бога активирована на &f" + duration + " сек.&#00FF66!"));

        Material finalMat = mat;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Block b : placed) {
                    if (b.getType() == finalMat) b.setType(Material.AIR);
                }
                p.getWorld().playSound(center, Sound.BLOCK_BEACON_DEACTIVATE, 1f, 1.2f);
            }
        }.runTaskLater(this, duration * 20L);
    }

    private void activateCataclysm(Player p) {
        Location center = p.getLocation();
        double radius = getConfig().getDouble("cataclysm.radius", 10.0);
        float power = (float) getConfig().getDouble("cataclysm.explosion-power", 7.5);

        center.getWorld().playSound(center, Sound.ENTITY_ENDERMAN_TELEPORT, 1.2f, 0.6f);
        center.getWorld().spawnParticle(Particle.PORTAL, center.clone().add(0, 1, 0), 120, 3, 1.5, 3, 0.2);

        for (Entity ent : center.getWorld().getNearbyEntities(center, radius, radius, radius)) {
            if (ent instanceof LivingEntity && !ent.getUniqueId().equals(p.getUniqueId())) {
                Vector pull = center.toVector().subtract(ent.getLocation().toVector()).normalize().multiply(1.6).setY(0.35);
                ent.setVelocity(pull);
            }
        }

        TNTPrimed visualTnt = (TNTPrimed) center.getWorld().spawnEntity(center.clone().add(0, 0.5, 0), EntityType.TNT);
        visualTnt.setFuseTicks(30);
        visualTnt.setYield(0f);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (visualTnt.isValid()) visualTnt.remove();
                center.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, center, 3);
                center.getWorld().createExplosion(center, power, false, false, p);
            }
        }.runTaskLater(this, 30L);
    }
    private void activateTotem(Player summoner, Location loc) {
        int summonSec = getConfig().getInt("totem.summon-seconds", 20);
        int fightSec = getConfig().getInt("totem.fight-seconds", 60);
        String bossTitle = color(getConfig().getString("totem.boss-name", "&#0055ff&l☠ &#00E5FF&lПОРОЖДЕНИЕ БЕЗДНЫ &#0055ff&l☠"));

        BossBar bar = Bukkit.createBossBar(color("&#00E5FF&l🔥 Ритуал призыва босса..."), BarColor.BLUE, BarStyle.SEGMENTED_20);
        bar.setProgress(0.0);
        for (Player pl : loc.getWorld().getPlayers()) {
            if (pl.getLocation().distanceSquared(loc) <= 2500) bar.addPlayer(pl);
        }

        loc.getWorld().playSound(loc, Sound.BLOCK_PORTAL_TRIGGER, 1.2f, 0.8f);

        new BukkitRunnable() {
            int ticks = 0;
            final int totalTicks = summonSec * 20;

            @Override
            public void run() {
                ticks += 5;
                double progress = Math.min(1.0, (double) ticks / totalTicks);
                bar.setProgress(progress);

                for (double y = 0; y <= 10.0; y += 0.4) {
                    double angle = (ticks * 0.25) + y;
                    double rx = Math.cos(angle) * 0.8;
                    double rz = Math.sin(angle) * 0.8;
                    loc.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(rx, y, rz), 1, 0, 0, 0, 0);
                }

                if (ticks >= totalTicks) {
                    cancel();
                    spawnSummonedBoss(loc, bar, bossTitle, fightSec);
                }
            }
        }.runTaskTimer(this, 0L, 5L);
    }

    private void spawnSummonedBoss(Location loc, BossBar bar, String bossTitle, int fightSec) {
        Location skyLoc = loc.clone().add(0, 7, 0);
        loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SPAWN, 1.2f, 1.0f);
        loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, skyLoc, 80, 1.2, 1.2, 1.2, 0.05);

        Ravager boss = (Ravager) loc.getWorld().spawnEntity(skyLoc, EntityType.RAVAGER);
        boss.setCustomName(bossTitle);
        boss.setCustomNameVisible(true);
        boss.setRemoveWhenFarAway(false);

        double maxHp = getConfig().getDouble("totem.boss-health", 800.0);
        if (boss.getAttribute(Attribute.MAX_HEALTH) != null) {
            boss.getAttribute(Attribute.MAX_HEALTH).setBaseValue(maxHp);
        }
        boss.setHealth(maxHp);
        summonedBosses.add(boss.getUniqueId());

        bar.setColor(BarColor.RED);
        bar.setTitle(bossTitle);
        bar.setProgress(1.0);

        new BukkitRunnable() {
            int elapsed = 0;

            @Override
            public void run() {
                if (!boss.isValid() || boss.isDead()) {
                    summonedBosses.remove(boss.getUniqueId());
                    bar.removeAll();
                    cancel();
                    return;
                }

                elapsed++;
                double hpRatio = Math.max(0.0, Math.min(1.0, boss.getHealth() / maxHp));
                bar.setProgress(hpRatio);
                bar.setTitle(bossTitle + color(" &8(&c" + (int) boss.getHealth() + " HP &8| &e" + (fightSec - elapsed) + "с&8)"));

                for (Player pl : loc.getWorld().getPlayers()) {
                    if (pl.getLocation().distanceSquared(boss.getLocation()) <= 2500) {
                        if (!bar.getPlayers().contains(pl)) bar.addPlayer(pl);
                    } else {
                        bar.removePlayer(pl);
                    }
                }

                if (elapsed >= fightSec) {
                    Location endLoc = boss.getLocation();
                    for (double y = 0; y <= 10.0; y += 0.3) {
                        endLoc.getWorld().spawnParticle(Particle.FLAME, endLoc.clone().add(0, y, 0), 8, 0.6, 0.1, 0.6, 0.02);
                    }
                    endLoc.getWorld().playSound(endLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.2f, 0.5f);
                    summonedBosses.remove(boss.getUniqueId());
                    boss.remove();
                    bar.removeAll();
                    cancel();
                }
            }
        }.runTaskTimer(this, 20L, 20L);
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent e) {
        if (!summonedBosses.remove(e.getEntity().getUniqueId())) return;
        Player killer = e.getEntity().getKiller();
        if (killer != null) {
            killer.sendMessage(color(getConfig().getString("prefix") + "&#00FF66Вы сокрушили призванного босса и получили награду!"));
            for (String cmd : getConfig().getStringList("totem.rewards")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", killer.getName()));
            }
        }
    }

    @EventHandler
    public void onBossBreakBlock(EntityChangeBlockEvent e) {
        if (summonedBosses.contains(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBossExplode(EntityExplodeEvent e) {
        if (summonedBosses.contains(e.getEntity().getUniqueId())) {
            e.blockList().clear();
        }
    }

    public static String color(String text) {
        if (text == null) return "";
        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder rep = new StringBuilder("§x");
            for (char c : hex.toCharArray()) rep.append('§').append(c);
            matcher.appendReplacement(buffer, rep.toString());
        }
        matcher.appendTail(buffer);
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
}
