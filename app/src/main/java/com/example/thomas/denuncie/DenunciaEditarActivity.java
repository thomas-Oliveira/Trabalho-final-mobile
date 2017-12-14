package com.example.thomas.denuncie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

public class DenunciaEditarActivity extends AppCompatActivity {

    private TextView id;
    private TextView urimidia;
    private Button Regravar;
    private Spinner categoria;
    private EditText Descricao;
    private Button salvar;
    private Denuncia denuncia_editada;
    private DenunciaDao DenumDAO = new DenunciaDao(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_editar);

        id =findViewById(R.id.denunciaEditar_id);
        urimidia = findViewById(R.id.denunciaEditar_urimidia);
        Regravar = findViewById(R.id.denunciaEditar_reenviar);
        categoria = findViewById(R.id.denunciaEditar_categoria);
        Descricao = findViewById(R.id.denunciaEditar_descricao);
        salvar = findViewById(R.id.denunciaEditar_salvar);

        categoria.setAdapter(ArrayAdapter.createFromResource(this, R.array.cadastro_spinnerCategoria, R.layout.layout_spinner));

        int id_item = getIntent().getIntExtra("_id", 0);
        denuncia_editada = DenumDAO.buscarDenunciaPorId(id_item);
    }


    public void Salvar(View view){
        denuncia_editada.setCategoria((String) categoria.getSelectedItem());
        denuncia_editada.setData(new Date());
        denuncia_editada.setDescricao(Descricao.getText().toString());
        denuncia_editada.setLatitude(-4.97813);
        denuncia_editada.setLongitude(-39.0188);

        DenumDAO.atualizarDenuncia(denuncia_editada);

        Intent i = new Intent(this, ListarActivity.class);
        String msg = "Denuncia Alterada!";
        Toast msgConfirm = Toast.makeText(this,msg,Toast.LENGTH_SHORT);
        msgConfirm.show();
        startActivity(i);
    }
}