package org.simulacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal que simula un sistema de transporte autónomo multimodal.
 * <p>
 * Este sistema modela un entorno logístico con diferentes tipos de vehículos autónomos
 * (terrestres, aéreos y acuáticos) que realizan misiones de transporte en un entorno
 * con obstáculos y condiciones variables, utilizando exclusivamente clases anidadas estáticas.
 * </p>
 *
 * @author [Frida Payán y David Lara]
 * @version 1.0
 */
public class Simulador {
    /**
     * Interfaz funcional para vehículos con capacidad de movimiento terrestre.
     */
    public interface Rodante {
        /**
         * Realiza las operaciones necesarias para que el vehículo se desplace por tierra.
         */
        void conducir();
    }
    /**
     * Interfaz funcional para vehículos con capacidad de vuelo.
     */
    public interface Volador {
        /**
         * Realiza las operaciones necesarias para que el vehículo mantenga vuelo.
         */
        void volar();
    }
    /**
     * Interfaz funcional para vehículos con capacidad acuática.
     */
    public interface Nadador {
        /**
         * Realiza las operaciones necesarias para que el vehículo navegue en agua.
         */
        void navegar();
    }
    /**
     * Interfaz para vehículos con capacidad de operación autónoma.
     */
    public interface Autonomo {
        /**
         * Activa el modo de operación autónoma del vehículo.
         */
        void activarAutonomia();
        /**
         * Desactiva el modo de operación autónoma del vehículo.
         */
        void desactivarAutonomia();
    }
    /**
     * Interfaz para vehículos que pueden ser monitoreados remotamente.
     */
    public interface Monitoreable {
        /**
         * Obtiene una representación en cadena del estado actual del vehículo.
         * @return Cadena formateada con la información de estado del vehículo,
         * incluyendo su identificador, capacidad y ubicación actual.
         */
        String obtenerEstado();
    }

    /**
     * Clase abstracta base para todos los vehículos del sistema de transporte.
     */
    public static abstract class Vehiculo implements Monitoreable {
        private final String id;
        private final double capacidad;
        private String ubicacion;

        /**
         * Constructor base para todos los vehículos del sistema.
         */
        public Vehiculo(String id, double capacidad, String ubicacion) {
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("El ID del vehículo no puede ser nulo o vacío");
            }
            if (capacidad <= 0) {
                throw new IllegalArgumentException("La capacidad debe ser un valor positivo");
            }
            this.id = id;
            this.capacidad = capacidad;
            this.ubicacion = ubicacion;
        }
        /**
         * Mueve el vehículo a la ubicación destino especificada.
         */
        public abstract void moverse(String destino);

        /**
         * Carga una cantidad específica de material en el vehículo.
         */
        public abstract void cargar(double cantidad);

        /**
         * Descarga una cantidad específica de material del vehículo.
         */
        public abstract void descargar(double cantidad);

        /**
         * Obtiene el identificador único del vehículo.
         * @return El ID del vehículo
         */
        public String getId() {
            return id;
        }

        /**
         * Obtiene la capacidad máxima de carga del vehículo.
         * @return Capacidad en kilogramos
         */
        public double getCapacidad() {
            return capacidad;
        }

        /**
         * Obtiene la ubicación actual del vehículo.
         * @return Ubicación actual como cadena descriptiva
         */
        public String getUbicacion() {
            return ubicacion;
        }

