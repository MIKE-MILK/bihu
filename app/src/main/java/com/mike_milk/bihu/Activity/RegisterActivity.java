package com.mike_milk.bihu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mike_milk.bihu.R;
import com.mike_milk.bihu.util.HttpConnectUtil;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText Acccount;
    private EditText Password;
    private Button RegisterBtn;
    private ImageView Back;
    private String AccountText;
    private String PasswordText;
    private String Url;
    private String Param;
    private String JsonDataValue;
    private String RegisterInfo;
    private JSONObject object=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
        initView();
        initOnClickListener();
    }
    private void initOnClickListener(){
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountText=Acccount.getText().toString();
                PasswordText=Password.getText().toString();
                if (PasswordText.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else if (AccountText.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    register();
                }
            }
        });
    }

    private void register(){
        Url="http://bihu.jay86.com/register.php";
        Param="username="+AccountText+"&password="+PasswordText;
        HttpConnectUtil.doAsyncPost(Url,Param,new HttpConnectUtil.CallBack(){
            @Override
            public void onResponse(String response) {
                JsonDataValue=response;
                parseJsonData(JsonDataValue);
                judgeData();
            }
        });
    }

    private void judgeData(){
        if ("success".equals(RegisterInfo)){
            skipToLoginActivity();
        }else {
            Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJsonData(String jsonData){
        try {
            object=new JSONObject(jsonData);
            RegisterInfo=object.getString("info");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void skipToLoginActivity(){
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        intent.putExtra("accountKey",AccountText);
        intent.putExtra("password",PasswordText);
        startActivity(intent);
        finish();
    }

    private void initView(){
        Back=this.findViewById(R.id.register_back);
        Acccount=this.findViewById(R.id.register_account);
        Password=this.findViewById(R.id.register_password);
        RegisterBtn=this.findViewById(R.id.btn_register);
    }

    public void back(View view){
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
