package taller.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import taller.enumeraciones.EstadoOperativo;
import taller.excepciones.EstacionFueraDeServicioException;
import taller.excepciones.LecturaInvalidaException;
import taller.excepciones.SensorNoOperativoException;

/**
 * Pluviometro que registra lecturas de precipitacion. Puede estar instalado en a lo sumo una estacion
 * y compone su historial de mediciones.
 */
public class Sensor {
    private static int generadorId;
    private int idSensor;
    private String marca;
    private String modelo;
    private double precision;
    private EstadoOperativo estado;
    private LocalDateTime ultimaCalibracion;
    private EstacionMeteorologica estacionMeteorologica;
    private List<Mantenimiento> mantenimientos;
    private List<Medicion> mediciones;

    /**
     * Crea un sensor operativo y sin instalar, con ID autogenerado.
     */
    public Sensor() {
        this.idSensor = ++generadorId;
        this.estado = EstadoOperativo.OPERATIVA;
        this.mantenimientos = new ArrayList<>();
        this.mediciones = new ArrayList<>();
    }

    /**
     * Crea un sensor con ID autogenerado.
     *
     * @param marca                 marca
     * @param modelo                modelo
     * @param precision             precision de medicion, en mm
     * @param estado                estado operativo inicial
     * @param ultimaCalibracion     fecha de la ultima calibracion
     * @param estacionMeteorologica estacion donde esta instalado, o null
     */
    public Sensor(String marca, String modelo, double precision, EstadoOperativo estado,
            LocalDateTime ultimaCalibracion, EstacionMeteorologica estacionMeteorologica) {
        if (!(precision > 0)) {
            throw new IllegalArgumentException("La precision debe ser mayor a 0.");
        }
        validarFechaCalibracion(ultimaCalibracion);
        this.idSensor = ++generadorId;
        this.marca = marca;
        this.modelo = modelo;
        this.precision = precision;
        this.estado = estado;
        this.ultimaCalibracion = ultimaCalibracion;
        this.estacionMeteorologica = estacionMeteorologica;
        this.mantenimientos = new ArrayList<>();
        this.mediciones = new ArrayList<>();
    }

    /** {@return el identificador del sensor} */
    public int getIdSensor() {
        return idSensor;
    }

    /** {@return la marca del sensor} */
    public String getMarca() {
        return marca;
    }

    /**
     * Modifica la marca del sensor.
     *
     * @param marca el nuevo valor
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /** {@return el modelo del sensor} */
    public String getModelo() {
        return modelo;
    }

    /**
     * Modifica el modelo del sensor.
     *
     * @param modelo el nuevo valor
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /** {@return la precision del sensor, en mm} */
    public double getPrecision() {
        return precision;
    }

    /**
     * Modifica la precision del sensor, en mm.
     *
     * @param precision el nuevo valor
     */
    public void setPrecision(double precision) {
        this.precision = precision;
    }

    /** {@return el estado operativo del sensor} */
    public EstadoOperativo getEstado() {
        return estado;
    }

    /**
     * Modifica el estado operativo del sensor.
     *
     * @param estado el nuevo valor
     */
    public void setEstado(EstadoOperativo estado) {
        this.estado = estado;
    }

    /** {@return la fecha de la ultima calibracion del sensor} */
    public LocalDateTime getUltimaCalibracion() {
        return ultimaCalibracion;
    }

    /**
     * Modifica la fecha de la ultima calibracion del sensor.
     *
     * @param ultimaCalibracion el nuevo valor
     */
    public void setUltimaCalibracion(LocalDateTime ultimaCalibracion) {
        this.ultimaCalibracion = ultimaCalibracion;
    }

    /** {@return la estacion del sensor} */
    public EstacionMeteorologica getEstacionMeteorologica() {
        return estacionMeteorologica;
    }

    /**
     * Modifica la estacion del sensor.
     *
     * @param estacionMeteorologica el nuevo valor
     */
    public void setEstacionMeteorologica(EstacionMeteorologica estacionMeteorologica) {
        this.estacionMeteorologica = estacionMeteorologica;
    }

    /** {@return los mantenimientos del sensor} */
    public List<Mantenimiento> getMantenimientos() {
        return mantenimientos;
    }

    /**
     * Modifica los mantenimientos del sensor.
     *
     * @param mantenimientos el nuevo valor
     */
    public void setMantenimientos(List<Mantenimiento> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    /**
     * Agrega un mantenimiento al historial del sensor.
     *
     * @param mantenimiento mantenimiento a agregar
     */
    public void agregarMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimientos.add(mantenimiento);
    }

    /** {@return las mediciones del sensor} */
    public List<Medicion> getMediciones() {
        return mediciones;
    }

    /**
     * Modifica las mediciones del sensor.
     *
     * @param mediciones el nuevo valor
     */
    public void setMediciones(List<Medicion> mediciones) {
        this.mediciones = mediciones;
    }

    /**
     * Devuelve la ultima medicion registrada.
     *
     * @return la ultima medicion, o null si no tiene mediciones
     */
    public Medicion obtenerUltimaMedicion() {
        if (mediciones.isEmpty()) {
            return null;
        }
        return mediciones.get(mediciones.size() - 1);
    }

