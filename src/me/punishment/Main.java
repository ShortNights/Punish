package me.punishment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
  extends JavaPlugin
{
  public String version = Bukkit.getVersion();
  public static Main instance;
  public static String noperm;

	/* Private Variables */
	private static Database database;
	private static Connection conn;
	public void onEnable()
  {
    instance = this;
    registerCmds();
    registerEvents();
    loadConfig();
    
	database = new Database(Database.DatabaseTemplate.MYSQL, "Punish", 5);
	conn = database.getIdleConnection();
    
    PluginDescriptionFile pdfFile = getDescription();
    Logger logger = getLogger();
    logger.info("[" + pdfFile.getName() + " v" + pdfFile.getVersion() + "]" + " Punishment by ShortNights: Enabled");
  }
  
  public void onDisable()
  {
    PluginDescriptionFile pdfFile = getDescription();
    Logger logger = getLogger();
    logger.info("[" + pdfFile.getName() + " v" + pdfFile.getVersion() + "]" + " Punishment by ShortNights: Disabled");
	database.killPool();
	database = null;
	conn = null;
	instance = null;
  }
  
	public static void close(Connection con, PreparedStatement ps, ResultSet res) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException localSQLException) {
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException localSQLException1) {
			}
		}
		if (res != null) {
			try {
				res.close();
			} catch (SQLException localSQLException2) {
			}
		}
	}

	// Setters
	public static void setConnection() {
		conn = database.getIdleConnection();
	}

	// Getters
	public static Main getInstance() {
		return instance;
	}

	public static Connection getConnection() {
		return conn;
	}

	public List<PunishMethod> getHistory(UUID player) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PunishMethod> punishHistory = new ArrayList<PunishMethod>();
		try {
			ps = getConnection().prepareStatement("SELECT FROM 'punishments' WHERE player = ?");
			ps.setString(1, player.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				punishHistory.add(new PunishMethod(UUID.fromString(rs.getString("player")), rs.getString("punisher"),
						rs.getString("reason"), Long.valueOf(rs.getLong("expire")), Long.valueOf(rs.getLong("banTime")),
						Type.valueOf(rs.getString("type")), rs.getInt("active")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(getConnection(), ps, rs);
		}
		return punishHistory;
	}

	public List<PunishMethod> getActivePunishments(UUID player) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PunishMethod> punishlist = new ArrayList<PunishMethod>();
		try {
			ps = getConnection().prepareStatement("SELECT * FROM `punishments` WHERE PLAYER = ? AND ACTIVE = 1");
			ps.setString(1, player.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				punishlist.add(new PunishMethod(UUID.fromString(rs.getString("player")), rs.getString("punisher"),
						rs.getString("reason"), Long.valueOf(rs.getLong("expire")), Long.valueOf(rs.getLong("bantime")),
						Type.valueOf(rs.getString("type")), rs.getInt("active")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(getConnection(), ps, rs);
		}
		return punishlist;
	}

	public Long getActualTime() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConnection().prepareStatement("select CAST(UNIX_TIMESTAMP(CURTIME(3)) * 1000 AS unsigned)");
			rs = ps.executeQuery();
			if (rs.next()) {
				return Long.valueOf(rs.getLong("CAST(UNIX_TIMESTAMP(CURTIME(3)) * 1000 AS unsigned)"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(getConnection(), ps, rs);
		}
		return Long.valueOf(System.currentTimeMillis());
	}
	
 
  
  private void registerCmds()
  {
   getCommand("ban").setExecutor(new BanCmd());
   getCommand("unpunish").setExecutor(new UnPunish());
  }
  
  private void registerEvents()
  {
    PluginManager pm = Bukkit.getPluginManager();		
    pm.registerEvents(new Listeners(), this);
    pm.registerEvents(new PreventCommands(), this);
  }
  
  
  private void loadConfig()
  {
    if (!getDataFolder().exists()) {
      getDataFolder().mkdirs();
    }
    List<String> prevent = new ArrayList<String>();
    prevent.add("whisper");
    prevent.add("msg");
    prevent.add("message");
    prevent.add("w");
    prevent.add("tell");
    prevent.add("t");
    prevent.add("me");
    prevent.add("minecraft:me");
    prevent.add("minecraft:tell");
    prevent.add("emsg");
    prevent.add("etell");
    prevent.add("ewhisper");
    prevent.add("emessage");
    getConfig().addDefault("prevented-cmds", prevent);
    getConfig().addDefault("noperm", ChatColor.translateAlternateColorCodes('&', "&6[Permissions]: &eNo permissions!"));
    
    getConfig().options().copyDefaults(true);
    saveConfig();
    
    noperm = getConfig().getString("noperm");
    
  }
}