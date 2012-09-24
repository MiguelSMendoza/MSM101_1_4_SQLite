package es.netrunners.sqlite;

import es.netrunners.sqlite.db.DBHelper;
import es.netrunners.sqlite.db.UsersColumns;
import es.netrunners.sqlite.db.UsersTable;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Binder;
import android.os.IBinder;

public class DBAdapter extends Service {

	private final IBinder mBinder = new LocalBinder();

	private DBHelper dbHelper;

	private SQLiteDatabase db;

	public class LocalBinder extends Binder {
		DBAdapter getService() {
			return DBAdapter.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		dbHelper = new DBHelper(this);
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
	}

	@Override
	public void onDestroy() {
		db.close();
	}

	public Cursor getCursorItem(long _rowIndex) throws SQLException {
		Cursor result = db.query(true, UsersTable.TABLE_NAME, UsersTable.COLS,
				UsersColumns._ID + "=" + _rowIndex, null, null, null, null,
				null);
		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("No items found for row: " + _rowIndex);
		}
		return result;
	}

	public Cursor getAllUsers() {
		return db.query(UsersTable.TABLE_NAME, UsersTable.COLS, null, null,
				null, null, UsersColumns._ID);
	}

	public boolean insertUser(String Name, String Subname) {
		// Create a new row of values to insert.
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put(UsersColumns.NAME, Name);
		newValues.put(UsersColumns.SURNAME, Subname);
		// Insert the row.
		long i = db.insert(UsersTable.TABLE_NAME, null, newValues);
		return i > 0;
	}

	public boolean updateUser(long _rowIndex, String name, String surname) {
		ContentValues newValues = new ContentValues();
		newValues.put(UsersColumns.NAME, name);
		newValues.put(UsersColumns.SURNAME, surname);
		long i = db.update(UsersTable.TABLE_NAME, newValues, UsersColumns._ID
				+ "=" + _rowIndex, null);
		return i > 0;
	}

	public boolean deleteUser(long _rowIndex) {
		long i = db.delete(UsersTable.TABLE_NAME, UsersColumns._ID + "="
				+ _rowIndex, null);
		return i > 0;
	}

}
