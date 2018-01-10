package me.punishment;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class Listeners implements Listener {
	

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		Player player = event.getPlayer();
		UUID p = player.getUniqueId();
		if (PunishUtils.isMuted(p)) {
			if (!message.startsWith("/")) {
				PunishMethod punish = null;
				for (PunishMethod pun : Main.getInstance().getActivePunishments(player.getUniqueId())) {
					if (pun.getType() == Type.MUTE || pun.getType() == Type.TEMP_MUTE) {
						punish = pun;
					}
				}
				if (punish.isPermanent()) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You are muted!");
					player.sendMessage(ChatColor.GOLD + "Reason: " + ChatColor.YELLOW + punish.getReason());
					player.sendMessage(ChatColor.GOLD + "Duration: " + ChatColor.YELLOW + "Permanent");
					return;
				} else {
					if (Long.valueOf(punish.getExpire()) - Main.getInstance().getActualTime() <= 0L) {
						PunishUtils.unPunish(player.getUniqueId(), Type.MUTE);
						event.setCancelled(false);
						return;
					}
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You are muted!");
					player.sendMessage(ChatColor.GOLD + "Reason: " + ChatColor.YELLOW + punish.getReason());
					player.sendMessage(ChatColor.GOLD + "Duration: " + ChatColor.YELLOW + Time.getMSG(
							Long.valueOf(punish.getExpire()) - Main.getInstance().getActualTime().longValue()));
					return;
				}
			}
		} else {
			event.setCancelled(false);
		}
	}

	/**
	 * Checks if the player is banned
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if (PunishUtils.isBanned(player.getUniqueId())) {
			for (PunishMethod pun : Main.getInstance().getActivePunishments(player.getUniqueId())) {
				if (pun.getType() == Type.BAN || pun.getType() == Type.TEMP_BAN) {
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
			
				event.disallow(Result.KICK_BANNED, s);
				return;
			}
		} 
	}