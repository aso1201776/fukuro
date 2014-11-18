package com.android.fukuro;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class Mylist extends Activity implements OnItemClickListener {
	// 要素をArrayListで設定
	private List<String> imgList = new ArrayList<String>();
	private List<Integer> filename = new ArrayList<Integer>();
	private List<String> favoList = new ArrayList<String>();
	private DBHelper dbHelper = new DBHelper(this);
	public static SQLiteDatabase db;

	GridView gridview;
	GridAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylist);

		db = dbHelper.getReadableDatabase();


	}

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();

		imgList = new ArrayList<String>();
		filename = new ArrayList<Integer>();
		favoList = new ArrayList<String>();


		// GridViewのインスタンスを生成
				gridview = (GridView) findViewById(R.id.gridview);
				// BaseAdapter を継承したGridAdapterのインスタンスを生成
				// 子要素のレイアウトファイル grid_items.xml を main.xml に inflate するためにGridAdapterに引数として渡す
				adapter = new GridAdapter(this.getApplicationContext(), R.layout.grid_item, imgList);
				// gridViewにadapterをセット
				gridview.setAdapter(adapter);
				// gridviewにクリックリスナーセット
				gridview.setOnItemClickListener(this);

				// それのパスを取り出す method
				getImagePath();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 自動生成されたメソッド・スタブ
		Intent vIntent = null;

		if(position == 0){
			vIntent = new Intent(this, AddMylist.class);
		}else{
			vIntent = new Intent(this, MylistDetails.class);
			vIntent.putExtra("ID", filename.get(position));
		}
		startActivity(vIntent);

	}

	private void getImagePath() {  //プラス画像とDBに格納してある画像のパスをimgListに挿入
		String destPath = null;

		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(Mylist.this, R.id.gridview);

		Cursor cr = db.rawQuery("SELECT * FROM Mylist ORDER BY favorite DESC, maked DESC", null);
		cr.moveToFirst();

		//プラスボタン
		//fileList.add("plus");
		imgList.add("plus"); //リソースから画像をとるのでダミーのパスを指定
		filename.add(0,0);
		favoList.add("plus");
		//プラスボタン画像をfileListに挿入

		for(int cnt = 1; cnt <= cr.getCount(); cnt++){
			destPath = "/data/data/"+this.getPackageName()+"/Item/" + cr.getString(1);
			System.out.println(cr.getString(1));

			// List<String> imgList にはファイルのパスを入れる
			imgList.add(destPath);
			filename.add(cnt,cr.getInt(0));
			favoList.add(cr.getString(3));
			cr.moveToNext();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
		dbHelper.close();
	}

	class ViewHolder {
		ImageView imageView;
		ImageView favo;
		ImageView nStar;
	}

	class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int layoutId;

		public GridAdapter(Context context,  int layoutId, List<String> imgList) {
			super();
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.layoutId = layoutId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String mFilepath = imgList.get(position);
			String mFavo =favoList.get(position);
			Log.d("image","favo="+favoList);

			ViewHolder holder;
			if (convertView == null) {
				// main.xml の <GridView .../> に grid_items.xml を inflate して convertView とする
				convertView = inflater.inflate(layoutId, parent, false);
				// ViewHolder を生成
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
				holder.favo = (ImageView)convertView.findViewById(R.id.favo);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}

			Resources r = getResources();
			Bitmap bmp2 = BitmapFactory.decodeResource(r,R.drawable.no_star);
			Bitmap bmp3 = BitmapFactory.decodeResource(r,R.drawable.white);

			if(position != 0){  //プラスボタン以外の画像読み出し
				Bitmap bmp = BitmapFactory.decodeFile(mFilepath);
				bmp = Bitmap.createScaledBitmap(bmp, 120, 160, true);
				holder.imageView.setImageBitmap(bmp);
				if(mFavo.equals("true")){

				}else{
					holder.favo.setImageBitmap(bmp2);
				}

			}else{  //プラスボタンの画像読み出し
				Bitmap bmp = BitmapFactory.decodeResource(r,R.drawable.plus);
				bmp = Bitmap.createScaledBitmap(bmp, 120, 160, true);
				holder.imageView.setImageBitmap(bmp);

				holder.favo.setImageBitmap(bmp3);
			}

			return convertView;
		}

		@Override
		public int getCount() {
			// List<String> imgList の全要素数を返す
			return imgList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}
}
