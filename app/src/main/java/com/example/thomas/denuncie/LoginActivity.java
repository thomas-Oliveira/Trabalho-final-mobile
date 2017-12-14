package com.example.thomas.denuncie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.usuario = (EditText) findViewById(R.id.login_usuario);

    }

    public void login(View view){
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            intent.putExtra("usuario", usuario.getText().toString() );
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            startActivity(intent);
    }

}