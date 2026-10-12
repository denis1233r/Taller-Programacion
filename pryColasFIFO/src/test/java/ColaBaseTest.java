import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

@Tag("base")
class ColaBaseTest {
    private Cola cola;

    @BeforeEach
    void preparar() {
        cola = new Cola();
    }

    @Test
    void colaNuevaEstaVacia() {
        assertTrue(cola.estaVacia());
        assertEquals(0, cola.size());
        assertTrue(cola.obtenerPendientes().isEmpty());
    }

    @Test
    void atencionEsFIFOAunqueLasCategoriasSeanDistintas() {
        cola.agregar(new Pasajero("Ana", 1, "ECONOMICA"));
        cola.agregar(new Pasajero("Juan", 2, "PRIMERA CLASE"));
        cola.agregar(new Pasajero("Flor", 3, "EJECUTIVA"));
        assertEquals(1, cola.extraer().getNumeroAsiento());
        assertEquals(2, cola.extraer().getNumeroAsiento());
        assertEquals(3, cola.extraer().getNumeroAsiento());
        assertTrue(cola.estaVacia());
    }

    @Test
    void frenteConsultaSinExtraer() {
        Pasajero ana = new Pasajero("Ana", 1, "ECONOMICA");
        cola.agregar(ana);
        assertSame(ana, cola.frente());
        assertSame(ana, cola.frente());
        assertEquals(1, cola.size());
        assertFalse(ana.isCheckin());
    }

    @Test
    void consultarOExtraerEnVacioGeneraExcepcion() {
        assertThrows(NoSuchElementException.class, () -> cola.frente());
        assertThrows(NoSuchElementException.class, () -> cola.extraer());
        assertEquals(0, cola.size());
    }

    @Test
    void pasajeroNuloSeRechazaSinCambios() {
        assertThrows(IllegalArgumentException.class, () -> cola.agregar(null));
        assertEquals(0, cola.size());
    }
}
