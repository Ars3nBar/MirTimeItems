package ru.mirtime.items;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class ItemMechanics implements Listener {

    private final MirTimeItems plugin;
    private final Map<String, Long> cooldowns = new HashMap<>();
    private final Set<UUID> summonedBosses = new HashSet<>();
    private final Set<UUID> drillLock = new HashSet<>();

    public ItemMechanics(MirTimeItems plugin) {
        this.plugin = plugin;
        startPassiveTask();
    }

    private String getItemId(ItemStack is) {
        if (is == null || !is.hasItemMeta()) return null;
        return is.getItemMeta().getPersistentDataContainer().get(plugin.getItemKey(), PersistentDataType.STRING);
    }

    private boolean checkCooldown(Player p, String id, int sec) {
        String key = p.getUniqueId() + ":" + id;
        long now = System.currentTimeMillis();
        long end = cooldowns.getOrDefault(key, 0L);
        if (now < end) {
            p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&cПерезарядка артефакта: ещё &#00E5FF" + ((end - now) / 1000 + 1) + " сек.&c!"));
            return false;
        }
        cooldowns.put(key, now + sec * 1000L);
        return true;
    }

    private void startPassiveTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    String off = getItemId(p.getInventory().getItemInOffHand());
                    if ("talisman_rage".equals(off)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 45, 1, true, false));
                        p.getWorld().spawnParticle(Particle.CRIMSON_SPORE, p.getLocation().add(0, 1, 0), 4, 0.3, 0.4, 0.3, 0.01);
                    }
                    if ("talisman_speed".equals(off)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 45, 1, true, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 45, 1, true, false));
                        p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 3, 0.2, 0.1, 0.2, 0.01);
                    }

                    String boots = getItemId(p.getInventory().getBoots());
                    if ("hermes_boots".equals(boots)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 45, 1, true, false));
                        p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation().add(0, 0.1, 0), 2, 0.2, 0.05, 0.2, 0.01);
                    }

                    String chest = getItemId(p.getInventory().getChestplate());
                    if ("titan_chest".equals(chest)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 45, 0, true, false));
                        if (p.getAttribute(Attribute.MAX_HEALTH) != null) p.getAttribute(Attribute.MAX_HEALTH).setBaseValue(28.0);
                    } else if (p.getAttribute(Attribute.MAX_HEALTH) != null && p.getAttribute(Attribute.MAX_HEALTH).getBaseValue() == 28.0) {
                        p.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20.0);
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;
        Player p = e.getPlayer();
        ItemStack is = p.getInventory().getItemInMainHand();
        String id = getItemId(is);
        if (id == null) return;

        if (id.equals("obsidian_breaker") && e.getAction() == Action.LEFT_CLICK_BLOCK && e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType() == Material.OBSIDIAN && !plugin.isBlocked(p.getLocation())) {
                Location bLoc = e.getClickedBlock().getLocation().add(0.5, 0.5, 0.5);
                e.getClickedBlock().breakNaturally();
                bLoc.getWorld().spawnParticle(Particle.LAVA, bLoc, 15, 0.3, 0.3, 0.3, 0.1);
                p.playSound(bLoc, Sound.ENTITY_GENERIC_EXPLODE, 0.6f, 1.4f);
                if (is.getItemMeta() instanceof Damageable dmg) {
                    dmg.setDamage(dmg.getDamage() + 1);
                    is.setItemMeta(dmg);
                    if (dmg.getDamage() >= 32) is.setAmount(0);
                }
            }
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (Set.of("vampire_blade", "thor_axe", "midas_sword", "soul_reaper", "snipe_bow", "drill_pickaxe", "magnet_pickaxe", "spawner_silk", "farmer_hoe", "hermes_boots", "titan_chest", "dragon_elytra", "talisman_rage", "talisman_speed", "talisman_wealth").contains(id)) return;

        e.setCancelled(true);
        if (plugin.isBlocked(p.getLocation())) {
            p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&cЭтот артефакт запрещён в мире &f" + p.getWorld().getName() + " &cили на спавне!"));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            p.updateInventory();
            return;
        }

        switch (id) {
            case "trapka" -> { if (checkCooldown(p, id, 30)) { consumeOne(p, is); buildCube(p, Material.OBSIDIAN, 2, 18); } }
            case "mega_trap" -> { if (checkCooldown(p, id, 45)) { consumeOne(p, is); buildCube(p, Material.CRYING_OBSIDIAN, 3, 25); } }
            case "cataclysm" -> { if (checkCooldown(p, id, 40)) { consumeOne(p, is); runCataclysm(p); } }
            case "frost_staff" -> {
                if (checkCooldown(p, id, 25)) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 1.4f, 0.7f);
                    p.getWorld().spawnParticle(Particle.SNOWFLAKE, p.getLocation().add(0, 1, 0), 180, 4.5, 1.2, 4.5, 0.15);
                    for (Entity ent : p.getNearbyEntities(9, 9, 9)) {
                        if (ent instanceof LivingEntity le) {
                            le.setFreezeTicks(160);
                            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 4));
                            le.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, le.getLocation().add(0, 1, 0), 30, 0.4, 0.6, 0.4, 0.1);
                        }
                    }
                    p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&#00E5FF&lЛедяной шторм! &fВраги вокруг вморожены в лёд!"));
                }
            }
            case "gravity_mace" -> {
                if (checkCooldown(p, id, 20)) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1.5f, 0.6f);
                    p.getWorld().spawnParticle(Particle.SONIC_BOOM, p.getLocation().add(0, 1, 0), 5, 2, 0.5, 2, 0);
                    for (Entity ent : p.getNearbyEntities(8, 8, 8)) {
                        if (ent instanceof LivingEntity) ent.setVelocity(new Vector(0, 1.6, 0));
                    }
                }
            }
            case "flash_pearl" -> {
                if (checkCooldown(p, id, 15)) {
                    consumeOne(p, is);
                    p.getWorld().spawnParticle(Particle.REVERSE_PORTAL, p.getLocation().add(0, 1, 0), 60, 0.5, 0.8, 0.5, 0.1);
                    Block target = p.getTargetBlockExact(35);
                    Location dest = target != null ? target.getLocation().add(0.5, 1.0, 0.5) : p.getLocation().add(p.getLocation().getDirection().multiply(25));
                    dest.setYaw(p.getLocation().getYaw());
                    dest.setPitch(p.getLocation().getPitch());
                    p.teleport(dest);
                    dest.getWorld().spawnParticle(Particle.PORTAL, dest.clone().add(0, 1, 0), 80, 0.5, 0.8, 0.5, 0.2);
                    p.playSound(dest, Sound.ENTITY_ENDERMAN_TELEPORT, 1.2f, 1.3f);
                }
            }
            case "disorient" -> {
                if (checkCooldown(p, id, 30)) {
                    consumeOne(p, is);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.4f, 0.8f);
                    p.getWorld().spawnParticle(Particle.FLASH, p.getLocation().add(0, 1.5, 0), 3);
                    p.getWorld().spawnParticle(Particle.WITCH, p.getLocation().add(0, 1, 0), 120, 4, 1, 4, 0.1);
                    for (Entity ent : p.getNearbyEntities(10, 10, 10)) {
                        if (ent instanceof Player target) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 1));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 120, 1));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 120, 2));
                        }
                    }
                }
            }
            case "divine_aura" -> {
                if (checkCooldown(p, id, 45)) {
                    consumeOne(p, is);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 3));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                    p.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, p.getLocation().add(0, 1, 0), 90, 0.8, 1, 0.8, 0.3);
                    p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 0.9f, 1.3f);
                }
            }
            case "web_bomb" -> {
                if (checkCooldown(p, id, 25)) {
                    consumeOne(p, is);
                    for (Entity ent : p.getNearbyEntities(9, 9, 9)) {
                        if (ent instanceof Player target) {
                            Location c = target.getLocation().getBlock().getLocation();
                            for (int x = -1; x <= 1; x++) for (int y = 0; y <= 1; y++) for (int z = -1; z <= 1; z++)
                                if (c.clone().add(x, y, z).getBlock().getType().isAir()) c.clone().add(x, y, z).getBlock().setType(Material.COBWEB);
                            target.getWorld().playSound(c, Sound.ENTITY_SPIDER_AMBIENT, 1.3f, 0.7f);
                            break;
                        }
                    }
                }
            }
            case "escape_feather" -> {
                if (checkCooldown(p, id, 20)) {
                    consumeOne(p, is);
                    Vector dir = p.getLocation().getDirection().multiply(1.9).setY(1.05);
                    p.setVelocity(dir);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 110, 0));
                    p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 80, 0.5, 0.3, 0.5, 0.15);
                    p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.2f, 1.1f);
                }
            }
            case "cleanse_totem" -> {
                if (checkCooldown(p, id, 20)) {
                    consumeOne(p, is);
                    p.getActivePotionEffects().forEach(ef -> p.removePotionEffect(ef.getType()));
                    p.setFireTicks(0);
                    double max = p.getAttribute(Attribute.MAX_HEALTH) != null ? p.getAttribute(Attribute.MAX_HEALTH).getBaseValue() : 20.0;
                    p.setHealth(Math.min(max, p.getHealth() + 10.0));
                    p.getWorld().spawnParticle(Particle.HEART, p.getLocation().add(0, 1.5, 0), 15, 0.6, 0.5, 0.6, 0.1);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.5f);
                }
            }
            case "warden_horn" -> {
                if (checkCooldown(p, id, 60)) {
                    consumeOne(p, is);
                    p.getWorld().playSound(p.getLocation(), Sound.EVENT_RAID_HORN, 2.0f, 0.7f);
                    Ravager rav = (Ravager) p.getWorld().spawnEntity(p.getLocation(), EntityType.RAVAGER);
                    rav.setCustomName(MirTimeItems.color("&3&lЗверь Бездны (" + p.getName() + ")"));
                    rav.setCustomNameVisible(true);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> { if (rav.isValid()) rav.remove(); }, 600L);
                }
            }
            case "loot_flare" -> {
                if (checkCooldown(p, id, 60)) {
                    consumeOne(p, is);
                    Location dropLoc = p.getLocation().add(0, 1, 0);
                    p.getWorld().spawnParticle(Particle.FIREWORK, dropLoc.clone().add(0, 2, 0), 70, 0.6, 1, 0.6, 0.15);
                    p.getWorld().dropItemNaturally(dropLoc, new ItemStack(Material.DIAMOND, 10));
                    p.getWorld().dropItemNaturally(dropLoc, new ItemStack(Material.GOLDEN_APPLE, 5));
                    p.getWorld().dropItemNaturally(dropLoc, new ItemStack(Material.EXPERIENCE_BOTTLE, 24));
                    p.playSound(dropLoc, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1.2f, 1f);
                }
            }
            case "totem" -> { if (checkCooldown(p, id, 60)) { consumeOne(p, is); runTotemRitual(p.getLocation()); } }
        }
    }

    private void consumeOne(Player p, ItemStack is) {
        is.setAmount(is.getAmount() - 1);
        p.updateInventory();
    }
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player attacker) || !(e.getEntity() instanceof LivingEntity victim)) return;
        String id = getItemId(attacker.getInventory().getItemInMainHand());
        if (id == null) return;

        if (id.equals("vampire_blade")) {
            double heal = e.getFinalDamage() * 0.30;
            double max = attacker.getAttribute(Attribute.MAX_HEALTH) != null ? attacker.getAttribute(Attribute.MAX_HEALTH).getBaseValue() : 20.0;
            attacker.setHealth(Math.min(max, attacker.getHealth() + heal));
            victim.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, victim.getLocation().add(0, 1, 0), 12, 0.3, 0.4, 0.3, 0.1);
        } else if (id.equals("thor_axe") && checkCooldown(attacker, "thor_axe", 10)) {
            victim.getWorld().strikeLightningEffect(victim.getLocation());
            victim.damage(7.0);
        } else if (id.equals("soul_reaper")) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 1));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 80, 1));
            victim.getWorld().spawnParticle(Particle.SOUL, victim.getLocation().add(0, 1, 0), 20, 0.4, 0.5, 0.4, 0.05);
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player p && "snipe_bow".equals(getItemId(e.getBow()))) {
            e.getProjectile().setGravity(false);
            e.getProjectile().setVelocity(e.getProjectile().getVelocity().multiply(1.4));
            e.getProjectile().setGlowing(true);
        }
    }

    @EventHandler
    public void onElytraBoost(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (e.isSneaking() && p.isGliding() && "dragon_elytra".equals(getItemId(p.getInventory().getChestplate()))) {
            if (checkCooldown(p, "dragon_elytra", 5)) {
                p.setVelocity(p.getLocation().getDirection().multiply(1.75));
                p.getWorld().spawnParticle(Particle.DRAGON_BREATH, p.getLocation(), 35, 0.3, 0.3, 0.3, 0.05);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 1.2f);
            }
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if ("hermes_boots".equals(getItemId(p.getInventory().getBoots()))) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (drillLock.contains(p.getUniqueId())) return;
        String id = getItemId(p.getInventory().getItemInMainHand());
        if (id == null) return;

        Block b = e.getBlock();
        if (id.equals("drill_pickaxe")) {
            drillLock.add(p.getUniqueId());
            for (int x = -1; x <= 1; x++) for (int y = -1; y <= 1; y++) for (int z = -1; z <= 1; z++) {
                Block rel = b.getRelative(x, y, z);
                if (rel.getType() != Material.BEDROCK && rel.getType() != Material.OBSIDIAN && !rel.getType().isAir()) rel.breakNaturally(p.getInventory().getItemInMainHand());
            }
            drillLock.remove(p.getUniqueId());
        } else if (id.equals("magnet_pickaxe")) {
            e.setDropItems(false);
            for (ItemStack drop : b.getDrops(p.getInventory().getItemInMainHand())) {
                if (drop.getType() == Material.RAW_IRON) drop.setType(Material.IRON_INGOT);
                if (drop.getType() == Material.RAW_GOLD) drop.setType(Material.GOLD_INGOT);
                p.getInventory().addItem(drop);
            }
        } else if (id.equals("spawner_silk") && b.getType() == Material.SPAWNER) {
            e.setDropItems(false);
            ItemStack spawnerItem = new ItemStack(Material.SPAWNER);
            if (b.getState() instanceof CreatureSpawner cs && spawnerItem.getItemMeta() instanceof BlockStateMeta bsm) {
                CreatureSpawner itemCs = (CreatureSpawner) bsm.getBlockState();
                itemCs.setSpawnedType(cs.getSpawnedType());
                bsm.setBlockState(itemCs);
                spawnerItem.setItemMeta(bsm);
            }
            b.getWorld().dropItemNaturally(b.getLocation(), spawnerItem);
            p.getInventory().getItemInMainHand().setAmount(0);
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
        } else if (id.equals("farmer_hoe")) {
            for (int x = -2; x <= 2; x++) for (int z = -2; z <= 2; z++) {
                Block rel = b.getRelative(x, 0, z);
                if (rel.getType() == Material.WHEAT || rel.getType() == Material.CARROTS || rel.getType() == Material.POTATOES || rel.getType() == Material.BEETROOTS) {
                    rel.breakNaturally(p.getInventory().getItemInMainHand());
                }
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (summonedBosses.remove(e.getEntity().getUniqueId()) && e.getEntity().getKiller() != null) {
            Player k = e.getEntity().getKiller();
            for (String cmd : plugin.getConfig().getStringList("totem.rewards")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", k.getName()));
            }
        }
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;
        if ("midas_sword".equals(getItemId(killer.getInventory().getItemInMainHand()))) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 1000");
        }
        if ("talisman_wealth".equals(getItemId(killer.getInventory().getItemInOffHand()))) {
            e.setDroppedExp(e.getDroppedExp() * 2);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 250");
        }
    }

    private void buildCube(Player p, Material mat, int r, int sec) {
        Location c = p.getLocation().getBlock().getLocation();
        List<Block> placed = new ArrayList<>();
        for (int x = -r; x <= r; x++) for (int y = -1; y <= r + 1; y++) for (int z = -r; z <= r; z++) {
            if (Math.abs(x) != r && Math.abs(z) != r && y != -1 && y != r + 1) continue;
            Block b = c.clone().add(x, y, z).getBlock();
            if (b.getType().isAir() || b.isLiquid()) { b.setType(mat); placed.add(b); }
        }
        p.getWorld().playSound(c, Sound.BLOCK_ANVIL_LAND, 1.2f, 0.7f);
        p.getWorld().spawnParticle(Particle.SMOKE, c.clone().add(0.5, 1, 0.5), 60, r, 1, r, 0.05);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Block b : placed) if (b.getType() == mat) b.setType(Material.AIR);
        }, sec * 20L);
    }

    private void runCataclysm(Player p) {
        Location c = p.getLocation();
        c.getWorld().playSound(c, Sound.ENTITY_ENDERMAN_TELEPORT, 1.4f, 0.5f);
        c.getWorld().spawnParticle(Particle.PORTAL, c.clone().add(0, 1, 0), 180, 3.5, 1.5, 3.5, 0.25);
        for (Entity ent : c.getWorld().getNearbyEntities(c, 10, 10, 10)) {
            if (ent instanceof LivingEntity && !ent.equals(p)) ent.setVelocity(c.toVector().subtract(ent.getLocation().toVector()).normalize().multiply(1.75).setY(0.4));
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            c.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, c, 4);
            c.getWorld().createExplosion(c, 8.0f, false, false, p);
        }, 30L);
    }

    private void runTotemRitual(Location loc) {
        String title = MirTimeItems.color(plugin.getConfig().getString("totem.boss-name", "&#0055ff&l☠ ПОРОЖДЕНИЕ БЕЗДНЫ"));
        BossBar bar = Bukkit.createBossBar(title, BarColor.BLUE, BarStyle.SEGMENTED_20);
        for (Player pl : loc.getWorld().getPlayers()) if (pl.getLocation().distanceSquared(loc) <= 2500) bar.addPlayer(pl);

        new BukkitRunnable() {
            int t = 0;
            @Override
            public void run() {
                t += 10;
                bar.setProgress(Math.min(1.0, t / 400.0));
                loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc.clone().add(0, 2, 0), 30, 0.7, 2.5, 0.7, 0.03);
                if (t >= 400) {
                    cancel();
                    loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SPAWN, 1.3f, 0.8f);
                    Ravager boss = (Ravager) loc.getWorld().spawnEntity(loc.clone().add(0, 3, 0), EntityType.RAVAGER);
                    boss.setCustomName(title);
                    boss.setCustomNameVisible(true);
                    summonedBosses.add(boss.getUniqueId());
                    Bukkit.getScheduler().runTaskLater(plugin, () -> { if (boss.isValid()) boss.remove(); bar.removeAll(); }, 1200L);
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    @EventHandler public void onBossBlock(EntityChangeBlockEvent e) { if (summonedBosses.contains(e.getEntity().getUniqueId())) e.setCancelled(true); }
    @EventHandler public void onBossExp(EntityExplodeEvent e) { if (summonedBosses.contains(e.getEntity().getUniqueId())) e.blockList().clear(); }
}
