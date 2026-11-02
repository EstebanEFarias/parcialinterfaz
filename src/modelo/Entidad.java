package modelo;

public abstract class Entidad {

    private static final double ENERGIA_BASE_ENVEJECIMIENTO = 2.0;

    private String nombre;
    private double energia;
    private int edad;
    private boolean viva;

    public Entidad(String nombre, double energia) {
        this.nombre = nombre;
        setEnergia(energia);
        this.edad = 0;
        this.viva = true;
    }

    public abstract void actuar(Ecosistema eco);

    public abstract String getTipo();

    public abstract String describirEstado();

    public void envejecer() {
        edad++;
        setEnergia(energia - ENERGIA_BASE_ENVEJECIMIENTO);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getEnergia() {
        return energia;
    }

    public void setEnergia(double energia) {
        this.energia = Math.max(energia, 0);
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        if (edad < 0) {
            throw new IllegalArgumentException("La edad no puede ser negativa.");
        }
        this.edad = edad;
    }

    public boolean isViva() {
        return viva;
    }

    public void setViva(boolean viva) {
        this.viva = viva;
    }
}