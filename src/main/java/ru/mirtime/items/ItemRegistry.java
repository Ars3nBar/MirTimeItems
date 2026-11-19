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
