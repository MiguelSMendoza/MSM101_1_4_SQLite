package es.netrunners.sqlite.db;

public class UsersTable implements UsersColumns {

	public final static String TABLE_NAME = "users";

	public final static String[] COLS = { UsersColumns._ID, UsersColumns.NAME,
			UsersColumns.SURNAME };

	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ NAME + " TEXT NOT NULL, " + SURNAME + " TEXT" + ");";

}
