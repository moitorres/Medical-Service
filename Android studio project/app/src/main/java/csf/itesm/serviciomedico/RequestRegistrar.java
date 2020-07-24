package csf.itesm.serviciomedico;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestRegistrar extends StringRequest {

    private static final String
            REGISTER_REQUEST_URL="http://ubiquitous.csf.itesm.mx/~pddm-1021323/content/Proyecto/REST/servicioCrearPaciente.php";

    private Map<String, String> params;

    public RequestRegistrar(String matricula, String password, String nombre, String appaterno, String apmaterno,
                            String edad, String padecimientos, String medicamentos, String contactoEmergencia,
                            String numContactoEmergencia, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("Matricula", matricula);
        params.put("Password", password);
        params.put("Nombre", nombre);
        params.put("ApellidoPaterno", appaterno);
        params.put("ApellidoMaterno", apmaterno);
        params.put("Edad", edad);
        params.put("Padecimientos", padecimientos);
        params.put("Medicamentos", medicamentos);
        params.put("NombreContactoEmergencia", contactoEmergencia);
        params.put("NumContactoEmergencia", numContactoEmergencia);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}