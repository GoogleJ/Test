package com.zxjk.duoduo.ui.msgpage.utils;




import com.zxjk.duoduo.bean.UserBean;
import com.zxjk.duoduo.network.response.FriendListResponse;

import java.util.Comparator;

/**
 * @author Administrator
 *
 */
public class PinyinComparator implements Comparator<FriendListResponse> {

	@Override
	public int compare(FriendListResponse o1, FriendListResponse o2) {
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
