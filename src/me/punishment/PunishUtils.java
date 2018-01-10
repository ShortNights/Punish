package me.punishment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
@SuppressWarnings("all")
public class PunishUtils {

	/* Public/Private Variables */
	public static Configuration config = Main.getInstance().getConfig();
	public static String banScreen = ChatColor.BLUE + "[Server] \n \n" + ChatColor.RED.toString()
			+ "Your account is banned from " + ChatColor.BOLD + "x Network" + "\n" + ChatColor.GRAY + "Issued: "
			+ ChatColor.BLUE + "%issued%" + "\n" + ChatColor.GRAY + "Reason: " + ChatColor.BLUE + "%reason%" + "\n"
			+ ChatColor.GRAY + "Duration: " + ChatColor.BLUE + "%duration%" + "\n \n" + ChatColor.GRAY + "Appeal at: "
			+ ChatColor.BLUE.toString() + ChatColor.UNDERLINE + "blank";
	public static String success = ChatColor.GREEN + "Successfully performed punishment!";
	public static List muteMessage;

	public static boolean isBanned(UUID player) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Main.setConnection();
		try {
			ps = Main.getInstance().getConnection()
					.prepareStatement("SELECT * FROM punishments WHERE player = ? AND active = 1 AND type = 'BAN'");
			ps.setString(1, player.toString());
			rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Main.close(Main.getInstance().getConnection(), ps, rs);
		}
		return false;
	}

	public static boolean isMuted(UUID player) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Main.setConnection();
		try {
			ps = Main.getInstance().getConnection()
					.prepareStatement("SELECT * FROM punishments WHERE player = ? AND active = 1 AND type = 'MUTE'");
			ps.setString(1, player.toString());
			rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Main.close(Main.getInstance().getConnection(), ps, rs);
		}
		return false;
	}

	public static void kick(final UUID punished, @Nullable String punisher, @Nullable String reason) {
		if (punished == null) {
			return;
		}
		final String punishee = (punisher == null) || (punisher.equalsIgnoreCase("")) ? "Console" : punisher;
		final String banReason = (reason == null) || (reason.equalsIgnoreCase("")) ? "No reason given" : reason;
		final Long banTime = Main.getInstance().getActualTime();
		final Long expiration = Long.valueOf(0L);
		new BukkitRunnable() {
			@Override
			public void run() {
				Main.setConnection();
				PreparedStatement ps = null;
				try {
					ps = Main.getInstance().getConnection().prepareStatement(
							"INSERT INTO punishments(player, punisher, reason, expire, time, type, active) VALUES (?,?,?,?,?,?,?)");

					ps.setString(1, punished.toString());
					ps.setString(2, punishee);
					ps.setString(3, banReason);
					ps.setLong(4, expiration.longValue());
					ps.setLong(5, banTime.longValue());
					ps.setString(6, Type.KICK.toString());
					ps.setInt(7, 0);
					ps.executeUpdate();
					PlayerPunishEvent event = new PlayerPunishEvent(punished, punishee, banReason, Type.KICK);
					Bukkit.getPluginManager().callEvent(event);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Main.close(Main.getInstance().getConnection(), ps, null);
				}
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 20);
	}

	public static void permanentBan(final UUID punished, @Nullable String punisher, @Nullable String reason) {
		if (punished == null) {
			return;
		}
		final String punishee = (punisher == null) || (punisher.equalsIgnoreCase("")) ? "Console" : punisher;
		final String banReason = (reason == null) || (reason.equalsIgnoreCase("")) ? "No reason given" : reason;
		final Long banTime = Main.getInstance().getActualTime();
		final Long expiration = Long.valueOf(0L);
		new BukkitRunnable() {
			@Override
			public void run() {
				Main.setConnection();
				PreparedStatement ps = null;
				try {
					ps = Main.getInstance().getConnection().prepareStatement(
							"INSERT INTO punishments(player, punisher, reason, expire, time, type, active) VALUES (?,?,?,?,?,?,?)");

					ps.setString(1, punished.toString());
					ps.setString(2, punishee);
					ps.setString(3, banReason);
					ps.setLong(4, expiration.longValue());
					ps.setLong(5, banTime.longValue());
					ps.setString(6, Type.BAN.toString());
					ps.setInt(7, 1);
					ps.executeUpdate();
					PlayerPunishEvent event = new PlayerPunishEvent(punished, punishee, banReason, Type.BAN);
					Bukkit.getPluginManager().callEvent(event);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Main.close(Main.getInstance().getConnection(), ps, null);
				}
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 20);
	}

	public static void permanentMute(final UUID punished, @Nullable String punisher, @Nullable String reason) {
		if (punished == null) {
			return;
		}
		final String punishee = (punisher == null) || (punisher.equalsIgnoreCase("")) ? "Console" : punisher;
		final String muteReason = (reason == null) || (reason.equalsIgnoreCase("")) ? "No reason given" : reason;
		final Long muteTime = Main.getInstance().getActualTime();
		final Long expiration = Long.valueOf(0L);
		new BukkitRunnable() {
			@Override
			public void run() {
				Main.setConnection();
				PreparedStatement ps = null;
				try {
					ps = Main.getInstance().getConnection().prepareStatement(
							"INSERT INTO punishments(player, punisher, reason, expire, time, type, active) VALUES (?,?,?,?,?,?,?)");

					ps.setString(1, punished.toString());
					ps.setString(2, punishee);
					ps.setString(3, muteReason);
					ps.setLong(4, expiration.longValue());
					ps.setLong(5, muteTime.longValue());
					ps.setString(6, Type.MUTE.toString());
					ps.setInt(7, 1);
					ps.executeUpdate();
					PlayerPunishEvent event = new PlayerPunishEvent(punished, punishee, muteReason, Type.MUTE);
					Bukkit.getPluginManager().callEvent(event);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Main.close(Main.getInstance().getConnection(), ps, null);
				}
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 20);
	}

	public static void tempMute(final UUID punished, long expire, @Nullable String punisher, @Nullable String reason) {
		if (punished == null) {
			return;
		}
		final String punishee = (punisher == null) || (punisher.equalsIgnoreCase("")) ? "Console" : punisher;
		final String mutereason = (reason == null) || (reason.equalsIgnoreCase("")) ? "No reason given" : reason;
		final Long bantime = Main.getInstance().getActualTime();
		final Long expiration = Long.valueOf(bantime.longValue() + expire);
		new BukkitRunnable() {
			public void run() {
				Main.setConnection();
				PreparedStatement ps = null;
				try {
					ps = Main.getInstance().getConnection().prepareStatement(
							"INSERT INTO punishments(player, punisher, reason, expire, time, type, active) VALUES (?,?,?,?,?,?,?)");

					ps.setString(1, punished.toString());
					ps.setString(2, punishee);
					ps.setString(3, mutereason);
					ps.setLong(4, expiration.longValue());
					ps.setLong(5, bantime.longValue());
					ps.setString(6, Type.MUTE.toString());
					ps.setInt(7, 1);
					ps.executeUpdate();
					PlayerPunishEvent event = new PlayerPunishEvent(punished, punishee, mutereason, Type.MUTE);
					Bukkit.getPluginManager().callEvent(event);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Main.close(Main.getInstance().getConnection(), ps, null);
				}
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 20);
	}

	public static void tempBan(final UUID punished, long expire, @Nullable String punisher, @Nullable String reason) {
		if (punished == null) {
			return;
		}
		final String punishee = (punisher == null) || (punisher.equalsIgnoreCase("")) ? "Console" : punisher;
		final String banreason = (reason == null) || (reason.equalsIgnoreCase("")) ? "No reason given" : reason;
		final Long bantime = Main.getInstance().getActualTime();
		final Long expiration = Long.valueOf(bantime.longValue() + expire);
		new BukkitRunnable() {
			public void run() {
				Main.setConnection();
				PreparedStatement ps = null;
				try {
					Database db = (Database) Main.getInstance().getDatabase();
					ps = Main.getInstance().getConnection().prepareStatement(
							"INSERT INTO punishments(player, punisher, reason, expire, time, type, active) VALUES (?,?,?,?,?,?,?)");

					ps.setString(1, punished.toString());
					ps.setString(2, punishee);
					ps.setString(3, banreason);
					ps.setLong(4, expiration.longValue());
					ps.setLong(5, bantime.longValue());
					ps.setString(6, Type.BAN.toString());
					ps.setInt(7, 1);
					ps.executeUpdate();
					PlayerPunishEvent event = new PlayerPunishEvent(punished, punishee, banreason, Type.BAN);
					Bukkit.getPluginManager().callEvent(event);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Main.close(Main.getInstance().getConnection(), ps, null);
				}
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 20);
	}

	@Deprecated
	public static void unPunish(final UUID player, final Type type) {
		new BukkitRunnable() {
			public void run() {
				Main.setConnection();
				PreparedStatement ps = null;
				try {
					ps = Main.getInstance().getConnection().prepareStatement(
							"UPDATE punishments SET active = ? WHERE player = ? AND active = 1 AND type = ?");
					ps.setInt(1, 0);
					ps.setString(2, player.toString());
					ps.setString(3, type.toString());
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Main.close(Main.getInstance().getConnection(), ps, null);
				}
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 20);
	}

}