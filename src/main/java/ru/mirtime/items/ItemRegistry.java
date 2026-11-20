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
            // Базовые 30 артефактов
            "cataclysm", "vampire_blade", "thor_axe", "midas_sword", "frost_staff", "gravity_mace", "soul_reaper", "snipe_bow",
            "trapka", "mega_trap", "flash_pearl", "disorient", "divine_aura", "web_bomb", "escape_feather", "cleanse_totem",
            "totem", "warden_horn", "loot_flare",
            "drill_pickaxe", "magnet_pickaxe", "obsidian_breaker", "spawner_silk", "farmer_hoe",
            "hermes_boots", "titan_chest", "dragon_elytra", "talisman_rage", "talisman_speed", "talisman_wealth",
            // Мега-артефакты (15)
            "meteor_staff", "black_hole", "bedrock_shield", "lightning_trident", "shadow_cloak",
            "vortex_hook", "nuke_tnt", "lava_prison", "midas_touch", "dragon_breath_bomb",
            "chrono_watch", "berserk_axe", "xray_compass", "auto_bridge", "phoenix_totem",
            // Кит «БОГ XV» (5)
            "god_helmet", "god_chestplate", "god_leggings", "god_boots", "god_sword",
            // Кит «ДЖЕК XV» (5)
            "jack_helmet", "jack_chestplate", "jack_leggings", "jack_boots", "jack_mace",
            // Кит «КОРОЛЬ X» (5)
            "king_helmet", "king_chestplate", "king_leggings", "king_boots", "king_scepter",
            // Кит «ХРИСТИАНИН X» (5)
            "christian_helmet", "christian_chestplate", "christian_leggings", "christian_boots", "christian_sword"
    );

    public static ItemStack create(NamespacedKey key, String id, int amount) {
        String type = id.toLowerCase();
        Material mat;
        String name;
        List<String> lore;

        switch (type) {
            case "cataclysm" -> { mat = Material.NETHER_STAR; name = "&#FF2A55&l💥 Ядерный Схлоп"; lore = List.of("&7Притягивает врагов в радиусе 10 блоков", "&7и устраивает мощный взрыв.", "", "&#00FF66▶ ПКМ для активации"); }
            case "vampire_blade" -> { mat = Material.NETHERITE_SWORD; name = "&c&l🩸 Клинок Вампира"; lore = List.of("&7Восстанавливает 30% от нанесённого", "&7урона в виде здоровья."); }
            case "thor_axe" -> { mat = Material.NETHERITE_AXE; name = "&#00E5FF&l⚡ Молот Тора"; lore = List.of("&7При ударе бьёт врага молнией", "&7и наносит дополнительный урон."); }
            case "midas_sword" -> { mat = Material.GOLDEN_SWORD; name = "&#FFD700&l🪙 Меч Мидаса"; lore = List.of("&7Даёт +1.000 Монет за каждое убийство."); }
            case "frost_staff" -> { mat = Material.BLAZE_ROD; name = "&#00E5FF&l❄ Ледяной Посох"; lore = List.of("&7Вмораживает всех врагов вокруг в лёд.", "", "&#00FF66▶ ПКМ для активации"); }
            case "gravity_mace" -> { mat = Material.MACE; name = "&d&l🌀 Грави-Булава"; lore = List.of("&7Подбрасывает всех врагов вокруг в воздух.", "", "&#00FF66▶ ПКМ для активации"); }
            case "soul_reaper" -> { mat = Material.NETHERITE_HOE; name = "&5&l☠ Коса Душ"; lore = List.of("&7Накладывает Иссушение, Слепоту и Утомление."); }
            case "snipe_bow" -> { mat = Material.BOW; name = "&#00FF66&l🎯 Снайперский Лук"; lore = List.of("&7Стрелы летят по прямой без гравитации."); }
            case "trapka" -> { mat = Material.OBSIDIAN; name = "&#0055ff&l⬛ Обсидиановая Трапка"; lore = List.of("&7Дизайнерская клетка 5x5 с подсветкой на 18 сек.", "", "&#00FF66▶ ПКМ для активации"); }
            case "mega_trap" -> { mat = Material.CRYING_OBSIDIAN; name = "&5&l🟪 Мега-Трапка Бездны"; lore = List.of("&7Огромный неоновый куб 7x7 на 25 сек.", "", "&#00FF66▶ ПКМ для активации"); }
            case "flash_pearl" -> { mat = Material.ENDER_PEARL; name = "&#00E5FF&l⚡ Мгновенный Пёрл"; lore = List.of("&7Мгновенно телепортирует в точку взгляда (до 35 бл).", "", "&#00FF66▶ ПКМ для телепорта"); }
            case "disorient" -> { mat = Material.ENDER_EYE; name = "&e&l👁 Дезориентация"; lore = List.of("&7Ослепляет, замедляет и ослабляет всех вокруг на 6 сек.", "", "&#00FF66▶ ПКМ для активации"); }
            case "divine_aura" -> { mat = Material.GOLDEN_APPLE; name = "&#FFD700&l✨ Божественная Аура"; lore = List.of("&7Даёт Сопротивление IV и Регенерацию III.", "", "&#00FF66▶ ПКМ для активации"); }
            case "web_bomb" -> { mat = Material.HEART_OF_THE_SEA; name = "&#00E5FF&l🌊 Шок-Волна Импульса"; lore = List.of("&7Отбрасывает врагов в радиусе 9 блоков и бьёт током.", "", "&#00FF66▶ ПКМ для импульса"); }
            case "escape_feather" -> { mat = Material.FEATHER; name = "&#00FF66&l🕊 Перо Побега"; lore = List.of("&7Мощный рывок вперёд и вверх + Плавное падение.", "", "&#00FF66▶ ПКМ для рывка"); }
            case "cleanse_totem" -> { mat = Material.TOTEM_OF_UNDYING; name = "&#00E5FF&l💧 Тотем Очищения"; lore = List.of("&7Снимает все негативные эффекты и лечит 5 сердец.", "", "&#00FF66▶ ПКМ для активации"); }
            case "totem" -> { mat = Material.RESPAWN_ANCHOR; name = "&#0055ff&l☠ Тотем Призыва Босса"; lore = List.of("&7Призывает Порождение Бездны с таймером на 1 минуту.", "", "&#00FF66▶ ПКМ для призыва"); }
            case "warden_horn" -> { mat = Material.GOAT_HORN; name = "&3&l📯 Рог Бездны"; lore = List.of("&7Призывает Разорителя на 30 секунд.", "", "&#00FF66▶ ПКМ для призыва"); }
            case "loot_flare" -> { mat = Material.FIREWORK_ROCKET; name = "&#FFD700&l🎆 Сигнальная Шашка Аирдропа"; lore = List.of("&7Вызывает аирдроп с таймером 10 секунд!", "", "&#00FF66▶ ПКМ для вызова"); }
            case "drill_pickaxe" -> { mat = Material.NETHERITE_PICKAXE; name = "&#FF2A55&l⛏ Бур 3x3"; lore = List.of("&7Копает туннель 3x3x3 одним ударом."); }
            case "magnet_pickaxe" -> { mat = Material.DIAMOND_PICKAXE; name = "&#00E5FF&l🧲 Магнитная Кирка"; lore = List.of("&7Авто-переплавка руды сразу в инвентарь."); }
            case "obsidian_breaker" -> { mat = Material.GOLDEN_PICKAXE; name = "&5&l🔨 Крушитель Обсидиана"; lore = List.of("&7Ломает обсидиан с 1 удара ЛКМ (32 заряда)."); }
            case "spawner_silk" -> { mat = Material.IRON_PICKAXE; name = "&#00FF66&l🕸 Кирка Спавнеров"; lore = List.of("&7Добывает спавнер с мобом (одноразовая)."); }
            case "farmer_hoe" -> { mat = Material.DIAMOND_HOE; name = "&#00FF66&l🌾 Мотыга Фермера 5x5"; lore = List.of("&7Собирает урожай в радиусе 5x5."); }
            case "hermes_boots" -> { mat = Material.NETHERITE_BOOTS; name = "&#00E5FF&l👟 Ботинки Гермеса"; lore = List.of("&7Скорость II и полный иммунитет к падению."); }
            case "titan_chest" -> { mat = Material.NETHERITE_CHESTPLATE; name = "&#0055ff&l🛡 Нагрудник Титана"; lore = List.of("&7Даёт +4 сердца (28 HP) и Сопротивление."); }
            case "dragon_elytra" -> { mat = Material.ELYTRA; name = "&d&l🐉 Элитры Дракона"; lore = List.of("&7Нажмите Shift в полёте для ускорения."); }
            case "talisman_rage" -> { mat = Material.RED_DYE; name = "&c&l🔥 Талисман Ярости"; lore = List.of("&7В левой руке: Сила II."); }
            case "talisman_speed" -> { mat = Material.LIGHT_BLUE_DYE; name = "&#00E5FF&l⚡ Талисман Скорости"; lore = List.of("&7В левой руке: Скорость II и Спешка II."); }
            case "talisman_wealth" -> { mat = Material.GOLD_NUGGET; name = "&#FFD700&l💰 Талисман Богатства"; lore = List.of("&7В левой руке: x2 опыт и +250 Монет за мобов."); }
            case "meteor_staff" -> { mat = Material.FIRE_CHARGE; name = "&#FF5500&l☄ Посох Метеорита"; lore = List.of("&7Обрушивает с неба пылающий метеорит", "&7в точку вашего прицела!", "", "&#00FF66▶ ПКМ для вызова метеорита"); }
            case "black_hole" -> { mat = Material.ENDER_CHEST; name = "&5&l🌌 Сингулярность (Чёрная Дыра)"; lore = List.of("&7Создаёт воронку на 5 сек, затягивающую", "&7всех врагов в центр и наносящую урон.", "", "&#00FF66▶ ПКМ для запуска"); }
            case "bedrock_shield" -> { mat = Material.BEACON; name = "&#00E5FF&l🛡 Эгида Титана"; lore = List.of("&7Строит укреплённый защитный купол", "&7с регенерацией внутри на 12 сек.", "", "&#00FF66▶ ПКМ для защиты"); }
            case "lightning_trident" -> { mat = Material.TRIDENT; name = "&#00E5FF&l🔱 Трезубец Посейдона"; lore = List.of("&7При ударе или броске бьёт серией молний", "&7по всем врагам в радиусе 8 блоков."); }
            case "shadow_cloak" -> { mat = Material.PHANTOM_MEMBRANE; name = "&8&l👤 Плащ Тени"; lore = List.of("&7Полная невидимость на 8 секунд", "&7для скрытной атаки в спину.", "", "&#00FF66▶ ПКМ для ухода в тень"); }
            case "vortex_hook" -> { mat = Material.FISHING_ROD; name = "&d&l🪝 Гарпун Бездны"; lore = List.of("&7Притягивает врага из прицела (до 25 бл)", "&7прямо к вам для комбо-удара!", "", "&#00FF66▶ ПКМ по врагу"); }
            case "nuke_tnt" -> { mat = Material.TNT; name = "&#FF2A55&l☢ Термоядерный Заряд"; lore = List.of("&7Взрыв через 3 сек, который пробивает", "&7даже обсидиан и базы в радиусе 4 бл!", "", "&#00FF66▶ ПКМ для установки"); }
            case "lava_prison" -> { mat = Material.MAGMA_BLOCK; name = "&#FF5500&l🔥 Огненная Клетка"; lore = List.of("&7Запирает ближайшего врага в клетку", "&7из магмы и огня на 10 секунд!", "", "&#00FF66▶ ПКМ по врагу"); }
            case "midas_touch" -> { mat = Material.GOLD_INGOT; name = "&#FFD700&l✋ Рука Мидаса"; lore = List.of("&7Превращает пол 5x5 в золото и забирает", "&7по 5.000 Монет у всех врагов рядом!", "", "&#00FF66▶ ПКМ для активации"); }
            case "dragon_breath_bomb" -> { mat = Material.DRAGON_BREATH; name = "&d&l🐉 Сфера Дыхания Дракона"; lore = List.of("&7Создаёт ядовитое облако на 8 сек,", "&7наносящее магический урон врагам.", "", "&#00FF66▶ ПКМ для активации"); }
            case "chrono_watch" -> { mat = Material.CLOCK; name = "&#00E5FF&l⏳ Часы Хроноса"; lore = List.of("&7Запоминает вашу позицию и HP, а через", "&75 сек возвращает вас назад во времени!", "", "&#00FF66▶ ПКМ для перемотки"); }
            case "berserk_axe" -> { mat = Material.DIAMOND_AXE; name = "&c&l🪓 Секира Берсерка"; lore = List.of("&7Наносит до +120% урона, когда у вас", "&7остаётся меньше 4 сердец здоровья!"); }
            case "xray_compass" -> { mat = Material.RECOVERY_COMPASS; name = "&#00FF66&l👁 Око Охотника"; lore = List.of("&7Подсвечивает всех игроков в радиусе", "&780 блоков сквозь стены на 15 секунд!", "", "&#00FF66▶ ПКМ для сканирования"); }
            case "auto_bridge" -> { mat = Material.CRYING_OBSIDIAN; name = "&#0055ff&l🌉 Мост Строителя"; lore = List.of("&7Мгновенно строит перед вами мост", "&7из обсидиана длиной 20 блоков.", "", "&#00FF66▶ ПКМ для постройки"); }
            case "phoenix_totem" -> { mat = Material.BLAZE_POWDER; name = "&#FF8800&l🔥 Перо Феникса (Пассив)"; lore = List.of("&7Лежит в инвентаре: спасает от смерти,", "&7взрывает врагов огнём и подкидывает в небо!"); }

            // === КИТ «БОГ XV» (Защита 15, Острота 15, без ограничений) ===
            case "god_helmet" -> { mat = Material.NETHERITE_HELMET; name = "&#e6c300&l✨ Шлем «БОГА» &8| &#e6c300З15"; lore = List.of("&7Божественная броня высшей пробы.", "&eЗащита XV &8| &eШипы V &8| &aБез проклятий"); }
            case "god_chestplate" -> { mat = Material.NETHERITE_CHESTPLATE; name = "&#e6c300&l✨ Нагрудник «БОГА» &8| &#e6c300З15"; lore = List.of("&7Божественная броня высшей пробы.", "&eЗащита XV &8| &eШипы V &8| &aБез проклятий"); }
            case "god_leggings" -> { mat = Material.NETHERITE_LEGGINGS; name = "&#e6c300&l✨ Поножи «БОГА» &8| &#e6c300З15"; lore = List.of("&7Божественная броня высшей пробы.", "&eЗащита XV &8| &eШипы V &8| &aБез проклятий"); }
            case "god_boots" -> { mat = Material.NETHERITE_BOOTS; name = "&#e6c300&l✨ Ботинки «БОГА» &8| &#e6c300З15"; lore = List.of("&7Божественная броня высшей пробы.", "&eЗащита XV &8| &eНевесомость X &8| &aБез проклятий"); }
            case "god_sword" -> { mat = Material.NETHERITE_SWORD; name = "&#e6c300&l⚔ Меч «БОГА» &8| &#e6c300XV"; lore = List.of("&7Легендарный клинок небесного судьи.", "&eОстрота XV &8| &eЗаговор огня V &8| &eДобыча X"); }

            // === КИТ «ДЖЕК XV» (Защита 15, Булава Джека XV, Привязка + Утрата) ===
            case "jack_helmet" -> { mat = Material.NETHERITE_HELMET; name = "&#ff0000&l🎃 Шлем «ДЖЕКА» &8| &#ff0000З15"; lore = List.of("&7Ультимативная броня повелителя тыкв.", "&eЗащита XV &8| &cПроклятие привязки и утраты"); }
            case "jack_chestplate" -> { mat = Material.NETHERITE_CHESTPLATE; name = "&#ff0000&l🎃 Нагрудник «ДЖЕКА» &8| &#ff0000З15"; lore = List.of("&7Ультимативная броня повелителя тыкв.", "&eЗащита XV &8| &cПроклятие привязки и утраты"); }
            case "jack_leggings" -> { mat = Material.NETHERITE_LEGGINGS; name = "&#ff0000&l🎃 Поножи «ДЖЕКА» &8| &#ff0000З15"; lore = List.of("&7Ультимативная броня повелителя тыкв.", "&eЗащита XV &8| &cПроклятие привязки и утраты"); }
            case "jack_boots" -> { mat = Material.NETHERITE_BOOTS; name = "&#ff0000&l🎃 Ботинки «ДЖЕКА» &8| &#ff0000З15"; lore = List.of("&7Ультимативная броня повелителя тыкв.", "&eЗащита XV &8| &cПроклятие привязки и утраты"); }
            case "jack_mace" -> { mat = Material.MACE; name = "&#ff0000&l🔨 Булава «ДЖЕКА» &8| &#ff0000XV"; lore = List.of("&7Сокрушительная булава с ветровым зарядом!", "&eПлотность XV &8| &eПорыв ветра III &8| &cУтрата"); }

            // === КИТ «КОРОЛЬ X» (Защита 10, Скипетр X, Проклятие Утраты) ===
            case "king_helmet" -> { mat = Material.NETHERITE_HELMET; name = "&#00bfff&l👑 Шлем «КОРОЛЯ» &8| &#00bfffЗ10"; lore = List.of("&7Доспехи королевской ковки.", "&bЗащита X &8| &eПроклятие утраты"); }
            case "king_chestplate" -> { mat = Material.NETHERITE_CHESTPLATE; name = "&#00bfff&l👑 Нагрудник «КОРОЛЯ» &8| &#00bfffЗ10"; lore = List.of("&7Доспехи королевской ковки.", "&bЗащита X &8| &eПроклятие утраты"); }
            case "king_leggings" -> { mat = Material.NETHERITE_LEGGINGS; name = "&#00bfff&l👑 Поножи «КОРОЛЯ» &8| &#00bfffЗ10"; lore = List.of("&7Доспехи королевской ковки.", "&bЗащита X &8| &eПроклятие утраты"); }
            case "king_boots" -> { mat = Material.NETHERITE_BOOTS; name = "&#00bfff&l👑 Ботинки «КОРОЛЯ» &8| &#00bfffЗ10"; lore = List.of("&7Доспехи королевской ковки.", "&bЗащита X &8| &eПроклятие утраты"); }
            case "king_scepter" -> { mat = Material.NETHERITE_SWORD; name = "&#00bfff&l🔱 Скипетр «КОРОЛЯ» &8| &#00bfffX"; lore = List.of("&7Священный боевой клинок правителя.", "&bОстрота X &8| &bЗаговор огня III &8| &eПроклятие утраты"); }

            // === КИТ «ХРИСТИАНИН X» (Алмазная броня Защита 10, Меч X, Привязка + Утрата) ===
            case "christian_helmet" -> { mat = Material.DIAMOND_HELMET; name = "&#9400d3&l⛪ Шлем «ХРИСТИАНИНА» &8| &#9400d3З10"; lore = List.of("&7Алмазная броня страдальца.", "&dЗащита X &8| &cПривязка + Утрата"); }
            case "christian_chestplate" -> { mat = Material.DIAMOND_CHESTPLATE; name = "&#9400d3&l⛪ Нагрудник «ХРИСТИАНИНА» &8| &#9400d3З10"; lore = List.of("&7Алмазная броня страдальца.", "&dЗащита X &8| &cПривязка + Утрата"); }
            case "christian_leggings" -> { mat = Material.DIAMOND_LEGGINGS; name = "&#9400d3&l⛪ Поножи «ХРИСТИАНИНА» &8| &#9400d3З10"; lore = List.of("&7Алмазная броня страдальца.", "&dЗащита X &8| &cПривязка + Утрата"); }
            case "christian_boots" -> { mat = Material.DIAMOND_BOOTS; name = "&#9400d3&l⛪ Ботинки «ХРИСТИАНИНА» &8| &#9400d3З10"; lore = List.of("&7Алмазная броня страдальца.", "&dЗащита X &8| &cПривязка + Утрата"); }
            case "christian_sword" -> { mat = Material.DIAMOND_SWORD; name = "&#9400d3&l🗡 Меч «ХРИСТИАНИНА» &8| &#9400d3X"; lore = List.of("&7Острый алмазный клинок аскета.", "&dОстрота X &8| &cПривязка + Утрата"); }

            default -> { return null; }
        }
        ItemStack is = new ItemStack(mat, amount);
        ItemMeta meta = is.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MirTimeItems.color(name));
            List<String> colored = new ArrayList<>();
            for (String s : lore) colored.add(MirTimeItems.color(s));
            meta.setLore(colored);
            meta.addEnchant(Enchantment.UNBREAKING, 5, true);

            // Специальные зачарования для старых артефактов
            if (type.equals("midas_sword") || type.equals("dragon_elytra") || type.equals("berserk_axe")) meta.setUnbreakable(true);
            if (type.equals("vampire_blade")) meta.addEnchant(Enchantment.SHARPNESS, 6, true);
            if (type.equals("soul_reaper")) meta.addEnchant(Enchantment.SHARPNESS, 7, true);
            if (type.equals("berserk_axe")) meta.addEnchant(Enchantment.SHARPNESS, 6, true);
            if (type.equals("lightning_trident")) meta.addEnchant(Enchantment.IMPALING, 5, true);
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

            // Кит «БОГ XV»
            if (type.startsWith("god_") && !type.equals("god_sword")) {
                meta.addEnchant(Enchantment.PROTECTION, 15, true);
                meta.addEnchant(Enchantment.THORNS, 5, true);
                meta.addEnchant(Enchantment.MENDING, 1, true);
                if (type.equals("god_boots")) meta.addEnchant(Enchantment.FEATHER_FALLING, 10, true);
                meta.setUnbreakable(true);
            }
            if (type.equals("god_sword")) {
                meta.addEnchant(Enchantment.SHARPNESS, 15, true);
                meta.addEnchant(Enchantment.FIRE_ASPECT, 5, true);
                meta.addEnchant(Enchantment.LOOTING, 10, true);
                meta.setUnbreakable(true);
            }

            // Кит «ДЖЕК XV»
            if (type.startsWith("jack_") && !type.equals("jack_mace")) {
                meta.addEnchant(Enchantment.PROTECTION, 15, true);
                meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
                meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                meta.setUnbreakable(true);
            }
            if (type.equals("jack_mace")) {
                meta.addEnchant(Enchantment.DENSITY, 15, true);
                meta.addEnchant(Enchantment.WIND_BURST, 3, true);
                meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                meta.setUnbreakable(true);
            }

            // Кит «КОРОЛЬ X»
            if (type.startsWith("king_") && !type.equals("king_scepter")) {
                meta.addEnchant(Enchantment.PROTECTION, 10, true);
                meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                meta.setUnbreakable(true);
            }
            if (type.equals("king_scepter")) {
                meta.addEnchant(Enchantment.SHARPNESS, 10, true);
                meta.addEnchant(Enchantment.FIRE_ASPECT, 3, true);
                meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                meta.setUnbreakable(true);
            }

            // Кит «ХРИСТИАНИН X»
            if (type.startsWith("christian_") && !type.equals("christian_sword")) {
                meta.addEnchant(Enchantment.PROTECTION, 10, true);
                meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
                meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            }
            if (type.equals("christian_sword")) {
                meta.addEnchant(Enchantment.SHARPNESS, 10, true);
                meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
                meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            }

            if (!type.startsWith("god_") && !type.startsWith("jack_") && !type.startsWith("king_") && !type.startsWith("christian_")) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
            }

            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, type);
            is.setItemMeta(meta);
        }
        return is;
    }
}
