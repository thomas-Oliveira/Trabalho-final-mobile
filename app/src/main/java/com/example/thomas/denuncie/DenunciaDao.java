package com.example.thomas.denuncie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DenunciaDao {

    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

    public DenunciaDao(Context context) {
        this.helper = new DatabaseHelper(context);
    }

    public List<Map<String, Object>> listarDenuncias() {
        db = helper.getReadableDatabase();
        cursor = db.query(DatabaseHelper.Denuncia.TABELA,
                DatabaseHelper.Denuncia.COLUNAS,
                null, null, null, null, null);

        List<Map<String, Object>> denuncias = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Denuncia._ID));
            String descricao = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Denuncia.DESCRICAO));
            String categoria = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Denuncia.CATEGORIA));
            String urimidia = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Denuncia.URIMIDIA));
            String usuario = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Denuncia.USUARIO));
            Double longitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Denuncia.LONGITUDE));
            Double latitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Denuncia.LATITUDE));
            Date data = new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Denuncia.DATA)));
            Map<String, Object> denuncia = new HashMap<>();
            denuncia.put(DatabaseHelper.Denuncia._ID, id);
            denuncia.put(DatabaseHelper.Denuncia.DESCRICAO, descricao);
            denuncia.put(DatabaseHelper.Denuncia.CATEGORIA, categoria);
            denuncia.put(DatabaseHelper.Denuncia.URIMIDIA, urimidia);
            denuncia.put(DatabaseHelper.Denuncia.USUARIO, usuario);
            denuncia.put(DatabaseHelper.Denuncia.LONGITUDE, longitude);
            denuncia.put(DatabaseHelper.Denuncia.LATITUDE, latitude);
            denuncia.put(DatabaseHelper.Denuncia.DATA, fmt.format(data));
            denuncias.add(denuncia);
        }
        cursor.close();
        return denuncias;
    }


    public Denuncia buscarDenunciaPorId(Integer id) {
        db = helper.getReadableDatabase();
        cursor = db.query(DatabaseHelper.Denuncia.TABELA,
                DatabaseHelper.Denuncia.COLUNAS,
                DatabaseHelper.Denuncia._ID + " = ?",
                new String[]{id.toString()}, null, null, null);

        if (cursor.moveToNext()) {
            Denuncia denuncia = criarDenuncia(cursor);
            cursor.close();
            return denuncia;
        }
        return null;
    }

    public long inserirDenuncia(Denuncia d) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Denuncia.DESCRICAO, d.getDescricao());
        values.put(DatabaseHelper.Denuncia.CATEGORIA, d.getCategoria().toString());
        values.put(DatabaseHelper.Denuncia.LATITUDE, d.getLatitude().toString());
        values.put(DatabaseHelper.Denuncia.LONGITUDE, d.getLongitude().toString());
        values.put(DatabaseHelper.Denuncia.DATA, d.getData().getTime());
        values.put(DatabaseHelper.Denuncia.URIMIDIA, d.getUriMidia());
        values.put(DatabaseHelper.Denuncia.USUARIO, d.getUsuario());

        db = helper.getWritableDatabase();
        long qtdInseridos = db.insert(DatabaseHelper.Denuncia.TABELA, null, values);
        return qtdInseridos;
    }

    public long atualizarDenuncia(Denuncia d) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Denuncia.DESCRICAO, d.getDescricao());
        values.put(DatabaseHelper.Denuncia.CATEGORIA, d.getCategoria().toString());
        values.put(DatabaseHelper.Denuncia.LATITUDE, d.getLatitude().toString());
        values.put(DatabaseHelper.Denuncia.LONGITUDE, d.getLongitude().toString());
        values.put(DatabaseHelper.Denuncia.DATA, d.getData().getTime());
        values.put(DatabaseHelper.Denuncia.URIMIDIA, d.getUriMidia());
        values.put(DatabaseHelper.Denuncia.USUARIO, d.getUsuario());

        db = helper.getWritableDatabase();
        long qtdAtualizados = db.update(DatabaseHelper.Denuncia.TABELA,
                values,
                DatabaseHelper.Denuncia._ID + " = ?",
                new String[]{d.getId().toString()});
        return qtdAtualizados;
    }

    public boolean removerContato(Integer id) {
        db = helper.getWritableDatabase();
        String where[] = new String[]{id.toString()};
        int removidos = db.delete(DatabaseHelper.Denuncia.TABELA, "_id = ?", where);
        return removidos > 0;
    }

    private Denuncia criarDenuncia(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Denuncia._ID));
        String descricao = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Denuncia.DESCRICAO));
        String categoria = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Denuncia.CATEGORIA));
        String urimidia = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Denuncia.URIMIDIA));
        String usuario = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Denuncia.USUARIO));
        Double longitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Denuncia.LONGITUDE));
        Double latitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Denuncia.LATITUDE));
        Date data = new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Denuncia.DATA)));
        Denuncia d = new Denuncia(id,descricao,data,longitude,latitude,urimidia,usuario,categoria);
        return d;
    }

    public void close() {
        helper.close();
        db = null;
    }

}