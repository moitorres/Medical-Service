package csf.itesm.serviciomedico;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestMandarUbicacion extends StringRequest{

    private static final String
            LOCATION_REQUEST_URL="http://ubiquitous.csf.itesm.mx/~pddm-1021323/content/Proyecto/REST/servicioMandarUbicacion.php";

    private Map<String, String> params;

    public RequestMandarUbicacion(String matricula, String Longitud, String Latitud, Response.Listener<String> listener){
        super(Method.POST, LOCATION_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("Matricula", matricula);
        params.put("Longitud", Longitud);
        params.put("Latitud", Latitud);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }

}
