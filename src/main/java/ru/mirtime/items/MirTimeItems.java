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
        getLogger().info("MirTimeItems v2.5.0 (30 уникальных артефактов) запущен!");
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
