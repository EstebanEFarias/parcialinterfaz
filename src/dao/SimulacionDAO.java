package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import modelo.Clima;
import modelo.Conejo;
import modelo.Ecosistema;
import modelo.Entidad;
import modelo.Lobo;
import modelo.Planta;
import modelo.PlantaVenenosa;

public class SimulacionDAO {
    private static final String ARCHIVO = "simulacion.txt";
    private static final String SEPARADOR = ";";

    public boolean existeGuardado() {
        return new File(ARCHIVO).exists();
    }

    public boolean guardar(Ecosistema eco) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO))) {
            escribir(escritor, "CONFIG", eco.getClimaActual().name(), eco.getTurnosTotales());
            escribir(escritor, "ESTADO", eco.getTurnoActual(), eco.getTotalLobosCreados(),
                    eco.getNacimientosPlantas(), eco.getNacimientosConejos(),
                    eco.getMuertesPlantas(), eco.getMuertesConejos(), eco.getMuertesLobos(),
                    eco.getTurnoMasActivo(), eco.getMaxEventosRegistrados(),
                    eco.getContadorPlantas(), eco.getContadorConejos(), eco.getContadorLobos(),
                    eco.isIntervinoEsteTurno(), eco.estaTerminada(), limpiar(eco.getCausaFin()));

            for (Entidad e : eco.getTodasLasEntidades()) {
                escritor.write(entidadATexto(e));
                escritor.newLine();
            }
            for (int[] r : eco.getHistorialPoblacion()) {
                escribir(escritor, "HISTORIAL", r[0], r[1], r[2], r[3]);
            }
            for (String evento : eco.getEventosTurno()) {
                escribir(escritor, "EVENTO", limpiar(evento));
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Ecosistema cargar() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return null;
        }

        Ecosistema eco = null;
        boolean hayEstado = false;

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] p = linea.split(SEPARADOR, -1);

                if (p[0].equals("CONFIG")) {
                    eco = new Ecosistema(Clima.valueOf(p[1]), Integer.parseInt(p[2]));
                    continue;
                }
                if (eco == null) {
                    return null;
                }

                switch (p[0]) {
                    case "ESTADO":
                        eco.restaurarDatos(entero(p[1]), entero(p[2]), entero(p[3]), entero(p[4]),
                                entero(p[5]), entero(p[6]), entero(p[7]), entero(p[8]), entero(p[9]),
                                entero(p[10]), entero(p[11]), entero(p[12]),
                                Boolean.parseBoolean(p[13]), Boolean.parseBoolean(p[14]), p[15]);
                        hayEstado = true;
                        break;
                    case "PLANTA":
                    case "VENENOSA":
                    case "CONEJO":
                    case "LOBO":
                        eco.restaurarEntidad(textoAEntidad(p));
                        break;
                    case "HISTORIAL":
                        eco.restaurarHistorial(entero(p[1]), entero(p[2]), entero(p[3]), entero(p[4]));
                        break;
                    case "EVENTO":
                        eco.restaurarEvento(p[1]);
                        break;
                    default:
                        return null;
                }
            }
        } catch (IOException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            return null;
        }

        if (eco == null || !hayEstado) {
            return null;
        }
        return eco;
    }

    public boolean borrar() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return true;
        }
        return archivo.delete();
    }

    private String entidadATexto(Entidad e) {
        String base = e.getNombre() + SEPARADOR + e.getEnergia() + SEPARADOR
                + e.getEdad() + SEPARADOR + e.isViva();

        if (e instanceof PlantaVenenosa) {
            return "VENENOSA" + SEPARADOR + base + SEPARADOR + ((Planta) e).getTamanio();
        } else if (e instanceof Planta) {
            return "PLANTA" + SEPARADOR + base + SEPARADOR + ((Planta) e).getTamanio();
        } else if (e instanceof Conejo) {
            return "CONEJO" + SEPARADOR + base;
        } else {
            return "LOBO" + SEPARADOR + base + SEPARADOR + ((Lobo) e).getExitosCaza();
        }
    }

    private Entidad textoAEntidad(String[] p) {
        String nombre = p[1];
        double energia = Double.parseDouble(p[2]);
        int edad = entero(p[3]);
        boolean viva = Boolean.parseBoolean(p[4]);

        Entidad e;
        switch (p[0]) {
            case "PLANTA":
                e = new Planta(nombre, energia, entero(p[5]));
                break;
            case "VENENOSA":
                e = new PlantaVenenosa(nombre, energia, entero(p[5]));
                break;
            case "CONEJO":
                e = new Conejo(nombre, energia);
                break;
            default:
                Lobo l = new Lobo(nombre, energia);
                l.setExitosCaza(entero(p[5]));
                e = l;
        }
        e.setEdad(edad);
        e.setViva(viva);
        return e;
    }

    private void escribir(BufferedWriter escritor, Object... campos) throws IOException {
        StringBuilder linea = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) {
                linea.append(SEPARADOR);
            }
            linea.append(campos[i]);
        }
        escritor.write(linea.toString());
        escritor.newLine();
    }

    private String limpiar(String texto) {
        return texto.replace(SEPARADOR, ",");
    }

    private int entero(String texto) {
        return Integer.parseInt(texto.trim());
    }
}