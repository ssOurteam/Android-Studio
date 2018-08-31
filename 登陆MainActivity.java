package com.example.lenovo.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private UserDataManager mUserDataManager;    //数据库
    private EditText username;                    //用户名
    private EditText password;               //密码
    private Button login;                         //登录按钮
    private Button register;                     //新用户注册按钮
    private Button forget;
    private SharedPreferences login_sp;
    private CheckBox mRememberCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        register=(Button)findViewById(R.id.register);
        forget=(Button)findViewById(R.id.forget);
        mRememberCheck=(CheckBox)findViewById(R.id.Login_Remember);


        login_sp = getSharedPreferences("userInfo", 0);
        String name = login_sp.getString("USER_NAME", "");
        String pwd = login_sp.getString("PASSWORD", "");
        boolean choseRemember = login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin = login_sp.getBoolean("mAutologinCheck", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if (choseRemember) {
            username.setText(name);
            password.setText(pwd);
            mRememberCheck.setChecked(true);
        }

        //采用OnClickListener方法设置不同按钮按下之后的监听事件
        register.setOnClickListener(mListener);
        login.setOnClickListener(mListener);
        forget.setOnClickListener(mListener);

        //建立本地数据库
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
    }
    View.OnClickListener mListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register:                            //登录界面的注册按钮
                    Intent intent_Login_to_Register = new Intent(MainActivity.this, RegisterActivity.class);    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_Register);
                    finish();
                    break;
                case R.id.login:                              //登录界面的登录按钮
                    login();
                    break;
                case R.id.forget:                             //登录界面的更改密码按钮
                    Intent intent_Login_to_reset = new Intent(MainActivity.this, FunctionActivity.class);    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_reset);
                    finish();
                    break;
            }
        }
    };
    public void login() {                                              //登录按钮监听事件
        if (isUserNameAndPwdValid()) {
            String userName = username.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = password.getText().toString().trim();
            SharedPreferences.Editor editor = login_sp.edit();
            int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if (result == 1) {                                             //返回1说明用户名和密码均正确
                //保存用户名和密码
                editor.putString("USER_NAME", userName);
                editor.putString("PASSWORD", userPwd);

                //是否记住密码
                if (mRememberCheck.isChecked()) {
                    editor.putBoolean("mRememberCheck", true);
                } else {
                    editor.putBoolean("mRememberCheck", false);
                }
                editor.commit();

                Intent intent = new Intent(MainActivity.this,FunctionActivity.class);    //切换Login Activity至User Activity
                startActivity(intent);
                finish();
                Toast.makeText(this,"登陆成功", Toast.LENGTH_SHORT).show();//登录成功提示
            } else if (result == 0) {
                Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();  //登录失败提示
            }
        }
    }
    public boolean isUserNameAndPwdValid() {              //用户名密码是否有效
        if (username.getText().toString().trim().equals("")) {
            Toast.makeText(this,"用户名不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().trim().equals("")) {
            Toast.makeText(this, "密码不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



}


