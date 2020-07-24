package csf.itesm.serviciomedico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Doctor_listaPacientes extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private Session session;
    MyRecyclerViewAdapter adapter;
    ProgressDialog barra_de_progreso;
    List<ObjetoPaciente> misElementos;
    public static String SERVICIO_PACIENTES;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_lista_pacientes);

        //Barra de progreso
        barra_de_progreso = new ProgressDialog(Doctor_listaPacientes.this);
        barra_de_progreso.setMessage("Cargando datos...");
        barra_de_progreso.show();

        //Se mapea el recycleview
        recyclerView = findViewById(R.id.datos_paciente);
        //Lista para pacientes
        misElementos = new ArrayList<>();
        //Sesión
        session = new Session(this);

        //Petición para recibir el json de pacientes
        enviarPeticion();

        //Inicializar el RecycleView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //El programa espera dos segundos para usar el recycleview, en lo que se envía la petición
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //Se crea un adapter con el arreglo de pacientes
                adapter = new MyRecyclerViewAdapter(Doctor_listaPacientes.this, misElementos);
                //Se les asigna un click listener
                adapter.setClickListener(Doctor_listaPacientes.this);
                //El adaptador se asigna al recycleview
                recyclerView.setAdapter(adapter);

                //Se elimina la barra de progreso
                barra_de_progreso.dismiss();
            }
        }, 2000);   //2 seconds

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
    }

    //Creamos la función a ejecutar cuando se oprima el botón en la GUI
    public void enviarPeticion(){

        //Definimos la url a donde vamos a realizar la petición
        SERVICIO_PACIENTES = "http://ubiquitous.csf.itesm.mx/~pddm-1021323/content/Proyecto/REST/servicioPacientes.php";

        JsonArrayRequest peticion = new JsonArrayRequest(Request.Method.GET, SERVICIO_PACIENTES,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Objeto","respuesta : " + response.toString());
                        for(int i = 0 ; i < response.length(); i++)
                        {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                ObjetoPaciente paciente = new ObjetoPaciente();

                                paciente.setMatricula(data.getString("Matricula"));
                                paciente.setNombre(data.getString("Nombre"));
                                paciente.setAppaterno(data.getString("ApellidoPaterno"));
                                paciente.setApmaterno(data.getString("ApellidoMaterno"));
                                paciente.setPadecimientos(data.getString("Padecimientos"));
                                paciente.setMedicamentos(data.getString("Medicamentos"));
                                misElementos.add(paciente);
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
    }

    //Funcion para ir a la actividad de mapa
    public void goToMap(View view){
        Intent intent = new Intent (this, Doctor_mapa.class);
        startActivity(intent);
    }

    //Funcion para cargar la actividad de pacientes
    public void goToPatients(View view){
        Intent intent = new Intent (this, Doctor_listaPacientes.class);
        startActivity(intent);
    }

    //Funcion para cargar la actividad de notificaciones
    public void goToNotifications(View view){
        Intent intent = new Intent (this, Doctor_notificaciones.class);
        startActivity(intent);
    }

    //Funcion para hacer logout
    public void logout(View view){
        Intent intent = new Intent (this, MainActivity.class);
        session.logOut();
        startActivity(intent);
    }

    //Función para ir a la actividad donde se agrega un paciente nuevo
    public void goToNewPatient(View view){
        Intent intent = new Intent (this, Doctor_pacienteNuevo.class);
        startActivity(intent);
    }

}
