package com.itheima.mobilesafe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.itheima.mobilesafe.adapter.MyGridViewAdapter;
import com.itheima.mobilesafe.utils.Encryption;

public class HomeActivity extends Activity implements View.OnClickListener {

    private SharedPreferences sp;
    private GridView list_home;
    private MyGridViewAdapter adapter;
    private static String[] names = {
            "手机防盗", "通讯卫士", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"

    };

    private static int[] ids = {
            R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
            R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
            R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        list_home = (GridView) findViewById(R.id.list_home);
        adapter = new MyGridViewAdapter(this, names, ids);
        list_home.setAdapter(adapter);
        list_home.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0://进入手机防盗
                        showAntiTheftDialog();
                        break;
                    case 8://进入设置中心
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);

                        break;

                    default:
                        break;
                }

            }
        });
    }

    private void showAntiTheftDialog() {
        if (isSetupPwd())
            showPwdDialog();
        else
            showSetupPwdDialog();
    }

    private EditText et_setup_pwd, et_confirm_pwd, et_type_pwd;
    private Button bt_ok, bt_cancel;
    private Dialog alertDialog;

    /**
     * 设置密码对话框
     */
    private void showSetupPwdDialog() {
        alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_setup_password);
        //设置dialog背景透明
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        et_setup_pwd = (EditText) alertDialog.findViewById(R.id.et_setup_pwd);
        et_confirm_pwd = (EditText) alertDialog.findViewById(R.id.et_confirm_pwd);
        bt_ok = (Button) alertDialog.findViewById(R.id.bt_setup_ok);
        bt_ok.setOnClickListener(this);
        bt_cancel = (Button) alertDialog.findViewById(R.id.bt_setup_cancel);
        bt_cancel.setOnClickListener(this);
    }

    /**
     * 输入密码对话框
     */
    private void showPwdDialog() {
        alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_type_password);
        //设置dialog背景透明
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        et_type_pwd = (EditText) alertDialog.findViewById(R.id.et_type_pwd);
        bt_ok = (Button) alertDialog.findViewById(R.id.bt_type_ok);
        bt_ok.setOnClickListener(this);
        bt_cancel = (Button) alertDialog.findViewById(R.id.bt_type_cancel);
        bt_cancel.setOnClickListener(this);
    }

    /**
     * 判断是否输入过密码
     *
     * @return
     */
    private boolean isSetupPwd() {
        String pwd = sp.getString("password", null);
        return !TextUtils.isEmpty(pwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_setup_ok:

                String password = et_setup_pwd.getText().toString().trim();
                String confirmingPassword = et_confirm_pwd.getText().toString().trim();

                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmingPassword)) {
                    Toast.makeText(this, "密码不得为空", Toast.LENGTH_LONG).show();
                    return;
                }

                if (password.equals(confirmingPassword)) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("password", Encryption.doMd5(password));
                    editor.commit();
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(this, "密码不一致", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case R.id.bt_setup_cancel:
                alertDialog.dismiss();
                break;

            case R.id.bt_type_ok:
                String input = et_type_pwd.getText().toString().trim();
                String savedPassword = sp.getString("password", "");
                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(this, "密码不得为空", Toast.LENGTH_LONG).show();
                    return;
                }

                if (Encryption.doMd5(input).equals(savedPassword)) {
                    alertDialog.dismiss();

                } else {
                    Toast.makeText(this, "密码错误", Toast.LENGTH_LONG).show();
                    return;
                }

                break;

            case R.id.bt_type_cancel:
                alertDialog.dismiss();
                break;
        }
    }
}
