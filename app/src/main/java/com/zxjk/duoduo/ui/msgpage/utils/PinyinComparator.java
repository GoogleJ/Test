package com.zxjk.duoduo.ui.msgpage.utils;




import com.zxjk.duoduo.bean.response.FriendInfoResponse;

import java.util.Comparator;

/**
 * @author Administrator
 *
 */
public class PinyinComparator implements Comparator<FriendInfoResponse> {

	@Override
	public int compare(FriendInfoResponse o1, FriendInfoResponse o2) {
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
