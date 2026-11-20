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
        if (id.startsWith("god_") || id.startsWith("jack_") || id.startsWith("king_") || id.startsWith("christian_")) return;
        if (Set.of("vampire_blade", "thor_axe", "midas_sword", "soul_reaper", "snipe_bow", "drill_pickaxe", "magnet_pickaxe", "spawner_silk", "farmer_hoe", "hermes_boots", "titan_chest", "dragon_elytra", "talisman_rage", "talisman_speed", "talisman_wealth", "lightning_trident", "berserk_axe", "phoenix_totem").contains(id)) return;

        e.setCancelled(true);
        if (plugin.isBlocked(p.getLocation())) {
            p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&cЭтот артефакт запрещён в мире &f" + p.getWorld().getName() + " &cили на спавне!"));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            p.updateInventory();
            return;
        }

        switch (id) {
            case "trapka" -> { if (checkCooldown(p, id, 30)) { consumeOne(p, is); buildStylishTrap(p, 2, 18, false); } }
            case "mega_trap" -> { if (checkCooldown(p, id, 45)) { consumeOne(p, is); buildStylishTrap(p, 3, 25, true); } }
            case "bedrock_shield" -> {
                if (checkCooldown(p, id, 40)) {
                    consumeOne(p, is);
                    buildStylishTrap(p, 2, 12, true);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 240, 2));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 240, 2));
                    p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&#00E5FF&lЭгида Титана активирована! &fНеоновый купол на 12 сек."));
                }
            }
            case "cataclysm" -> { if (checkCooldown(p, id, 40)) { consumeOne(p, is); runCataclysm(p); } }
            case "frost_staff" -> {
                if (checkCooldown(p, id, 25)) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 1.4f, 0.7f);
                    p.getWorld().spawnParticle(Particle.SNOWFLAKE, p.getLocation().add(0, 1, 0), 180, 4.5, 1.2, 4.5, 0.15);
                    for (Entity ent : p.getNearbyEntities(9, 9, 9)) {
                        if (ent instanceof LivingEntity le && !ent.equals(p)) {
                            le.setFreezeTicks(160);
                            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 4));
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
                        if (ent instanceof LivingEntity && !ent.equals(p)) ent.setVelocity(new Vector(0, 1.75, 0));
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
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.5f, 0.8f);
                    p.getWorld().spawnParticle(Particle.FLASH, p.getLocation().add(0, 1.5, 0), 4);
                    p.getWorld().spawnParticle(Particle.WITCH, p.getLocation().add(0, 1, 0), 140, 4.5, 1.2, 4.5, 0.15);
                    for (Entity ent : p.getNearbyEntities(10, 10, 10)) {
                        if (ent instanceof LivingEntity target && !target.equals(p)) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 1, false, true));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 120, 1, false, true));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 120, 2, false, true));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 120, 1, false, true));
                        }
                    }
                    p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&#00E5FF&lДезориентация! &fВсе враги вокруг ослеплены и ослаблены на 6 сек."));
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
                if (checkCooldown(p, id, 20)) {
                    consumeOne(p, is);
                    Location c = p.getLocation();
                    c.getWorld().playSound(c, Sound.ENTITY_WARDEN_SONIC_BOOM, 1.3f, 1.2f);
                    c.getWorld().spawnParticle(Particle.SONIC_BOOM, c.clone().add(0, 1, 0), 8, 3, 0.5, 3, 0.1);
                    for (Entity ent : p.getNearbyEntities(9, 9, 9)) {
                        if (ent instanceof LivingEntity le && !ent.equals(p)) {
                            Vector push = le.getLocation().toVector().subtract(c.toVector()).normalize().multiply(2.2).setY(0.85);
                            le.setVelocity(push);
                            le.damage(6.0, p);
                            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 2));
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
                    runAirdropFlare(p, p.getLocation().clone());
                }
            }
            case "totem" -> {
                if (checkCooldown(p, id, 60)) {
                    consumeOne(p, is);
                    runTimedBoss(p.getLocation().clone());
                }
            }
            case "armageddon_beacon" -> {
                if (checkCooldown(p, id, 120)) {
                    consumeOne(p, is);
                    runArmageddon(p, p.getLocation().getBlock().getLocation());
                }
            }
            case "meteor_staff" -> {
                if (checkCooldown(p, id, 25)) {
                    Block target = p.getTargetBlockExact(50);
                    Location hit = target != null ? target.getLocation().add(0.5, 1, 0.5) : p.getLocation().add(p.getLocation().getDirection().multiply(25));
                    Location sky = hit.clone().add(0, 22, 0);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.5f, 0.6f);
                    LargeFireball fb = p.getWorld().spawn(sky, LargeFireball.class);
                    fb.setDirection(new Vector(0, -1, 0));
                    fb.setVelocity(new Vector(0, -1.8, 0));
                    fb.setYield(4.5f);
                    fb.setIsIncendiary(true);
                    fb.setShooter(p);
                }
            }
            case "black_hole" -> {
                if (checkCooldown(p, id, 35)) {
                    consumeOne(p, is);
                    Location center = p.getLocation().add(p.getLocation().getDirection().multiply(4));
                    p.getWorld().playSound(center, Sound.ENTITY_WARDEN_SONIC_BOOM, 1.3f, 0.5f);
                    new BukkitRunnable() {
                        int ticks = 0;
                        @Override
                        public void run() {
                            ticks += 5;
                            center.getWorld().spawnParticle(Particle.REVERSE_PORTAL, center, 80, 2.5, 1.5, 2.5, 0.2);
                            center.getWorld().spawnParticle(Particle.SQUID_INK, center, 25, 0.8, 0.8, 0.8, 0.05);
                            for (Entity ent : center.getWorld().getNearbyEntities(center, 9, 9, 9)) {
                                if (ent instanceof LivingEntity le && !ent.equals(p)) {
                                    Vector pull = center.toVector().subtract(le.getLocation().toVector()).normalize().multiply(0.65);
                                    le.setVelocity(pull);
                                    le.damage(2.5, p);
                                }
                            }
                            if (ticks >= 100) cancel();
                        }
                    }.runTaskTimer(plugin, 0L, 5L);
                }
            }
            case "shadow_cloak" -> {
                if (checkCooldown(p, id, 30)) {
                    consumeOne(p, is);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 160, 0, false, false));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 2, false, false));
                    p.getWorld().spawnParticle(Particle.SMOKE, p.getLocation().add(0, 1, 0), 60, 0.4, 0.8, 0.4, 0.05);
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.6f);
                    p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&8&lВы растворились в тени на 8 секунд!"));
                }
            }
            case "vortex_hook" -> {
                if (checkCooldown(p, id, 15)) {
                    LivingEntity target = null;
                    for (Entity ent : p.getNearbyEntities(25, 25, 25)) {
                        if (ent instanceof LivingEntity le && !ent.equals(p) && p.hasLineOfSight(le)) {
                            Vector toTarget = le.getEyeLocation().toVector().subtract(p.getEyeLocation().toVector()).normalize();
                            if (toTarget.dot(p.getLocation().getDirection()) > 0.92) { target = le; break; }
                        }
                    }
                    if (target != null) {
                        Vector pull = p.getLocation().toVector().subtract(target.getLocation().toVector()).normalize().multiply(2.1).setY(0.45);
                        target.setVelocity(pull);
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1.5f, 0.7f);
                        target.getWorld().spawnParticle(Particle.PORTAL, target.getLocation().add(0, 1, 0), 50, 0.4, 0.6, 0.4, 0.2);
                    } else {
                        p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&cНаведите прицел на врага в радиусе 25 блоков!"));
                    }
                }
            }
            case "nuke_tnt" -> {
                if (checkCooldown(p, id, 45)) {
                    consumeOne(p, is);
                    Location loc = p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
                    TNTPrimed tnt = loc.getWorld().spawn(loc, TNTPrimed.class);
                    tnt.setFuseTicks(60);
                    tnt.setCustomName(MirTimeItems.color("&#FF2A55&l☢ ТЕРМОЯДЕРНЫЙ ЗАРЯД"));
                    tnt.setCustomNameVisible(true);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        loc.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, loc, 6);
                        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.5f);
                        for (int x = -3; x <= 3; x++) for (int y = -3; y <= 3; y++) for (int z = -3; z <= 3; z++) {
                            Block b = loc.clone().add(x, y, z).getBlock();
                            if (b.getType() != Material.BEDROCK && b.getType() != Material.END_PORTAL_FRAME && !b.getType().isAir()) {
                                b.setType(Material.AIR);
                            }
                        }
                    }, 60L);
                }
            }
            case "lava_prison" -> {
                if (checkCooldown(p, id, 35)) {
                    LivingEntity target = null;
                    for (Entity ent : p.getNearbyEntities(12, 12, 12)) {
                        if (ent instanceof LivingEntity le && !ent.equals(p) && !(ent instanceof ArmorStand)) { target = le; break; }
                    }
                    Location c = target != null ? target.getLocation().getBlock().getLocation() : p.getLocation().add(p.getLocation().getDirection().multiply(4)).getBlock().getLocation();
                    consumeOne(p, is);
                    List<Block> cage = new ArrayList<>();
                    for (int x = -1; x <= 1; x++) for (int y = -1; y <= 2; y++) for (int z = -1; z <= 1; z++) {
                        if (Math.abs(x) == 1 || Math.abs(z) == 1 || y == -1 || y == 2) {
                            Block b = c.clone().add(x, y, z).getBlock();
                            if (b.isReplaceable()) {
                                b.setType((Math.abs(x) == 1 && Math.abs(z) == 1) ? Material.MAGMA_BLOCK : Material.RED_STAINED_GLASS);
                                cage.add(b);
                            }
                        }
                    }
                    Block feet = c.getBlock();
                    if (feet.isReplaceable()) { feet.setType(Material.FIRE); cage.add(feet); }
                    c.getWorld().playSound(c, Sound.ITEM_BUCKET_EMPTY_LAVA, 1.4f, 0.8f);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> { for (Block b : cage) b.setType(Material.AIR); }, 200L);
                }
            }
            case "midas_touch" -> {
                if (checkCooldown(p, id, 30)) {
                    consumeOne(p, is);
                    Location c = p.getLocation().getBlock().getLocation();
                    for (int x = -2; x <= 2; x++) for (int z = -2; z <= 2; z++) {
                        Block b = c.clone().add(x, -1, z).getBlock();
                        if (b.getType().isSolid() && b.getType() != Material.BEDROCK) b.setType(Material.GOLD_BLOCK);
                    }
                    int robbed = 0;
                    for (Entity ent : p.getNearbyEntities(3, 3, 3)) {
                        if (ent instanceof Player target && !target.equals(p)) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 3));
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + target.getName() + " 5000");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + p.getName() + " 5000");
                            target.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&cИгрок &f" + p.getName() + " &cиспользовал Руку Мидаса и забрал у вас &#FFD7005.000 Монет&c!"));
                            robbed += 5000;
                        }
                    }
                    p.getWorld().playSound(c, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.5f, 1.5f);
                    p.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, c.clone().add(0.5, 1, 0.5), 60, 2, 0.5, 2, 0.1);
                    p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&#FFD700&lРука Мидаса! &fЗона 5x5 обращена в золото! Собрано: &#00FF66" + robbed + " Монет"));
                }
            }
            case "dragon_breath_bomb" -> {
                if (checkCooldown(p, id, 25)) {
                    consumeOne(p, is);
                    AreaEffectCloud cloud = p.getWorld().spawn(p.getLocation(), AreaEffectCloud.class);
                    cloud.setParticle(Particle.DRAGON_BREATH);
                    cloud.setRadius(5.5f);
                    cloud.setDuration(160);
                    cloud.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 1, 1), true);
                    cloud.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 100, 1), true);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1.2f);
                }
            }
            case "chrono_watch" -> {
                if (checkCooldown(p, id, 30)) {
                    consumeOne(p, is);
                    Location savedLoc = p.getLocation().clone();
                    double savedHp = p.getHealth();
                    p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&#00E5FF&lТочка времени записана! &fВозврат через 5 секунд..."));
                    p.getWorld().spawnParticle(Particle.END_ROD, savedLoc.clone().add(0, 1, 0), 50, 0.5, 0.8, 0.5, 0.05);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        if (p.isOnline() && !p.isDead()) {
                            p.teleport(savedLoc);
                            double max = p.getAttribute(Attribute.MAX_HEALTH) != null ? p.getAttribute(Attribute.MAX_HEALTH).getBaseValue() : 20.0;
                            p.setHealth(Math.min(max, Math.max(p.getHealth(), savedHp)));
                            p.getWorld().playSound(savedLoc, Sound.BLOCK_BEACON_DEACTIVATE, 1.3f, 1.5f);
                            p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&#00FF66&lВремя перемотано назад!"));
                        }
                    }, 100L);
                }
            }
            case "xray_compass" -> {
                if (checkCooldown(p, id, 35)) {
                    consumeOne(p, is);
                    int found = 0;
                    for (Entity ent : p.getNearbyEntities(80, 80, 80)) {
                        if (ent instanceof Player target && !target.equals(p)) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 300, 0));
                            found++;
                        }
                    }
                    p.playSound(p.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.4f, 1.4f);
                    p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&#00FF66&lОко Охотника: &fобнаружено игроков рядом: &#00E5FF" + found + " &7(подсвечены на 15 сек.)"));
                }
            }
            case "auto_bridge" -> {
                if (checkCooldown(p, id, 15)) {
                    consumeOne(p, is);
                    Vector dir = p.getLocation().getDirection().setY(0).normalize();
                    Location start = p.getLocation().getBlock().getLocation().add(0, -1, 0);
                    List<Block> bridge = new ArrayList<>();
                    for (int i = 1; i <= 20; i++) {
                        Location step = start.clone().add(dir.clone().multiply(i));
                        for (int w = -1; w <= 1; w++) {
                            Block b = step.clone().add(-dir.getZ() * w, 0, dir.getX() * w).getBlock();
                            if (b.isReplaceable()) { b.setType(w == 0 ? Material.CRYING_OBSIDIAN : Material.OBSIDIAN); bridge.add(b); }
                        }
                    }
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_STONE_PLACE, 1.5f, 0.8f);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> { for (Block b : bridge) b.setType(Material.AIR); }, 300L);
                }
            }
        }
    }

    private void consumeOne(Player p, ItemStack is) {
        is.setAmount(is.getAmount() - 1);
        p.updateInventory();
    }
    private void triggerPoseidonLightning(Player attacker, Location center) {
        if (!checkCooldown(attacker, "lightning_trident", 8)) return;
        center.getWorld().playSound(center, Sound.ITEM_TRIDENT_THUNDER, 1.5f, 1.0f);
        int hits = 0;
        for (Entity ent : center.getWorld().getNearbyEntities(center, 8, 8, 8)) {
            if (ent instanceof LivingEntity le && !ent.equals(attacker) && !(ent instanceof ArmorStand)) {
                le.getWorld().strikeLightningEffect(le.getLocation());
                le.damage(7.0, attacker);
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 2));
                hits++;
                if (hits >= 5) break;
            }
        }
    }

    @EventHandler
    public void onTridentThrowHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Trident trident && trident.getShooter() instanceof Player p) {
            if ("lightning_trident".equals(getItemId(trident.getItemStack()))) {
                triggerPoseidonLightning(p, trident.getLocation());
            }
        }
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
        } else if (id.equals("lightning_trident")) {
            triggerPoseidonLightning(attacker, victim.getLocation());
        } else if (id.equals("berserk_axe")) {
            if (attacker.getHealth() <= 8.0) {
                e.setDamage(e.getDamage() * 2.1);
                victim.getWorld().spawnParticle(Particle.CRIT, victim.getLocation().add(0, 1, 0), 25, 0.4, 0.5, 0.4, 0.2);
            }
        }
    }

    @EventHandler
    public void onPhoenixSave(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && "hermes_boots".equals(getItemId(p.getInventory().getBoots()))) {
            e.setCancelled(true);
            return;
        }
        if (p.getHealth() - e.getFinalDamage() <= 0.5 && !plugin.isBlocked(p.getLocation())) {
            for (ItemStack item : p.getInventory().getContents()) {
                if ("phoenix_totem".equals(getItemId(item))) {
                    e.setCancelled(true);
                    item.setAmount(item.getAmount() - 1);
                    p.setHealth(14.0);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 0));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 2));
                    p.setVelocity(new Vector(0, 1.35, 0));
                    p.getWorld().spawnParticle(Particle.FLAME, p.getLocation().add(0, 1, 0), 140, 1.5, 1, 1.5, 0.2);
                    p.getWorld().playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1.4f, 0.8f);
                    for (Entity ent : p.getNearbyEntities(6, 6, 6)) {
                        if (ent instanceof LivingEntity le && !ent.equals(p)) { le.setFireTicks(120); le.damage(8.0); }
                    }
                    p.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&#FF8800&lПеро Феникса воскресило вас из пепла!"));
                    break;
                }
            }
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
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + k.getName() + " 2500");
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
    private void buildStylishTrap(Player p, int r, int sec, boolean neon) {
        Location c = p.getLocation().getBlock().getLocation();
        Location center = c.clone().add(0.5, 0, 0.5);
        center.setYaw(p.getLocation().getYaw());
        center.setPitch(p.getLocation().getPitch());
        p.teleport(center);

        List<Block> placed = new ArrayList<>();
        for (int x = -r; x <= r; x++) for (int y = -1; y <= r + 1; y++) for (int z = -r; z <= r; z++) {
            boolean isEdgeX = Math.abs(x) == r;
            boolean isEdgeZ = Math.abs(z) == r;
            boolean isFloor = y == -1;
            boolean isRoof = y == r + 1;
            if (!isEdgeX && !isEdgeZ && !isFloor && !isRoof) continue;

            Block b = c.clone().add(x, y, z).getBlock();
            if (!b.isReplaceable()) continue;

            Material mat;
            if (isEdgeX && isEdgeZ) {
                mat = Material.CRYING_OBSIDIAN;
            } else if (isRoof && x == 0 && z == 0) {
                mat = Material.SEA_LANTERN;
            } else if (y == 1 && (x == 0 || z == 0)) {
                mat = neon ? Material.LIGHT_BLUE_STAINED_GLASS : Material.BLACK_STAINED_GLASS;
            } else {
                mat = neon ? Material.CRYING_OBSIDIAN : Material.OBSIDIAN;
            }
            b.setType(mat);
            placed.add(b);
        }
        p.getWorld().playSound(c, Sound.BLOCK_BEACON_ACTIVATE, 1.3f, 0.8f);
        p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, center.clone().add(0, 1, 0), 70, r * 0.7, 1, r * 0.7, 0.05);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Block b : placed) b.setType(Material.AIR);
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

    private void runAirdropFlare(Player p, Location loc) {
        BossBar bar = Bukkit.createBossBar(MirTimeItems.color("&#FFD700&l🎆 АИРДРОП ПРИБУДЕТ ЧЕРЕЗ: &#00FF6610 сек."), BarColor.YELLOW, BarStyle.SEGMENTED_10);
        for (Player pl : loc.getWorld().getPlayers()) if (pl.getLocation().distanceSquared(loc) <= 3600) bar.addPlayer(pl);
        loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.5f, 0.8f);

        new BukkitRunnable() {
            int secLeft = 10;
            @Override
            public void run() {
                loc.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(0.5, 1, 0.5), 25, 0.2, 3.0, 0.2, 0.03);
                bar.setProgress(Math.max(0.0, secLeft / 10.0));
                bar.setTitle(MirTimeItems.color("&#FFD700&l🎆 АИРДРОП ПРИБУДЕТ ЧЕРЕЗ: &#00FF66" + secLeft + " сек."));
                if (secLeft <= 0) {
                    cancel();
                    bar.removeAll();
                    Location dropLoc = loc.clone().add(0.5, 1.5, 0.5);
                    loc.getWorld().spawnParticle(Particle.FIREWORK, dropLoc, 100, 0.8, 1.2, 0.8, 0.2);
                    loc.getWorld().playSound(dropLoc, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1.5f, 1.0f);
                    loc.getWorld().dropItemNaturally(dropLoc, new ItemStack(Material.DIAMOND, 16));
                    loc.getWorld().dropItemNaturally(dropLoc, new ItemStack(Material.GOLDEN_APPLE, 8));
                    loc.getWorld().dropItemNaturally(dropLoc, new ItemStack(Material.NETHERITE_SCRAP, 2));
                    loc.getWorld().dropItemNaturally(dropLoc, new ItemStack(Material.EXPERIENCE_BOTTLE, 32));
                }
                secLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void runTimedBoss(Location loc) {
        String title = MirTimeItems.color(plugin.getConfig().getString("totem.boss-name", "&#0055ff&l☠ ПОРОЖДЕНИЕ БЕЗДНЫ"));
        loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SPAWN, 1.4f, 0.8f);
        loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc.clone().add(0, 1.5, 0), 120, 1.2, 2.0, 1.2, 0.08);

        Ravager boss = (Ravager) loc.getWorld().spawnEntity(loc.clone().add(0, 1, 0), EntityType.RAVAGER);
        boss.setCustomName(title);
        boss.setCustomNameVisible(true);
        summonedBosses.add(boss.getUniqueId());

        BossBar bar = Bukkit.createBossBar(title + MirTimeItems.color(" &8| &#00E5FF60 сек."), BarColor.BLUE, BarStyle.SEGMENTED_20);
        for (Player pl : loc.getWorld().getPlayers()) if (pl.getLocation().distanceSquared(loc) <= 3600) bar.addPlayer(pl);

        new BukkitRunnable() {
            int secLeft = 60;
            @Override
            public void run() {
                if (!boss.isValid() || boss.isDead()) {
                    cancel();
                    bar.removeAll();
                    return;
                }
                bar.setProgress(Math.max(0.0, secLeft / 60.0));
                bar.setTitle(title + MirTimeItems.color(" &8| &#00E5FF" + secLeft + " сек."));
                if (secLeft <= 0) {
                    cancel();
                    Location bLoc = boss.getLocation();
                    bLoc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, bLoc.clone().add(0, 1.5, 0), 120, 1.2, 2.0, 1.2, 0.08);
                    bLoc.getWorld().playSound(bLoc, Sound.ENTITY_WITHER_DEATH, 1.2f, 1.2f);
                    summonedBosses.remove(boss.getUniqueId());
                    boss.remove();
                    bar.removeAll();
                }
                secLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    private void runArmageddon(Player caller, Location impact) {
        World w = impact.getWorld();
        if (w == null) return;

        Block centerBlock = impact.getBlock();
        centerBlock.setType(Material.RESPAWN_ANCHOR);

        String title = MirTimeItems.color("&#FF2A55&l☄ АРМАГЕДОН &8| &fУдар через: &#FFD70020 сек.");
        BossBar bar = Bukkit.createBossBar(title, BarColor.RED, BarStyle.SEGMENTED_20);
        for (Player pl : w.getPlayers()) {
            if (pl.getLocation().distanceSquared(impact) <= 10000) {
                bar.addPlayer(pl);
                pl.sendMessage(MirTimeItems.color(plugin.getPrefix() + "&#FF2A55&lВНИМАНИЕ! &fИгрок &#00E5FF" + caller.getName() + " &fвызвал &#FF2A55&lМЕТЕОРИТ &fна координатах &e" + impact.getBlockX() + ", " + impact.getBlockY() + ", " + impact.getBlockZ() + "&f!"));
            }
        }
        w.playSound(impact, Sound.EVENT_RAID_HORN, 3.0f, 0.5f);

        new BukkitRunnable() {
            int secLeft = 20;

            @Override
            public void run() {
                if (secLeft > 0) {
                    bar.setProgress(Math.max(0.0, secLeft / 20.0));
                    bar.setTitle(MirTimeItems.color("&#FF2A55&l☄ АРМАГЕДОН &8| &fУдар через: &#FFD700" + secLeft + " сек."));

                    double meteorY = impact.getY() + (secLeft * 3.5);
                    Location meteorLoc = new Location(w, impact.getX() + 0.5, meteorY, impact.getZ() + 0.5);

                    w.spawnParticle(Particle.EXPLOSION_EMITTER, meteorLoc, 2, 1.2, 1.2, 1.2, 0.05);
                    w.spawnParticle(Particle.FLAME, meteorLoc, 140, 2.5, 2.5, 2.5, 0.15);
                    w.spawnParticle(Particle.LAVA, meteorLoc, 50, 2.0, 2.0, 2.0, 0.1);

                    for (int y = 0; y < 35; y += 2) {
                        w.spawnParticle(Particle.SOUL_FIRE_FLAME, impact.clone().add(0.5, y, 0.5), 6, 0.25, 0.2, 0.25, 0.01);
                    }
                    if (secLeft <= 5) {
                        w.spawnParticle(Particle.FLASH, impact.clone().add(0.5, 1.5, 0.5), 2);
                        w.playSound(impact, Sound.ENTITY_WARDEN_HEARTBEAT, 2.5f, 1.5f);
                    } else if (secLeft % 4 == 0) {
                        w.playSound(impact, Sound.ENTITY_BLAZE_AMBIENT, 2.0f, 0.5f);
                    }
                    secLeft--;
                } else {
                    cancel();
                    bar.removeAll();
                    if (centerBlock.getType() == Material.RESPAWN_ANCHOR) centerBlock.setType(Material.AIR);

                    w.spawnParticle(Particle.EXPLOSION_EMITTER, impact, 15, 6, 4, 6, 0.2);
                    w.spawnParticle(Particle.FLASH, impact.clone().add(0, 2, 0), 8);
                    w.playSound(impact, Sound.ENTITY_GENERIC_EXPLODE, 4.0f, 0.4f);
                    w.playSound(impact, Sound.ENTITY_WITHER_SPAWN, 2.5f, 0.5f);

                    for (int i = 0; i < 6; i++) {
                        double angle = (Math.PI * 2 / 6) * i;
                        w.strikeLightningEffect(impact.clone().add(Math.cos(angle) * 8, 0, Math.sin(angle) * 8));
                    }

                    for (Entity ent : w.getNearbyEntities(impact, 18, 18, 18)) {
                        if (ent instanceof LivingEntity le && !ent.equals(caller)) {
                            Vector knock = le.getLocation().toVector().subtract(impact.toVector()).normalize().multiply(2.4).setY(1.2);
                            le.setVelocity(knock);
                            le.setFireTicks(160);
                            le.damage(14.0, caller);
                        }
                    }

                    int r = 15;
                    Random rnd = new Random();
                    for (int x = -r; x <= r; x++) {
                        for (int y = -8; y <= 10; y++) {
                            for (int z = -r; z <= r; z++) {
                                double dist = Math.sqrt(x * x + (y * 1.4) * (y * 1.4) + z * z);
                                if (dist > r) continue;

                                Block b = impact.clone().add(x, y, z).getBlock();
                                if (b.getType() == Material.BEDROCK || b.getType() == Material.END_PORTAL_FRAME) continue;
                                if (plugin.isBlocked(b.getLocation())) continue;

                                if (dist < r - 1.5) {
                                    b.setType(Material.AIR);
                                } else {
                                    int roll = rnd.nextInt(100);
                                    if (Math.abs(x) <= 2 && Math.abs(z) <= 2 && y <= -5 && roll < 25) {
                                        b.setType(Material.ANCIENT_DEBRIS);
                                    } else if (roll < 35) {
                                        b.setType(Material.MAGMA_BLOCK);
                                    } else if (roll < 65) {
                                        b.setType(Material.OBSIDIAN);
                                    } else {
                                        b.setType(Material.NETHERRACK);
                                    }
                                    if (y < 0 && b.getRelative(0, 1, 0).getType().isAir() && rnd.nextInt(100) < 25) {
                                        b.getRelative(0, 1, 0).setType(Material.FIRE);
                                    }
                                }
                            }
                        }
                    }

                    Location spawnBase = impact.clone().add(0.5, -4, 0.5);
                    for (int i = 0; i < 2; i++) {
                        Ravager rav = (Ravager) w.spawnEntity(spawnBase.clone().add(i * 3 - 1.5, 0, 0), EntityType.RAVAGER);
                        rav.setCustomName(MirTimeItems.color("&#FF2A55&l☄ Титан Метеорита"));
                        rav.setCustomNameVisible(true);
                        rav.setGlowing(true);
                        summonedBosses.add(rav.getUniqueId());
                    }
                    for (int i = 0; i < 4; i++) {
                        WitherSkeleton ws = (WitherSkeleton) w.spawnEntity(spawnBase.clone().add(rnd.nextInt(7) - 3, 0, rnd.nextInt(7) - 3), EntityType.WITHER_SKELETON);
                        ws.setCustomName(MirTimeItems.color("&#FF8800&l🔥 Страж Кратера"));
                        ws.setCustomNameVisible(true);
                        ws.setGlowing(true);
                        if (ws.getEquipment() != null) {
                            ws.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
                            ws.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
                            ws.getEquipment().setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD));
                        }
                        summonedBosses.add(ws.getUniqueId());
                    }
                    for (int i = 0; i < 4; i++) {
                        Blaze bl = (Blaze) w.spawnEntity(spawnBase.clone().add(rnd.nextInt(7) - 3, 2, rnd.nextInt(7) - 3), EntityType.BLAZE);
                        bl.setCustomName(MirTimeItems.color("&#FFD700&l⚡ Осколок Солнца"));
                        bl.setCustomNameVisible(true);
                        bl.setGlowing(true);
                        summonedBosses.add(bl.getUniqueId());
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    @EventHandler public void onBossBlock(EntityChangeBlockEvent e) { if (summonedBosses.contains(e.getEntity().getUniqueId())) e.setCancelled(true); }
    @EventHandler public void onBossExp(EntityExplodeEvent e) { if (summonedBosses.contains(e.getEntity().getUniqueId())) e.blockList().clear(); }
}
