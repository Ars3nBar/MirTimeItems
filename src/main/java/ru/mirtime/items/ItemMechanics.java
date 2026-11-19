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
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
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
            p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&cПерезарядка: ещё &#00E5FF" + ((end - now) / 1000 + 1) + " сек.&c!"));
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
                    if ("talisman_rage".equals(off)) p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 45, 1, true, false));
                    if ("talisman_speed".equals(off)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 45, 1, true, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 45, 1, true, false));
                    }

                    String boots = getItemId(p.getInventory().getBoots());
                    if ("hermes_boots".equals(boots)) p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 45, 1, true, false));

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

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack is = e.getItem();
        String id = getItemId(is);
        if (id == null) return;

        if (id.equals("obsidian_breaker") && e.getAction() == Action.LEFT_CLICK_BLOCK && e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType() == Material.OBSIDIAN && !plugin.isBlocked(p.getLocation())) {
                e.getClickedBlock().breakNaturally();
                p.playSound(p.getLocation(), Sound.BLOCK_STONE_BREAK, 1f, 0.8f);
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
            p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&cЭтот артефакт запрещён на спавне или в мире game!"));
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
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.2f, 0.5f);
                    p.getWorld().spawnParticle(Particle.SNOWFLAKE, p.getLocation().add(0, 1, 0), 100, 4, 1, 4, 0.1);
                    for (Entity ent : p.getNearbyEntities(8, 8, 8)) {
                        if (ent instanceof LivingEntity le) {
                            le.setFreezeTicks(140);
                            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 3));
                        }
                    }
                    p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&#00E5FFВраги вокруг заморожены!"));
                }
            }
            case "gravity_mace" -> {
                if (checkCooldown(p, id, 20)) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1.2f, 0.7f);
                    for (Entity ent : p.getNearbyEntities(8, 8, 8)) {
                        if (ent instanceof LivingEntity) ent.setVelocity(new Vector(0, 1.45, 0));
                    }
                }
            }
            case "flash_pearl" -> {
                if (checkCooldown(p, id, 15)) {
                    consumeOne(p, is);
                    Block target = p.getTargetBlockExact(35);
                    Location dest = target != null ? target.getLocation().add(0.5, 1.0, 0.5) : p.getLocation().add(p.getLocation().getDirection().multiply(25));
                    dest.setYaw(p.getLocation().getYaw());
                    dest.setPitch(p.getLocation().getPitch());
                    p.teleport(dest);
                    p.playSound(dest, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.2f);
                }
            }
            case "disorient" -> {
                if (checkCooldown(p, id, 30)) {
                    consumeOne(p, is);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1f, 0.8f);
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
                    p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 0.8f, 1.4f);
                }
            }
            case "web_bomb" -> {
                if (checkCooldown(p, id, 25)) {
                    consumeOne(p, is);
                    for (Entity ent : p.getNearbyEntities(8, 8, 8)) {
                        if (ent instanceof Player target) {
                            Location c = target.getLocation().getBlock().getLocation();
                            for (int x = -1; x <= 1; x++) for (int z = -1; z <= 1; z++)
                                if (c.clone().add(x, 0, z).getBlock().getType().isAir()) c.clone().add(x, 0, z).getBlock().setType(Material.COBWEB);
                            break;
                        }
                    }
                }
            }
            case "escape_feather" -> {
                if (checkCooldown(p, id, 20)) {
                    consumeOne(p, is);
                    Vector dir = p.getLocation().getDirection().multiply(1.8).setY(0.95);
                    p.setVelocity(dir);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 0));
                    p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1.2f);
                }
            }
            case "cleanse_totem" -> {
                if (checkCooldown(p, id, 20)) {
                    consumeOne(p, is);
                    p.getActivePotionEffects().forEach(ef -> p.removePotionEffect(ef.getType()));
                    p.setFireTicks(0);
                    p.setHealth(Math.min(20.0, p.getHealth() + 8.0));
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.5f);
                }
            }
            case "warden_horn" -> {
                if (checkCooldown(p, id, 60)) {
                    consumeOne(p, is);
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
                    p.getWorld().dropItemNaturally(dropLoc, new ItemStack(Material.DIAMOND, 8));
                    p.getWorld().dropItemNaturally(dropLoc, new ItemStack(Material.GOLDEN_APPLE, 4));
                    p.getWorld().dropItemNaturally(dropLoc, new ItemStack(Material.EXPERIENCE_BOTTLE, 16));
                    p.playSound(dropLoc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1f, 1f);
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
            double heal = e.getFinalDamage() * 0.25;
            double max = attacker.getAttribute(Attribute.MAX_HEALTH) != null ? attacker.getAttribute(Attribute.MAX_HEALTH).getBaseValue() : 20.0;
            attacker.setHealth(Math.min(max, attacker.getHealth() + heal));
        } else if (id.equals("thor_axe") && checkCooldown(attacker, "thor_axe", 12)) {
            victim.getWorld().strikeLightningEffect(victim.getLocation());
            victim.damage(6.0);
        } else if (id.equals("soul_reaper")) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player p && "snipe_bow".equals(getItemId(e.getBow()))) {
            e.getProjectile().setGravity(false);
            e.getProjectile().setVelocity(e.getProjectile().getVelocity().multiply(1.3));
        }
    }

    @EventHandler
    public void onElytraBoost(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (e.isSneaking() && p.isGliding() && "dragon_elytra".equals(getItemId(p.getInventory().getChestplate()))) {
            if (checkCooldown(p, "dragon_elytra", 5)) {
                p.setVelocity(p.getLocation().getDirection().multiply(1.65));
                p.getWorld().spawnParticle(Particle.DRAGON_BREATH, p.getLocation(), 25, 0.3, 0.3, 0.3, 0.05);
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
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 500");
        }
        if ("talisman_wealth".equals(getItemId(killer.getInventory().getItemInOffHand()))) {
            e.setDroppedExp(e.getDroppedExp() * 2);
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
        p.getWorld().playSound(c, Sound.BLOCK_ANVIL_LAND, 1f, 0.8f);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Block b : placed) if (b.getType() == mat) b.setType(Material.AIR);
        }, sec * 20L);
    }

    private void runCataclysm(Player p) {
        Location c = p.getLocation();
        for (Entity ent : c.getWorld().getNearbyEntities(c, 10, 10, 10)) {
            if (ent instanceof LivingEntity && !ent.equals(p)) ent.setVelocity(c.toVector().subtract(ent.getLocation().toVector()).normalize().multiply(1.6).setY(0.35));
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> c.getWorld().createExplosion(c, 7.5f, false, false, p), 30L);
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
                loc.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(0, 2, 0), 25, 0.6, 2, 0.6, 0.02);
                if (t >= 400) {
                    cancel();
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
