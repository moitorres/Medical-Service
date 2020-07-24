package csf.itesm.serviciomedico;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    //String para el url del servico REST
    public static String SERVICIO_LOGIN;
    //Key para los valores a enviar al servico
    public static final String USUARIO = "usuario";
    public static final String PASSWORD = "password";
    //Tag para usar en logs
    private final String TAG = "Datos";
    //Objetos para guardar la información del usuario
    private ObjetoPaciente paciente;
    private ObjetoDoctor doctor;
    //Variables para conseguir la información de los EditText
    EditText usuarioTexto, passwordTexto;
    String usuario, password, encryptedPassword;

    //Clase session para las shared preferences
    private Session session;
    //Clase encryption para encriptar la contraseña
    private Encryption encryption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Se verifica si ya había una sesión iniciada
        session = new Session(this);
        if(session.isLoggedIn() && session.getStatus().equals("Doctor")){
            Intent intent = new Intent (this, Doctor_mapa.class);
            startActivity(intent);
        }
        else if(session.isLoggedIn() && session.getStatus().equals("Paciente")){
            Intent intent = new Intent (this, Paciente_principal.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);

        //Si no hay una sesión, se carga el layout
        setContentView(R.layout.activity_main);

        encryption = new Encryption();
    }

    public void login(View view){

        //Barra de progreso para el login
        final ProgressDialog barraDeProgreso = new ProgressDialog(MainActivity.this);
        barraDeProgreso.setMessage("Iniciando sesion...");
        barraDeProgreso.show();

        //Los EditText con el texto del usuario y de la contraseña son obtenidos
        usuarioTexto = (EditText) findViewById(R.id.editText);
        passwordTexto = (EditText) findViewById(R.id.editText2);

        SERVICIO_LOGIN = "http://ubiquitous.csf.itesm.mx/~pddm-1021323/content/Proyecto/REST/servicio.login.php?";

        //Se valida que el usuario haya llenado los campos de usuario y contraseña
        if(validateFields(usuarioTexto, passwordTexto)){

            //Los EditText son transformados a strings
            usuario = usuarioTexto.getText().toString();
            password = passwordTexto.getText().toString();

            /*Se encripta la contraseña
            try{
                encryptedPassword = encryption.encryption(password);
            }
            //En caso de excepción
            catch (Exception e){
                Toast.makeText(MainActivity.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }*/

            //A la url se añaden el usuario y contraseña
            SERVICIO_LOGIN = SERVICIO_LOGIN + USUARIO + "=" + usuario + "&" + PASSWORD + "=" + password;
            //Se registra el url en un log
            Log.d(TAG,SERVICIO_LOGIN.toString());

            JsonArrayRequest peticion = new JsonArrayRequest(SERVICIO_LOGIN, new Response.Listener<JSONArray>() {
                @Override public void onResponse(JSONArray response) {

                    barraDeProgreso.hide();

                    try {
                        //Se guarda el json con el codigo de autenticación
                        JSONObject autenticacion = (JSONObject) response.get(0);
                        //El código se guarda en un string
                        String codigo_autenticacion = autenticacion.getString("Codigo").toString();

                        //Si se logra hacer login como paciente
                        if (codigo_autenticacion.equals("01")) {

                            //Se guarda el json con los objetos del usuario
                            JSONObject username = (JSONObject) response.get(2);

                            //Todos los datos del paciente son guardados en un objeto
                            paciente.getInstance().setNombre(username.getString("Nombre").toString());
                            paciente.getInstance().setAppaterno(username.getString("ApellidoPaterno").toString());
                            paciente.getInstance().setApmaterno(username.getString("ApellidoMaterno").toString());
                            paciente.getInstance().setApmaterno(username.getString("Edad").toString());
                            paciente.getInstance().setMatricula(usuario);
                            paciente.getInstance().setPassword(password);

                            //Se le da la bienvenida al usuario
                            Toast.makeText(MainActivity.this, "Bienvenido " + username.getString("Nombre").toString(), Toast.LENGTH_LONG).show();
                            Log.d(TAG,response.toString());

                            //Se inicia la sesión de paciente y se carga la actividad
                            Intent intent = new Intent(MainActivity.this, Paciente_principal.class);
                            session.logIn(usuario,"Paciente");

                            //Se agrega el objeto paciente al intent
                            intent.putExtra("paciente", paciente);

                            //Se inicia la actividad
                            startActivity(intent);
                        }

                        //Si se logra hacer login como doctor
                        else if (codigo_autenticacion.equals("02")) {
                            JSONObject username = (JSONObject) response.get(2);

                            doctor.getInstance().setNombre(username.getString("Nombre").toString());
                            doctor.getInstance().setAppaterno(username.getString("ApellidoPaterno").toString());
                            doctor.getInstance().setApmaterno(username.getString("ApellidoMaterno").toString());
                            doctor.getInstance().setUsuario(usuario);
                            doctor.getInstance().setPassword(password);

                            Toast.makeText(MainActivity.this,
                                    "Bienvenido Doctor " + username.getString("ApellidoPaterno").toString(), Toast.LENGTH_LONG).show();
                            Log.d(TAG,response.toString());

                            //Se inicia la sesión de doctor y se carga la actividad
                            Intent intent = new Intent(MainActivity.this, Doctor_mapa.class);
                            session.logIn(usuario,"Doctor");

                            //Se agrega el objeto paciente al intent
                            intent.putExtra("doctor", doctor);

                            //Se inicia la actividad
                            startActivity(intent);
                        }

                        //Si falla la autenticación
                        else if (codigo_autenticacion.equals("04")) {
                            Toast.makeText(MainActivity.this, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                            Log.d(TAG,"Usuario o password incorrecto");
                        }
                    }
                    //En caso de excepción
                    catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override public void onErrorResponse(VolleyError error) {
                    barraDeProgreso.hide();
                    Toast.makeText(MainActivity.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                //Parámetros para la request
                @Override protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(USUARIO, usuario);
                    map.put(PASSWORD, password);
                    return map;
                }
                //Credenciales para la página
                @Override public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentiales = "A01021323:1021323";
                    String autenticacion = "Basic " + Base64.encodeToString(credentiales.getBytes(), Base64.NO_WRAP);
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", autenticacion);
                    return headers;
                }
            };

            //Se hace la petición al servicio
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(peticion);

        }
        //Si el usuario no ha ingresado un dato, ocultar la barra de progreso
        else{
            barraDeProgreso.hide();
        }
    }

    //Funcion para comprobar que el usuario llenó los campos indicados
    private boolean validateFields(EditText editText1, EditText editText2) {

        if (editText1.getText().toString().equals("")) {
            editText1.setError("Ingrese el usuario");
            return false;
        }
        else if (editText2.getText().toString().equals("")) {
            editText2.setError("Ingrese la contraseña");
            return false;
        }
        else
            return true;
    }

}
