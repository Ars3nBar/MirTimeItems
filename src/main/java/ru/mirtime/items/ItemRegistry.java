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
                mat = Material.TNT; name = "&c&l[💥] &4&lЯдерный Схлоп";
                lore = List.of("&7Создаёт чёрную дыру в радиусе &#00E5FF10 блоков&7,", "&7стягивает всех врагов в центр и взрывает!", "", "&#00FF66➥ ПКМ&f: активировать");
            }
            case "vampire_blade" -> {
                mat = Material.NETHERITE_SWORD; name = "&4&l[🩸] &c&lКлинок Кровавой Луны";
                lore = List.of("&7Вытягивает душу из врага при ударе:", "&7восстанавливает &#00FF6630% &7от нанесённого урона.", "", "&cОстрота VI | Вампиризм");
            }
            case "thor_axe" -> {
                mat = Material.NETHERITE_AXE; name = "&#00E5FF&l[⚡] &b&lГнев Перуна";
                lore = List.of("&7При ударе обрушивает небесную молнию,", "&7пробивающую броню врага чистым уроном!", "", "&eПерезарядка: &f10 сек.");
            }
            case "midas_sword" -> {
                mat = Material.GOLDEN_SWORD; name = "&#FFD700&l[👑] &6&lКлинок Алчности";
                lore = List.of("&7Озолотит любого убийцу:", "&7выбивает &#FFD700+1.000 монет &7с каждого фрага.", "", "&eНеразрушимый артефакт");
            }
            case "frost_staff" -> {
                mat = Material.BREEZE_ROD; name = "&#00E5FF&l[❄] &b&lЛедяной Скипетр";
                lore = List.of("&7Выпускает ледяную волну в радиусе &#00E5FF9 блоков&7:", "&7вмораживает врагов в лёд на 4 секунды!", "", "&#00FF66➥ ПКМ&f: ледяной шторм");
            }
            case "gravity_mace" -> {
                mat = Material.MACE; name = "&#9400d3&l[🌀] &d&lМолот Сингулярности";
                lore = List.of("&7Создаёт ударную волну, подбрасывающую", "&7всех противников вокруг в стратосферу!", "", "&#00FF66➥ ПКМ&f: ударная волна");
            }
            case "soul_reaper" -> {
                mat = Material.NETHERITE_HOE; name = "&8&l[☠] &5&lЖнец Душ";
                lore = List.of("&7Касание смерти: накладывает Иссушение II,", "&7Слепоту и Утомление на 4 секунды.", "", "&5Острота VII");
            }
            case "snipe_bow" -> {
                mat = Material.BOW; name = "&#00FF66&l[🎯] &a&lЛук «Фантом»";
                lore = List.of("&7Стрелы летят со скоростью света по прямой", "&7и оставляют за собой неоновый след!", "", "&aСила VI | Бесконечность");
            }
            case "trapka" -> {
                mat = Material.NETHERITE_SCRAP; name = "&#0055ff&l[📦] &#00E5FF&lТрапка";
                lore = List.of("&7Мгновенно запирает вас и врага", "&7в нерушимую коробку 5×5×5 на &#00FF6618 сек.&7!", "", "&#00FF66➥ ПКМ&f: поставить трапку");
            }
            case "mega_trap" -> {
                mat = Material.CRYING_OBSIDIAN; name = "&#9400d3&l[🏰] &d&lПласт-Цитадель";
                lore = List.of("&7Возводит гигантский купол 7×7×7", "&7из плачущего обсидиана на &#00FF6625 сек.&7!", "", "&#00FF66➥ ПКМ&f: возвести купол");
            }
            case "flash_pearl" -> {
                mat = Material.ENDER_EYE; name = "&#00E5FF&l[👁] &b&lОко Пустоты";
                lore = List.of("&7Мгновенный блинк в точку взгляда", "&7до &#00E5FF35 блоков &7сквозь пространство без урона!", "", "&#00FF66➥ ПКМ&f: совершить скачок");
            }
            case "disorient" -> {
                mat = Material.ENDER_PEARL; name = "&c&l[💫] &e&lДезориентация";
                lore = List.of("&7Взрывается вспышкой в радиусе 10 блоков:", "&7ослепляет и оглушает всех врагов на 6 сек.!", "", "&#00FF66➥ ПКМ&f: ослепить врагов");
            }
            case "divine_aura" -> {
                mat = Material.NETHER_STAR; name = "&#FFD700&l[🛡] &e&lЭгида Бессмертия";
                lore = List.of("&7Активирует золотой купол защиты:", "&7Сопротивление IV и Регенерация III на 5 сек.", "", "&#00FF66➥ ПКМ&f: активировать щит");
            }
            case "web_bomb" -> {
                mat = Material.COBWEB; name = "&f&l[🕸] &7&lПаутинный Кокон";
                lore = List.of("&7Намертво запутывает ближайшего врага", "&7в плотную паутину 3×3 в радиусе 9 блоков!", "", "&#00FF66➥ ПКМ&f: бросить кокон");
            }
            case "escape_feather" -> {
                mat = Material.FEATHER; name = "&#FFD700&l[🔥] &6&lПеро Феникса";
                lore = List.of("&7Огненный рывок в небеса с оставлением", "&7шлейфа пламени и Плавным падением!", "", "&#00FF66➥ ПКМ&f: взлететь");
            }
            case "cleanse_totem" -> {
                mat = Material.GLISTERING_MELON_SLICE; name = "&#00FF66&l[✨] &a&lСлеза Ангела";
                lore = List.of("&7Мгновенно снимает все дебаффы,", "&7тушит огонь и восстанавливает 5 сердец!", "", "&#00FF66➥ ПКМ&f: очиститься");
            }
            case "totem" -> {
                mat = Material.TOTEM_OF_UNDYING; name = "&#0055ff&l[🔥] &#00FF66&lТотем Призыва Босса";
                lore = List.of("&7Запускает 20-секундный огненный ритуал", "&7и призывает Порождение Бездны с наградой!", "", "&#00FF66➥ ПКМ&f: начать ритуал");
            }
            case "warden_horn" -> {
                mat = Material.GOAT_HORN; name = "&#0055ff&l[📯] &3&lРог Разрушения";
                lore = List.of("&7Зовёт из глубин разъяренного Разрушителя", "&7на 30 секунд в гущу сражения!", "", "&#00FF66➥ ПКМ&f: протрубить");
            }
            case "loot_flare" -> {
                mat = Material.FIREWORK_ROCKET; name = "&#FFD700&l[🎆] &e&lСигнальная Шашка";
                lore = List.of("&7Вызывает с неба личный Аирдроп", "&7с алмазами, чарками и пузырьками опыта!", "", "&#00FF66➥ ПКМ&f: вызвать дроп");
            }
            case "drill_pickaxe" -> {
                mat = Material.NETHERITE_PICKAXE; name = "&#00E5FF&l[⛏] &b&lБур «Крот» 3×3";
                lore = List.of("&7Прогрызает туннели &#00FF663×3 блока", "&7одним ударом! Лучший друг рейдера.", "", "&bЭффективность VII | Прочность V");
            }
            case "magnet_pickaxe" -> {
                mat = Material.DIAMOND_PICKAXE; name = "&#FFD700&l[🧲] &e&lКирка-Магнит";
                lore = List.of("&7Мгновенно переплавляет добытые руды", "&7и телепортирует весь лут прямо в рюкзак!", "", "&eУдача IV | Авто-переплавка");
            }
            case "obsidian_breaker" -> {
                mat = Material.GOLDEN_PICKAXE; name = "&c&l[💣] &4&lКрушитель Обсидиана";
                lore = List.of("&7С одного удара ломает любой обсидиан!", "&7Запас прочности: &c32 блока&7.", "", "&#00FF66➥ ЛКМ по обсидиану");
            }
            case "spawner_silk" -> {
                mat = Material.IRON_PICKAXE; name = "&#9400d3&l[🕷] &d&lКирка Похитителя Спавнеров";
                lore = List.of("&7Позволяет аккуратно вырезать &d1 спавнер&7,", "&7после чего рассыпается в пыль.", "", "&cОдноразовый артефакт");
            }
            case "farmer_hoe" -> {
                mat = Material.DIAMOND_HOE; name = "&#00FF66&l[🌾] &a&lКоса Урожая 5×5";
                lore = List.of("&7Собирает посевы в радиусе &#00FF665×5 блоков", "&7с увеличенным выпадением ресурсов!", "", "&aУдача V");
            }
            case "hermes_boots" -> {
                mat = Material.NETHERITE_BOOTS; name = "&#00E5FF&l[👟] &b&lСкороходы Гермеса";
                lore = List.of("&7Даруют постоянную Скорость II", "&7и полную неуязвимость к падению с высоты!", "", "&bЗащита V | Невесомость V");
            }
            case "titan_chest" -> {
                mat = Material.NETHERITE_CHESTPLATE; name = "&#FFD700&l[🦺] &6&lКираса Джаггернаута";
                lore = List.of("&7Добавляет владельцу &#00FF66+4 сердца (28 HP)", "&7и постоянное Сопротивление урону I!", "", "&6Защита V | Прочность V");
            }
            case "dragon_elytra" -> {
                mat = Material.ELYTRA; name = "&#9400d3&l[🐉] &d&lКрылья Пустоты";
                lore = List.of("&7Неразрушимые элитры со встроенным", "&7реактивным ускорителем на кнопку &#00FF66Shift&7!", "", "&dНеразрушимость");
            }
            case "talisman_rage" -> {
                mat = Material.RED_DYE; name = "&c&l[🔥] &4&lТалисман Берсерка";
                lore = List.of("&7В левой руке наполняет яростью:", "&7даёт постоянный эффект &cСилы II&7!", "", "&7Активен в левой руке");
            }
            case "talisman_speed" -> {
                mat = Material.LIGHT_BLUE_DYE; name = "&#00E5FF&l[💨] &b&lТалисман Урагана";
                lore = List.of("&7В левой руке ускоряет движения:", "&7даёт &#00E5FFСкорость II &7и &#00FF66Спешку II&7!", "", "&7Активен в левой руке");
            }
            case "talisman_wealth" -> {
                mat = Material.GOLD_NUGGET; name = "&#FFD700&l[💰] &e&lТалисман Магната";
                lore = List.of("&7В левой руке притягивает богатство:", "&7удваивает опыт и даёт монеты за мобов!", "", "&7Активен в левой руке");
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
            if (type.equals("vampire_blade")) meta.addEnchant(Enchantment.SHARPNESS, 6, true);
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
