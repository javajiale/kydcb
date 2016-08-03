package com.fangjl.vocabulary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

/**
 * Created by sjj on 2016/3/23.
 */
public class WelComeActivity extends Activity {

    private EditText ed_name;
    private EditText ed_pwd;
    private Button bt;

    @Override
    protected void onCreate(Bundle saveInstanceState) {


        DBHelper helper = new DBHelper(this,"vocabulary.db",null,1);
        try {
            helper.deleteDatabase(this);
            helper.createDatabase(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onCreate(saveInstanceState);
        setContentView(R.layout.welcome);

        ed_name = (EditText)findViewById(R.id.username_edit);
        ed_name.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        ed_pwd = (EditText)findViewById(R.id.password_edit);
        ed_pwd.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        bt = (Button)findViewById(R.id.signin_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = ed_name.getText().toString();
                String pwd = ed_pwd.getText().toString();
                String turePwd = getPwd(userName);
                if(turePwd.equals(pwd)) {
                    startActivity(new Intent(WelComeActivity.this, MainActivity.class));
                    WelComeActivity.this.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                }
                else
                    ed_pwd.setText("账号密码不匹配");
            }
        });
    }

    private String getPwd(String userName){
        DBHelper dbHelper = new DBHelper(this,"vocabulary.db",null,1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user",new String[]{"pwd"},"userId="+userName,null,null,null,null,null);
        while(cursor.moveToNext()) {
            String pwd = cursor.getString(0);
            return pwd;
        }
        return null;
    }
}
