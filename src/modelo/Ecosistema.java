package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ecosistema {
    public static final int MIN_PLANTAS = 5, MAX_PLANTAS = 30;
    public static final int MIN_CONEJOS = 2, MAX_CONEJOS = 15;
    public static final int MIN_LOBOS = 1, MAX_LOBOS = 5;
    public static final int MIN_TURNOS = 10, MAX_TURNOS = 50;
    public static final int MAX_LOBOS_SIMULACION = 5;
    public static final int CADA_CUANTOS_TURNOS_INTERVENIR = 3;

    private static final double PROBABILIDAD_VENENOSA = 0.15;
    private static final Random RANDOM = new Random();

    private final ArrayList<Planta> plantas;
    private final ArrayList<Conejo> conejos;
    private final ArrayList<Lobo> lobos;
    private Clima climaActual;
    private int turnoActual;
    private final int turnosTotales;

    private int totalLobosCreados;

    private int nacimientosPlantas;
    private int nacimientosConejos;
    private int muertesPlantas;
    private int muertesConejos;
    private int muertesLobos;

    private int eventosTurnoActual;
    private int turnoMasActivo;
    private int maxEventosRegistrados;

    private int contadorPlantas;
    private int contadorConejos;
    private int contadorLobos;

    private boolean intervinoEsteTurno;
    private boolean terminada;
    private String causaFin;

    private final ArrayList<String> eventosTurno;
    private final List<int[]> historialPoblacion;

    public Ecosistema(Clima climaInicial, int turnosTotales) {
        this.plantas = new ArrayList<>();
        this.conejos = new ArrayList<>();
        this.lobos = new ArrayList<>();
        this.climaActual = climaInicial;
        this.turnosTotales = turnosTotales;
        this.turnoActual = 0;
        this.totalLobosCreados = 0;
        this.historialPoblacion = new ArrayList<>();
        this.eventosTurno = new ArrayList<>();
        this.maxEventosRegistrados = -1;
        this.turnoMasActivo = 0;
        this.terminada = false;
        this.causaFin = "";
    }

    public void poblarInicial(int cantPlantas, int cantConejos, int cantLobos) {
        for (int i = 0; i < cantPlantas; i++) {
            plantas.add(crearPlanta(20 + RANDOM.nextInt(20)));
        }
        for (int i = 0; i < cantConejos; i++) {
            conejos.add(crearConejo(40 + RANDOM.nextInt(20)));
        }
        for (int i = 0; i < cantLobos; i++) {
            lobos.add(crearLobo(50 + RANDOM.nextInt(20)));
        }
        anotar("Ecosistema creado con " + cantPlantas + " plantas, " + cantConejos
                + " conejos y " + cantLobos + " lobos. Clima: " + climaActual + ".");
    }

    public Planta crearPlanta(double energia) {
        contadorPlantas++;
        int tamanio = 1 + RANDOM.nextInt(5);
        if (RANDOM.nextDouble() < PROBABILIDAD_VENENOSA) {
            return new PlantaVenenosa("Cicuta-" + contadorPlantas, energia, tamanio);
        }
        return new Planta("Helecho-" + contadorPlantas, energia, tamanio);
    }

    public Conejo crearConejo(double energia) {
        contadorConejos++;
        return new Conejo("Conejo-" + contadorConejos, energia);
    }

    private Lobo crearLobo(double energia) {
        contadorLobos++;
        totalLobosCreados++;
        return new Lobo("Lobo-" + contadorLobos, energia);
    }

    public List<String> procesarTurno() {
        if (terminada) {
            return getEventosTurno();
        }
        turnoActual++;
        eventosTurnoActual = 0;
        eventosTurno.clear();
        intervinoEsteTurno = false;

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
                registrarEvento("Planta '" + p.getNombre() + "' se marchitó.");
            }
        }
        for (Conejo c : conejos) {
            if (c.isViva()) {
                c.verificarMuerte();
                if (!c.isViva()) {
                    muertesConejos++;
                    registrarEvento("Conejo '" + c.getNombre() + "' murió de inanición.");
                }
            }
        }
        for (Lobo l : lobos) {
            if (l.isViva()) {
                l.verificarMuerte();
                if (!l.isViva()) {
                    muertesLobos++;
                    registrarEvento("Lobo '" + l.getNombre() + "' murió de hambre.");
                }
            }
        }

        if (eventosTurno.isEmpty()) {
            anotar("Turno tranquilo: no pasó nada.");
        }

        registrarHistorial();
        if (eventosTurnoActual > maxEventosRegistrados) {
            maxEventosRegistrados = eventosTurnoActual;
            turnoMasActivo = turnoActual;
        }

        verificarFin();
        return getEventosTurno();
    }

    private void verificarFin() {
        if (ecosistemaColapsado()) {
            terminada = true;
            String extinta = poblacionExtinta();
            String articulo = extinta.equals("plantas") ? "las " : "los ";
            causaFin = "Colapso del ecosistema: se extinguieron " + articulo + extinta + ".";
        } else if (turnoActual >= turnosTotales) {
            terminada = true;
            causaFin = "Se completaron los " + turnosTotales + " turnos configurados.";
        }
        if (terminada) {
            anotar("FIN DE LA SIMULACIÓN. " + causaFin);
        }
    }

    private void registrarHistorial() {
        historialPoblacion.add(new int[]{
            turnoActual, contarPlantasVivas(), contarConejosVivos(), contarLobosVivos()
        });
    }

    public void registrarEvento(String texto) {
        eventosTurnoActual++;
        eventosTurno.add(texto);
    }

    private void anotar(String texto) {
        eventosTurno.add(texto);
    }

    public boolean puedeIntervenir() {
        return !terminada
                && turnoActual > 0
                && turnoActual % CADA_CUANTOS_TURNOS_INTERVENIR == 0
                && !intervinoEsteTurno;
    }

    public int getProximoTurnoIntervencion() {
        int resto = turnoActual % CADA_CUANTOS_TURNOS_INTERVENIR;
        return turnoActual + (CADA_CUANTOS_TURNOS_INTERVENIR - resto);
    }

    public boolean cambiarClima(Clima nuevo) {
        if (!puedeIntervenir()) {
            return false;
        }
        Clima anterior = climaActual;
        climaActual = nuevo;
        intervinoEsteTurno = true;
        anotar("INTERVENCIÓN: el clima cambió de " + anterior + " a " + nuevo + ".");
        return true;
    }

    public boolean puedeAgregarLobo() {
        return totalLobosCreados < MAX_LOBOS_SIMULACION;
    }

    public boolean agregarEntidad(String tipo) {
        if (!puedeIntervenir()) {
            return false;
        }
        Entidad nueva;
        switch (tipo.toLowerCase()) {
            case "planta":
                Planta p = crearPlanta(20 + RANDOM.nextInt(15));
                plantas.add(p);
                nueva = p;
                break;
            case "conejo":
                Conejo c = crearConejo(40 + RANDOM.nextInt(20));
                conejos.add(c);
                nueva = c;
                break;
            case "lobo":
                if (!puedeAgregarLobo()) {
                    return false;
                }
                Lobo l = crearLobo(50 + RANDOM.nextInt(20));
                lobos.add(l);
                nueva = l;
                break;
            default:
                return false;
        }
        intervinoEsteTurno = true;
        anotar("INTERVENCIÓN: se agregó " + nueva.getTipo().toLowerCase()
                + " '" + nueva.getNombre() + "'.");
        return true;
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

    public void sumarMuertePlanta() {
        muertesPlantas++;
    }

    public void sumarMuerteConejo() {
        muertesConejos++;
    }

    public int contarConejosVivos() {
        return contarVivas(conejos);
    }

    public int contarLobosVivos() {
        return contarVivas(lobos);
    }

    public int contarPlantasVivas() {
        return contarVivas(plantas);
    }

    private int contarVivas(List<? extends Entidad> lista) {
        int total = 0;
        for (Entidad e : lista) {
            if (e.isViva()) {
                total++;
            }
        }
        return total;
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

    public List<Entidad> getEntidadesVivas() {
        List<Entidad> vivas = new ArrayList<>();
        for (Entidad e : getTodasLasEntidades()) {
            if (e.isViva()) {
                vivas.add(e);
            }
        }
        return vivas;
    }

    public List<Entidad> getTodasLasEntidades() {
        List<Entidad> todas = new ArrayList<>();
        todas.addAll(plantas);
        todas.addAll(conejos);
        todas.addAll(lobos);
        return todas;
    }

    public List<String> getEventosTurno() {
        return new ArrayList<>(eventosTurno);
    }

    public Entidad getMasLongeva(String tipo) {
        List<? extends Entidad> lista;
        switch (tipo.toLowerCase()) {
            case "planta":
                lista = plantas;
                break;
            case "conejo":
                lista = conejos;
                break;
            default:
                lista = lobos;
        }
        Entidad masLongeva = null;
        for (Entidad e : lista) {
            if (masLongeva == null || e.getEdad() > masLongeva.getEdad()) {
                masLongeva = e;
            }
        }
        return masLongeva;
    }

    public Lobo getMejorCazador() {
        Lobo mejor = null;
        for (Lobo l : lobos) {
            if (mejor == null || l.getExitosCaza() > mejor.getExitosCaza()) {
                mejor = l;
            }
        }
        return mejor;
    }

    public int[] getMaximoYMinimo(int columna) {
        int max = -1, turnoMax = 0, min = Integer.MAX_VALUE, turnoMin = 0;
        for (int[] registro : historialPoblacion) {
            if (registro[columna] > max) {
                max = registro[columna];
                turnoMax = registro[0];
            }
            if (registro[columna] < min) {
                min = registro[columna];
                turnoMin = registro[0];
            }
        }
        if (historialPoblacion.isEmpty()) {
            return new int[]{0, 0, 0, 0};
        }
        return new int[]{max, turnoMax, min, turnoMin};
    }

    public List<int[]> getHistorialPoblacion() {
        List<int[]> copia = new ArrayList<>();
        for (int[] r : historialPoblacion) {
            copia.add(r.clone());
        }
        return copia;
    }

    public void restaurarDatos(int turnoActual, int totalLobosCreados,
            int nacimientosPlantas, int nacimientosConejos,
            int muertesPlantas, int muertesConejos, int muertesLobos,
            int turnoMasActivo, int maxEventosRegistrados,
            int contadorPlantas, int contadorConejos, int contadorLobos,
            boolean intervinoEsteTurno, boolean terminada, String causaFin) {
        if (turnoActual < 0 || turnoActual > turnosTotales) {
            throw new IllegalArgumentException("Turno guardado inválido.");
        }
        this.turnoActual = turnoActual;
        this.totalLobosCreados = totalLobosCreados;
        this.nacimientosPlantas = nacimientosPlantas;
        this.nacimientosConejos = nacimientosConejos;
        this.muertesPlantas = muertesPlantas;
        this.muertesConejos = muertesConejos;
        this.muertesLobos = muertesLobos;
        this.turnoMasActivo = turnoMasActivo;
        this.maxEventosRegistrados = maxEventosRegistrados;
        this.contadorPlantas = contadorPlantas;
        this.contadorConejos = contadorConejos;
        this.contadorLobos = contadorLobos;
        this.intervinoEsteTurno = intervinoEsteTurno;
        this.terminada = terminada;
        this.causaFin = causaFin;
    }

    public void restaurarEntidad(Entidad e) {
        if (e instanceof Planta) {
            plantas.add((Planta) e);
        } else if (e instanceof Conejo) {
            conejos.add((Conejo) e);
        } else if (e instanceof Lobo) {
            lobos.add((Lobo) e);
        }
    }

    public void restaurarHistorial(int turno, int cantPlantas, int cantConejos, int cantLobos) {
        historialPoblacion.add(new int[]{turno, cantPlantas, cantConejos, cantLobos});
    }

    public void restaurarEvento(String texto) {
        eventosTurno.add(texto);
    }

    public Clima getClimaActual() {
        return climaActual;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public int getTurnosTotales() {
        return turnosTotales;
    }

    public boolean estaTerminada() {
        return terminada;
    }

    public String getCausaFin() {
        return causaFin;
    }

    public boolean isIntervinoEsteTurno() {
        return intervinoEsteTurno;
    }

    public int getTotalLobosCreados() {
        return totalLobosCreados;
    }

    public int getNacimientosPlantas() {
        return nacimientosPlantas;
    }

    public int getNacimientosConejos() {
        return nacimientosConejos;
    }

    public int getMuertesPlantas() {
        return muertesPlantas;
    }

    public int getMuertesConejos() {
        return muertesConejos;
    }

    public int getMuertesLobos() {
        return muertesLobos;
    }

    public int getTurnoMasActivo() {
        return turnoMasActivo;
    }

    public int getMaxEventosRegistrados() {
        return maxEventosRegistrados;
    }

    public int getContadorPlantas() {
        return contadorPlantas;
    }

    public int getContadorConejos() {
        return contadorConejos;
    }

    public int getContadorLobos() {
        return contadorLobos;
    }
}