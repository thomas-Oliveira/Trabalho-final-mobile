package com.example.thomas.denuncie;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class DenunciaActivity extends AppCompatActivity {

    private TextView id;
    private TextView descricao;
    private TextView data;
    private TextView longitude;
    TextView latitude;
    VideoView urimidia;
    TextView categoria;
    TextView usuario;
    ImageView image;

    private String urlBase = "http://maps.googleapis.com/maps/api/staticmap" +
            "?size=400x400&sensor=true&markers=color:red|%s,%s&key=AIzaSyADsCUTgD_u4LT-VTbLcOOUsinzu_O_MP0";

    private WebView mWebView;
    private TextView provedor;
    private LocationManager locationManager;

    DenunciaDao DenumDAO = new DenunciaDao(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia);

        id = findViewById(R.id.denuncia_id);
        descricao = findViewById(R.id.denuncia_descricao);
        data = findViewById(R.id.denuncia_data);
        longitude = findViewById(R.id.denuncia_longitude);
        latitude = findViewById(R.id.denuncia_latitude);
        urimidia = findViewById(R.id.denuncia_urimidia);
        usuario = findViewById(R.id.denuncia_usuario);
        categoria = findViewById(R.id.denuncia_categoria);
        provedor = findViewById(R.id.denuncia_provedor);

        int id_item = getIntent().getIntExtra("_id", 0);
        Denuncia item = DenumDAO.buscarDenunciaPorId(id_item);

        id.setText(item.getId().toString());
        descricao.setText(item.getDescricao());
        data.setText(item.getData().toString());
        longitude.setText(item.getLongitude().toString());
        latitude.setText(item.getLatitude().toString());
        usuario.setText(item.getUsuario().toString());
        categoria.setText(item.getCategoria().toString());

        mWebView = findViewById(R.id.denuncia_mapa);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(false);
        getLocationManager();

        image = findViewById(R.id.denuncia_imageView);
        image.setImageURI(Uri.parse(item.getUriMidia()));

        String msgErro = item.getUriMidia().toString();
        Toast toast = Toast.makeText(this,msgErro,Toast.LENGTH_SHORT * 40);
        toast.show();

    }

    private void getLocationManager() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET},
                    1);
            return;
        }

        Listener listener = new Listener();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        long tempoAtualizacao = 0;
        float distancia = 0;
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, tempoAtualizacao, distancia, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, tempoAtualizacao, distancia, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    getLocationManager();
                } else {
                    Toast.makeText(this, "Sem permissão para uso de gps ou rede.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private class Listener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            String latitudeStr = String.valueOf(location.getLatitude());
            String longitudeStr = String.valueOf(location.getLongitude());

            provedor.setText(location.getProvider());
            latitude.setText(latitudeStr);
            longitude.setText(longitudeStr);

            String url = String.format(urlBase, latitudeStr, longitudeStr);
            mWebView.loadUrl(url);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {    }
        @Override
        public void onProviderEnabled(String provider) {  }
        @Override
        public void onProviderDisabled(String provider) { }
    }


}