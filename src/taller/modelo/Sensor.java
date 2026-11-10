package taller.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import taller.enumeraciones.EstadoOperativo;
import taller.excepciones.EstacionFueraDeServicioException;
import taller.excepciones.LecturaInvalidaException;
import taller.excepciones.SensorNoOperativoException;

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

    public Sensor() {
        this.idSensor = ++generadorId;
        this.estado = EstadoOperativo.OPERATIVA;
        this.mantenimientos = new ArrayList<>();
        this.mediciones = new ArrayList<>();
    }

    public Sensor(String marca, String modelo, double precision, EstadoOperativo estado,
            LocalDateTime ultimaCalibracion, EstacionMeteorologica estacionMeteorologica) {
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

    public int getIdSensor() {
        return idSensor;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public EstadoOperativo getEstado() {
        return estado;
    }

    public void setEstado(EstadoOperativo estado) {
        this.estado = estado;
    }

    public LocalDateTime getUltimaCalibracion() {
        return ultimaCalibracion;
    }

    public void setUltimaCalibracion(LocalDateTime ultimaCalibracion) {
        this.ultimaCalibracion = ultimaCalibracion;
    }

    public EstacionMeteorologica getEstacionMeteorologica() {
        return estacionMeteorologica;
    }

    public void setEstacionMeteorologica(EstacionMeteorologica estacionMeteorologica) {
        this.estacionMeteorologica = estacionMeteorologica;
    }

    public List<Mantenimiento> getMantenimientos() {
        return mantenimientos;
    }

    public void setMantenimientos(List<Mantenimiento> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    public void agregarMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimientos.add(mantenimiento);
    }

    public List<Medicion> getMediciones() {
        return mediciones;
    }

    public void setMediciones(List<Medicion> mediciones) {
        this.mediciones = mediciones;
    }

    public Medicion obtenerUltimaMedicion() {
        if (mediciones.isEmpty()) {
            return null;
        }
        return mediciones.get(mediciones.size() - 1);
    }

    public List<Medicion> obtenerUltimasMediciones(int cantidad) {
        int totalMediciones = mediciones.size();
        if (cantidad <= 0 || totalMediciones == 0) {
            return new ArrayList<>();
        }
        int inicio = Math.max(0, totalMediciones - cantidad);
        return mediciones.subList(inicio, totalMediciones);
    }

    public List<Medicion> obtenerMedicionesAnomalas() {
        List<Medicion> medicionesAnomalas = new ArrayList<>();
        for (Medicion medicion : mediciones) {
            if (medicion.isAnomala()) {
                medicionesAnomalas.add(medicion);
            }
        }
        return medicionesAnomalas;
    }

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

    public void calibrar(LocalDateTime fechaCalibracion) {
        this.ultimaCalibracion = fechaCalibracion;
    }

    public void registrarMedicion(LocalDateTime fecha, double milimetros)
            throws LecturaInvalidaException, SensorNoOperativoException, EstacionFueraDeServicioException {
        if (!validar(milimetros)) {
            throw new LecturaInvalidaException("Valor de milímetros inválido: " + milimetros);
        }
        if (this.estado != EstadoOperativo.OPERATIVA) {
            throw new SensorNoOperativoException("El sensor no está operativo y no puede registrar mediciones.");
        }
        if (estacionMeteorologica == null || !estacionMeteorologica.estaOperativa()) {
            throw new EstacionFueraDeServicioException("La estación meteorológica asociada no está operativa.");
        }
        Medicion anterior = obtenerUltimaMedicion();
        boolean anomala = anterior != null && Math.abs(milimetros - anterior.getMilimetros()) > 100;

        this.mediciones.add(new Medicion(fecha, milimetros, anomala));

    }

    public boolean requiereCalibracion() {
        if (this.ultimaCalibracion == null) {
            return true;
        } else {
            LocalDateTime limite = LocalDateTime.now().minusMonths(12);
            return this.ultimaCalibracion.isBefore(limite);
        }
    }

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
