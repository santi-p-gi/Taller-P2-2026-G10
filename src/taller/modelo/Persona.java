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
     * @throws IllegalArgumentException si algun dato no tiene un formato valido
     */
    public Persona(String dni, String nombre, String apellido, String correoElectronico, String celular) {
        if (dni == null || !dni.matches("\\d{7,8}")) {
            throw new IllegalArgumentException("El DNI debe tener 7 u 8 digitos, sin puntos.");
        }
        if (nombre == null || !nombre.matches("[\\p{L} ]+")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios.");
        }
        if (apellido == null || !apellido.matches("[\\p{L} ]+")) {
            throw new IllegalArgumentException("El apellido solo puede contener letras y espacios.");
        }
        if (correoElectronico == null || !correoElectronico.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new IllegalArgumentException("El correo electronico debe tener el formato usuario@dominio.com.");
        }
        if (celular == null || !celular.matches("\\+?[0-9][0-9 -]{6,18}[0-9]")) {
            throw new IllegalArgumentException(
                    "El celular solo puede contener numeros, espacios y guiones (minimo 8 caracteres).");
        }
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
