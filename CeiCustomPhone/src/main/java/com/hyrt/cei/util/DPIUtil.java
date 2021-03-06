package com.hyrt.cei.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import com.hyrt.cei.application.CeiApplication;

public class DPIUtil {

	private static Display defaultDisplay;
	private static float mDensity;
	private static float screenSize = -1.0F;

	public DPIUtil() {
	}

	public static int dip2px(float f) {
		return (int) (0.5F + f * mDensity);
	}

	public static Display getDefaultDisplay() {
		if (defaultDisplay == null)
			defaultDisplay = ((WindowManager)CeiApplication.getInstance().getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();
		return defaultDisplay;
	}

	public static boolean isBigScreen() {
		if (getSizeOfScreen() < 7.5D)
			return false;
		return true;
	}

	public static float getSizeOfScreen() {
		if (-1.0F == screenSize) {
			int i = getWidth();
			int j = getHeight();
			screenSize = (float) (Math.sqrt(i * i + j * j) / (160.0F * getDensity()));
		}
		return screenSize;
	}

	public static float getDensity() {
		return mDensity;
	}

	public static int getHeight() {
		return getDefaultDisplay().getHeight();
	}

	public static int getWidth() {
		return getDefaultDisplay().getWidth();
	}

	public static int percentHeight(float f) {
		return (int) (f * (float) getHeight());
	}

	public static int percentWidth(float f) {
		return (int) (f * (float) getWidth());
	}

	public static int px2dip(Context context, float f) {
		return (int) (0.5F + f / mDensity);
	}

	/**
	 * 应用启动时，需获取屏幕密度参数
	 * @param f
	 */
	public static void setDensity(float f) {
		mDensity = f;
	}
}
