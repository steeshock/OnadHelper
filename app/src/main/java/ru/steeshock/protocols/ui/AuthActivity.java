package ru.steeshock.protocols.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ru.steeshock.protocols.R;

public class AuthActivity extends AppCompatActivity {


    public static final String TAG = "myLog";
    private Button btnLogin;
    private EditText etLogin, etPassword;
    public static final String LOGIN = "onad";
    public static final String PASSWORD = "12345";
    public static final String TOKEN_STRING = "abcde12345";
    public JSONObject token = new JSONObject();
    public static JSONObject response = new JSONObject();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization);

        btnLogin = findViewById(R.id.btn_login);
        etLogin = findViewById(R.id.et_login);
        etPassword = findViewById(R.id.et_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFieldsIsEmpty()){
                    if (authorization()){
                        getResponseFromServer(token);
                        Intent openMainActivity = new Intent(AuthActivity.this, MainActivity.class);
                        startActivity (openMainActivity);
                        finish();
                    } else Toast.makeText(AuthActivity.this, "Неверный логин или пароль!", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(AuthActivity.this, "Введите данные для входа!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean authorization() {
        if(etLogin.getText().toString().equals(LOGIN) && etPassword.getText().toString().equals(PASSWORD)){
            try{
                token.put("token", "abcde12345");
            } catch (JSONException e){
                //smth
            }
            return true;
        }
        return false;
    }

    private JSONObject getResponseFromServer (JSONObject jsonObject) {
        try {
            if (jsonObject.get("token").equals(TOKEN_STRING)){
                response.put("firstname", "Евгений");
                response.put("lastname", "Николаев");
                response.put("avatar", "");
                response.put("role", "Тестовый пользователь");
            }
        }catch (JSONException e){
            //smth
        }
        return response;
    }

    public boolean isFieldsIsEmpty() {

        boolean flag = false;

        if (TextUtils.isEmpty(etLogin.getText())) {
            etLogin.setError(getString(R.string.error));
            flag = true;
        }
        if (TextUtils.isEmpty(etPassword.getText()))  {
            etPassword.setError(getString(R.string.error));
            flag = true;
        }

        return flag;
    }
}