    /**
     * Devuelve las ultimas mediciones registradas, de la mas antigua a la mas reciente.
     *
     * @param cantidad cantidad maxima de mediciones a devolver
     * @return lista con las ultimas mediciones
     */
    public List<Medicion> obtenerUltimasMediciones(int cantidad) {
        List<Medicion> ultimas = new ArrayList<>();
        int inicio = Math.max(0, mediciones.size() - cantidad);
        for (int i = inicio; i < mediciones.size(); i++) {
            ultimas.add(mediciones.get(i));
        }
        return ultimas;
    }

    /**
     * Devuelve las mediciones marcadas como anomalas.
     *
     * @return lista de mediciones anomalas
     */
    public List<Medicion> obtenerMedicionesAnomalas() {
        List<Medicion> medicionesAnomalas = new ArrayList<>();
        for (Medicion medicion : mediciones) {
            if (medicion.isAnomala()) {
                medicionesAnomalas.add(medicion);
            }
        }
        return medicionesAnomalas;
    }

    /**
     * Suma los milimetros registrados en un rango de fechas.
     *
     * @param fechaInicio inicio del rango (incluido)
     * @param fechaFin    fin del rango (incluido)
     * @return milimetros acumulados
     */
    public double acumuladorEntre(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Medicion> medicionesEnRango = new ArrayList<>();
        for (Medicion medicion : mediciones) {
            if ((medicion.getFechaHora().isEqual(fechaInicio) || medicion.getFechaHora().isAfter(fechaInicio))
                    && (medicion.getFechaHora().isEqual(fechaFin) || medicion.getFechaHora().isBefore(fechaFin))) {
                medicionesEnRango.add(medicion);
            }
        }
        double acumulado = 0;
        for (Medicion medicion : medicionesEnRango) {
            acumulado += medicion.getMilimetros();
        }
        return acumulado;
    }

    /**
     * Registra una calibracion del sensor.
     *
     * @param fechaCalibracion fecha de la calibracion
     */
    public void calibrar(LocalDateTime fechaCalibracion) {
        validarFechaCalibracion(fechaCalibracion);
        this.ultimaCalibracion = fechaCalibracion;
    }

    private void validarFechaCalibracion(LocalDateTime fecha) {
        if (fecha != null && fecha.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de calibracion no puede ser posterior a la fecha actual.");
        }
    }

    /**
     * Registra una lectura de precipitacion. Si difiere en mas de 100 mm de la anterior, queda marcada
     * como anomala.
     *
     * @param milimetros milimetros registrados
     * @param fechaHora  fecha y hora de la lectura
     * @return la medicion registrada
     * @throws LecturaInvalidaException          si el valor no esta entre 0 y 500 mm
     * @throws SensorNoOperativoException        si el sensor no esta operativo
     * @throws EstacionFueraDeServicioException si el sensor no esta instalado o su estacion no esta operativa
     */
    public Medicion registrar(double milimetros, LocalDateTime fechaHora)
            throws LecturaInvalidaException, SensorNoOperativoException, EstacionFueraDeServicioException {
        if (!validar(milimetros)) {
            throw new LecturaInvalidaException("Valor de milimetros invalido: " + milimetros);
        }
        if (this.estado != EstadoOperativo.OPERATIVA) {
            throw new SensorNoOperativoException("El sensor no esta operativo y no puede registrar mediciones.");
        }
        if (estacionMeteorologica == null || !estacionMeteorologica.estaOperativa()) {
            throw new EstacionFueraDeServicioException("La estacion meteorologica asociada no esta operativa.");
        }
        if (fechaHora.isAfter(LocalDateTime.now())) {
            throw new LecturaInvalidaException("La fecha de la lectura no puede ser posterior a la fecha actual.");
        }
        Medicion anterior = obtenerUltimaMedicion();
        if (anterior != null && fechaHora.isBefore(anterior.getFechaHora())) {
            throw new LecturaInvalidaException("La lectura no puede ser anterior a la ultima lectura del sensor ("
                    + anterior.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ").");
        }
        boolean anomala = anterior != null && Math.abs(milimetros - anterior.getMilimetros()) > 100;

        Medicion medicion = new Medicion(fechaHora, milimetros, anomala);
        this.mediciones.add(medicion);
        return medicion;
    }

    /**
     * Indica si pasaron mas de 12 meses desde la ultima calibracion, o si nunca se calibro.
     *
     * @return true si requiere calibracion
     */
    public boolean requiereCalibracion() {
        if (this.ultimaCalibracion == null) {
            return true;
        } else {
            LocalDateTime limite = LocalDateTime.now().minusMonths(12);
            return this.ultimaCalibracion.isBefore(limite);
        }
    }

    /**
     * Valida que una lectura este dentro del rango fisico admitido.
     *
     * @param milimetros valor a validar
     * @return true si esta entre 0 y 500 mm
     */
    public boolean validar(double milimetros) {
        return milimetros >= 0.0 && milimetros <= 500.0;
    }

    public String toString() {
        return "Sensor [idSensor=" + idSensor + ", marca=" + marca + ", modelo=" + modelo + ", precision=" + precision
                + ", estado=" + estado + ", ultimaCalibracion=" + ultimaCalibracion
                + ", estacionMeteorologica="
                + (estacionMeteorologica != null ? estacionMeteorologica.getIdEstacion() : "null") + "]";
    }
}
