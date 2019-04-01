package com.zxjk.duoduo.ui.msgpage.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.zxjk.duoduo.bean.PhoneInfo;

import java.util.ArrayList;
import java.util.List;

import static com.zxjk.duoduo.utils.PermissionUtils.havaReadContacts;
import static com.zxjk.duoduo.utils.PermissionUtils.havaWriteContacts;

public class GetPhoneNumberFromMobileUtils {
    private List<PhoneInfo> list;

    public List<PhoneInfo> getPhoneNumberFromMobile(Context context) {
        // TODO Auto-generated constructor stub
        havaWriteContacts(context);
        havaReadContacts(context);
        list = new ArrayList<PhoneInfo>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        //moveToNext方法返回的是一个boolean类型的数据
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            PhoneInfo phoneInfo = new PhoneInfo(name, number);
            list.add(phoneInfo);
        }
        return list;
    }

}