package me.punishment;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class PlayerUtil {

	public static Player getPlayer(String name) {
		UUID u = null;
		try {
			u = UUID.fromString(name);
		} catch (IllegalArgumentException localIllegalArgumentException) {
		}
		if (u != null) {
			return Bukkit.getPlayer(u);
		}
		return Bukkit.getPlayer(name);
	}

	public static OfflinePlayer getOfflinePlayer(String name) {
		UUID u = null;
		try {
			u = UUID.fromString(name);
		} catch (IllegalArgumentException localIllegalArgumentException) {
		}
		if (u != null) {
			return Bukkit.getOfflinePlayer(u);
		}
		return Bukkit.getOfflinePlayer(name);
	}

}