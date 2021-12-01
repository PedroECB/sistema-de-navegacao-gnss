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
import android.widget.TextView;
import android.widget.Toast;

public class LocationManagerActivity extends AppCompatActivity implements LocationListener{
    private LocationManager locationManager; // O Gerente de localização
    private LocationProvider locationProvider; // O provedor de localizações
    private GnssStatusCallback  gnssStatusCallback; //O escutador do sistema GNSS
    private static final int REQUEST_LOCATION=2;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_manager);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        this.ativaGNSS();
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
                    this);
            gnssStatusCallback = new GnssStatusCallback();
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
        locationManager.removeUpdates(this);
        locationManager.unregisterGnssStatusCallback(gnssStatusCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // O usuário acabou de dar a permissão
                ativaGNSS();
            }
            else {
                // O usuário não deu a permissão solicitada
                Toast.makeText(this,"Sem permissão para acessar o sistema de posicionamento",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        TextView textViewLocation=(TextView)findViewById(R.id.textViewLocation);
        String mens="Dados da Última posição\n";
        if (location!=null) {
            mens+="Latitude(graus)="+Location.convert(location.getLatitude(),Location.FORMAT_SECONDS)+"\n"
                    +"Longitude(graus)="+Location.convert(location.getLongitude(),Location.FORMAT_SECONDS)+"\n"
                    +"Velocidade(m/s)="+location.getSpeed()+"\n"
                    +"Rumo(graus)="+location.getBearing();
        }
        else {
            mens+="Localização Não disponível";
        }
        textViewLocation.setText(mens);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStop() {
        super.onStop();
        desativaGNSS();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private class GnssStatusCallback extends GnssStatus.Callback {
        public GnssStatusCallback() {
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

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
            TextView textViewGnss =(TextView)findViewById(R.id.textViewGnss);
            String mens="Dados do Sitema de Posicionamento\n\n";
            if (status!=null) {
                mens+="Número de Satélites:"+status.getSatelliteCount()+"\n\n";
                for(int i=0;i<status.getSatelliteCount();i++) {
                    mens+="SVID="+status.getSvid(i)+"-"+"\n"+
                            "Azi="+status.getAzimuthDegrees(i)+"\n"+
                            "Elev="+status.getElevationDegrees(i)+"\n"+
                            "Used in Fix:"+status.usedInFix(i)+"\n"+
                            "Constelation: "+status.getConstellationType(i)+"\n"+
                            "CNR: "+status.getCarrierFrequencyHz(i)+"\n"+
                            "SNR="+status.getCn0DbHz(i)+"|X|\n\n\n";
                }
            }
            else {
                mens+="GNSS Não disponível";
            }
            textViewGnss.setText(mens);
        }

    }


}