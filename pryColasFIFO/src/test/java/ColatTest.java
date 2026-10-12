import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ColatTest {
    private Cola cola;

    @BeforeEach
    void preparar() {
        cola = new Cola();
    }

    @Test
    void buscarNoSacaAlPasajeroDeLaFila() {
        cola.agregar(new Pasajero("Ana", 5, "ECONOMICA"));
        Pasajero encontrado = cola.buscarPorAsiento(5);
        assertEquals("Ana", encontrado.getNombre());
        assertEquals(1, cola.size());
        assertSame(encontrado, cola.frente());
    }

    @Test
    void cancelarElUnicoPasajeroDejaLaColaVacia() {
        cola.agregar(new Pasajero("Ana", 5, "ECONOMICA"));
        Pasajero cancelado = cola.cancelarPorAsiento(5);
        assertEquals("Ana", cancelado.getNombre());
        assertTrue(cola.estaVacia());
    }

    @Test
    void posicionSeActualizaDespuesDeCancelar() {
        cola.agregar(new Pasajero("Ana", 5, "ECONOMICA"));
        cola.agregar(new Pasajero("Juan", 8, "PRIMERA CLASE"));
        cola.agregar(new Pasajero("Flor", 12, "EJECUTIVA"));
        cola.cancelarPorAsiento(5);
        assertEquals(1, cola.posicionPorAsiento(8));
        assertEquals(2, cola.posicionPorAsiento(12));
        assertEquals(-1, cola.posicionPorAsiento(5));
    }
}