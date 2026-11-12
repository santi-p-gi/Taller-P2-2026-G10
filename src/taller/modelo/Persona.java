package taller.modelo;

/**
 * Datos basicos de identificacion de una persona. Es heredada por ResponsableTecnico.
 */
public abstract class Persona {
    private String dni;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String celular;

    /**
     * Crea una persona con sus datos de identificacion.
     *
     * @param dni               documento, identifica a la persona
     * @param nombre            nombre
     * @param apellido          apellido
     * @param correoElectronico correo electronico
     * @param celular           numero de celular
     */
    public Persona(String dni, String nombre, String apellido, String correoElectronico, String celular) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    /**
     * Devuelve el nombre y el apellido juntos.
     *
     * @return nombre completo de la persona
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

}
