package csf.itesm.serviciomedico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Doctor_pacienteNuevo extends AppCompatActivity {

    EditText nombreTexto, appaternoTexto, apmaternoTexto, edadTexto, contactoEmergenciaTexto,
            numContactoEmergenciaTexto, padecimientosTexto, medicamentosTexto, matriculaTexto, passwordTexto;
    String nombre, edad, appaterno, apmaterno, contactoEmergencia, numContactoEmergencia,
            padecimientos, medicamentos, matricula, password, encryptedPassword;

    private Encryption encryption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_paciente_nuevo);

        encryption = new Encryption();
    }

    //Funcion para cargar la actividad de pacientes
    public void goToPatients(View view){
        Intent intent = new Intent (this, Doctor_listaPacientes.class);
        startActivity(intent);
    }

    public void agregarPaciente(View view){

        //Los EditText con el texto del usuario y de la contraseña son obtenidos
        nombreTexto = (EditText) findViewById(R.id.editText4);
        appaternoTexto = (EditText) findViewById(R.id.editText3);
        apmaternoTexto = (EditText) findViewById(R.id.editText10);
        edadTexto = (EditText) findViewById(R.id.editText5);
        padecimientosTexto = (EditText) findViewById(R.id.editText6);
        medicamentosTexto = (EditText) findViewById(R.id.editText7);
        contactoEmergenciaTexto = (EditText) findViewById(R.id.editText11);
        numContactoEmergenciaTexto = (EditText) findViewById(R.id.editText12);
        matriculaTexto = (EditText) findViewById(R.id.editText8);
        passwordTexto = (EditText) findViewById(R.id.editText9);

        //Se valida que el usuario haya llenado todos los campos
        if(validateFields(nombreTexto, appaternoTexto, apmaternoTexto,
                edadTexto, padecimientosTexto, medicamentosTexto, contactoEmergenciaTexto,
                numContactoEmergenciaTexto, matriculaTexto, passwordTexto)){

            //Los EditText son transformados a variables
            nombre = nombreTexto.getText().toString();
            appaterno = appaternoTexto.getText().toString();
            apmaterno = apmaternoTexto.getText().toString();
            edad = edadTexto.getText().toString();
            padecimientos = padecimientosTexto.getText().toString();
            medicamentos = medicamentosTexto.getText().toString();
            contactoEmergencia = contactoEmergenciaTexto.getText().toString();
            numContactoEmergencia = numContactoEmergenciaTexto.getText().toString();
            matricula = matriculaTexto.getText().toString();
            password = passwordTexto.getText().toString();

            //Encriptar la contraseña
            try{
                encryptedPassword = encryption.encryption(password);
            }
            catch (Exception e){
                Toast.makeText(Doctor_pacienteNuevo.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            //Response listener
            Response.Listener<String> responseListener = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        //Si se logra registrar al paciente
                        if (success){
                            Toast.makeText(Doctor_pacienteNuevo.this, "Paciente agregado exitosamente", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Doctor_pacienteNuevo.this, Doctor_listaPacientes.class);
                            Doctor_pacienteNuevo.this.startActivity(intent);
                        }
                        //Si falla el registro
                        else{
                            AlertDialog.Builder builder= new AlertDialog.Builder(Doctor_pacienteNuevo.this);
                            builder.setMessage("Registro fallido")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }
                    }
                    //En caso de una excepción
                    catch (JSONException e) {
                        Toast.makeText(Doctor_pacienteNuevo.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            };

            //Enviar la petición
            RequestRegistrar registerRequest = new RequestRegistrar(matricula, password, nombre,
                    appaterno, apmaterno, edad, padecimientos, medicamentos,
                    contactoEmergencia, numContactoEmergencia,responseListener);
            RequestQueue queue = Volley.newRequestQueue(Doctor_pacienteNuevo.this);
            queue.add(registerRequest);

        }

    }


    //Funcion para comprobar que el usuario llenó los campos indicados
    private boolean validateFields(EditText editText1, EditText editText2, EditText editText3,
                                   EditText editText4, EditText editText5, EditText editText6,
                                   EditText editText7, EditText editText8, EditText editText9,EditText editText10) {

        if (editText1.getText().equals("")) {
            editText1.setError("Ingrese el nombre.");
            return false;
        }
        else if (editText2.getText().toString().equals("")) {
            editText2.setError("Ingrese el apellido paterno");
            return false;
        }
        else if (editText3.getText().toString().equals("")) {
            editText2.setError("Ingrese el apellido materno");
            return false;
        }
        else if (editText4.getText().toString().equals("")) {
            editText2.setError("Ingrese la edad");
            return false;
        }
        else if (editText5.getText().toString().equals("")) {
            editText2.setError("Ingrese los padecimientos");
            return false;
        }
        else if (editText6.getText().toString().equals("")) {
            editText2.setError("Ingrese los medicamentos");
            return false;
        }
        else if (editText7.getText().toString().equals("")) {
            editText2.setError("Ingrese el nombre del contacto de emergencia");
            return false;
        }
        else if (editText8.getText().toString().equals("")) {
            editText2.setError("Ingrese el número del contacto de emergencia");
            return false;
        }
        else if (editText9.getText().toString().equals("")) {
            editText2.setError("Ingrese la matrícula del paciente");
            return false;
        }
        else if (editText10.getText().toString().equals("")) {
            editText2.setError("Ingrese la contraseña del paciente");
            return false;
        }
        else
            return true;
    }
}
