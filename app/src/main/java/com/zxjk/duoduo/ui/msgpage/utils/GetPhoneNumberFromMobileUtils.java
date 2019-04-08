package com.zxjk.duoduo.ui.msgpage.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;

import com.zxjk.duoduo.bean.PhoneInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.zxjk.duoduo.utils.PermissionUtils.constantPermission;


public class GetPhoneNumberFromMobileUtils {
    private List<PhoneInfo> list;

    public List<PhoneInfo> getPhoneNumberFromMobile(Context context) {
        // TODO Auto-generated constructor stub
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_CONTACTS},1001);
            }
        }else {
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.READ_CONTACTS},1001);
            }
        }else {
        }

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
