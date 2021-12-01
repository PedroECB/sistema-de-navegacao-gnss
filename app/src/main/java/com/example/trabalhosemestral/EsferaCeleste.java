package com.example.trabalhosemestral;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class EsferaCeleste extends AppCompatActivity implements LocationListener{
    private LocationManager locationManager; // O Gerente de localização
    private LocationProvider locationProvider; // O provedor de localizações
    private MyGnssStatusCallback gnssStatusCallback; //O escutador do sistema GNSS
    private static final int REQUEST_LOCATION = 2;
    CircleView circleView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esfera_celeste);
        setContentView(R.layout.circle_view);
        circleView = findViewById(R.id.circle_view);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);

        this.ativaGNSS();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStop() {
        super.onStop();
        desativaGNSS();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ativaGNSS() {
        // Se a app já possui a permissão, ativa a camada de localização
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(locationProvider.getName(),
                    5*1000,
                    0.1f,
                    (LocationListener) this);
            gnssStatusCallback = new MyGnssStatusCallback();
            locationManager.registerGnssStatusCallback(gnssStatusCallback);
        } else {
            // Solicite a permissão
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void desativaGNSS() {
        locationManager.unregisterGnssStatusCallback(gnssStatusCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // O usuário acabou de dar a permissão
                ativaGNSS();
            } else {
                // O usuário não deu a permissão solicitada
                Toast.makeText(this, "Sem permissão para acessar o sistema de posicionamento",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private class MyGnssStatusCallback extends GnssStatus.Callback {

        @RequiresApi(api = Build.VERSION_CODES.N)
        public MyGnssStatusCallback() {
            super();
        }

        @Override
        public void onStarted() {
        }

        @Override
        public void onStopped() {
        }

        @Override
        public void onFirstFix(int ttffMillis) {
        }

        @Override
        public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
            circleView.onSatelliteStatusChanged(status);
            circleView.invalidate();
        }
    }

    public void Invalidate(){
        this.circleView.invalidate();
    }


}