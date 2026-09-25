# Simulador de Ecosistema

Simulación por turnos en Java (consola) de un ecosistema con plantas, conejos y lobos.
Trabajo práctico de Interfaz Gráfica — primera instancia evaluativa.

## Instrucciones de ejecución

- IDE: Apache NetBeans (proyecto Ant / Java Application).
- JDK requerido: 17 o superior.
- Pasos:
  1. Abrir NetBeans → File → Open Project → seleccionar la carpeta `SimuladorEcosistema`.
  2. Click derecho sobre el proyecto → Run (o F6).
  3. La clase principal es `ecosistema.SimuladorEcosistemaMain`.
- No usa librerías externas, solo `java.util` (Scanner, ArrayList, List, Random).

## Estructura del proyecto

Todas las clases están en el paquete `ecosistema` (`src/ecosistema/`):

| Archivo | Qué es |
|---|---|
| `Entidad.java` | Clase abstracta raíz: nombre, energía, edad, viva; `envejecer()` concreto. |
| `Reproducible.java` | Interfaz con método default `intentarReproduccion()`. |
| `Mortal.java` | Interfaz con método default `verificarMuerte()`. |
| `Peligroso.java` | Interfaz extra (bonus) para el ranking de peligro. |
| `Clima.java` | Enum con el efecto numérico de cada clima. |
| `Planta.java` | Extiende Entidad, implementa Reproducible. |
| `PlantaVenenosa.java` | Extra (bonus): extiende Planta, implementa Peligroso. |
| `Animal.java` | Clase abstracta intermedia (extiende Entidad, implementa Mortal). |
| `Conejo.java` | Extiende Animal, implementa Reproducible. |
| `Lobo.java` | Extiende Animal, implementa Peligroso. No es Reproducible. |
| `Ecosistema.java` | Dueña de las listas de entidades, procesa cada turno y arma el reporte final. |
| `SimuladorEcosistemaMain.java` | Configuración inicial, loop de turnos, menú de intervención y reporte final. |

## Conceptos de POO aplicados

- Herencia en 3 niveles: `Entidad` → `Animal` → `Conejo`/`Lobo`; `Entidad` → `Planta` → `PlantaVenenosa`.
- Clases abstractas: `Entidad`, `Animal`.
- Interfaces con método default: `Reproducible`, `Mortal`.
- Polimorfismo: `Ecosistema` recorre `ArrayList<Planta>`, `ArrayList<Conejo>`, `ArrayList<Lobo>` llamando siempre a `actuar()`, que cada clase implementa distinto; `instanceof` se usa para el listado de `Peligroso`.
- Encapsulamiento: todos los atributos son `private`, con getters/setters validados (energía nunca negativa, tamaño de planta entre 1 y 5, etc.).
- Sobrecarga: `Ecosistema.agregarEntidad(String tipo)` y `agregarEntidad(String tipo, double energiaInicial)`.

## Desafíos encontrados

_(completar entre el equipo: qué costó más, qué tuvieron que rehacer, etc.)_

## Uso de IA / herramientas externas

_(completar de forma individual, según lo que pide la consigna: capturas de prompts o links a las conversaciones completas)_

## Integrantes

_(nombre y rol de cada integrante del grupo)_
