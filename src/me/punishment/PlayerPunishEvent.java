package me.punishment;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class PlayerPunishEvent extends Event {

	/* Public/Private (Static) Variables */
	private static final HandlerList handlers = new HandlerList();
	public final UUID player;
	public final String reason;
	public final String punisher;
	public final Type punishmentType;

	public PlayerPunishEvent(UUID player, String reason, String punisher, Type punishmentType) {
		this.player = player;
		this.reason = reason;
		this.punisher = punisher;
		this.punishmentType = punishmentType;
	}

	// Getters
	public UUID getPlayer() {
		return player;
	}

	public String getReason() {
		return reason;
	}

	public String getPunisher() {
		return punisher;
	}

	public Type getPunishmentType() {
		return punishmentType;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlersList() {
		return handlers;
	}

}
