package com.victor.lnlibrary.config;

public class Config {
	private static boolean nightmode = false;
	private static boolean awake = false;
	private static float fontsize = 18.0f;
	private static float linespace = 1.2f;
	private static int HorV = 0;
	
	public static boolean isNightmode() {
		return nightmode;
	}
	public static void setNightmode(boolean nightmode) {
		Config.nightmode = nightmode;
	}
	public static float getFontsize() {
		return fontsize;
	}
	public static void setFontsize(float fontsize) {
		Config.fontsize = fontsize;
	}
	public static float getLinespace() {
		return linespace;
	}
	public static void setLinespace(float linespace) {
		Config.linespace = linespace;
	}
	public static boolean isAwake() {
		return awake;
	}
	public static void setAwake(boolean awake) {
		Config.awake = awake;
	}
	public static int getHorV() {
		return HorV;
	}
	public static void setHorV(int horV) {
		HorV = horV;
	}
}
