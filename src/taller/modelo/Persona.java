package taller.modelo;

public abstract class Persona {
    String dni;
    String nombre;
    String correoElectronico;
    String celular;

    public Persona(String dni, String nombre, String correoElectronico, String celular) {
        this.dni = dni;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.celular = celular;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

}
