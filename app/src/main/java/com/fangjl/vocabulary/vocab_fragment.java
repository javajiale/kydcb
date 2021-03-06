package com.fangjl.vocabulary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

/**
 * Created by javajiale on 3/08/2016.
 */
public class vocab_fragment  extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<String> mWordList;
    private SwipeMenuListView mListView;
    private AppAdapter mAdapter;
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vocabulary, null);
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
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
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
                        // delete
//					    delete(item);
//						mWordList.remove(position);
//						mAdapter.notifyDataSetChanged();
//						mListView.smoothScrollToPosition(getPosition());
                        del(item);
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



        mSwipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.id_swipe_ly);

        mSwipeLayout.setOnRefreshListener(this);

    }

    private void del(String item){
        DBHelper dbHelper = new DBHelper(getActivity(),"vocabulary.db",null,1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("delete from vocab where word = '"+item+"'");
    }
    private List<String> getData(){

        List<String> data = new ArrayList<String>();

        DBHelper dbHelper = new DBHelper(getActivity(),"vocabulary.db",null,1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("vocab",new String[]{"word"}, null, null, null, null, null);

        if(cursor.moveToNext()==false){
            db.close();
            return data;
        }
        do {
            String word = cursor.getString(0);
            data.add(word);
        }while(cursor.moveToNext());

        db.close();
        return data;
    }


    @Override
    public void onRefresh() {
        mWordList = getData();
        mAdapter.notifyDataSetChanged();
        mSwipeLayout.setRefreshing(false);
    }


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