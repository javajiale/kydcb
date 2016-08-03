package com.fangjl.vocabulary;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

public class vocabulary_fragment extends Fragment {
//	private List<ApplicationInfo> mAppList;
	private List<String> mWordList;
	private SwipeMenuListView mListView;
	private AppAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_listening_vocabulary, null);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	//	mAppList = getActivity().getPackageManager().getInstalledApplications(0);
		mWordList = getData();

		mListView = (SwipeMenuListView)getActivity().findViewById(R.id.listView);
		mAdapter = new AppAdapter();
	//	Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, getData(getArguments().getString("type")));
		mListView.setAdapter(mAdapter);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(
						getActivity().getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				//openItem.setTitle("Open");
				openItem.setIcon(R.drawable.ic_action_favorite);
				// set item title fontsize
				//openItem.setTitleSize(18);
				// set item title font color
				//openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getActivity().getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_action_important);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
		//		ApplicationInfo item = mAppList.get(position);
				String item = mWordList.get(position);
				switch (index) {
					case 0:
						// open
						String word = item.split("\\[")[0];
						Uri uri = Uri.parse("http://dict.so.163.com/w/"+word+"/#keyfrom=dict2.top");
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
					//	open(item);
						break;
					case 1:
						// delete
//					    delete(item);
						mWordList.remove(position);
						mAdapter.notifyDataSetChanged();
						break;
				}
			}
		});

		// set SwipeListener
		mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

		// test item long click
		mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
										   int position, long id) {
				Toast.makeText(getActivity().getApplicationContext(), position + " long click", 0).show();
				return false;
			}
		});
	}

	private List<String> getData(){

		List<String> data = new ArrayList<String>();

		DBHelper dbHelper = new DBHelper(getActivity(),"vocabulary.db",null,1);
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		Cursor cursor = db.query("word",new String[]{"word"}, null, null, null, null, null);

		while(cursor.moveToNext()) {
				String word = cursor.getString(0);
				data.add(word);
		}

		db.close();
		return data;
	}

	private void delete(ApplicationInfo item) {
		// delete app
		try {
			Intent intent = new Intent(Intent.ACTION_DELETE);
			intent.setData(Uri.fromParts("package", item.packageName, null));
			startActivity(intent);
		} catch (Exception e) {
		}
	}

//	private void open(ApplicationInfo item) {
//		// open app
//		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
//		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//		resolveIntent.setPackage(item.packageName);
//		List<ResolveInfo> resolveInfoList = getActivity().getPackageManager()
//				.queryIntentActivities(resolveIntent, 0);
//		if (resolveInfoList != null && resolveInfoList.size() > 0) {
//			ResolveInfo resolveInfo = resolveInfoList.get(0);
//			String activityPackageName = resolveInfo.activityInfo.packageName;
//			String className = resolveInfo.activityInfo.name;
//
//			Intent intent = new Intent(Intent.ACTION_MAIN);
//			intent.addCategory(Intent.CATEGORY_LAUNCHER);
//			ComponentName componentName = new ComponentName(
//					activityPackageName, className);
//
//			intent.setComponent(componentName);
//			startActivity(intent);
//		}
//	}

	class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mWordList.size();
		}

		@Override
		public String getItem(int position) {
			return mWordList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getActivity().getApplicationContext(),
						R.layout.item_list_app, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			String  item = getItem(position);
			holder.tv_name.setText(item);
			return convertView;
		}

		class ViewHolder {
			TextView tv_name;
			public ViewHolder(View view) {
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(this);
			}
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}

