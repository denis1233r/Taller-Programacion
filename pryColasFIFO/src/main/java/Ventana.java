import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame {
    private final Cola cola = new Cola();
    private final JTextField txtNombre = new JTextField(14);
    private final JSpinner spiAsiento =
            new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
    private final JComboBox<String> cboTipo = new JComboBox<>(
            new String[]{"ECONOMICA", "EJECUTIVA", "PRIMERA CLASE"});
    private final JSpinner spiConsulta =
            new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
    private final JTextArea txtListado = new JTextArea(12, 55);
    private final JLabel lblCantidad = new JLabel();
    private final JLabel lblMensaje = new JLabel("Registre un pasajero para comenzar.");

    public Ventana() {
        super("Taller de pasajeros - Cola FIFO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel registro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        registro.add(new JLabel("Nombre:"));
        registro.add(txtNombre);
        registro.add(new JLabel("Asiento:"));
        registro.add(spiAsiento);
        registro.add(cboTipo);
        JButton btnAgregar = new JButton("Agregar pasajero");
        registro.add(btnAgregar);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acciones.add(new JLabel("Asiento para buscar o cancelar:"));
        acciones.add(spiConsulta);
        JButton btnBuscar = new JButton("Buscar y ver posición");
        JButton btnCancelar = new JButton("Cancelar pasajero");
        acciones.add(btnBuscar);
        acciones.add(btnCancelar);

        JPanel superior = new JPanel(new GridLayout(2, 1));
        superior.add(registro);
        superior.add(acciones);
        add(superior, BorderLayout.NORTH);

        txtListado.setEditable(false);
        txtListado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        add(new JScrollPane(txtListado), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnFrente = new JButton("Ver siguiente");
        JButton btnAtender = new JButton("Atender check-in");
        botones.add(btnFrente);
        botones.add(btnAtender);
        botones.add(lblCantidad);

        JPanel inferior = new JPanel(new GridLayout(2, 1));
        inferior.add(botones);
        inferior.add(lblMensaje);
        add(inferior, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> ejecutar(() -> {
            Pasajero p = new Pasajero(txtNombre.getText(),
                    (Integer) spiAsiento.getValue(),
                    (String) cboTipo.getSelectedItem());
            cola.agregar(p);
            txtNombre.setText("");
            lblMensaje.setText("Pasajero registrado: " + p.getNombre());
        }));

        btnBuscar.addActionListener(e -> ejecutar(() -> {
            int asiento = (Integer) spiConsulta.getValue();
            Pasajero p = cola.buscarPorAsiento(asiento);
            if (p == null) {
                lblMensaje.setText("El asiento no está en la fila.");
            } else {
                lblMensaje.setText(p.getNombre() + " está en la posición "
                        + cola.posicionPorAsiento(asiento));
            }
        }));

        btnCancelar.addActionListener(e -> ejecutar(() -> {
            Pasajero p = cola.cancelarPorAsiento((Integer) spiConsulta.getValue());
            lblMensaje.setText("Registro cancelado: " + p.getNombre());
        }));

        btnFrente.addActionListener(e -> ejecutar(() ->
                lblMensaje.setText("Siguiente: " + cola.frente())));

        btnAtender.addActionListener(e -> ejecutar(() -> {
            Pasajero p = cola.extraer();
            p.setCheckin(true);
            lblMensaje.setText("Check-in completado: " + p);
        }));

        actualizarVista();
        pack();
        setLocationRelativeTo(null);
    }

    private void ejecutar(Runnable accion) {
        try {
            accion.run();
        } catch (RuntimeException ex) {
            lblMensaje.setText(ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        actualizarVista();
    }

    private void actualizarVista() {
        txtListado.setText(cola.toString());
        lblCantidad.setText("Pendientes: " + cola.size());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ventana().setVisible(true));
    }
}
