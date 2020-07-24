package csf.itesm.serviciomedico;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void logIn(String usuario, String status) {
        prefs.edit().putString("Usuario", usuario).commit();
        prefs.edit().putString("Status",status).commit();
    }

    //Al hacer logut se borran todas las preferencias
    public void logOut(){
        prefs.edit().clear().commit();
    }

    public String getUsuario() {
        String usuario = prefs.getString("Usuario","");
        return usuario;
    }

    public String getStatus(){
        String status = prefs.getString("Status","");
        return status;
    }

    public boolean isLoggedIn(){

        if(!prefs.getString("Usuario","").equals(""))
            return true;
        else
            return false;

    }
}
