package csf.itesm.serviciomedico;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestIncidente extends StringRequest {

    private static final String
            REGISTER_REQUEST_URL="http://ubiquitous.csf.itesm.mx/~pddm-1021323/content/Proyecto/REST/servicioCrearIncidente.php";

    private Map<String, String> params;

    public RequestIncidente(String matricula, String descripcion, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("Matricula", matricula);
        params.put("Descripcion", descripcion);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}