package com.loosoo100.campus100.chat.sortlistview;

import com.loosoo100.campus100.zzboss.chat.fragment.SortFollows;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparators implements Comparator<SortFollows> {

	public int compare(SortFollows o1, SortFollows o2) {
		if (o1.getLetters().equals("@")
				|| o2.getLetters().equals("#")) {
			return -1;
		} else if (o1.getLetters().equals("#")
				|| o2.getLetters().equals("@")) {
			return 1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}
