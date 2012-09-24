package es.netrunners.sqlite;

import es.netrunners.sqlite.DBAdapter.LocalBinder;
import es.netrunners.sqlite.db.UsersColumns;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

public class SQLiteActivity extends ListActivity {

	DBAdapter dbAdapter;
	Boolean mBound;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	private void loadList() {
		final String[] from = new String[] { UsersColumns.NAME,
				UsersColumns.SURNAME };
		final int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
		Cursor cursor = dbAdapter.getAllUsers();
		startManagingCursor(cursor);
		final SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_expandable_list_item_2, cursor, from,
				to);
		setListAdapter(cursorAdapter);
	}

	public void insertUser(View v) {
		EditText name = (EditText) findViewById(R.id.name);
		EditText surname = (EditText) findViewById(R.id.subname);
		dbAdapter.insertUser(name.getText().toString(), surname.getText()
				.toString());
		loadList();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Bind to LocalService
		Intent intent = new Intent(this, DBAdapter.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Unbind from the service
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			LocalBinder binder = (LocalBinder) service;
			dbAdapter = binder.getService();
			mBound = true;
			loadList();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};
}