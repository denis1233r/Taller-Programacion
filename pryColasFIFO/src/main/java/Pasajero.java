public class Pasajero {
    private final String nombre;
    private final int numeroAsiento;
    private final String tipoAsiento;
    private boolean checkin;

    public Pasajero(String nombre, int numeroAsiento, String tipoAsiento) {
        this.nombre = nombre;
        this.numeroAsiento = numeroAsiento;
        this.tipoAsiento = tipoAsiento;
        checkin = false;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNumeroAsiento() {
        return numeroAsiento;
    }

    public String getTipoAsiento() {
        return tipoAsiento;
    }

    public boolean isCheckin() {
        return checkin;
    }

    public void setCheckin(boolean checkin) {
        this.checkin = checkin;
    }

    @Override
    public String toString() {
        return nombre + " | Asiento: " + numeroAsiento
                + " | " + tipoAsiento + " | Check-in: " + checkin;
    }
}
