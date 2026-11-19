package ru.mirtime.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistry {

    public static final List<String> ALL_IDS = List.of(
            "cataclysm", "vampire_blade", "thor_axe", "midas_sword", "frost_staff", "gravity_mace", "soul_reaper", "snipe_bow",
            "trapka", "mega_trap", "flash_pearl", "disorient", "divine_aura", "web_bomb", "escape_feather", "cleanse_totem",
            "totem", "warden_horn", "loot_flare",
            "drill_pickaxe", "magnet_pickaxe", "obsidian_breaker", "spawner_silk", "farmer_hoe",
            "hermes_boots", "titan_chest", "dragon_elytra", "talisman_rage", "talisman_speed", "talisman_wealth"
    );

    public static ItemStack create(NamespacedKey key, String id, int amount) {
        String type = id.toLowerCase();
        Material mat;
        String name;
        List<String> lore;

        switch (type) {
            case "cataclysm" -> {
                mat = Material.TNT; name = "&#0055ff&l[💥] &c&lЯдерный Схлоп";
                lore = List.of("&7Стягивает врагов в радиусе 10 блоков", "&7и вызывает мощный взрыв без разрушения!", "", "&#00FF66➥ ПКМ&f: активировать");
            }
            case "vampire_blade" -> {
                mat = Material.NETHERITE_SWORD; name = "&4&l[🩸] &c&lКлинок Вампира";
                lore = List.of("&7Поглощает жизненную силу врага:", "&7лечит вас на &#00FF6625% &7от нанесённого урона.", "", "&cПассивно при ударе в PvP");
            }
            case "thor_axe" -> {
                mat = Material.NETHERITE_AXE; name = "&#00E5FF&l[⚡] &b&lСекира Перуна";
                lore = List.of("&7При ударе призывает небесную молнию,", "&7наносящую чистый урон врагу.", "", "&eПерезарядка: &f12 сек.");
            }
            case "midas_sword" -> {
                mat = Material.GOLDEN_SWORD; name = "&#FFD700&l[🪙] &e&lМеч Мидаса";
                lore = List.of("&7Превращает поверженных врагов в золото:", "&7выбивает &#FFD700+500 монет &7за каждое убийство.", "", "&eНеразрушимый артефакт");
            }
            case "frost_staff" -> {
                mat = Material.BREEZE_ROD; name = "&#00E5FF&l[❄] &b&lПосох Вечной Мерзлоты";
                lore = List.of("&7Замораживает всех врагов в радиусе 8 блоков", "&7на &#00E5FF4 секунды &7и накладывает Замедление IV.", "", "&#00FF66➥ ПКМ&f: заморозить");
            }
            case "gravity_mace" -> {
                mat = Material.MACE; name = "&#9400d3&l[🌀] &d&lБулава Гравитации";
                lore = List.of("&7Создаёт антигравитационный импульс,", "&7подбрасывая всех врагов вокруг в воздух!", "", "&#00FF66➥ ПКМ&f: импульс");
            }
            case "soul_reaper" -> {
                mat = Material.NETHERITE_HOE; name = "&8&l[☠] &5&lКоса Жнеца";
                lore = List.of("&7Проклинает цель при ударе, накладывая", "&7Иссушение II и Слепоту на 3 секунды.", "", "&5Острота VII");
            }
            case "snipe_bow" -> {
                mat = Material.BOW; name = "&#00FF66&l[🏹] &a&lЛук Снайпера";
                lore = List.of("&7Стрелы летят по прямой без падения", "&7и наносят &#00FF66+30% &7дополнительного урона.", "", "&aСила VI | Бесконечность");
            }
            case "trapka" -> {
                mat = Material.NETHERITE_SCRAP; name = "&#0055ff&l[📦] &#00E5FF&lТрапка Бога";
                lore = List.of("&7Мгновенно возводит клетку 5×5×5", "&7из прочного обсидиана на 18 секунд!", "", "&#00FF66➥ ПКМ&f: запереть врага");
            }
            case "mega_trap" -> {
                mat = Material.CRYING_OBSIDIAN; name = "&#9400d3&l[🏰] &d&lЦитадель Титана";
                lore = List.of("&7Огромная арена-клетка 7×7×7", "&7из плачущего обсидиана на 25 секунд!", "", "&#00FF66➥ ПКМ&f: возвести цитадель");
            }
            case "flash_pearl" -> {
                mat = Material.ENDER_EYE; name = "&#00E5FF&l[👁] &b&lМгновенный Жемчуг";
                lore = List.of("&7Мгновенный телепорт в точку взгляда", "&7до 35 блоков без урона от падения!", "", "&#00FF66➥ ПКМ&f: телепортация");
            }
            case "disorient" -> {
                mat = Material.ENDER_PEARL; name = "&c&l[💫] &e&lДезориентация";
                lore = List.of("&7Накладывает на врагов в радиусе 10 блоков", "&7Слепоту, Тошноту и Медлительность на 6 сек.", "", "&#00FF66➥ ПКМ&f: дезориентировать");
            }
            case "divine_aura" -> {
                mat = Material.NETHER_STAR; name = "&#FFD700&l[🛡] &e&lСфера Неуязвимости";
                lore = List.of("&7Окружает вас божественным щитом:", "&7Сопротивление IV и Регенерация III на 5 сек.", "", "&#00FF66➥ ПКМ&f: активировать щит");
            }
            case "web_bomb" -> {
                mat = Material.COBWEB; name = "&f&l[🕸] &7&lПаутинная Граната";
                lore = List.of("&7Запутывает ближайшего врага в радиусе 8 блоков", "&7в плотный кокон из паутины на 8 секунд!", "", "&#00FF66➥ ПКМ&f: бросить");
            }
            case "escape_feather" -> {
                mat = Material.FEATHER; name = "&#FFD700&l[🔥] &6&lПеро Феникса";
                lore = List.of("&7Мощный рывок вперёд и в небо с эффектом", "&7Плавного падения для быстрого отхода.", "", "&#00FF66➥ ПКМ&f: рывок");
            }
            case "cleanse_totem" -> {
                mat = Material.GLISTERING_MELON_SLICE; name = "&#00FF66&l[✨] &a&lСфера Очищения";
                lore = List.of("&7Мгновенно снимает все дебаффы,", "&7тушит огонь и лечит 4 сердца.", "", "&#00FF66➥ ПКМ&f: очиститься");
            }
            case "totem" -> {
                mat = Material.TOTEM_OF_UNDYING; name = "&#0055ff&l[🔥] &#00FF66&lПламенный Тотем Призыва";
                lore = List.of("&720-секундный ритуал призыва босса", "&7на 1 минуту с щедрой наградой!", "", "&#00FF66➥ ПКМ&f: начать ритуал");
            }
            case "warden_horn" -> {
                mat = Material.GOAT_HORN; name = "&#0055ff&l[📯] &3&lРог Бездны";
                lore = List.of("&7Призывает разъяренного Разорителя", "&7на 30 секунд в точку боя!", "", "&#00FF66➥ ПКМ&f: протрубить");
            }
            case "loot_flare" -> {
                mat = Material.FIREWORK_ROCKET; name = "&#FFD700&l[🎆] &e&lСигнальная Ракета";
                lore = List.of("&7Вызывает с неба личный тайник", "&7с алмазами, золотыми яблоками и опытом!", "", "&#00FF66➥ ПКМ&f: вызвать дроп");
            }
            case "drill_pickaxe" -> {
                mat = Material.NETHERITE_PICKAXE; name = "&#00E5FF&l[⛏] &b&lБур Анархиста";
                lore = List.of("&7Выкапывает туннели &#00FF663×3 блока", "&7одним ударом! Идеально для поиска баз.", "", "&bЭффективность VII | Прочность V");
            }
            case "magnet_pickaxe" -> {
                mat = Material.DIAMOND_PICKAXE; name = "&#FFD700&l[🧲] &e&lМагнитная Кирка";
                lore = List.of("&7Автоматически переплавляет руды", "&7и телепортирует весь лут прямо в инвентарь!", "", "&eУдача IV | Авто-плавка");
            }
            case "obsidian_breaker" -> {
                mat = Material.GOLDEN_PICKAXE; name = "&c&l[💣] &4&lКрушитель Обсидиана";
                lore = List.of("&7Мгновенно разрушает обсидиан по клику!", "&7Запас прочности: &c50 блоков&7.", "", "&#00FF66➥ ЛКМ по обсидиану");
            }
            case "spawner_silk" -> {
                mat = Material.IRON_PICKAXE; name = "&#9400d3&l[🕷] &d&lКирка Ловца Спавнеров";
                lore = List.of("&7Позволяет аккуратно добыть &d1 спавнер мобов&7,", "&7после чего инструмент разрушается.", "", "&cОдноразовый инструмент");
            }
            case "farmer_hoe" -> {
                mat = Material.DIAMOND_HOE; name = "&#00FF66&l[🌾] &a&lМотыга Изобилия";
                lore = List.of("&7Собирает урожай в радиусе &#00FF665×5", "&7с тройным выпадением плодов!", "", "&aУдача V");
            }
            case "hermes_boots" -> {
                mat = Material.NETHERITE_BOOTS; name = "&#00E5FF&l[👟] &b&lСапоги Гермеса";
                lore = List.of("&7Даруют владельцу постоянную Скорость II", "&7и полную защиту от урона при падении.", "", "&bЗащита V | Невесомость V");
            }
            case "titan_chest" -> {
                mat = Material.NETHERITE_CHESTPLATE; name = "&#FFD700&l[🦺] &6&lПанцирь Титана";
                lore = List.of("&7Увеличивает максимальное здоровье на &#00FF66+4 ❤", "&7и даёт постоянное Сопротивление урону I.", "", "&6Защита V | Прочность V");
            }
            case "dragon_elytra" -> {
                mat = Material.ELYTRA; name = "&#9400d3&l[🐉] &d&lКрылья Дракона";
                lore = List.of("&7Неразрушимые крылья с реактивным рывком", "&7при нажатии &#00FF66Shift &7в полёте!", "", "&dНеразрушимость");
            }
            case "talisman_rage" -> {
                mat = Material.RED_DYE; name = "&c&l[🔥] &4&lТалисман Ярости";
                lore = List.of("&7Держите в левой руке, чтобы получить", "&7постоянный эффект &cСилы II&7.", "", "&7Работает в левой руке");
            }
            case "talisman_speed" -> {
                mat = Material.LIGHT_BLUE_DYE; name = "&#00E5FF&l[💨] &b&lТалисман Ветра";
                lore = List.of("&7Держите в левой руке, чтобы получить", "&7постоянную &#00E5FFСкорость II &7и &#00FF66Спешку II&7.", "", "&7Работает в левой руке");
            }
            case "talisman_wealth" -> {
                mat = Material.GOLD_NUGGET; name = "&#FFD700&l[💰] &e&lТалисман Богатства";
                lore = List.of("&7Держите в левой руке, чтобы получать", "&7в &#FFD7002 раза больше &7опыта и монет с мобов.", "", "&7Работает в левой руке");
            }
            default -> { return null; }
        }

        ItemStack is = new ItemStack(mat, amount);
        ItemMeta meta = is.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MirTimeItems.color(name));
            List<String> colored = new ArrayList<>();
            for (String s : lore) colored.add(MirTimeItems.color(s));
            meta.setLore(colored);
            meta.addEnchant(Enchantment.UNBREAKING, 3, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);

            if (type.equals("midas_sword") || type.equals("dragon_elytra")) meta.setUnbreakable(true);
            if (type.equals("soul_reaper")) meta.addEnchant(Enchantment.SHARPNESS, 7, true);
            if (type.equals("snipe_bow")) {
                meta.addEnchant(Enchantment.POWER, 6, true);
                meta.addEnchant(Enchantment.INFINITY, 1, true);
            }
            if (type.equals("drill_pickaxe")) meta.addEnchant(Enchantment.EFFICIENCY, 7, true);
            if (type.equals("magnet_pickaxe")) {
                meta.addEnchant(Enchantment.EFFICIENCY, 6, true);
                meta.addEnchant(Enchantment.FORTUNE, 4, true);
            }
            if (type.equals("hermes_boots") || type.equals("titan_chest")) meta.addEnchant(Enchantment.PROTECTION, 5, true);

            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, type);
            is.setItemMeta(meta);
        }
        return is;
    }
}
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
package ru.mirtime.items;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MirTimeItems extends JavaPlugin implements CommandExecutor, TabCompleter {

    private NamespacedKey itemKey;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        itemKey = new NamespacedKey(this, "mt_custom_item");

        if (getCommand("mtitems") != null) {
            getCommand("mtitems").setExecutor(this);
            getCommand("mtitems").setTabCompleter(this);
        }
        Bukkit.getPluginManager().registerEvents(new ItemMechanics(this), this);
        getLogger().info("MirTimeItems v2.0.0 (30 уникальных артефактов) запущен!");
    }

    public NamespacedKey getItemKey() { return itemKey; }
    public String getPrefix() { return getConfig().getString("prefix", "&#0055ff&l⚡ &#00E5FF&lMIR-TIME &8» "); }

    public boolean isBlocked(Location loc) {
        if (loc.getWorld().getName().equalsIgnoreCase("game")) return true;
        try {
            if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) return false;
            Class<?> wgClass = Class.forName("com.sk89q.worldguard.WorldGuard");
            Object wg = wgClass.getMethod("getInstance").invoke(null);
            Object platform = wgClass.getMethod("getPlatform").invoke(wg);
            Object container = platform.getClass().getMethod("getRegionContainer").invoke(platform);
            Object query = container.getClass().getMethod("createQuery").invoke(container);
            Object weLoc = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter").getMethod("adapt", Location.class).invoke(null, loc);
            Object pvpFlag = Class.forName("com.sk89q.worldguard.protection.flags.Flags").getField("PVP").get(null);
            Object array = java.lang.reflect.Array.newInstance(Class.forName("com.sk89q.worldguard.protection.flags.StateFlag"), 1);
            java.lang.reflect.Array.set(array, 0, pvpFlag);
            boolean pvpAllowed = (boolean) query.getClass().getMethod("testState",
                    Class.forName("com.sk89q.worldedit.util.Location"),
                    Class.forName("com.sk89q.worldguard.protection.association.RegionAssociable"),
                    Class.forName("[Lcom.sk89q.worldguard.protection.flags.StateFlag;")).invoke(query, weLoc, null, array);
            return !pvpAllowed;
        } catch (Exception ignored) { return false; }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = List.of(args);
        if (!list.isEmpty() && list.get(0).equalsIgnoreCase("reload")) {
            reloadConfig();
            sender.sendMessage(color(getPrefix() + "&#00FF66Конфиг перезагружен!"));
            return true;
        }
        if (list.size() < 3 || !list.get(0).equalsIgnoreCase("give")) {
            sender.sendMessage(color(getPrefix() + "&fИспользование: &#00FF66/mtitems give <ник> <предмет> [кол-во]"));
            return true;
        }
        Player target = Bukkit.getPlayer(list.get(1));
        if (target == null) {
            sender.sendMessage(color(getPrefix() + "&cИгрок не в сети."));
            return true;
        }
        int amt = 1;
        if (list.size() >= 4) {
            try { amt = Math.max(1, Integer.parseInt(list.get(3))); } catch (Exception ignored) {}
        }
        ItemStack is = ItemRegistry.create(itemKey, list.get(2), amt);
        if (is == null) {
            sender.sendMessage(color(getPrefix() + "&cНеизвестный ID предмета! Используйте Tab для выбора из 30 предметов."));
            return true;
        }
        target.getInventory().addItem(is);
        sender.sendMessage(color(getPrefix() + "&#00FF66Выдан предмет &f" + list.get(2) + " &7(x" + amt + ") &#00FF66игроку &f" + target.getName()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) return List.of("give", "reload");
        if (args.length == 2 && List.of(args).get(0).equalsIgnoreCase("give")) return null;
        if (args.length == 3 && List.of(args).get(0).equalsIgnoreCase("give")) {
            String start = List.of(args).get(2).toLowerCase();
            return ItemRegistry.ALL_IDS.stream().filter(s -> s.startsWith(start)).toList();
        }
        return List.of();
    }

    public static String color(String text) {
        if (text == null) return "";
        Matcher m = Pattern.compile("&#([A-Fa-f0-9]{6})").matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            StringBuilder rep = new StringBuilder("§x");
            for (char c : m.group(1).toCharArray()) rep.append('§').append(c);
            m.appendReplacement(sb, rep.toString());
        }
        m.appendTail(sb);
        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }
}
