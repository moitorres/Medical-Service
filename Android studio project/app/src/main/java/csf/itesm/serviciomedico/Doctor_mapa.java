package csf.itesm.serviciomedico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

public class Doctor_mapa extends AppCompatActivity implements OnMapReadyCallback {

    private Session session;
    private ObjetoDoctor doctor;
    public static String SERVICIO_UBICACIONES;

    //Objetos para el mapa
    private MapView mapView;
    private GoogleMap gmap;

    String matricula;
    String nombre;
    String longitud;
    String latitud;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_mapa);

        //El objeto doctor es extraido del intent
        doctor = (ObjetoDoctor) getIntent().getSerializableExtra("doctor");

        session = new Session(this);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
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

        //Mover el gps a la ubicación del tec
        LatLng tec = new LatLng(19.358385, -99.258438);

        //Definimos la url a donde vamos a realizar la petición
        SERVICIO_UBICACIONES = "http://ubiquitous.csf.itesm.mx/~pddm-1021323/content/Proyecto/REST/servicioUbicaciones.php";

        JsonArrayRequest peticion = new JsonArrayRequest(Request.Method.GET, SERVICIO_UBICACIONES,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Objeto","respuesta : " + response.toString());
                        for(int i = 0 ; i < response.length(); i++)
                        {
                            try {
                                JSONObject data = response.getJSONObject(i);

                                matricula = data.getString("Pacientes_Matricula");
                                nombre = data.getString("Nombre");
                                nombre += " "+data.getString("ApellidoPaterno");
                                longitud = data.getString("Longitud");
                                latitud = data.getString("Latitud");

                                String coordenadas = latitud + " " + longitud;

                                //Se guardan las coordenadas de latitud y longitud
                                LatLng ubicacion = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
                                Log.d("ubicacion: ",coordenadas);

                                //Se crean los markers de los pacientes
                                MarkerOptions markerOptions = new MarkerOptions();
                                //Se agrega su ubicación
                                markerOptions.position(ubicacion);
                                //Se agrega el nombre del paciente como título
                                markerOptions.title(nombre);
                                //Se hace visible
                                markerOptions.visible(true);
                                //Se añade al mapa
                                gmap.addMarker(markerOptions);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley", "Error : " + error.getMessage());
                    }
        });

        //Se hace la petición al servicio
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(peticion);

        CameraPosition.Builder camBuilder = CameraPosition.builder();
        camBuilder.target(tec);
        camBuilder.zoom(17);

        CameraPosition cp = camBuilder.build();

        // Animating to the touched position
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
    }

    //Funcion para ir a la misma actividad
    public void goToMap(View view){
        Intent intent = new Intent (this, Doctor_mapa.class);
        //Se agrega el objeto paciente al intent
        intent.putExtra("doctor", doctor);
        startActivity(intent);
    }

    //Funcion para cargar la actividad de pacientes
    public void goToPatients(View view){
        Intent intent = new Intent (this, Doctor_listaPacientes.class);
        //Se agrega el objeto paciente al intent
        intent.putExtra("doctor", doctor);
        startActivity(intent);
    }

    //Funcion para cargar la actividad de notificaciones
    public void goToNotifications(View view){
        Intent intent = new Intent (this, Doctor_notificaciones.class);
        //Se agrega el objeto paciente al intent
        intent.putExtra("doctor", doctor);
        startActivity(intent);
    }

    //Funcion para hacer logout
    public void logout(View view){
        Intent intent = new Intent (this, MainActivity.class);
        session.logOut();
        startActivity(intent);
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
