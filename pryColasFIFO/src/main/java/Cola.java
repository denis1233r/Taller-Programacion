import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Cola {
    private final Queue<Pasajero> coleccion;

    public Cola() {
        coleccion = new LinkedList<>();
    }

    public void agregar(Pasajero p) {
        if (p == null) {
            throw new IllegalArgumentException("El pasajero no puede ser nulo");
        }

        // R1: validar todos los datos antes de modificar la cola.
        String nombre = p.getNombre();
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        int asiento = p.getNumeroAsiento();
        validarRangoAsiento(asiento);

        for (Pasajero pendiente : coleccion) {
            if (pendiente.getNumeroAsiento() == asiento) {
                throw new IllegalArgumentException(
                        "El asiento " + asiento + " ya está ocupado en la fila");
            }
        }

        coleccion.offer(p);
    }

    public Pasajero extraer() {
        if (coleccion.isEmpty()) {
            throw new NoSuchElementException("Cola sin elementos");
        }
        return coleccion.poll();
    }

    public Pasajero frente() {
        if (coleccion.isEmpty()) {
            throw new NoSuchElementException("Cola sin elementos");
        }
        return coleccion.peek();
    }

    public int size() {
        return coleccion.size();
    }

    public boolean estaVacia() {
        return coleccion.isEmpty();
    }

    public List<Pasajero> obtenerPendientes() {
        return Collections.unmodifiableList(new ArrayList<>(coleccion));
    }

    public Pasajero buscarPorAsiento(int asiento) {
        // R2: consultar sin retirar ni reordenar pasajeros.
        validarRangoAsiento(asiento);
        for (Pasajero p : coleccion) {
            if (p.getNumeroAsiento() == asiento) {
                return p;
            }
        }
        return null;
    }

    public int posicionPorAsiento(int asiento) {
        // R2: posiciones basadas en 1; -1 si no se encuentra.
        validarRangoAsiento(asiento);
        int posicion = 1;
        for (Pasajero p : coleccion) {
            if (p.getNumeroAsiento() == asiento) {
                return posicion;
            }
            posicion++;
        }
        return -1;
    }

    public Pasajero cancelarPorAsiento(int asiento) {
        // R3: reconstruir la cola sin el pasajero elegido, conservando FIFO.
        validarRangoAsiento(asiento);
        Queue<Pasajero> restantes = new LinkedList<>();
        Pasajero cancelado = null;

        while (!coleccion.isEmpty()) {
            Pasajero actual = coleccion.poll();
            if (actual.getNumeroAsiento() == asiento) {
                cancelado = actual;
            } else {
                restantes.offer(actual);
            }
        }

        coleccion.addAll(restantes);
        if (cancelado == null) {
            throw new NoSuchElementException(
                    "No existe un pasajero pendiente con el asiento " + asiento);
        }
        return cancelado;
    }

    private void validarRangoAsiento(int asiento) {
        if (asiento < 1 || asiento > 30) {
            throw new IllegalArgumentException("El asiento debe estar entre 1 y 30");
        }
    }

    @Override
    public String toString() {
        if (coleccion.isEmpty()) {
            return "Cola sin elementos";
        }
        StringBuilder sb = new StringBuilder();
        int posicion = 1;
        for (Pasajero p : coleccion) {
            sb.append(posicion++).append(". ").append(p).append("\n");
        }
        return sb.toString();
    }
}
