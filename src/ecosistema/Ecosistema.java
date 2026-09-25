package ecosistema;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ecosistema {

    private static final int MAX_LOBOS_SIMULACION = 5;
    private static final Random RANDOM = new Random();

    private final ArrayList<Planta> plantas;
    private final ArrayList<Conejo> conejos;
    private final ArrayList<Lobo> lobos;
    private Clima climaActual;
    private int turnoActual;

    private int totalLobosCreados;

    private int nacimientosPlantas;
    private int nacimientosConejos;
    private int muertesPlantas;
    private int muertesConejos;
    private int muertesLobos;

    private int eventosTurnoActual;
    private int turnoMasActivo;
    private int maxEventosRegistrados;

    private final List<int[]> historialPoblacion;

    private int contadorPlantasIniciales = 0;
    private int contadorConejosIniciales = 0;
    private int contadorLobosIniciales = 0;

    public Ecosistema(Clima climaInicial) {
        this.plantas = new ArrayList<>();
        this.conejos = new ArrayList<>();
        this.lobos = new ArrayList<>();
        this.climaActual = climaInicial;
        this.turnoActual = 0;
        this.totalLobosCreados = 0;
        this.historialPoblacion = new ArrayList<>();
        this.maxEventosRegistrados = -1;
        this.turnoMasActivo = 0;
    }

    public void procesarTurno() {
        turnoActual++;
        eventosTurnoActual = 0;

        System.out.println("\n=== TURNO " + turnoActual + " | Clima: " + climaActual + " ===");
        mostrarEstado();
        System.out.println("-- Eventos --");

        for (Planta p : new ArrayList<>(plantas)) {
            if (p.isViva()) {
                p.actuar(this);
            }
        }

        for (Conejo c : new ArrayList<>(conejos)) {
            if (c.isViva()) {
                c.actuar(this);
            }
        }

        for (Lobo l : new ArrayList<>(lobos)) {
            if (l.isViva()) {
                l.actuar(this);
            }
        }

        for (Planta p : plantas) {
            if (p.isViva()) {
                p.envejecer();
            }
        }
        for (Conejo c : conejos) {
            if (c.isViva()) {
                c.envejecer();
                c.aplicarEfectoClima(climaActual);
            }
        }
        for (Lobo l : lobos) {
            if (l.isViva()) {
                l.envejecer();
                l.aplicarEfectoClima(climaActual);
            }
        }

        for (Planta p : plantas) {
            if (p.isViva() && p.getEnergia() <= 0) {
                p.setViva(false);
                muertesPlantas++;
                System.out.println("Planta '" + p.getNombre() + "' se marchitó.");
            }
        }
        for (Conejo c : conejos) {
            if (c.isViva()) {
                c.verificarMuerte();
                if (!c.isViva()) {
                    muertesConejos++;
                }
            }
        }
        for (Lobo l : lobos) {
            if (l.isViva()) {
                l.verificarMuerte();
                if (!l.isViva()) {
                    muertesLobos++;
                }
            }
        }

        System.out.print("Estado: ");
        mostrarEstado();

        registrarHistorial();
        if (eventosTurnoActual > maxEventosRegistrados) {
            maxEventosRegistrados = eventosTurnoActual;
            turnoMasActivo = turnoActual;
        }
    }

    private void registrarHistorial() {
        historialPoblacion.add(new int[]{
            turnoActual, contarPlantasVivas(), contarConejosVivos(), contarLobosVivos()
        });
    }

    public void contarEvento() {
        eventosTurnoActual++;
    }

    public void agregarEntidad(String tipo, double energiaInicial) {
        switch (tipo.toLowerCase()) {
            case "planta":
                contadorPlantasIniciales++;
                Planta p = new Planta("Helecho-" + contadorPlantasIniciales,
                        energiaInicial, 1 + RANDOM.nextInt(5));
                plantas.add(p);
                System.out.println("Se agregó '" + p.getNombre() + "' al ecosistema.");
                break;

            case "conejo":
                contadorConejosIniciales++;
                Conejo c = new Conejo("Conejo-" + contadorConejosIniciales, energiaInicial);
                conejos.add(c);
                System.out.println("Se agregó '" + c.getNombre() + "' al ecosistema.");
                break;

            case "lobo":
                if (totalLobosCreados >= MAX_LOBOS_SIMULACION) {
                    System.out.println("No se puede agregar: ya se alcanzó el máximo de "
                            + MAX_LOBOS_SIMULACION + " lobos para toda la simulación.");
                    return;
                }
                contadorLobosIniciales++;
                totalLobosCreados++;
                Lobo l = new Lobo("Lobo-" + contadorLobosIniciales, energiaInicial);
                lobos.add(l);
                System.out.println("Se agregó '" + l.getNombre() + "' al ecosistema.");
                break;

            default:
                System.out.println("Tipo de entidad desconocido: " + tipo);
        }
    }

    public void agregarEntidad(String tipo) {
        double energiaPorDefecto;
        switch (tipo.toLowerCase()) {
            case "planta":
                energiaPorDefecto = 20 + RANDOM.nextInt(15);
                break;
            case "conejo":
                energiaPorDefecto = 40 + RANDOM.nextInt(20);
                break;
            case "lobo":
                energiaPorDefecto = 50 + RANDOM.nextInt(20);
                break;
            default:
                energiaPorDefecto = 30;
        }
        agregarEntidad(tipo, energiaPorDefecto);
    }

    public void cambiarClima(Clima nuevo) {
        this.climaActual = nuevo;
        System.out.println("El clima cambió a " + nuevo + ".");
    }

    public Planta buscarPlantaViva() {
        List<Planta> vivas = new ArrayList<>();
        for (Planta p : plantas) {
            if (p.isViva()) {
                vivas.add(p);
            }
        }
        if (vivas.isEmpty()) {
            return null;
        }
        return vivas.get(RANDOM.nextInt(vivas.size()));
    }

    public Conejo elegirConejoAlAzar() {
        List<Conejo> vivos = new ArrayList<>();
        for (Conejo c : conejos) {
            if (c.isViva()) {
                vivos.add(c);
            }
        }
        if (vivos.isEmpty()) {
            return null;
        }
        return vivos.get(RANDOM.nextInt(vivos.size()));
    }

    public void registrarNuevaPlanta(Planta p) {
        plantas.add(p);
        nacimientosPlantas++;
    }

    public void registrarNuevoConejo(Conejo c) {
        conejos.add(c);
        nacimientosConejos++;
    }

    public int contarConejosVivos() {
        int total = 0;
        for (Conejo c : conejos) {
            if (c.isViva()) {
                total++;
            }
        }
        return total;
    }

    public int contarLobosVivos() {
        int total = 0;
        for (Lobo l : lobos) {
            if (l.isViva()) {
                total++;
            }
        }
        return total;
    }

    public int contarPlantasVivas() {
        int total = 0;
        for (Planta p : plantas) {
            if (p.isViva()) {
                total++;
            }
        }
        return total;
    }

    public void mostrarEstado() {
        System.out.println("Plantas: " + contarPlantasVivas()
                + " Conejos: " + contarConejosVivos()
                + " Lobos: " + contarLobosVivos());
    }

    public boolean ecosistemaColapsado() {
        return contarPlantasVivas() == 0 || contarConejosVivos() == 0 || contarLobosVivos() == 0;
    }

    public String poblacionExtinta() {
        if (contarPlantasVivas() == 0) {
            return "plantas";
        }
        if (contarConejosVivos() == 0) {
            return "conejos";
        }
        if (contarLobosVivos() == 0) {
            return "lobos";
        }
        return "ninguna";
    }

    public void generarReporteFinal(String causaFin) {
        System.out.println("\n==================== REPORTE FINAL ====================");
        System.out.println("Turnos jugados: " + turnoActual);
        System.out.println("Causa de fin: " + causaFin);
        System.out.println("Turno de mayor actividad: " + turnoMasActivo
                + " (" + maxEventosRegistrados + " eventos)");

        System.out.println("\n-- Longevidad --");
        mostrarMasLongeva("Planta", plantas);
        mostrarMasLongeva("Conejo", conejos);
        mostrarMasLongeva("Lobo", lobos);

        Lobo mejorCazador = null;
        for (Lobo l : lobos) {
            if (mejorCazador == null || l.getExitosCaza() > mejorCazador.getExitosCaza()) {
                mejorCazador = l;
            }
        }
        System.out.println("\n-- Mejor cazador --");
        if (mejorCazador != null) {
            System.out.println("Lobo '" + mejorCazador.getNombre() + "' con "
                    + mejorCazador.getExitosCaza() + " cacerias exitosas.");
        } else {
            System.out.println("No hubo lobos en la simulación.");
        }

        System.out.println("\n-- Nacimientos y muertes --");
        System.out.println("Plantas -> nacimientos: " + nacimientosPlantas + " | muertes: " + muertesPlantas);
        System.out.println("Conejos -> nacimientos: " + nacimientosConejos + " | muertes: " + muertesConejos);
        System.out.println("Lobos   -> nacimientos: 0 | muertes: " + muertesLobos);

        mostrarMaximosYMinimosPoblacion();
        mostrarElementosPeligrosos();

        System.out.println("=========================================================");
    }

    private void mostrarMasLongeva(String tipo, List<? extends Entidad> lista) {
        Entidad masLongeva = null;
        for (Entidad e : lista) {
            if (masLongeva == null || e.getEdad() > masLongeva.getEdad()) {
                masLongeva = e;
            }
        }
        if (masLongeva != null) {
            System.out.println(tipo + " mas longeva: '" + masLongeva.getNombre()
                    + "' con " + masLongeva.getEdad() + " turnos de edad.");
        } else {
            System.out.println(tipo + ": no hubo entidades de este tipo.");
        }
    }

    private void mostrarMaximosYMinimosPoblacion() {
        if (historialPoblacion.isEmpty()) {
            return;
        }
        int maxPlantas = -1, minPlantas = Integer.MAX_VALUE, turnoMaxPlantas = 0, turnoMinPlantas = 0;
        int maxConejos = -1, minConejos = Integer.MAX_VALUE, turnoMaxConejos = 0, turnoMinConejos = 0;
        int maxLobos = -1, minLobos = Integer.MAX_VALUE, turnoMaxLobos = 0, turnoMinLobos = 0;

        for (int[] registro : historialPoblacion) {
            int turno = registro[0], p = registro[1], c = registro[2], l = registro[3];
            if (p > maxPlantas) { maxPlantas = p; turnoMaxPlantas = turno; }
            if (p < minPlantas) { minPlantas = p; turnoMinPlantas = turno; }
            if (c > maxConejos) { maxConejos = c; turnoMaxConejos = turno; }
            if (c < minConejos) { minConejos = c; turnoMinConejos = turno; }
            if (l > maxLobos) { maxLobos = l; turnoMaxLobos = turno; }
            if (l < minLobos) { minLobos = l; turnoMinLobos = turno; }
        }

        System.out.println("\n-- Estadisticas de poblacion --");
        System.out.println("Plantas -> maximo: " + maxPlantas + " (turno " + turnoMaxPlantas
                + ") | minimo: " + minPlantas + " (turno " + turnoMinPlantas + ")");
        System.out.println("Conejos -> maximo: " + maxConejos + " (turno " + turnoMaxConejos
                + ") | minimo: " + minConejos + " (turno " + turnoMinConejos + ")");
        System.out.println("Lobos   -> maximo: " + maxLobos + " (turno " + turnoMaxLobos
                + ") | minimo: " + minLobos + " (turno " + turnoMinLobos + ")");
    }

    private void mostrarElementosPeligrosos() {
        List<Peligroso> peligrosos = new ArrayList<>();
        peligrosos.addAll(lobos);
        for (Planta p : plantas) {
            if (p instanceof Peligroso) {
                peligrosos.add((Peligroso) p);
            }
        }
        if (peligrosos.isEmpty()) {
            return;
        }
        peligrosos.sort((a, b) -> Integer.compare(b.getNivelPeligro(), a.getNivelPeligro()));

        System.out.println("\n-- Elementos peligrosos --");
        for (Peligroso elem : peligrosos) {
            Entidad e = (Entidad) elem;
            System.out.println("'" + e.getNombre() + "' | nivel de peligro: " + elem.getNivelPeligro());
        }
    }

    public Clima getClimaActual() {
        return climaActual;
    }

    public int getTurnoActual() {
        return turnoActual;
    }
}
