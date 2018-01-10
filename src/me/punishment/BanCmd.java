package me.punishment;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class BanCmd implements CommandExecutor {
	
	  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	  {
	    if (!(sender instanceof Player))
	    {
	      sender.sendMessage(ChatColor.RED + "Players can only use this command.");
	      return false;
	    }
	    Player player = (Player)sender;
	    if (!player.hasPermission("punish.ban"))
	    {
	      player.sendMessage(Main.noperm);
	      return false;
	    }
	    if (args.length < 2)
	    {
	      player.sendMessage(ChatColor.RED + "Incorrect Arguments! /ban <user> <reason>");
	      return false;
	    }
	    
	    String reason = "";
	    for (int i = 1; i < args.length; i++) {
	      reason = reason + args[i] + " ";
	    }
	    reason = reason.trim();
	    {
			OfflinePlayer target = PlayerUtil.getOfflinePlayer(args[0]);
			PunishUtils.permanentBan(target.getUniqueId(), player.getName(), reason);
	    }
		OfflinePlayer target2 = PlayerUtil.getOfflinePlayer(args[0]);
	    for (Player po : Main.getInstance().getServer().getOnlinePlayers()) {
        if (po.hasPermission("punish.alert")) {
            po.sendMessage(ChatColor.RED + sender.getName() + " has perm-banned " + target2.getName() + " for " + reason);
      }
	}
		 PunishMethod punish = null;
	      String kicked = ChatColor.RED + "" + ChatColor.BOLD + "BAN";
	      String kicked2 = " " + ChatColor.GRAY + "    Your account has been banned from " + ChatColor.RED + "x" + ChatColor.GRAY + " network!";
	      String kicked3 = " ";
	      String kicked4 = ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Banned by: " + ChatColor.GOLD + punish.getPunisher();
	      String kicked5 = ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Reason: " + ChatColor.GOLD + punish.getReason();
	      String kicked6 = " ";
	      String kicked7 = ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Duration: " + ChatColor.GOLD + Time.getMSG(Long.valueOf(punish.getExpire()) - Main.getInstance().getActualTime().longValue());
	      String unfairly = ChatColor.DARK_RED + "Unfairly banned? Appeal at " + ChatColor.RED + "Blank";
	      String s = kicked + "\n" + kicked2 + "\n" + kicked3 + "\n" + kicked4 + "\n" + kicked5 + "\n" + kicked6 + "\n" + kicked7 + "\n" + unfairly;
	      ((Player) target2).kickPlayer(s);
	    return false;
	}
}

