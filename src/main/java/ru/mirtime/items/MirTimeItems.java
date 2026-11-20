package ru.mirtime.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MirTimeItems extends JavaPlugin implements CommandExecutor, TabCompleter {

    private NamespacedKey itemKey;
    private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        itemKey = new NamespacedKey(this, "mt_item_id");

        if (getCommand("mtitems") != null) {
            getCommand("mtitems").setExecutor(this);
            getCommand("mtitems").setTabCompleter(this);
        }

        Bukkit.getPluginManager().registerEvents(new ItemMechanics(this), this);
        getLogger().info("MirTimeItems v2.6.0 (Белый список миров анархии) запущен!");
    }

    public NamespacedKey getItemKey() {
        return itemKey;
    }

    public String getPrefix() {
        return color(getConfig().getString("prefix", "&#0055ff&l⚡ &#00E5FF&lMIR-TIME &8» "));
    }

    public String getItemId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
    }

    public boolean checkCooldown(Player p, String id, int seconds) {
        long now = System.currentTimeMillis();
        Map<String, Long> playerCd = cooldowns.computeIfAbsent(p.getUniqueId(), k -> new ConcurrentHashMap<>());
        long end = playerCd.getOrDefault(id, 0L);

        if (now < end) {
            long left = (end - now) / 1000L + 1L;
            p.sendMessage(getPrefix() + color("&cПодождите &f" + left + " сек. &cперед повторным использованием!"));
            return false;
        }
        playerCd.put(id, now + (seconds * 1000L));
        return true;
    }

    public boolean isBlocked(Location loc) {
        if (loc == null || loc.getWorld() == null) return true;
        String wName = loc.getWorld().getName().toLowerCase();

        // Белый список миров, где РАЗРЕШЕНЫ предметы
        List<String> allowedWorlds = getConfig().getStringList("allowed-worlds");
        if (allowedWorlds.isEmpty()) {
            allowedWorlds = List.of("world", "world_nether", "world_the_end", "anarchy");
        } else {
            List<String> lower = new ArrayList<>();
            for (String w : allowedWorlds) lower.add(w.toLowerCase());
            allowedWorlds = lower;
        }

        if (!allowedWorlds.contains(wName)) {
            return true;
        }

        // Проверка привата spawn / hub через Reflection
        try {
            if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
                Class<?> wgClass = Class.forName("com.sk89q.worldguard.WorldGuard");
                Object wg = wgClass.getMethod("getInstance").invoke(null);
                Object platform = wgClass.getMethod("getPlatform").invoke(wg);
                Object container = platform.getClass().getMethod("getRegionContainer").invoke(platform);

                Class<?> adapterClass = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter");
                Object weWorld = adapterClass.getMethod("adapt", org.bukkit.World.class).invoke(null, loc.getWorld());
                Object regions = container.getClass().getMethod("get", Class.forName("com.sk89q.worldedit.world.World")).invoke(container, weWorld);

                if (regions != null) {
                    Class<?> vecClass = Class.forName("com.sk89q.worldedit.math.BlockVector3");
                    Object vec = vecClass.getMethod("at", double.class, double.class, double.class).invoke(null, loc.getX(), loc.getY(), loc.getZ());
                    Object appRegions = regions.getClass().getMethod("getApplicableRegions", vecClass).invoke(regions, vec);

                    for (Object reg : (Iterable<?>) appRegions) {
                        String regId = (String) reg.getClass().getMethod("getId").invoke(reg);
                        if ("spawn".equalsIgnoreCase(regId) || "hub".equalsIgnoreCase(regId)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception ignored) {}

        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = List.of(args);

        if (list.isEmpty()) {
            if (sender instanceof Player p) {
                ItemRegistry.openGui(p, this);
            } else {
                sender.sendMessage("Используйте: /mtitems give <игрок> <предмет> [кол-во]");
            }
            return true;
        }

        if (list.get(0).equalsIgnoreCase("gui") && sender instanceof Player p) {
            ItemRegistry.openGui(p, this);
            return true;
        }

        if (!sender.hasPermission("mirtime.items.admin")) {
            sender.sendMessage(getPrefix() + color("&cНедостаточно прав!"));
            return true;
        }

        if (list.get(0).equalsIgnoreCase("give") && list.size() >= 3) {
            Player target = Bukkit.getPlayer(list.get(1));
            if (target == null) {
                sender.sendMessage(getPrefix() + color("&cИгрок не в сети!"));
                return true;
            }
            String id = list.get(2).toLowerCase();
            int amount = 1;
            if (list.size() >= 4) {
                try { amount = Math.max(1, Math.min(64, Integer.parseInt(list.get(3)))); } catch (Exception ignored) {}
            }

            ItemStack item = ItemRegistry.getItem(this, id, amount);
            if (item == null) {
                sender.sendMessage(getPrefix() + color("&cПредмет не найден!"));
                return true;
            }
            target.getInventory().addItem(item);
            sender.sendMessage(getPrefix() + color("&#00FF66Выдан предмет &f" + id + " &7(x" + amount + ") &#00FF66игроку &f" + target.getName()));
            return true;
        }

        sender.sendMessage(getPrefix() + color("&fКоманды: &#00FF66/mtitems gui &7или &#00FF66/mtitems give <ник> <id> [кол-во]"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) return List.of("gui", "give");
        if (args.length == 2 && args[0].equalsIgnoreCase("give")) return null;
        if (args.length == 3 && args[0].equalsIgnoreCase("give")) return ItemRegistry.getAllIds();
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
