package com.android.fukuro;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MylistDetails extends Activity implements OnClickListener {

	private DBHelper dbHelper = new DBHelper(this);
	public static SQLiteDatabase db;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylist_details);

		db = dbHelper.getReadableDatabase();

		// 現在のintentを取得する
		Intent vIntent = getIntent();
		// intentから指定キーの文字列を取得する
		String id = vIntent.getStringExtra( "ID" );



	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
		dbHelper.close();
	}

}