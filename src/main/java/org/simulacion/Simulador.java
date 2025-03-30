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
     * <p>
     * Define el contrato que deben implementar los vehículos que pueden operar
     * en superficies terrestres.
     * </p>
     */
    public interface Rodante {
        /**
         * Realiza las operaciones necesarias para que el vehículo se desplace por tierra.
         * <p>
         * Implementa la lógica específica de conducción para cada tipo de vehículo terrestre.
         * </p>
         */
        void conducir();
    }

    /**
     * Interfaz funcional para vehículos con capacidad de vuelo.
     * <p>
     * Define el contrato que deben implementar los vehículos aéreos autónomos.
     * </p>
     */
    public interface Volador {
        /**
         * Realiza las operaciones necesarias para que el vehículo mantenga vuelo.
         * <p>
         * Incluye la gestión de altitud, dirección y estabilidad durante el vuelo.
         * </p>
         */
        void volar();
    }

    /**
     * Interfaz funcional para vehículos con capacidad acuática.
     * <p>
     * Define el contrato que deben implementar los vehículos que operan en ambientes acuáticos.
     * </p>
     */
    public interface Nadador {
        /**
         * Realiza las operaciones necesarias para que el vehículo navegue en agua.
         * <p>
         * Incluye gestión de profundidad y movimiento en medios líquidos.
         * </p>
         */
        void navegar();
    }

    /**
     * Interfaz para vehículos con capacidad de operación autónoma.
     * <p>
     * Define los métodos para controlar el modo de operación autónoma del vehículo.
     * </p>
     */
    public interface Autonomo {
        /**
         * Activa el modo de operación autónoma del vehículo.
         * <p>
         * Cuando se activa, el vehículo debe ser capaz de tomar decisiones
         * de navegación sin intervención humana.
         * </p>
         */
        void activarAutonomia();

        /**
         * Desactiva el modo de operación autónoma del vehículo.
         * <p>
         * Requiere que el vehículo pase a modo manual o espere instrucciones.
         * </p>
         */
        void desactivarAutonomia();
    }

    /**
     * Interfaz para vehículos que pueden ser monitoreados remotamente.
     * <p>
     * Proporciona métodos para obtener información del estado actual del vehículo.
     * </p>
     */
    public interface Monitoreable {
        /**
         * Obtiene una representación en cadena del estado actual del vehículo.
         *
         * @return Cadena formateada con la información de estado del vehículo,
         *         incluyendo su identificador, capacidad y ubicación actual.
         */
        String obtenerEstado();
    }

    /**
     * Clase abstracta base para todos los vehículos del sistema de transporte.
     * <p>
     * Define los atributos y métodos comunes a todos los vehículos autónomos
     * del sistema, implementando la interfaz Monitoreable.
     * </p>
     */
    public static abstract class Vehiculo implements Monitoreable {
        private final String id;
        private final double capacidad;
        private String ubicacion;

        /**
         * Constructor base para todos los vehículos del sistema.
         *
         * @param id Identificador único alfanumérico del vehículo
         * @param capacidad Capacidad máxima de carga en kilogramos (valor positivo)
         * @param ubicacion Ubicación inicial del vehículo (nombre descriptivo)
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
         * <p>
         * Este método abstracto debe implementar la lógica específica de movimiento
         * para cada tipo de vehículo.
         * </p>
         *
         * @param destino Ubicación a la que se debe mover el vehículo
         */
        public abstract void moverse(String destino);

        /**
         * Carga una cantidad específica de material en el vehículo.
         *
         * @param cantidad Peso en kilogramos del material a cargar (debe ser positiva)
         */
        public abstract void cargar(double cantidad);

        /**
         * Descarga una cantidad específica de material del vehículo.
         *
         * @param cantidad Peso en kilogramos del material a descargar (debe ser positiva)
         */
        public abstract void descargar(double cantidad);

        /**
         * Obtiene el identificador único del vehículo.
         *
         * @return El ID del vehículo
         */
        public String getId() {
            return id;
        }

        /**
         * Obtiene la capacidad máxima de carga del vehículo.
         *
         * @return Capacidad en kilogramos
         */
        public double getCapacidad() {
            return capacidad;
        }

        /**
         * Obtiene la ubicación actual del vehículo.
         *
         * @return Ubicación actual como cadena descriptiva
         */
        public String getUbicacion() {
            return ubicacion;
        }

        /**
         * Establece la ubicación actual del vehículo.
         * <p>
         * Este método es protegido para que solo las subclases puedan modificar la ubicación.
         * </p>
         *
         * @param ubicacion Nueva ubicación del vehículo
         */
        protected void setUbicacion(String ubicacion) {
            this.ubicacion = ubicacion;
        }

        /**
         * {@inheritDoc}
         * <p>
         * Implementación de la interfaz Monitoreable que proporciona información
         * básica del estado del vehículo.
         * </p>
         */
        @Override
        public String obtenerEstado() {
            return String.format("Vehículo ID: %s, Capacidad: %.2f kg, Ubicación: %s",
                    id, capacidad, ubicacion);
        }

        /**
         * Clase concreta que representa un vehículo automóvil autónomo terrestre.
         * <p>
         * Implementa las interfaces Rodante para movimiento terrestre y Autonomo
         * para capacidad de operación independiente.
         * </p>
         */
        public static class Auto extends Vehiculo implements Rodante, Autonomo {
            private boolean autonomiaActivada;

            /**
             * Constructor para crear un nuevo automóvil autónomo.
             *
             * @param id Identificador único (ej. "AUTO-001")
             * @param capacidad Capacidad de carga en kg (valor positivo)
             * @param ubicacion Ubicación inicial (ej. "Base Central")
             */
            public Auto(String id, double capacidad, String ubicacion) {
                super(id, capacidad, ubicacion);
                this.autonomiaActivada = false;
            }

            /**
             * {@inheritDoc}
             * <p>
             * Implementación específica para movimiento de automóvil, que incluye
             * el método conducir() y actualización de ubicación.
             * </p>
             */
            @Override
            public void moverse(String destino) {
                System.out.printf("Auto %s moviéndose por carretera hacia %s\n", getId(), destino);
                conducir();
                setUbicacion(destino);
            }

            /**
             * {@inheritDoc}
             * <p>
             * Implementación de carga para automóvil, con validación de capacidad.
             * </p>
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
             * <p>
             * Implementación de conducción que varía según el modo de operación (autónomo/manual).
             * </p>
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
             * <p>
             * Extiende el estado base con información sobre el modo de autonomía.
             * </p>
             */
            @Override
            public String obtenerEstado() {
                return super.obtenerEstado() + String.format(", Autonomía: %s",
                        autonomiaActivada ? "Activada" : "Desactivada");
            }
        }

        /**
         * Clase concreta que representa un vehículo aéreo no tripulado (dron).
         * <p>
         * Implementa las interfaces Volador para capacidad de vuelo y Autonomo
         * para operación independiente.
         * </p>
         */
        public static class Dron extends Vehiculo implements Volador, Autonomo {
            private double altitud;

            /**
             * Constructor para crear un nuevo dron autónomo.
             *
             * @param id Identificador único (ej. "DRON-001")
             * @param capacidad Capacidad de carga en kg (valor positivo)
             * @param ubicacion Ubicación inicial (ej. "Hangar Norte")
             */
            public Dron(String id, double capacidad, String ubicacion) {
                super(id, capacidad, ubicacion);
                this.altitud = 0;
            }

            /**
             * {@inheritDoc}
             * <p>
             * Implementación específica para movimiento de dron, que incluye
             * el método volar() y actualización de ubicación.
             * </p>
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
             * <p>
             * Implementación de vuelo que establece una altitud estándar de operación.
             * </p>
             */
            @Override
            public void volar() {
                altitud = 100; // Altitud de vuelo en metros
                System.out.printf("Dron %s volando a %.1f metros de altura\n", getId(), altitud);
            }

            /**
             * {@inheritDoc}
             * <p>
             * Los drones siempre operan en modo autónomo.
             * </p>
             */
            @Override
            public void activarAutonomia() {
                System.out.printf("Dron %s: autonomía siempre activa\n", getId());
            }

            /**
             * {@inheritDoc}
             * <p>
             * Los drones no pueden desactivar su autonomía por razones de seguridad.
             * </p>
             */
            @Override
            public void desactivarAutonomia() {
                System.out.println("Los drones no pueden desactivar la autonomía");
            }

            /**
             * {@inheritDoc}
             * <p>
             * Extiende el estado base con información sobre la altitud actual.
             * </p>
             */
            @Override
            public String obtenerEstado() {
                return super.obtenerEstado() + String.format(", Altitud: %.1f m", altitud);
            }
        }

        /**
         * Clase concreta que representa un vehículo submarino autónomo.
         * <p>
         * Implementa la interfaz Nadador para capacidad de navegación submarina.
         * </p>
         */
        public static class Submarino extends Vehiculo implements Nadador {
            private double profundidad;

            /**
             * Constructor para crear un nuevo submarino autónomo.
             *
             * @param id Identificador único (ej. "SUB-001")
             * @param capacidad Capacidad de carga en kg (valor positivo)
             * @param ubicacion Ubicación inicial (ej. "Puerto Este")
             */
            public Submarino(String id, double capacidad, String ubicacion) {
                super(id, capacidad, ubicacion);
                this.profundidad = 0;
            }

            /**
             * {@inheritDoc}
             * <p>
             * Implementación específica para movimiento de submarino, que incluye
             * el método navegar() y actualización de ubicación.
             * </p>
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
             * <p>
             * Implementación de navegación submarina que establece una profundidad estándar.
             * </p>
             */
            @Override
            public void navegar() {
                profundidad = 50; // Profundidad en metros
                System.out.printf("Submarino %s navegando a %.1f metros de profundidad\n",
                        getId(), profundidad);
            }

            /**
             * {@inheritDoc}
             * <p>
             * Extiende el estado base con información sobre la profundidad actual.
             * </p>
             */
            @Override
            public String obtenerEstado() {
                return super.obtenerEstado() + String.format(", Profundidad: %.1f m", profundidad);
            }
        }

        /**
         * Clase concreta que representa un vehículo anfibio autónomo.
         * <p>
         * Implementa las interfaces Rodante y Nadador para capacidad de operación
         * tanto terrestre como acuática.
         * </p>
         */
        public static class Anfibio extends Vehiculo implements Rodante, Nadador {
            private boolean enAgua;
            private boolean enTierra;

            /**
             * Constructor para crear un nuevo vehículo anfibio autónomo.
             *
             * @param id Identificador único (ej. "ANF-001")
             * @param capacidad Capacidad de carga en kg (valor positivo)
             * @param ubicacion Ubicación inicial (ej. "Base Mixta")
             */
            public Anfibio(String id, double capacidad, String ubicacion) {
                super(id, capacidad, ubicacion);
                this.enAgua = false;
                this.enTierra = true;
            }

            /**
             * {@inheritDoc}
             * <p>
             * Implementación específica para movimiento anfibio, que selecciona
             * automáticamente el modo de desplazamiento según el medio actual.
             * </p>
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
             * <p>
             * Implementación de conducción terrestre para el vehículo anfibio.
             * </p>
             */
            @Override
            public void conducir() {
                enAgua = false;
                enTierra = true;
                System.out.printf("Anfibio %s conduciendo por carretera\n", getId());
            }

            /**
             * {@inheritDoc}
             * <p>
             * Implementación de navegación acuática para el vehículo anfibio.
             * </p>
             */
            @Override
            public void navegar() {
                enAgua = true;
                enTierra = false;
                System.out.printf("Anfibio %s navegando en el agua\n", getId());
            }

            /**
             * Cambia el modo de operación del vehículo entre terrestre y acuático.
             * <p>
             * Alterna el estado interno del vehículo entre modos de operación.
             * </p>
             */
            public void cambiarModo() {
                enAgua = !enAgua;
                enTierra = !enTierra;
                System.out.printf("Anfibio %s ahora está %s\n",
                        getId(), enAgua ? "en el agua" : "en tierra");
            }

            /**
             * {@inheritDoc}
             * <p>
             * Extiende el estado base con información sobre el modo de operación actual.
             * </p>
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
     * <p>
     * Define los atributos y comportamientos básicos que todas las misiones deben implementar.
     * </p>
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
             * Cada tipo de misión implementa su propio comportamiento.
             */
            public abstract void iniciar();

            /**
             * Método abstracto para completar la misión.
             * Cada tipo de misión implementa su propio comportamiento.
             */
            public abstract void completar();

            /**
             * Asigna un vehículo a esta misión.
             * <p>
             * Notifica al vehículo sobre su asignación.
             * </p>
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
            public String getId() { return id; }
            /**
             * Obtiene el punto de origen de la misión.
             * @return Ubicación de inicio de la misión
             */
            public String getOrigen() { return origen; }
            /**
             * Obtiene el destino de la misión.
             * @return Ubicación objetivo donde debe completarse la misión
             */
            public String getDestino() { return destino; }
            /**
             * Obtiene el peso de la carga asociada a esta misión.
             * @return Cantidad de carga en kilogramos
             */
            public double getCarga() { return carga; }
            /**
             * Obtiene el vehículo asignado actualmente a esta misión.
             * @return El objeto Vehiculo asignado, o null si no hay vehículo asignado
             */
            public Vehiculo getVehiculoAsignado() { return vehiculoAsignado; }
            /**
             * Verifica si la misión ha sido completada.
             * @return true si la misión está marcada como completada, false en caso contrario
             */
            public boolean isCompletada() { return completada; }

            /**
             * Obtiene un resumen del estado actual de la misión.
             * <p>
             * Mantiene el formato detallado del código original.
             * </p>
             */
            public String getEstado() {
                return String.format("Misión ID: %s, Origen: %s, Destino: %s, Carga: %.2f kg, Estado: %s",
                        id, origen, destino, carga,
                        completada ? "Completada" : "Pendiente");
            }
        }

        /**
         * Clase concreta que representa una misión de entrega urgente.
         * <p>
         * Implementa un comportamiento optimizado para entregas rápidas con menos pasos.
         * </p>
         */
        public static class EntregaUrgente extends Mision {
            /**
             * Constructor para crear una nueva misión de entrega urgente.
             *
             * @param id Identificador único de la misión
             * @param origen Ubicación de origen de la misión
             * @param destino Ubicación de destino de la misión
             * @param carga Cantidad de carga a transportar (en kg)
             */
            public EntregaUrgente(String id, String origen, String destino, double carga) {
                super(id, origen, destino, carga);
            }

            @Override
            public void iniciar() {
                System.out.printf("\n=== Iniciando entrega urgente %s de %s a %s ===\n",
                        id, origen, destino);

                if (vehiculoAsignado == null) {
                    throw new IllegalStateException("No se puede iniciar la misión sin vehículo asignado");
                }

                vehiculoAsignado.moverse(destino);
            }

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
     * <p>
     * Implementa comportamientos específicos para operaciones de rescate,
     * incluyendo activación de autonomía en los vehículos.
     * </p>
     */
        public static class MisionDeRescate extends Mision {
        /**
         * Constructor para crear una nueva misión de rescate.
         *
         * @param id Identificador único de la misión
         * @param origen Ubicación de origen de la misión
         * @param destino Ubicación donde se realizará el rescate
         * @param carga Cantidad de personas/carga a rescatar (en kg)
         */
            public MisionDeRescate(String id, String origen, String destino, double carga) {
                super(id, origen, destino, carga);
            }

            @Override
            public void iniciar() {
                System.out.printf("\n=== Iniciando misión de rescate %s en %s ===\n",
                        id, destino);

                if (vehiculoAsignado == null) {
                    throw new IllegalStateException("No se puede iniciar la misión sin vehículo asignado");
                }

                vehiculoAsignado.moverse(destino);

                // Comportamiento específico para rescate
                if (vehiculoAsignado instanceof Autonomo) {
                    ((Autonomo) vehiculoAsignado).activarAutonomia();
                }
            }

            @Override
            public void completar() {
                System.out.printf("=== Misión de rescate %s completada ===\n", id);
                completada = true;

                if (vehiculoAsignado != null) {
                    vehiculoAsignado.cargar(carga); // Rescatando personas/carga

                    if (vehiculoAsignado instanceof Autonomo) {
                        ((Autonomo) vehiculoAsignado).desactivarAutonomia();
                    }
                }
            }
        }

    /**
     * Clase que representa el entorno de simulación.
     * <p>
     * Gestiona los vehículos y misiones, y coordina la ejecución de los ciclos de simulación.
     * </p>
     */
        public static class Entorno {
            private final List<Vehiculo> vehiculos;
            private final List<Mision> misiones;

            /**
             * Constructor del entorno de simulación.
             * <p>
             * Inicializa las listas de vehículos y misiones.
             * </p>
             */
            public Entorno() {
                this.vehiculos = new ArrayList<>();
                this.misiones = new ArrayList<>();
            }

        /**
         * Agrega un vehículo al entorno de simulación.
         *
         * @param vehiculo Vehículo a agregar al entorno
         */
            public void agregarVehiculo(Vehiculo vehiculo) {
                vehiculos.add(vehiculo);
                System.out.printf("Vehículo %s agregado al entorno\n", vehiculo.getId());
            }

        /**
         * Agrega una misión al entorno de simulación.
         *
         * @param mision Misión a agregar al entorno
         */
            public void agregarMision(Mision mision) {
                misiones.add(mision);
                System.out.printf("Misión %s (%s) agregada al entorno\n",
                        mision.getId(), mision.getClass().getSimpleName());
            }

            /**
             * Ciclo de simulación mejorado.
             * <p>
             * Mantiene el formato detallado del código original pero con
             * la lógica de ejecución por pasos del segundo código.
             * </p>
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
             * <p>
             * Mejorado para verificar la ubicación del vehículo.
             * </p>
             */
            private void asignarVehiculoAMision(Mision mision) {
                for (Vehiculo vehiculo : vehiculos) {
                    // Verificar si el vehículo está disponible y en el origen
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
             * Demostración de polimorfismo mejorada.
             * <p>
             * Mantiene el estilo detallado del código original.
             * </p>
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
         * Método principal mejorado.
         * <p>
         * Crea un entorno con diferentes tipos de misiones y vehículos,
         * demostrando las nuevas capacidades.
         * </p>
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