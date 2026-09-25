package ecosistema;

public class PlantaVenenosa extends Planta implements Peligroso {

    private static final double DANIO_AL_SER_COMIDA = 30.0;

    public PlantaVenenosa(String nombre, double energia, int tamanio) {
        super(nombre, energia, tamanio);
    }

    @Override
    public double serComida() {
        super.serComida();
        return -DANIO_AL_SER_COMIDA;
    }

    @Override
    public int getNivelPeligro() {
        return 3;
    }
}
