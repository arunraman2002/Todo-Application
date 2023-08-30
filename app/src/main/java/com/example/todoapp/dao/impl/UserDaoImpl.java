package com.example.todoapp.dao.impl;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todoapp.dao.UserDao;
import com.example.todoapp.database.DataBaseHelper;
import com.example.todoapp.database.table.UserContract;
import com.example.todoapp.model.UserProfile;

public class UserDaoImpl implements UserDao {

    private SQLiteDatabase database;
    private final DataBaseHelper dataBaseHelper;

    public UserDaoImpl(final Context context) {
        this.dataBaseHelper = new DataBaseHelper(context);
    }

    @Override
    public void open() {
        database = dataBaseHelper.getWritableDatabase();
    }

    @Override
    public void close() {
        dataBaseHelper.close();
    }

    @Override
    public long insert(final UserProfile userProfile) {
        final ContentValues values = new ContentValues();

        values.put(UserContract.COLUMN_NAME, userProfile.getName());
        values.put(UserContract.COLUMN_DESCRIPTION, userProfile.getTitle());

        return database.insert(UserContract.TABLE_NAME, null, values);
    }

    @SuppressLint("Range")
    @Override
    public UserProfile getUserProfile() {
        final SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        UserProfile userProfile = null;
        final Cursor cursor = sqLiteDatabase.query(UserContract.TABLE_NAME, null,
                null, null, null, null, null);

        if (null != cursor && cursor.moveToFirst()) {
            userProfile = new UserProfile();

            userProfile.setId(cursor.getLong(cursor.getColumnIndex(UserContract.COLUMN_ID)));
            userProfile.setName(cursor.getString(cursor.getColumnIndex(UserContract.COLUMN_NAME)));
            userProfile.setTitle(cursor.getString(cursor.getColumnIndex(UserContract
                    .COLUMN_DESCRIPTION)));
            cursor.close();
        }

        return userProfile;
    }

    @Override
    public long update(final UserProfile userProfile) {
        final ContentValues values = new ContentValues();

        values.put(UserContract.COLUMN_NAME, userProfile.getName());
        values.put(UserContract.COLUMN_DESCRIPTION, userProfile.getTitle());

        return database.update(UserContract.TABLE_NAME, values, String.format("%s = ?",
                UserContract.COLUMN_ID), new String[] {String.valueOf(userProfile.getId())});
    }
}
