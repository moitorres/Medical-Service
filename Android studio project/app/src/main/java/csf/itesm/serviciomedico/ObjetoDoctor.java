package csf.itesm.serviciomedico;

import java.io.Serializable;

public class ObjetoDoctor implements Serializable {

    private String Usuario;
    private String Nombre;
    private String Appaterno;
    private String Apmaterno;


    private String password;


    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getAppaterno() {
        return Appaterno;
    }

    public void setAppaterno(String Appaterno) {
        this.Appaterno = Appaterno;
    }

    public String getApmaterno() {
        return Apmaterno;
    }

    public void setApmaterno(String Apmaterno) {
        this.Apmaterno = Apmaterno;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private static final ObjetoDoctor info = new ObjetoDoctor();

    public static ObjetoDoctor getInstance() {
        return info;
    }
}
