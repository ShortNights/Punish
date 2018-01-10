package me.punishment;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PreventCommands
  implements Listener
{
  @EventHandler
  public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event)
  {
    Player player = event.getPlayer();
    String message = event.getMessage().toLowerCase();
    List<String> cmds = Main.getInstance().getConfig().getStringList("prevented-cmds");
    if (PunishUtils.isMuted(player.getUniqueId())) {
      for (String blacklist : cmds) {
        if ((message.startsWith("/" + blacklist.toLowerCase() + " ")) || (message.equals("/" + blacklist.toLowerCase())))
        {
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
    }
  }
  }
}
