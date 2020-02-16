package com.mike_milk.bihu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mike_milk.bihu.R;
import com.mike_milk.bihu.db.User;
import com.mike_milk.bihu.util.HttpConnectUtil;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText Account;
    private EditText Password;
    private TextView Register;
    private Button Login;
    private String PasswordText;
    private String AccountText;
    private String Token;
    private String LoginInfo;
    private User User;
    private String Token1;
    private String Avatar;
    private String UserName;
    public static final String TAG="login";
    private JSONObject object;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
        initData();
        initClickListener();
    }

    private void initData(){
        Intent intent=getIntent();
        String accountValue=intent.getStringExtra("accountKey");
        String passwordValue=intent.getStringExtra("password");
        Account.setText(accountValue);
        Password.setText(passwordValue);
    }

    private void initClickListener(){
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordText=Password.getText().toString();
                AccountText=Account.getText().toString();
                if (PasswordText.isEmpty()){
                    Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else if (AccountText.isEmpty()){
                    Toast.makeText(LoginActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    login();
                }
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(){
        String url="http://bihu.jay86.com/login.php";
        String param="username="+AccountText+"&password="+PasswordText;
        HttpConnectUtil.doAsyncPost(url, param, new HttpConnectUtil.CallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"登录返回的response"+response);
                Token1=parseJsonData(response);
                Log.d(TAG,"登录时获得的token"+Token1);
                parseInfoAndAvatar(response);
                User=new User();
                User.setToken(Token1);
                User.setAvatar(Avatar);
                User.setUserName(UserName);
                judeData();
            }
        });
    }
    private void judeData(){
        if("success".equals(LoginInfo)){
            skipToMainActivity();
        }else {
            Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void parseInfoAndAvatar(String jsonData){
        try {
            object=new JSONObject(jsonData);
            LoginInfo=object.getString("info");
            JSONObject data=object.getJSONObject("data");
            Avatar=data.getString("avatar");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void skipToMainActivity(){
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("user",User);
        startActivity(intent);
        finish();
    }

    private String parseJsonData(String jsonData){
        try {
            JSONObject jsonObject=new JSONObject(jsonData);
            if ("200".equals(jsonObject.getString("status"))){
                JSONObject data=jsonObject.getJSONObject("data");
                Token=data.getString("token");
                UserName=data.getString("username");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Token;
    }

    private void initView(){
        Account=this.findViewById(R.id.login_account);
        Password=this.findViewById(R.id.login_password);
        Login=this.findViewById(R.id.btn_login);
        Register=this.findViewById(R.id.login_register);
    }
}

