package csf.itesm.serviciomedico;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Paciente_principal extends AppCompatActivity implements OnMapReadyCallback {

    private Session session;
    private ObjetoPaciente paciente;

    String longitud;
    String latitud;

    String Matricula, Descripcion;
    EditText DescripcionText;

    //Objetos para el mapa
    private MapView mapView;
    private GoogleMap gmap;

    protected LocationManager locationManager;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds

    int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_principal);

        //El objeto paciente es extraido del intent
        paciente = (ObjetoPaciente) getIntent().getSerializableExtra("paciente");

        //Sesión
        session = new Session(this);

        //location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Checar permisos de gps
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }

        //Checar permisos de gps
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }

        //Activar el location listener
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new LocationService()
        );

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapView2);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    protected void showCurrentLocation() {

        //Checar permisos de gps
        if (ContextCompat.checkSelfPermission(Paciente_principal.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Paciente_principal.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(Paciente_principal.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Paciente_principal.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(Paciente_principal.this, message,
                    Toast.LENGTH_LONG).show();
        }

    }

    //Funcion para hacer logout
    public void logout(View view){
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);

        session.logOut();
    }

    public void enviarNotificacion(View view){
        DescripcionText = (EditText) findViewById(R.id.editText13);
        Descripcion = DescripcionText.getText().toString();

        //Response listener
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    //Si se logra registrar al paciente
                    if (success){
                        Toast.makeText(Paciente_principal.this, "Notificación de emergencia enviada exitosamente", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Paciente_principal.this, Paciente_principal.class);
                        Paciente_principal.this.startActivity(intent);
                    }
                    //Si falla el registro
                    else{
                        AlertDialog.Builder builder= new AlertDialog.Builder(Paciente_principal.this);
                        builder.setMessage("Fallo en el envío de la notificación de emergencia")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                }
                //En caso de una excepción
                catch (JSONException e) {
                    Toast.makeText(Paciente_principal.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };

        //Enviar la petición
        RequestIncidente registerRequest = new RequestIncidente(session.getUsuario(),Descripcion,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Paciente_principal.this);
        queue.add(registerRequest);
    }

    private class LocationService implements LocationListener {

        public void onLocationChanged(Location location) {

            //Response listener
            Response.Listener<String> responseListener = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        //Si se logra registrar al paciente
                        if (success){
                            Toast.makeText(Paciente_principal.this, "Ubicacion actualizada", Toast.LENGTH_LONG).show();
                        }

                        //Si falla el registro
                        else{
                            AlertDialog.Builder builder= new AlertDialog.Builder(Paciente_principal.this);
                            Log.d("Ubicacion: ","Error");
                        }
                    }
                    //En caso de una excepción
                    catch (JSONException e) {
                        Toast.makeText(Paciente_principal.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            };

            //Obtener la longitud y latitud
            longitud = location.getLongitude()+"";
            latitud = location.getLatitude()+"";

            //Enviar la petición
            RequestMandarUbicacion locationRequest;
            locationRequest = new RequestMandarUbicacion(session.getUsuario(), longitud, latitud, responseListener);
            RequestQueue queue = Volley.newRequestQueue(Paciente_principal.this);
            queue.add(locationRequest);
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(Paciente_principal.this, "Cambio de status del proveedor",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(Paciente_principal.this,
                    "GPS apagado",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(Paciente_principal.this,
                    "GPS prendido",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Guardar el mapa
        gmap = googleMap;

        //Ajustar preferencias
        gmap.setIndoorEnabled(true);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        //Se guarda la posición del usuario
        LatLng ubicacion = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));

        CameraPosition.Builder camBuilder = CameraPosition.builder();
        camBuilder.target(ubicacion);
        camBuilder.zoom(17);

        CameraPosition cp = camBuilder.build();

        // Animando a la posición del usuario
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));

        //Se crea un marker para el paciente
        MarkerOptions markerOptions = new MarkerOptions();
        //Se agrega su ubicación
        markerOptions.position(ubicacion);
        //Se hace visible
        markerOptions.visible(true);
        //Se añade al mapa
        gmap.addMarker(markerOptions);
    }

    //For mapView
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
        mapView.onSaveInstanceState(outstate);
    }

}
