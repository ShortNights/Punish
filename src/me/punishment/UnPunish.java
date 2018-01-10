package me.punishment;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnPunish implements CommandExecutor {
	
	  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	  {
	    if (!(sender instanceof Player))
	    {
	      sender.sendMessage(ChatColor.RED + "Players can only use this command.");
	      return false;
	    }
	    Player player = (Player)sender;
	    if (!player.hasPermission("punish.use"))
	    {
	      player.sendMessage(Main.noperm);
	      return false;
	    }
	    if (args.length < 2)
	    {
	      player.sendMessage(ChatColor.RED + "Incorrect Arguments! /unpunish <user> <type> (Ban/Mute/Warn/Kick)");
	      return false;
	    }
			Player p = Bukkit.getPlayer(args[0]);
			UUID pp = p.getUniqueId();
			
	    String type = args[1];		
		if (type.equalsIgnoreCase("Ban")) {
			PunishUtils.unPunish(pp, Type.BAN);
		}
		if (type.equalsIgnoreCase("Mute")) {
			PunishUtils.unPunish(pp, Type.MUTE);
		}
		if (type.equalsIgnoreCase("Warn")) {
			PunishUtils.unPunish(pp, Type.WARN);
		}
		if (type.equalsIgnoreCase("Kick")) {
			PunishUtils.unPunish(pp, Type.KICK);
		}
		
	    return false;
	}
	}

