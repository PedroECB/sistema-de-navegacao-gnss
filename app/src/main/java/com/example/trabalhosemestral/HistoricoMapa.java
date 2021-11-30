package com.example.trabalhosemestral;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.trabalhosemestral.databinding.ActivityHistoricoMapaBinding;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HistoricoMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityHistoricoMapaBinding binding;


    public static final String FILE_NAME = "historico.txt";
    public List<String> historicoList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoricoMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng salvador = new LatLng(-12.934827965320725, -38.425965561976085);

        //mMap.addMarker(new MarkerOptions().position(salvador).title("Marker in Salvador"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(salvador));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(salvador)
                .zoom(3.0f)
                .tilt(0)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 150,null);


        this.getHistoryLocation();
        this.setMarkersHistory();
        this.addPolyLine();
    }




    public void getHistoryLocation(){

        FileInputStream fis = null;

        try {

            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String text;

            //Percorrendo as linhas do arquivo

            while((text = br.readLine()) != null){
                this.historicoList.add(text);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //Fechando leitura de arquivo
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //this.historicoList.add("-12.917205,-38.4356941;26/11/2021 23:56:30");
        //this.historicoList.add("-12.9172019,-38.4356972;26/11/2021 23:56:43");
        //this.historicoList.size();

    }

    public void setMarkersHistory(){
        Toast.makeText(this, "Tamanho do historico atual2: "+this.historicoList.size(), Toast.LENGTH_SHORT).show();

        if(!this.historicoList.isEmpty()){
            String initialPointString = this.historicoList.get(0);
            String endPointString = this.historicoList.get(this.historicoList.size() - 1);

            double latitudeInitial = Double.parseDouble(initialPointString.split(";")[0].split(",")[0]);
            double longitudeInitial = Double.parseDouble(initialPointString.split(";")[0].split(",")[1]);

            double latitudeEnd = Double.parseDouble(endPointString.split(";")[0].split(",")[0]);
            double longitudeEnd = Double.parseDouble(endPointString.split(";")[0].split(",")[1]);

            LatLng start = new LatLng(latitudeInitial, longitudeInitial);
            LatLng end = new LatLng(latitudeEnd, longitudeEnd);

            mMap.addMarker(new MarkerOptions().position(start).title("Ponto inicial"));
            mMap.addMarker(new MarkerOptions().position(end).title("Último ponto registrado"));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(start));

           CameraPosition cameraPositionOne = new CameraPosition.Builder()
                    .target(start)
                    .zoom(14.0f)
                    .tilt(0)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionOne), 150,null);

            Toast.makeText(this, "Ponto Inicial"+initialPointString, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Ponto Final"+endPointString, Toast.LENGTH_SHORT).show();



        }

    }


    public void addPolyLine(){

        if(!this.historicoList.isEmpty()){
            List<LatLng> listaLatLng = new ArrayList<LatLng>();

            for(String stringLine : this.historicoList){
                double latitude = Double.parseDouble(stringLine.split(";")[0].split(",")[0]);
                double longitude = Double.parseDouble(stringLine.split(";")[0].split(",")[1]);

                listaLatLng.add(new LatLng(latitude, longitude));
            }

            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(listaLatLng)
                    .color(Color.GREEN)
                    .width(10);
            mMap.addPolyline(polylineOptions);
        }
    }

}