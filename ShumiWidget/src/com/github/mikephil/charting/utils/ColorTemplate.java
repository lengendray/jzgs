package com.github.mikephil.charting.utils;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;

/**
 * Class that holds predefined color arrays (e.g.
 * ColorTemplate.VORDIPLOM_COLORS) and convenience methods for loading colors
 * from resources.
 * 
 * @author Philipp Jahoda
 */
public class ColorTemplate {

	/**
	 * THE COLOR THEMES ARE PREDEFINED, FEEL FREE TO CREATE YOUR OWN WITH AS
	 * MANY DIFFERENT COLORS AS YOU WANT
	 */

	public static final int[] FRESH_COLORS = { Color.parseColor("#FFFFB3"),
			Color.parseColor("#94FF85"), Color.parseColor("#75FFC2"),
			Color.parseColor("#8FDEFF"), Color.parseColor("#B3B3FF") };
	public static final int[] MONO_COLORS = { Color.parseColor("#042D42"),
			Color.parseColor("#0B84C2"), Color.parseColor("#075075"),
			Color.parseColor("#57BEF2"), Color.parseColor("#326E8C") };
	public static final int[] LIBERTY_COLORS = { Color.parseColor("#CFF8F6"),
			Color.parseColor("#94D4D4"), Color.parseColor("#88B4BB"),
			Color.parseColor("#76AEAF"), Color.parseColor("#2A6D82") };
	public static final int[] COLORFUL_COLORS = { Color.parseColor("#C12552"),
			Color.parseColor("#FF6600"), Color.parseColor("#F5C700"),
			Color.parseColor("#6A961F"), Color.parseColor("#008885") };
	public static final int[] GREEN_COLORS = { Color.parseColor("#55FC70"),
			Color.parseColor("#62E24C"), Color.parseColor("#ACF960"),
			Color.parseColor("#CBE24C"), Color.parseColor("#FCF355") };
	public static final int[] JOYFUL_COLORS = { Color.parseColor("#D9508A"),
			Color.parseColor("#FE9507"), Color.parseColor("#FEF778"),
			Color.parseColor("#6AA786"), Color.parseColor("#35C2D1") };
	public static final int[] PASTEL_COLORS = { Color.parseColor("#405980"),
			Color.parseColor("#95A57C"), Color.parseColor("#D9B8A2"),
			Color.parseColor("#BF8686"), Color.parseColor("#B33050") };
	public static final int[] VORDIPLOM_COLORS = { Color.parseColor("#C0FF8C"),
			Color.parseColor("#FFF78C"), Color.parseColor("#FFD08C"),
			Color.parseColor("#8CEAFF"), Color.parseColor("#FF8C9D") };

	/**
	 * turn an array of resource-colors into an arraylist of actual color values
	 * 
	 * @param c
	 * @param colors
	 *            e.g. ColorTemplate.MONO_COLORS
	 * @return
	 */
	public static ArrayList<Integer> createColors(Context c, int[] colors) {

		ArrayList<Integer> result = new ArrayList<Integer>();

		for (int i : colors) {
			result.add(c.getResources().getColor(i));
		}

		return result;
	}

	/**
	 * Turns an array of colors (already converted with
	 * getResources().getColor(...)) into an ArrayList of colors.
	 * 
	 * @param colors
	 * @return
	 */
	public static ArrayList<Integer> createColors(int[] colors) {

		ArrayList<Integer> result = new ArrayList<Integer>();

		for (int i : colors) {
			result.add(i);
		}

		return result;
	}

}
