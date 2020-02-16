package com.mike_milk.bihu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ChangePasswordActivity extends AppCompatActivity {
    private TextView ChangeBack;
    private EditText password;
    private Button changBtn;
    private String info;
    private String token;
    private String status;
    private Intent intent;
    private User user;
    private String Url;
    private String param;
    private String NewPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        initview();
        setClick();
    }

    private void setClick(){
        ChangeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        changBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=getIntent();
                user=intent.getParcelableExtra("user");
                String token1=user.getToken();
                Url = "http://bihu.jay86.com/changePassword.php";
                NewPassword=password.getText().toString();
                Log.d("lll","www"+NewPassword);
                param="password="+NewPassword+"&token="+token1;
                HttpConnectUtil.doAsyncPost(Url, param, new HttpConnectUtil.CallBack() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object=new JSONObject(response);
                            status=object.getString("status");
                            info=object.getString("info");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if ("200".equals(status)){
                            HttpConnectUtil.doAsyncPost("http://bihu.jay86.com/login.php", "username=" + user.getUserName() + "&password=" + NewPassword, new HttpConnectUtil.CallBack() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject object=new JSONObject(response);
                                        JSONObject jsonObject=object.getJSONObject("data");
                                        token=jsonObject.getString("token");
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    user.setToken(token);
                                    Intent intent=new Intent(ChangePasswordActivity.this,LoginActivity.class);
                                    intent.putExtra("user",user);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(ChangePasswordActivity.this,"密码更改成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            Toast.makeText(ChangePasswordActivity.this,"info",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void initview(){
        password=findViewById(R.id.change_password_et);
        ChangeBack=findViewById(R.id.change_password_back);
        /*
        名字对应错误
         */
        changBtn=findViewById(R.id.change_password_btn);
    }
}
