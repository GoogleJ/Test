package com.zxjk.duoduo.ui.msg.utils;




import com.zxjk.duoduo.bean.UserBean;

import java.util.Comparator;

public class PinyinComparator implements Comparator<UserBean> {

	@Override
	public int compare(UserBean o1, UserBean o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}
}
