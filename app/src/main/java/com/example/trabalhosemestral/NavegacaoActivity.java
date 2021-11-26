package com.example.trabalhosemestral;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.trabalhosemestral.databinding.ActivityNavegacaoBinding;
import com.google.android.gms.tasks.OnSuccessListener;

public class NavegacaoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityNavegacaoBinding binding;
    private SharedPreferences sharedPreferences;

    ArrayAdapter arrayCoordenadas;
    ArrayAdapter arrayVelocidade;
    ArrayAdapter arrayOrientacoesMapa;
    ArrayAdapter arrayTiposMapa;

    private static final int REQUEST_LAST_LOCATION = 1;
    private static final int REQUEST_LOCATION_UPDATE = 2;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    TextView labelCoordenadas, labelVelocidade, labelTipoMapa, labelInfoTrafego, labelOrientacaoMapa;

    MarkerOptions markerOptions;
    Circle circleMap;
    Marker marker;
    CameraPosition cameraPositionOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavegacaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        labelCoordenadas = (TextView) findViewById(R.id.labelCoordenadasMapa);
        labelVelocidade = (TextView) findViewById(R.id.labelVelocidadeMapa);
        labelTipoMapa = (TextView) findViewById(R.id.labelTipoMapa);
        labelInfoTrafego = (TextView) findViewById(R.id.labelInfoTrafegoMapa);
        labelOrientacaoMapa = (TextView) findViewById(R.id.labelOrientacaoMapa);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng salvador = new LatLng(-12.934827965320725, -38.425965561976085);

        //Configurando marcador

        markerOptions = new MarkerOptions();
        markerOptions.position(salvador);
        markerOptions.title("Localização");
        markerOptions.snippet("Brasil");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icone_marcador));

        marker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(salvador));
        marker.setAnchor(0.5f, 0.5f);


        circleMap = mMap.addCircle(new CircleOptions()
                .center(salvador)
                .strokeColor(0x8011A0B3)
                .fillColor(0x8011A0B3));

        //markerOptions.visible(false);

        //Ajustando posição da câmera "movecamera = bearing para girar o course up"
        // Circulo refletir a acurácia -> Classe Projection

        cameraPositionOne = new CameraPosition.Builder()
                .target(salvador)
                .zoom(17.0f)
                .tilt(0)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionOne), 150,null);



        //Aplicando configurações de mapa baseadas na preferência do usuário
        this.applyConfigPreferences();

        //Habilita a localização
        this.enableMyLocation();

        this.hasLastLocation();
        this.startLocationUpdates();
    }



    public void applyConfigPreferences(){

        arrayOrientacoesMapa = ArrayAdapter.createFromResource(this, R.array.orientacoesMapa, android.R.layout.simple_spinner_item);
        arrayTiposMapa = ArrayAdapter.createFromResource(this, R.array.tiposMapa, android.R.layout.simple_spinner_item);

        //Obtendo as preferências salvas nas configurações
        sharedPreferences = getSharedPreferences("UserMapPreferences", MODE_PRIVATE);

        String preferenciaTipoMapa = sharedPreferences.getString("TipoMapa", arrayTiposMapa.getItem(0).toString());
        String preferenciaInfoTrafego = sharedPreferences.getString("InfoTrafego", "Off");
        String preferenciaOrientacaoMapa = sharedPreferences.getString("OrientacaoMapa", arrayOrientacoesMapa.getItem(0).toString());

        //Setando tipo de mapa
        if(preferenciaTipoMapa.equals(arrayTiposMapa.getItem(0).toString())) {
            //Vetorial
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            labelTipoMapa.setText("Tipo de mapa: Vetorial");

        }else{
            //Satellite
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            labelTipoMapa.setText("Tipo de mapa: Imagem de satélite");

        }

        //Setando informação de tráfego

        if(preferenciaInfoTrafego.equals("On")){
            mMap.setTrafficEnabled(true);
            labelInfoTrafego.setText("Informação de tráfego: Ativado");
        }else{
            mMap.setTrafficEnabled(false);
            labelInfoTrafego.setText("Informação de tráfego: Desativado");

        }

        //Setando orientação do mapa

        if(preferenciaOrientacaoMapa.equals(arrayOrientacoesMapa.getItem(0).toString())){
            //Nenhuma
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            labelOrientacaoMapa.setText("Orientação: Nenhuma");

        }else if(preferenciaOrientacaoMapa.equals(arrayOrientacoesMapa.getItem(1).toString())){
            //North up
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            labelOrientacaoMapa.setText("Orientação: North up");

        }else if(preferenciaOrientacaoMapa.equals(arrayOrientacoesMapa.getItem(2).toString())){
            //Course up
            labelOrientacaoMapa.setText("Orientação: Course up");
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }


    }


    public void updateStatusBar(Location location){
        arrayCoordenadas = ArrayAdapter.createFromResource(this, R.array.fomatosCoordenadas, android.R.layout.simple_spinner_item);
        arrayVelocidade = ArrayAdapter.createFromResource(this, R.array.fomatosVelocidade, android.R.layout.simple_spinner_item);

        String preferenciaCoordenadas = sharedPreferences.getString("FormatoCoordenadas", arrayCoordenadas.getItem(0).toString());
        String preferenciaVelocidade = sharedPreferences.getString("FormatoVelocidade", arrayVelocidade.getItem(0).toString());

        if(location != null){
            //Label de Coordenadas
            if(preferenciaCoordenadas.equals(arrayCoordenadas.getItem(0).toString())){
                //Grau decimal
                labelCoordenadas.setText("Coordenadas: "+location.getLatitude()+", "+location.getLongitude());

            }else if(preferenciaCoordenadas.equals(arrayCoordenadas.getItem(1).toString())){
                //Grau minuto
                labelCoordenadas.setText(" "+this.toDegreeMinute(location.getLatitude(), location.getLongitude(), location));

            }else if(preferenciaCoordenadas.equals(arrayCoordenadas.getItem(2).toString())){
                //Grau minuto segundo
                labelCoordenadas.setText(" "+this.toDegreeMinuteSecond(location.getLatitude(), location.getLongitude(), location));
            }


            //Label de velocidade
            if(preferenciaVelocidade.equals(arrayVelocidade.getItem(0).toString())){
                //Kmh
                labelVelocidade.setText(this.toKmh(location.getSpeed()));

            }else if(preferenciaVelocidade.equals(arrayVelocidade.getItem(1).toString())){
                //Mph
                labelVelocidade.setText("Velocidade (Mph):"+ location.getSpeed());
            }
        }


    }

    public void updateMarkerAndCircle(Location location){

        arrayOrientacoesMapa = ArrayAdapter.createFromResource(this, R.array.orientacoesMapa, android.R.layout.simple_spinner_item);
        String preferenciaOrientacaoMapa = sharedPreferences.getString("OrientacaoMapa", arrayOrientacoesMapa.getItem(0).toString());

        if(location != null){
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());


            marker.setPosition(currentLocation);
            marker.setRotation(location.getBearing());
            circleMap.setCenter(currentLocation);
            circleMap.setRadius(location.getAccuracy()*10);
            circleMap.setStrokeColor(0x8011A0B3);
            circleMap.setFillColor(0x8011A0B3);

            //Toast.makeText(this, "Bearing location: "+location.getBearing(), Toast.LENGTH_SHORT).show();

            if(preferenciaOrientacaoMapa.equals(arrayOrientacoesMapa.getItem(2).toString())){
                //Course up
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentLocation)
                        .bearing(location.getBearing())
                        .zoom(17.0f)
                        .tilt(0)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 150,null);

            }else{

//                Projection projection = mMap.getProjection();
//
//
//
//                CameraPosition cameraPosition = new CameraPosition.Builder()
//                        .target(currentLocation)
//                        .bearing(cameraPositionOne.bearing)
//                        .zoom(17.0f)
//                        .tilt(0)
//                        .build();
//
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionOne), 150,null);
//
//                Toast.makeText(this, "Bearing camera "+cameraPosition.bearing, Toast.LENGTH_SHORT).show();
            }

        }else{

            Toast.makeText(this, "Não foi possível obter as informações da localização!", Toast.LENGTH_SHORT).show();
        }


    }


    public void enableMyLocation(){
        // mMap.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
        // Toast.makeText(this, "Permissão de localização concedida!", Toast.LENGTH_SHORT).show();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

        }else{
            Toast.makeText(this, "A aplicação necessita de acesso a localização para funcionar!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }


    }

        //Obtendo a ultima localização do usuário
    public void getLastLocation(Location location){
        String mensagem = "";

        if(location != null){
            mensagem+= String.valueOf("Latitude (graus) = ")+location.getLatitude()+"\n"
                   +String.valueOf("Longitude (graus) = ")+location.getLongitude()+"\n"
                    +String.valueOf("Velocidade (m/s) = ")+location.getSpeed()+"\n"
                    +String.valueOf("Rumo (graus) = ")+location.getBearing()+"\n"
                    +String.valueOf("Acuracia (graus) ss= ")+location.getAccuracy()+"\n";

            labelCoordenadas.setText(mensagem);

        }else{
            mensagem = "Dados não disponíveis";
            labelCoordenadas.setText(mensagem);
        }
    }

    public void getUpdateLocation(Location location){
        String mensagem = "";

        if(location != null){
            mensagem+= String.valueOf("Latitude (graus) = ")+location.getLatitude()+"\n"
                    +String.valueOf("Longitude (graus) = ")+location.getLongitude()+"\n"
                    +String.valueOf("Velocidade (m/s) = ")+location.getSpeed()+"\n"
                    +String.valueOf("Rumo (graus) = ")+location.getBearing()+"\n"
                    +String.valueOf("Acuracia (graus) = ")+location.getAccuracy()+"\n"
                    +String.valueOf("Zoom  = ss ")+ mMap.getMaxZoomLevel()+"\n"
                    +String.valueOf("-> ")+ this.toDegreeMinute(location.getLatitude(), location.getLongitude(), location)+"\n";

            labelCoordenadas.setText(mensagem);

        LatLng latLngAtual = new LatLng(location.getLatitude(), location.getLongitude());

        markerOptions.position(latLngAtual);
            Toast.makeText(this, "Atualizando marker...", Toast.LENGTH_SHORT).show();
        mMap.addMarker(markerOptions).setAnchor(0.5f, 0.5f);



            circleMap = mMap.addCircle(new CircleOptions()
                    .center(latLngAtual)
                    .radius(location.getAccuracy()*10)
                    .strokeColor(0x8011A0B3)
                    .fillColor(0x8011A0B3));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngAtual));


            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLngAtual)
                    .bearing(location.getBearing())
                    .zoom(17.0f)
                    .tilt(0)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 150,null);


        }else{
            mensagem = "Não coletando informações...";
            labelCoordenadas.setText(mensagem);
        }
    }

    private boolean hasLastLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
             Toast.makeText(this, "Permissão de localização concedida!", Toast.LENGTH_SHORT).show();
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            //getLastLocation(location);
                            //getUpdateLocation(location);


                        }
                    });

            return true;

        }else{
            Toast.makeText(this, "A aplicação necessita de acesso a localização para funcionar!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 0);
            return  false;
        }

    }

    private void  startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // Toast.makeText(this, "Permissão de localização concedida!", Toast.LENGTH_SHORT).show();
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            mLocationRequest = LocationRequest.create();

            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(5*1000);
            mLocationRequest.setFastestInterval(1*1000);

            mLocationCallback = new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult){
                    super.onLocationResult(locationResult);
                    Location location = locationResult.getLastLocation();
                    //getUpdateLocation(location);
                    updateStatusBar(location);
                    updateMarkerAndCircle(location);
                }
            };

            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        }else{
            Toast.makeText(this, "A aplicação necessita de acesso a localização para funcionar!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 0){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                getLastLocation();
            }

        }else{
            Toast.makeText(this, "Sem permissão para acessar a última localização...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    public String toDegreeMinute(double lat, double lon, Location location){
        String latSN = (int)lat >= 0 ? "N" : "S";
        String lonEW = (int)lon >= 0 ? "E" : "W";

        String strLatitude = Location.convert(location.getLatitude(), Location.FORMAT_MINUTES).replace(":","° ").replace(",","\' ") + latSN;
        String strLongitude = Location.convert(location.getLongitude(), Location.FORMAT_MINUTES).replace(":","° ").replace(",","\' ") + lonEW;

        return "LAT-LONG: " + strLatitude + ", " + ", " + strLongitude;
    }

    public String toDegreeMinuteSecond(double lat, double lon, Location location){

        String latSN = (int)lat >= 0 ? "N" : "S";
        String lonEW = (int)lon >= 0 ? "E" : "W";

        String strLatitudeSeconds = Location.convert(location.getLatitude(), Location.FORMAT_SECONDS).replaceFirst(":","° ").replace(":","\' ").replace(",",".") + "\" " + latSN;
        String strLongitudeSeconds = Location.convert(location.getLongitude(), Location.FORMAT_SECONDS).replaceFirst(":","° ").replace(":","\' ").replace(",",".") + "\" " + lonEW;

        return "LAT-LONG: " + strLatitudeSeconds + ", " + ", " + strLongitudeSeconds + "\n";

    }

    public String toKmh(float speed){
        return "Velocidade (Km/h): "+(Math.floor(speed*3.6));
    }


} //Fim da Atividade