import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

@Tag("taller")
class ColaTallerTest {
    private Cola cola;

    @BeforeEach
    void preparar() {
        cola = new Cola();
    }

    private void cargarTres() {
        cola.agregar(new Pasajero("Ana", 1, "ECONOMICA"));
        cola.agregar(new Pasajero("Juan", 2, "PRIMERA CLASE"));
        cola.agregar(new Pasajero("Flor", 3, "EJECUTIVA"));
    }

    private List<Integer> asientos() {
        List<Integer> resultado = new ArrayList<>();
        for (Pasajero p : cola.obtenerPendientes()) {
            resultado.add(p.getNumeroAsiento());
        }
        return resultado;
    }

    // R1: registro validado.
    @Test
    @Tag("r1")
    void r1AceptaRegistrosValidosEnLosLimites() {
        cola.agregar(new Pasajero("Ana", 1, "ECONOMICA"));
        cola.agregar(new Pasajero("Juan", 30, "PRIMERA CLASE"));
        assertEquals(Arrays.asList(1, 30), asientos());
        assertEquals(2, cola.size());
    }

    @Test
    @Tag("r1")
    void r1RechazaNombresInvalidos() {
        for (String nombre : new String[]{null, "", "   "}) {
            assertThrows(IllegalArgumentException.class,
                    () -> cola.agregar(new Pasajero(nombre, 1, "ECONOMICA")));
            assertEquals(0, cola.size());
        }
    }

    @Test
    @Tag("r1")
    void r1RechazaAsientosFueraDelRango() {
        for (int asiento : new int[]{0, 31}) {
            assertThrows(IllegalArgumentException.class,
                    () -> cola.agregar(new Pasajero("Ana", asiento, "ECONOMICA")));
            assertEquals(0, cola.size());
        }
    }

    @Test
    @Tag("r1")
    void r1RechazaAsientoRepetidoEnLaFila() {
        cola.agregar(new Pasajero("Ana", 1, "ECONOMICA"));
        assertThrows(IllegalArgumentException.class,
                () -> cola.agregar(new Pasajero("Juan", 1, "EJECUTIVA")));
        assertEquals(1, cola.size());
        assertEquals("Ana", cola.frente().getNombre());
    }

    @Test
    @Tag("r1")
    void r1RegistroRechazadoNoCambiaTamanoNiOrden() {
        cargarTres();
        assertThrows(IllegalArgumentException.class,
                () -> cola.agregar(new Pasajero("   ", 4, "ECONOMICA")));
        assertEquals(3, cola.size());
        assertEquals(Arrays.asList(1, 2, 3), asientos());
    }

    // R2: búsqueda y posición.
    @Test
    @Tag("r2")
    void r2EncuentraPrimeroIntermedioYUltimo() {
        cargarTres();
        assertEquals("Ana", cola.buscarPorAsiento(1).getNombre());
        assertEquals("Juan", cola.buscarPorAsiento(2).getNombre());
        assertEquals("Flor", cola.buscarPorAsiento(3).getNombre());
    }

    @Test
    @Tag("r2")
    void r2AsientoAusenteDevuelveNull() {
        cargarTres();
        assertNull(cola.buscarPorAsiento(30));
        assertEquals(3, cola.size());
    }

    @Test
    @Tag("r2")
    void r2PosicionComienzaEnUnoYAusenteDevuelveMenosUno() {
        cargarTres();
        assertEquals(1, cola.posicionPorAsiento(1));
        assertEquals(2, cola.posicionPorAsiento(2));
        assertEquals(3, cola.posicionPorAsiento(3));
        assertEquals(-1, cola.posicionPorAsiento(30));
    }

    @Test
    @Tag("r2")
    void r2ConsultasNoCambianOrdenNiCheckin() {
        cargarTres();
        cola.buscarPorAsiento(2);
        cola.posicionPorAsiento(3);
        cola.buscarPorAsiento(30);
        assertEquals(Arrays.asList(1, 2, 3), asientos());
        assertEquals(3, cola.size());
        for (Pasajero p : cola.obtenerPendientes()) {
            assertFalse(p.isCheckin());
        }
    }

    @Test
    @Tag("r2")
    void r2ConsultasFueraDeRangoSeRechazan() {
        cargarTres();
        for (int asiento : new int[]{0, 31}) {
            assertThrows(IllegalArgumentException.class,
                    () -> cola.buscarPorAsiento(asiento));
            assertThrows(IllegalArgumentException.class,
                    () -> cola.posicionPorAsiento(asiento));
        }
        assertEquals(Arrays.asList(1, 2, 3), asientos());
    }

    // R3: cancelación sin alterar FIFO.
    @Test
    @Tag("r3")
    void r3CancelaIntermedioYConservaElOrden() {
        cargarTres();
        Pasajero cancelado = cola.cancelarPorAsiento(2);
        assertEquals("Juan", cancelado.getNombre());
        assertFalse(cancelado.isCheckin());
        assertEquals(Arrays.asList(1, 3), asientos());
        assertEquals(2, cola.size());
    }

    @Test
    @Tag("r3")
    void r3CancelaPrimeroYUltimo() {
        cargarTres();
        assertEquals(1, cola.cancelarPorAsiento(1).getNumeroAsiento());
        assertEquals(Arrays.asList(2, 3), asientos());
        assertEquals(3, cola.cancelarPorAsiento(3).getNumeroAsiento());
        assertEquals(Arrays.asList(2), asientos());
        assertEquals(1, cola.size());
    }

    @Test
    @Tag("r3")
    void r3AsientoInexistenteGeneraExcepcionSinCambios() {
        cargarTres();
        assertThrows(NoSuchElementException.class,
                () -> cola.cancelarPorAsiento(30));
        assertEquals(3, cola.size());
        assertEquals(Arrays.asList(1, 2, 3), asientos());
    }

    @Test
    @Tag("r3")
    void r3ReutilizaAsientoCanceladoAlFinalDeLaFila() {
        cargarTres();
        cola.cancelarPorAsiento(2);
        cola.agregar(new Pasajero("Pepe", 2, "PRIMERA CLASE"));
        assertEquals(Arrays.asList(1, 3, 2), asientos());
        assertEquals(1, cola.extraer().getNumeroAsiento());
        assertEquals(3, cola.extraer().getNumeroAsiento());
        assertEquals("Pepe", cola.extraer().getNombre());
    }

    @Test
    @Tag("r3")
    void r3CancelacionFueraDeRangoSeRechaza() {
        cargarTres();
        for (int asiento : new int[]{0, 31}) {
            assertThrows(IllegalArgumentException.class,
                    () -> cola.cancelarPorAsiento(asiento));
        }
        assertEquals(Arrays.asList(1, 2, 3), asientos());
    }
}
