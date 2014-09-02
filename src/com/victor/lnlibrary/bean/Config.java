package com.victor.lnlibrary.bean;

public class Config {
	private static boolean nightmode = false;
	private static float fontsize = 18.0f;
	private static float linespace = 1.2f;
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
	
}
