package modelo;

import java.util.Random;

public class Planta extends Entidad implements Reproducible {
    private static final double ENERGIA_MINIMA_REPRODUCCION = 25.0;
    private static final double PROBABILIDAD_BASE_REPRODUCCION = 0.35;
    private static final double COSTO_ENERGIA_REPRODUCCION = 12.0;
    private static final Random RANDOM = new Random();

    private int tamanio;

    public Planta(String nombre, double energia, int tamanio) {
        super(nombre, energia);
        setTamanio(tamanio);
    }

    @Override
    public void actuar(Ecosistema eco) {
        intentarReproduccion(eco);
    }

    @Override
    public String getTipo() {
        return "Planta";
    }

    @Override
    public String describirEstado() {
        return "Tamaño " + tamanio;
    }

    @Override
    public boolean puedeReproducirse() {
        return isViva() && getEnergia() >= ENERGIA_MINIMA_REPRODUCCION;
    }

    @Override
    public void reproducirse(Ecosistema eco) {
        double factorClima = eco.getClimaActual().getFactorReproduccionPlanta();
        if (factorClima <= 0) {
            return;
        }
        double probabilidad = Math.min(PROBABILIDAD_BASE_REPRODUCCION * factorClima, 0.95);
        if (RANDOM.nextDouble() > probabilidad) {
            return;
        }

        Planta nueva = eco.crearPlanta(20 + RANDOM.nextInt(20));
        eco.registrarNuevaPlanta(nueva);
        setEnergia(getEnergia() - COSTO_ENERGIA_REPRODUCCION);

        eco.registrarEvento("Nació " + nueva.getTipo().toLowerCase() + " '" + nueva.getNombre()
                + "' (de '" + getNombre() + "')");
    }

    public double serComida() {
        double valorNutritivo = tamanio * 10;
        setEnergia(0);
        setViva(false);
        return valorNutritivo;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        if (tamanio < 1 || tamanio > 5) {
            throw new IllegalArgumentException("El tamaño de la planta debe estar entre 1 y 5.");
        }
        this.tamanio = tamanio;
    }
}