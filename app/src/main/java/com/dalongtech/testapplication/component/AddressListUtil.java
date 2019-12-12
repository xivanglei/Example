package com.dalongtech.testapplication.component;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.dalongtech.testapplication.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xianglei
 * Date: 2019-09-05 17:51
 * Description:
 */
public class AddressListUtil {

    public static List<ContactBean> getContacts(Context context) {
        //联系人的Uri，也就是content://com.android.contacts/contacts
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //指定获取_id和display_name两列数据，display_name即为姓名
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        //根据Uri查询相应的ContentProvider，cursor为获取到的数据集
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        List<ContactBean> contacts = null;
        if (cursor != null && cursor.moveToFirst()) {
            contacts = new ArrayList<>();
            do {
                ContactBean contactBean = new ContactBean();
                Long id = cursor.getLong(0);
                //获取姓名
                String name = cursor.getString(1);
                //指定获取NUMBER这一列数据
                String[] phoneProjection = new String[] {
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                contactBean.setName(name);
                //根据联系人的ID获取此人的电话号码
                Cursor phonesCusor = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        phoneProjection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null,
                        null);

                //因为每个联系人可能有多个电话号码，所以需要遍历
                if (phonesCusor != null && phonesCusor.moveToFirst()) {
                    List<String> phones = new ArrayList<>();
                    do {
                        String num = phonesCusor.getString(0);
                        phones.add(num);
                    }while (phonesCusor.moveToNext());
                    contactBean.setPhones(phones);
                    phonesCusor.close();
                }
                contacts.add(contactBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contacts;
    }

}
