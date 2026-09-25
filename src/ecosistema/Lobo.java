package ecosistema;

import java.util.Random;

public class Lobo extends Animal implements Peligroso {

    private static final double ENERGIA_GANADA_CAZA = 30.0;
    private static final Random RANDOM = new Random();

    private int exitosCaza;

    public Lobo(String nombre, double energia) {
        super(nombre, energia, 12, 35.0);
        this.exitosCaza = 0;
    }

    @Override
    public void actuar(Ecosistema eco) {
        comer(eco);
    }

    @Override
    public void comer(Ecosistema eco) {
        Conejo presa = eco.elegirConejoAlAzar();
        if (presa == null) {
            System.out.println("Lobo '" + getNombre() + "' no encontró presas.");
            return;
        }

        double probabilidadExito = calcularProbabilidadCaza(eco.getClimaActual());
        eco.contarEvento();

        if (RANDOM.nextDouble() <= probabilidadExito) {
            presa.morir();
            setEnergia(getEnergia() + ENERGIA_GANADA_CAZA);
            exitosCaza++;
            System.out.println("Lobo '" + getNombre() + "' cazó a Conejo '" + presa.getNombre()
                    + "' (+" + (int) ENERGIA_GANADA_CAZA + " energia) [cacerias: " + exitosCaza + "]");
        } else {
            System.out.println("Lobo '" + getNombre() + "' falló la caza.");
        }
    }

    private double calcularProbabilidadCaza(Clima clima) {
        double base = Math.min(getEnergia() / 100.0, 0.9);
        return Math.min(base + clima.getBonusCazaLobo(), 0.95);
    }

    public void aplicarEfectoClima(Clima clima) {
        setEnergia(getEnergia() + clima.getEfectoEnergiaLobo());
    }

    @Override
    public void morir() {
        setViva(false);
        System.out.println("Lobo '" + getNombre() + "' murió de hambre.");
    }

    @Override
    public void mostrarEstado() {
        System.out.println("Lobo '" + getNombre() + "' | energia: " + (int) getEnergia()
                + " | cacerias exitosas: " + exitosCaza);
    }

    @Override
    public int getNivelPeligro() {
        return 5 + exitosCaza;
    }

    public int getExitosCaza() {
        return exitosCaza;
    }
}
