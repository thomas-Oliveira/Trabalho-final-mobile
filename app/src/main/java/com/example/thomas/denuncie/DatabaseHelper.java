package com.example.thomas.denuncie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "denuncia";
    private static final int VERSAO = 1;

    public static class Denuncia {
        public static final String TABELA = "Denuncia";
        public static final String _ID = "_id";
        public static final String DESCRICAO = "descricao";
        public static final String DATA = "data";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String URIMIDIA = "urimidia";
        public static final String CATEGORIA = "categoria";
        public static final String USUARIO = "usuario";

        public static final String[] COLUNAS =
                new String[]{_ID,DESCRICAO,DATA,LONGITUDE,LATITUDE,URIMIDIA,CATEGORIA,USUARIO};
    }
    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE denuncia (_id INTEGER PRIMARY KEY," +
                "descricao TEXT, longitude NUMERIC, latitude NUMERIC," +
                "urimidia TEXT, categoria ENUM, usuario TEXT, data DATE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE denuncia;");
        onCreate(db);
    }

}