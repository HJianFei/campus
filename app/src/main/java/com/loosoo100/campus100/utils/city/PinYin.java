package com.loosoo100.campus100.utils.city;

import java.util.ArrayList;

import android.widget.Toast;

import com.loosoo100.campus100.utils.city.HanziToPinyin3.Token;


public class PinYin {
	// 返回首字母并转化为大写
	public static String getPinYin(String input) {
		ArrayList<Token> tokens = HanziToPinyin3.getInstance().get(input);
//		StringBuilder sb = new StringBuilder();
//		if (tokens != null && tokens.size() > 0) {
//			for (Token token : tokens) {
//				if (Token.PINYIN == token.type) {
//					sb.append(token.target);
//				} else {
//					sb.append(token.source);
//				}
//			}
//		}
		return tokens.get(0).target.toString().substring(0, 1).toUpperCase();
	}
}
