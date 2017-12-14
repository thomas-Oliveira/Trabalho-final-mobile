package com.example.thomas.denuncie;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.Console;
import java.io.File;
import java.util.Date;

public class CadastroActivity extends Activity {

    private Spinner spinner;
    private EditText descricao;
    private String usuario;
    private Denuncia d;
    private String uri_salvar;
    private DenunciaDao DenumDAO = new DenunciaDao(this);

    private static final int CAPTURAR_VIDEO = 1;
    private Uri uri;

    private VideoView videoView;
    private Boolean possuiCartaoSD = false;
    private Boolean dispositivoSuportaCartaoSD = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        d = new Denuncia();
        spinner = findViewById(R.id.cadastro_spinnerCategoria);
        descricao = findViewById(R.id.cadastro_edit);
        usuario = getIntent().getStringExtra("usuario");
        spinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.cadastro_spinnerCategoria, R.layout.layout_spinner));

        videoView = findViewById(R.id.video_videoView);
        possuiCartaoSD = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        dispositivoSuportaCartaoSD = Environment.isExternalStorageRemovable();

        setArquivoVideo();
    }

    public void image(View v){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);
    }

    public void video(View v){
        this.capturarVideo(v);
    }

    public void cadastrarDenuncia(View v){
        String msgErro = "Erro: " + usuario;
        Toast toast = Toast.makeText(this,msgErro, Toast.LENGTH_SHORT * 4);
        toast.show();

        d.setUsuario(usuario.toString());
        d.setCategoria(spinner.getSelectedItem().toString());
        d.setDescricao(descricao.getText().toString());
        d.setData(new Date());
        d.setLatitude(-4.97813);
        d.setLongitude(-39.0188);
        d.setUriMidia(uri.toString());

        DenumDAO.inserirDenuncia(d);

        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("usuario", usuario);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == CAPTURAR_VIDEO) {
            if (resultCode == RESULT_OK) {
                String msg = "Vídeo gravado em " + data.getDataString();
                uri = data.getData();
                uri = Uri.parse(uri.toString()+".mp4");
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Video não gravado!", Toast.LENGTH_LONG).show();
            }

            return;
        }

        if(data != null){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                Bitmap img = (Bitmap) bundle.get("data");

                ImageView iv = (ImageView) findViewById(R.id.cadastro_imageView);
                iv.setImageBitmap(img);

                uri = data.getData();
                uri = Uri.parse(uri.toString()+".jpg");
            }
        }
    }

    //Video
    private void iniciarGravacaoDeVideo() {
        try {
            setArquivoVideo();
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAPTURAR_VIDEO);
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao iniciar a câmera.", Toast.LENGTH_LONG).show();
        }
    }

    public void capturarVideo(View v) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            getPermissoes();
        } else {
            iniciarGravacaoDeVideo();
        }
    }

    private void getPermissoes() {
        String CAMERA = Manifest.permission.CAMERA;
        String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
        int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

        boolean permissaoCamera = ActivityCompat.checkSelfPermission(this, CAMERA) == PERMISSION_GRANTED;
        boolean permissaoEscrita = ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED;
        boolean permissaoLeitura = ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED;

        if (permissaoCamera && permissaoEscrita && permissaoLeitura) {
            iniciarGravacaoDeVideo();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                                grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                                grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    iniciarGravacaoDeVideo();
                } else {
                    Toast.makeText(this, "Sem permissão para uso de câmera.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void setArquivoVideo () {
        try {
            String nomeArquivo = "VideoTrabMobile.mp4";
            File pathDoVideo = getDiretorioDeSalvamento(nomeArquivo);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authority = this.getApplicationContext().getPackageName() + ".fileprovider";
                uri = FileProvider.getUriForFile(this, authority, pathDoVideo);
            } else {
                uri = Uri.fromFile(pathDoVideo);
            }
        }catch (Exception e){}
    }

    private File getDiretorioDeSalvamento(String nomeArquivo) {
        try {
            File pathDaVideo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + nomeArquivo);
            return pathDaVideo;
        }catch (Exception e) {
            return null;
        }
    }

    public void visualizarVideo(View v) {
        Toast.makeText(this,uri.toString(), Toast.LENGTH_LONG).show();
        videoView.setVideoURI(uri);
        MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);
        videoView.start();
    }
}

