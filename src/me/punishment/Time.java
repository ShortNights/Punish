package me.punishment;


public enum Time {
	SECOND("s", 0), MINUTE("m", 1), HOUR("h", 60), DAY("d", 1440), WEEK("w", 10080), MONTH("mon", 43200), YEAR("y",
			518400);

	public String name;
	public int multi;

	private Time(String n, int mult) {
		this.name = n;
		this.multi = mult;
	}

	public static long getTicks(String un, int time) {
		long s;
		try {
			s = time * 60;
		} catch (NumberFormatException ex) {
			return 0L;
		}
		for (Time unit : values()) {
			if (un.startsWith(unit.name)) {
				return s *= unit.multi * 1000L;
			}
		}
		return 0L;
	}

	public static String getMSG(long time) {
		time /= 1000L;
		StringBuilder message = new StringBuilder();
		if (time >= 31104000L) {
			long years = time / 31104000L;
			time %= 31104000L;
			message.append(years + " Year(s)");
		}
		if (time >= 2592000L) {
			long months = time / 2592000L;
			time %= 2592000L;
			message.append(" " + months + " Month(s)");
		}
		if (time >= 604800L) {
			long weeks = time / 604800L;
			time %= 604800L;
			message.append(" " + weeks + " Weeks(s)");
		}
		if (time >= 86400L) {
			long days = time / 86400L;
			time %= 86400L;
			message.append(" " + days + " Day(s)");
		}
		if (time >= 3600L) {
			long hours = time / 3600L;
			time %= 3600L;
			message.append(" " + hours + " Hour(s)");
		}
		if (time >= 60L) {
			long mins = time / 60L;
			time %= 60L;
			message.append(" " + mins + " Minute(s)");
		}
		if ((time < 60L) && (time != 0L)) {
			message.append(" " + time + " Second(s)");
		}
		return message.toString();
	}

	public String getName() {
		return this.name;
	}

	public int getMulti() {
		return this.multi;
	}
}