        /**
         * Establece la ubicación actual del vehículo.
         */
        protected void setUbicacion(String ubicacion) {
            this.ubicacion = ubicacion;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String obtenerEstado() {
            return String.format("Vehículo ID: %s, Capacidad: %.2f kg, Ubicación: %s",
                    id, capacidad, ubicacion);
        }

        /**
         * Clase concreta que representa un vehículo automóvil autónomo terrestre.
         */
        public static class Auto extends Vehiculo implements Rodante, Autonomo {
            private boolean autonomiaActivada;

            /**
             * Constructor para crear un nuevo automóvil autónomo.
             */
            public Auto(String id, double capacidad, String ubicacion) {
                super(id, capacidad, ubicacion);
                this.autonomiaActivada = false;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void moverse(String destino) {
                System.out.printf("Auto %s moviéndose por carretera hacia %s\n", getId(), destino);
                conducir();
                setUbicacion(destino);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void cargar(double cantidad) {
                if (cantidad > getCapacidad()) {
                    throw new IllegalArgumentException("La carga excede la capacidad del vehículo");
                }
                System.out.printf("Auto %s cargando %.2f kg\n", getId(), cantidad);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void descargar(double cantidad) {
                System.out.printf("Auto %s descargando %.2f kg\n", getId(), cantidad);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void conducir() {
                System.out.printf("Auto %s conduciendo %s\n",
                        getId(), autonomiaActivada ? "en modo autónomo" : "manualmente");
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void activarAutonomia() {
                autonomiaActivada = true;
                System.out.printf("Auto %s: autonomía activada\n", getId());
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void desactivarAutonomia() {
                autonomiaActivada = false;
                System.out.printf("Auto %s: autonomía desactivada\n", getId());
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String obtenerEstado() {
                return super.obtenerEstado() + String.format(", Autonomía: %s",
                        autonomiaActivada ? "Activada" : "Desactivada");
            }
        }

        /**
         * Clase concreta que representa un vehículo aéreo no tripulado (dron).
         */
        public static class Dron extends Vehiculo implements Volador, Autonomo {
            private double altitud;
            /**
             * Constructor para crear un nuevo dron autónomo.
             */
            public Dron(String id, double capacidad, String ubicacion) {
                super(id, capacidad, ubicacion);
                this.altitud = 0;
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void moverse(String destino) {
                System.out.printf("Dron %s volando hacia %s\n", getId(), destino);
                volar();
                setUbicacion(destino);
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void cargar(double cantidad) {
                if (cantidad > getCapacidad()) {
                    throw new IllegalArgumentException("La carga excede la capacidad del vehículo");
                }
                System.out.printf("Dron %s cargando %.2f kg\n", getId(), cantidad);
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void descargar(double cantidad) {
                System.out.printf("Dron %s descargando %.2f kg\n", getId(), cantidad);
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void volar() {
                altitud = 100; // Altitud de vuelo en metros
                System.out.printf("Dron %s volando a %.1f metros de altura\n", getId(), altitud);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void activarAutonomia() {
                System.out.printf("Dron %s: autonomía siempre activa\n", getId());
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void desactivarAutonomia() {
                System.out.println("Los drones no pueden desactivar la autonomía");
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String obtenerEstado() {
                return super.obtenerEstado() + String.format(", Altitud: %.1f m", altitud);
            }
        }

        /**
         * Clase concreta que representa un vehículo submarino autónomo.
         */
        public static class Submarino extends Vehiculo implements Nadador {
            private double profundidad;

            /**
             * Constructor para crear un nuevo submarino autónomo.
             */
            public Submarino(String id, double capacidad, String ubicacion) {
                super(id, capacidad, ubicacion);
                this.profundidad = 0;
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void moverse(String destino) {
                System.out.printf("Submarino %s navegando hacia %s\n", getId(), destino);
                navegar();
                setUbicacion(destino);
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void cargar(double cantidad) {
                if (cantidad > getCapacidad()) {
                    throw new IllegalArgumentException("La carga excede la capacidad del vehículo");
                }
                System.out.printf("Submarino %s cargando %.2f kg\n", getId(), cantidad);
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void descargar(double cantidad) {
                System.out.printf("Submarino %s descargando %.2f kg\n", getId(), cantidad);
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void navegar() {
                profundidad = 50; // Profundidad en metros
                System.out.printf("Submarino %s navegando a %.1f metros de profundidad\n",
                        getId(), profundidad);
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public String obtenerEstado() {
                return super.obtenerEstado() + String.format(", Profundidad: %.1f m", profundidad);
            }
        }

        /**
         * Clase concreta que representa un vehículo anfibio autónomo.
         */
        public static class Anfibio extends Vehiculo implements Rodante, Nadador {
            private boolean enAgua;
            private boolean enTierra;
            /**
             * Constructor para crear un nuevo vehículo anfibio autónomo.
             */
            public Anfibio(String id, double capacidad, String ubicacion) {
                super(id, capacidad, ubicacion);
                this.enAgua = false;
                this.enTierra = true;
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void moverse(String destino) {
                System.out.printf("Anfibio %s desplazándose hacia %s\n", getId(), destino);
                if (enAgua) {
                    navegar();
                } else {
                    conducir();
                }
                setUbicacion(destino);
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void cargar(double cantidad) {
                if (cantidad > getCapacidad()) {
                    throw new IllegalArgumentException("La carga excede la capacidad del vehículo");
                }
                System.out.printf("Anfibio %s cargando %.2f kg\n", getId(), cantidad);
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void descargar(double cantidad) {
                System.out.printf("Anfibio %s descargando %.2f kg\n", getId(), cantidad);
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void conducir() {
                enAgua = false;
                enTierra = true;
                System.out.printf("Anfibio %s conduciendo por carretera\n", getId());
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void navegar() {
                enAgua = true;
                enTierra = false;
                System.out.printf("Anfibio %s navegando en el agua\n", getId());
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public String obtenerEstado() {
                return super.obtenerEstado() + String.format(", Modo: %s",
                        enAgua ? "Acuático" : "Terrestre");
            }
        }

    }
    /**
     * Clase abstracta que representa una misión genérica en el sistema de transporte.
     */
    public static abstract class Mision {
        protected final String id;
        protected final String origen;
        protected final String destino;
        protected final double carga;
        protected Vehiculo vehiculoAsignado;
        protected boolean completada;
        /**
         * Constructor base para todas las misiones.
         */
        public Mision(String id, String origen, String destino, double carga) {
            this.id = id;
            this.origen = origen;
            this.destino = destino;
            this.carga = carga;
            this.completada = false;
        }
        /**
         * Método abstracto para iniciar la misión.
         */
        public abstract void iniciar();
        /**
         * Método abstracto para completar la misión.
         */
        public abstract void completar();
        /**
         * Asigna un vehículo a esta misión.
         */
        public void asignarVehiculo(Vehiculo vehiculo) {
            this.vehiculoAsignado = vehiculo;
            System.out.printf("Vehículo %s asignado a la misión %s\n",
                    vehiculo.getId(), id);
        }
        /**
         * Obtiene el identificador único de la misión.
         * @return Cadena con el ID de la misión
         */
        public String getId() {
            return id;
        }
        /**
         * Obtiene el punto de origen de la misión.
         * @return Ubicación de inicio de la misión
         */
        public String getOrigen() {
            return origen;
        }

        /**
         * Obtiene el destino de la misión.
         * @return Ubicación objetivo donde debe completarse la misión
         */
        public String getDestino() {
            return destino;
        }
        /**
         * Obtiene el peso de la carga asociada a esta misión.
         * @return Cantidad de carga en kilogramos
         */
        public double getCarga() {
            return carga;
        }
        /**
         * Obtiene el vehículo asignado actualmente a esta misión.
         * @return El objeto Vehiculo asignado, o null si no hay vehículo asignado
         */
        public Vehiculo getVehiculoAsignado() {
            return vehiculoAsignado;
        }
        /**
         * Verifica si la misión ha sido completada.
         * @return true si la misión está marcada como completada, false en caso contrario
         */
        public boolean isCompletada() {
            return completada;
        }
        /**
         * Obtiene un resumen del estado actual de la misión.
         */
        public String getEstado() {
            return String.format("Misión ID: %s, Origen: %s, Destino: %s, Carga: %.2f kg, Estado: %s",
                    id, origen, destino, carga,
                    completada ? "Completada" : "Pendiente");
        }
    }
    /**
     * Clase concreta que representa una misión de entrega urgente.
     */
    public static class EntregaUrgente extends Mision {
        /**
         * Constructor para crear una nueva misión de entrega urgente.
         */
        public EntregaUrgente(String id, String origen, String destino, double carga) {
            super(id, origen, destino, carga);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void iniciar() {
            System.out.printf("\n=== Iniciando entrega urgente %s de %s a %s ===\n",
                    id, origen, destino);

            if (vehiculoAsignado == null) {
                throw new IllegalStateException("No se puede iniciar la misión sin vehículo asignado");
            }

            vehiculoAsignado.moverse(destino);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void completar() {
            System.out.printf("=== Entrega urgente %s completada ===\n", id);
            completada = true;

            if (vehiculoAsignado != null) {
                vehiculoAsignado.descargar(carga);
            }
        }
    }

    /**
     * Clase concreta que representa una misión de rescate.
     */
    public static class MisionDeRescate extends Mision {
        /**
         * Constructor para crear una nueva misión de rescate.
         */
        public MisionDeRescate(String id, String origen, String destino, double carga) {
            super(id, origen, destino, carga);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void iniciar() {
            System.out.printf("\n=== Iniciando misión de rescate %s en %s ===\n",
                    id, destino);

            if (vehiculoAsignado == null) {
                throw new IllegalStateException("No se puede iniciar la misión sin vehículo asignado");
            }
            vehiculoAsignado.moverse(destino);
            if (vehiculoAsignado instanceof Autonomo) {
                ((Autonomo) vehiculoAsignado).activarAutonomia();
            }
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void completar() {
            System.out.printf("=== Misión de rescate %s completada ===\n", id);
            completada = true;
            if (vehiculoAsignado != null) {
                vehiculoAsignado.cargar(carga);
                if (vehiculoAsignado instanceof Autonomo) {
                    ((Autonomo) vehiculoAsignado).desactivarAutonomia();
                }
            }
        }
    }
    /**
     * Clase que representa el entorno de simulación.
     */
    public static class Entorno {
        private final List<Vehiculo> vehiculos;
        private final List<Mision> misiones;
        /**
         * Constructor del entorno de simulación.
         */
        public Entorno() {
            this.vehiculos = new ArrayList<>();
            this.misiones = new ArrayList<>();
        }
        /**
         * Agrega un vehículo al entorno de simulación.
         */
        public void agregarVehiculo(Vehiculo vehiculo) {
            vehiculos.add(vehiculo);
            System.out.printf("Vehículo %s agregado al entorno\n", vehiculo.getId());
        }
        /**
         * Agrega una misión al entorno de simulación.
         */
        public void agregarMision(Mision mision) {
            misiones.add(mision);
            System.out.printf("Misión %s (%s) agregada al entorno\n",
                    mision.getId(), mision.getClass().getSimpleName());
        }
        /**
         * Ciclo de simulación.
         */
        public void simularCiclo() {
            System.out.println("\n=== Iniciando ciclo de simulación ===");
            for (Mision mision : misiones) {
                if (mision.getVehiculoAsignado() == null && mision.isCompletada()) {
                    asignarVehiculoAMision(mision);
                }
            }
            for (Mision mision : misiones) {
                if (!mision.isCompletada() && mision.getVehiculoAsignado() != null) {
                    Vehiculo vehiculo = mision.getVehiculoAsignado();
                    if (vehiculo.getUbicacion().equals(mision.getOrigen())) {
                        mision.iniciar();
                    } else if (vehiculo.getUbicacion().equals(mision.getDestino())) {
                        mision.completar();
                    } else {
                        System.out.printf("Vehículo %s en ruta hacia %s para misión %s\n",
                                vehiculo.getId(), mision.getDestino(), mision.getId());
                    }
                }
            }
            System.out.println("\n--- Estado de vehículos ---");
            for (Vehiculo vehiculo : vehiculos) {
                System.out.println(vehiculo.obtenerEstado());
            }
            System.out.println("\n--- Estado de misiones ---");
            for (Mision mision : misiones) {
                System.out.println(mision.getEstado());
            }
            System.out.println("=== Ciclo de simulación completado ===\n");
        }
        /**
         * Asigna vehículos a misiones considerando ubicación.
         */
        private void asignarVehiculoAMision(Mision mision) {
            for (Vehiculo vehiculo : vehiculos) {
                boolean disponible = vehiculo.getUbicacion().equals(mision.getOrigen());
                for (Mision m : misiones) {
                    if (vehiculo.equals(m.getVehiculoAsignado()) && !m.isCompletada()) {
                        disponible = false;
                        break;
                    }
                }
                if (disponible) {
                    mision.asignarVehiculo(vehiculo);
                    return;
                }
            }
            System.out.printf("No hay vehículos disponibles en %s para la misión %s\n",
                    mision.getOrigen(), mision.getId());
        }
        /**
         * Demostración de polimorfismo.
         */
        public void demostrarPolimorfismo() {
            System.out.println("\n=== Demostración de polimorfismo ===");
            for (Vehiculo vehiculo : vehiculos) {
                System.out.println("\nProcesando vehículo: " + vehiculo.getId());
                System.out.print("Movimiento: ");
                vehiculo.moverse("destino genérico");
                if (vehiculo instanceof Rodante) {
                    ((Rodante) vehiculo).conducir();
                }
                if (vehiculo instanceof Volador) {
                    ((Volador) vehiculo).volar();
                }
                if (vehiculo instanceof Nadador) {
                    ((Nadador) vehiculo).navegar();
                }
                if (vehiculo instanceof Autonomo) {
                    ((Autonomo) vehiculo).activarAutonomia();
                }
                System.out.println("Estado actual: " + vehiculo.obtenerEstado());
            }
            System.out.println("=== Fin de demostración de polimorfismo ===\n");
        }
    }

    /**
     * Método principal.
     */
    public static void main(String[] args) {
        Entorno entorno = new Entorno();

        Vehiculo.Auto auto1 = new Vehiculo.Auto("AUTO-001", 500, "Base Central");
        Vehiculo.Dron dron1 = new Vehiculo.Dron("DRON-001", 10, "Hangar Norte");
        Vehiculo.Submarino submarino1 = new Vehiculo.Submarino("SUB-001", 2000, "Puerto Este");
        Vehiculo.Anfibio anfibio1 = new Vehiculo.Anfibio("ANF-001", 800, "Base Mixta");

        entorno.agregarVehiculo(auto1);
        entorno.agregarVehiculo(dron1);
        entorno.agregarVehiculo(submarino1);
        entorno.agregarVehiculo(anfibio1);

        Mision entregaUrgente = new EntregaUrgente("M001", "Base Central", "Centro de Distribución", 300);
        Mision rescateAereo = new MisionDeRescate("M002", "Hangar Norte", "Zona de Desastre", 0);
        Mision entregaMaritima = new EntregaUrgente("M003", "Puerto Este", "Isla Remota", 1500);
        Mision rescateCostero = new MisionDeRescate("M004", "Base Mixta", "Playa Accidentada", 5);

        entorno.agregarMision(entregaUrgente);
        entorno.agregarMision(rescateAereo);
        entorno.agregarMision(entregaMaritima);
        entorno.agregarMision(rescateCostero);

        entorno.demostrarPolimorfismo();

        for (int i = 0; i < 2; i++) {
            entorno.simularCiclo();
        }
    }
}