package me.punishment;

import java.util.UUID;

public class PunishMethod {

	/* Private Variables */
	private UUID player;
	private String punisher;
	private String reason;
	private long expire;
	private long banTime;
	private Type type;
	private int active;

	/**
	 * Main class constructor
	 * 
	 * @param player
	 * @param punisher
	 * @param reason
	 * @param expire
	 * @param banTime
	 * @param type
	 * @param active
	 */
	public PunishMethod(UUID player, String punisher, String reason, long expire, long banTime, Type type, int active) {
		this.player = player;
		this.punisher = punisher;
		this.reason = reason;
		this.expire = expire;
		this.banTime = banTime;
		this.type = type;
		this.active = active;
	}

	/* Setters */
	public void setPunished(UUID player) {
		this.player = player;
	}

	public void setPunished(String player) {
		this.player = UUID.fromString(player);
	}

	public void setPlayer(UUID player) {
		this.player = player;
	}

	public void setPlayer(String player) {
		this.player = UUID.fromString(player);
	}

	public void setPunisher(String punisher) {
		this.punisher = punisher;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

	public void setDuration(long duration) {
		this.banTime = duration;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setActive(int active) {
		this.active = active;
	}

	/* Getters */
	@Override
	public String toString() {
		return "Punishment(player=" + getPlayer() + ", punisher=" + getPunisher() + ", reason=" + getReason()
				+ ", expire=" + getExpire() + ", banTime=" + getDuration() + ", type=" + getType() + ", active="
				+ isActive() + ")";
	}

	public UUID getPunished() {
		return player;
	}

	public UUID getPlayer() {
		return player;
	}

	public String getPunisher() {
		return punisher;
	}

	public String getReason() {
		return reason;
	}

	public long getExpire() {
		return expire;
	}

	public long getDuration() {
		return banTime;
	}

	public Type getType() {
		return type;
	}

	public int getActive() {
		return active;
	}

	/* Booleans */
	public boolean isPermanent() {
		if (expire == 0L) {
			return true;
		} else
			return false;
	}

	public boolean isActive() {
		if (active == 1) {
			return true;
		} else
			return false;
	}
}
