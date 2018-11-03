package ru.steeshock.protocols.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ru.steeshock.protocols.R;
import ru.steeshock.protocols.ui.Records.MainActivity;
import ru.steeshock.protocols.utils.UserSettings;

public class AuthActivity extends AppCompatActivity {

    private EditText etLogin, etPassword;
    private CheckBox mCheckBox;

    public static final String LOGIN []= {"onad015", "onad019", "onad017", "onad013", "onad001"};
    public static final String PASSWORD = "12345";
    public static final String TOKEN_STRING = "abcde12345";
    public JSONObject token = new JSONObject();
    public static JSONObject response = new JSONObject();

    private UserSettings mUserSettings;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization);

        getSupportActionBar().setTitle(R.string.auth);

        Button btnLogin = findViewById(R.id.btn_login);
        etLogin = findViewById(R.id.et_login);
        etPassword = findViewById(R.id.et_password);
        mCheckBox = findViewById(R.id.checkbox);

        mUserSettings = new UserSettings(getApplicationContext());//берем значение об уже авторизованном пользователе (нажал галочку Запомнить меня) из SharedPreferences
        UserSettings.SAVE_USER_AUTH_FLAG = mUserSettings.mSharedPreferences.getBoolean(UserSettings.SAVE_USER_AUTH_KEY, false);


        if (UserSettings.SAVE_USER_AUTH_FLAG) {
            Intent openMainActivity = new Intent(AuthActivity.this, MainActivity.class);
            startActivity (openMainActivity);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFieldsIsEmpty()){
                    if (authorization()){
                        getResponseFromServer(token);
                        UserSettings.SAVE_USER_AUTH_FLAG = mCheckBox.isChecked();
                        UserSettings.USER_TOKEN = etLogin.getText().toString();

                        mUserSettings.mSharedPreferences.edit().putBoolean(UserSettings.SAVE_USER_AUTH_KEY, UserSettings.SAVE_USER_AUTH_FLAG).apply();
                        mUserSettings.mSharedPreferences.edit().putString(UserSettings.USER_TOKEN_KEY,UserSettings.USER_TOKEN).apply();

                        Intent openMainActivity = new Intent(AuthActivity.this, MainActivity.class);
                        startActivity (openMainActivity);
                        finish();
                    } else Toast.makeText(AuthActivity.this, "Неверный логин или пароль!", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(AuthActivity.this, "Введите данные для входа!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean authorization() {
        for (String login:LOGIN) {
            if(etLogin.getText().toString().equals(login) && etPassword.getText().toString().equals(PASSWORD)){
                try{
                    token.put("token", "abcde12345");
                } catch (JSONException e){
                    //smth
                }
                return true;
            }
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
