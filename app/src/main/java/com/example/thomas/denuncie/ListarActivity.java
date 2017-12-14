package com.example.thomas.denuncie;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ListarActivity extends Activity implements SimpleAdapter.ViewBinder,
        AdapterView.OnItemClickListener,MenuDialogFragment.MenuClickListener {

    private SimpleAdapter adapter;
    private ListView listView;
    private DenunciaDao DenumDAO = new DenunciaDao(this);
    private List<Map<String, Object>> mapList;

    private String[] chave = {DatabaseHelper.Denuncia._ID,DatabaseHelper.Denuncia.DATA, DatabaseHelper.Denuncia.USUARIO, DatabaseHelper.Denuncia.CATEGORIA };
    private int[] valor = {R.id.itemlist_id,R.id.itemlist_data,R.id.itemlist_usuario,R.id.itemlist_categoria};



    @Override
    protected void onResume() {
        super.onResume();
        listView.refreshDrawableState();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

//        try {
//            Date data = new Date();
//            Denuncia d = new Denuncia(1,"d1", data,  -39.0188,  -4.97813,"../test/t","admin","teste");
//            DenumDAO.inserirDenuncia(d);
//        }catch (Exception e){
//            String msgErro = "Erro: " + e.getMessage();
//            Toast toast = Toast.makeText(this,msgErro,Toast.LENGTH_SHORT * 4);
//            toast.show();
//        }

        adapter = new SimpleAdapter(this, DenumDAO.listarDenuncias(), R.layout.item_lista, chave, valor);
        listView = findViewById(R.id.listar_listarDenuncia);
        adapter.setViewBinder(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


    }

    @Override
    public void onDenunciaItemClick(int IdDenuncia, int op) {
        switch (op) {
            case 0: {
                Intent i = new Intent(this, DenunciaActivity.class);
                i.putExtra("_id", IdDenuncia);
                startActivity(i);
                break;
            }
            case 1:{
                Intent i = new Intent(this, DenunciaEditarActivity.class);
                i.putExtra("_id", IdDenuncia);
                startActivity(i);
                break;
            }
            case 2:{
                DenumDAO.removerContato(IdDenuncia);
                String msg = "Denuncia Removida";
                Toast msgConfirm = Toast.makeText(this,msg,Toast.LENGTH_SHORT);
                msgConfirm.show();
                listView.setAdapter(new SimpleAdapter(this, DenumDAO.listarDenuncias(), R.layout.item_lista, chave, valor));
                break;
            }
        }
    }


    @Override
    public boolean setViewValue(View view, Object o, String s) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        MenuDialogFragment fragmento = new MenuDialogFragment();
        Map<String, Object> item = (DenumDAO.listarDenuncias()).get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", (int) item.get(DatabaseHelper.Denuncia._ID));
        fragmento.setArguments(bundle);
        fragmento.show(this.getFragmentManager(), "Opções");
    }
}