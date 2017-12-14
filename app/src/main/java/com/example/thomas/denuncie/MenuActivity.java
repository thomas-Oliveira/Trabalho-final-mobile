package com.example.thomas.denuncie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        usuario = getIntent().getStringExtra("usuario");

    }

    public void selecionarOpcao(View view) {
        if (view.getId() == R.id.menu_cadastro) {
            Intent i = new Intent(this, CadastroActivity.class);
            i.putExtra("usuario", usuario);
            startActivity(i);
        }
        if (view.getId() == R.id.menu_Listar_denuncia) {
            startActivity(new Intent(this, ListarActivity.class));
        }
    }
}