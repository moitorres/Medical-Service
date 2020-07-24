package csf.itesm.serviciomedico;

import java.io.Serializable;

public class ObjetoPaciente implements Serializable {
    private String Matricula;
    private String Nombre;
    private String Appaterno;
    private String Apmaterno;
    private String Padecimientos;
    private String Medicamentos;
    private Long Edad;
    private String Descripcion;
    private String NombreContactoEmergencia;
    private String NumContactoEmergencia;

    private String password;


    public String getMatricula() {
        return Matricula;
    }

    public void setMatricula(String Matricula) {
        this.Matricula = Matricula;
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

    public String getPadecimientos() {
        return Padecimientos;
    }

    public void setPadecimientos(String Padecimientos) {
        this.Padecimientos = Padecimientos;
    }

    public String getMedicamentos() {
        return Medicamentos;
    }

    public void setMedicamentos(String Medicamentos) {
        this.Medicamentos = Medicamentos;
    }

    public Long getEdad() {
        return Edad;
    }

    public void setEdad(Long Edad) {
        this.Edad = Edad;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getNombreContactoEmergencia() {
        return NombreContactoEmergencia;
    }

    public void setNombreContactoEmergencia(String NombreContactoEmergencia) {
        this.NombreContactoEmergencia = NombreContactoEmergencia;
    }

    public String getNumContactoEmergencia() {
        return NumContactoEmergencia;
    }

    public void setNumContactoEmergencia(String NumContactoEmergencia) {
        this.NumContactoEmergencia = NumContactoEmergencia;
    }

    private static final ObjetoPaciente info = new ObjetoPaciente();

    public static ObjetoPaciente getInstance() {
        return info;
    }
}
