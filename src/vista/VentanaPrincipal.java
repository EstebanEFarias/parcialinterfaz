/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import controlador.SimulacionController;
import java.awt.CardLayout;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class VentanaPrincipal extends javax.swing.JFrame {

    private SimulacionController controlador;
    private DefaultTableModel modeloEntidades;
    private DefaultTableModel modeloResumen;
    private DefaultTableModel modeloPorTipo;
    private DefaultTableModel modeloHistorial;
    private DefaultListModel<String> modeloEventos;

    public VentanaPrincipal() {
        initComponents();
        modeloEntidades = (DefaultTableModel) tblEntidades.getModel();
        modeloResumen = (DefaultTableModel) tblResumen.getModel();
        modeloPorTipo = (DefaultTableModel) tblPorTipo.getModel();
        modeloHistorial = (DefaultTableModel) tblHistorial.getModel();
        modeloEntidades.setRowCount(0);
        modeloResumen.setRowCount(0);
        modeloPorTipo.setRowCount(0);
        modeloHistorial.setRowCount(0);

        modeloEventos = new DefaultListModel<>();
        lstEventos.setModel(modeloEventos);
    }

    public void setControlador(SimulacionController controlador) {
        this.controlador = controlador;
    }

    public void mostrarPantalla(String nombreCard) {
        CardLayout cl = (CardLayout) pnlContenedor.getLayout();
        cl.show(pnlContenedor, nombreCard);
    }

    public void cargarClimas(String[] climas) {
        cboClimaInicial.removeAllItems();
        cboClimaIntervencion.removeAllItems();
        for (String clima : climas) {
            cboClimaInicial.addItem(clima);
            cboClimaIntervencion.addItem(clima);
        }
    }

    public void actualizarContadores(int turno, int turnosTotales, String clima,
            int plantas, int conejos, int lobos) {
        lblTurno.setText("Turno: " + turno + " / " + turnosTotales);
        lblClima.setText("Clima: " + clima);
        lblPlantas.setText("Plantas: " + plantas);
        lblConejos.setText("Conejos: " + conejos);
        lblLobos.setText("Lobos: " + lobos);
    }

    public void actualizarTabla(List<Object[]> filas) {
        modeloEntidades.setRowCount(0);
        for (Object[] fila : filas) {
            modeloEntidades.addRow(fila);
        }
    }

    public void actualizarEventos(List<String> eventos) {
        modeloEventos.clear();
        if (eventos.isEmpty()) {
            modeloEventos.addElement("Todavía no hay eventos.");
        }
        for (String evento : eventos) {
            modeloEventos.addElement(evento);
        }
    }

    public void habilitarIntervencion(boolean habilitado, String texto) {
        cboClimaIntervencion.setEnabled(habilitado);
        btnCambiarClima.setEnabled(habilitado);
        cboTipoEntidad.setEnabled(habilitado);
        btnAgregarEntidad.setEnabled(habilitado);
        lblIntervencion.setText(texto);
    }

    public void habilitarAvanzar(boolean habilitado) {
        btnAvanzar.setEnabled(habilitado);
    }

    public void habilitarVerReporte(boolean habilitado) {
        btnVerReporte.setEnabled(habilitado);
    }

    public void mostrarReporte(List<Object[]> resumen, List<Object[]> porTipo, List<Object[]> historial) {
        modeloResumen.setRowCount(0);
        for (Object[] fila : resumen) {
            modeloResumen.addRow(fila);
        }
        modeloPorTipo.setRowCount(0);
        for (Object[] fila : porTipo) {
            modeloPorTipo.addRow(fila);
        }
        modeloHistorial.setRowCount(0);
        for (Object[] fila : historial) {
            modeloHistorial.addRow(fila);
        }
    }

    public void limpiarFormulario() {
        txtPlantas.setText("");
        txtConejos.setText("");
        txtLobos.setText("");
        txtTurnos.setText("");
        if (cboClimaInicial.getItemCount() > 0) {
            cboClimaInicial.setSelectedIndex(0);
        }
    }

    public void limpiarSimulacion() {
        lblTurno.setText("Turno: -");
        lblClima.setText("Clima: -");
        lblPlantas.setText("Plantas: -");
        lblConejos.setText("Conejos: -");
        lblLobos.setText("Lobos: -");
        modeloEntidades.setRowCount(0);
        modeloEventos.clear();
        modeloEventos.addElement("Todavía no hay una simulación en curso.");
        modeloResumen.setRowCount(0);
        modeloPorTipo.setRowCount(0);
        modeloHistorial.setRowCount(0);
    }

    public void mostrarMensaje(String texto) {
        lblEstado.setText(texto);
    }

    public void mostrarError(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarInformacion(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean confirmar(String texto) {
        int respuesta = JOptionPane.showConfirmDialog(this, texto, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlContenedor = new javax.swing.JPanel();
        pnlConfig = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlFormulario = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPlantas = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtConejos = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtLobos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cboClimaInicial = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtTurnos = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnIniciar = new javax.swing.JButton();
        pnlSimulacion = new javax.swing.JPanel();
        pnlContadores = new javax.swing.JPanel();
        lblTurno = new javax.swing.JLabel();
        lblClima = new javax.swing.JLabel();
        lblPlantas = new javax.swing.JLabel();
        lblConejos = new javax.swing.JLabel();
        lblLobos = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEntidades = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstEventos = new javax.swing.JList<>();
        pnlAcciones = new javax.swing.JPanel();
        lblIntervencion = new javax.swing.JLabel();
        pnlIntervencion = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cboClimaIntervencion = new javax.swing.JComboBox<>();
        btnCambiarClima = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        cboTipoEntidad = new javax.swing.JComboBox<>();
        btnAgregarEntidad = new javax.swing.JButton();
        pnlBotonesSim = new javax.swing.JPanel();
        btnVerReporte = new javax.swing.JButton();
        btnAvanzar = new javax.swing.JButton();
        pnlReporte = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        pnlTablasReporte = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblResumen = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPorTipo = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblHistorial = new javax.swing.JTable();
        pnlBotonesReporte = new javax.swing.JPanel();
        btnVolver = new javax.swing.JButton();
        btnNuevaSimulacion = new javax.swing.JButton();
        lblEstado = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuSimulacion = new javax.swing.JMenu();
        itemNueva = new javax.swing.JMenuItem();
        itemSalir = new javax.swing.JMenuItem();
        menuReportes = new javax.swing.JMenu();
        itemVerReporte = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlContenedor.setPreferredSize(new java.awt.Dimension(1000, 620));
        pnlContenedor.setLayout(new java.awt.CardLayout());

        pnlConfig.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Configurar un nuevo ecosistema");
        pnlConfig.add(jLabel1, java.awt.BorderLayout.NORTH);

        pnlFormulario.setBorder(javax.swing.BorderFactory.createEmptyBorder(120, 250, 120, 250));
        pnlFormulario.setLayout(new java.awt.GridLayout(6, 2, 10, 15));

        jLabel2.setText("Cantidad de plantas (5 a 30):");
        pnlFormulario.add(jLabel2);
        pnlFormulario.add(txtPlantas);

        jLabel3.setText("Cantidad de conejos (2 a 15):");
        pnlFormulario.add(jLabel3);
        pnlFormulario.add(txtConejos);

        jLabel4.setText("Cantidad de lobos (1 a 5):");
        pnlFormulario.add(jLabel4);
        pnlFormulario.add(txtLobos);

        jLabel5.setText("Clima inicial:");
        pnlFormulario.add(jLabel5);

        cboClimaInicial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "  SOLEADO", "  LLUVIOSO", "  SEQUIA", "  INVIERNO" }));
        pnlFormulario.add(cboClimaInicial);

        jLabel6.setText("Cantidad de turnos (10 a 50):");
        pnlFormulario.add(jLabel6);
        pnlFormulario.add(txtTurnos);
        pnlFormulario.add(jLabel7);

        btnIniciar.setText("Iniciar simulación");
        btnIniciar.addActionListener(this::btnIniciarActionPerformed);
        pnlFormulario.add(btnIniciar);

        pnlConfig.add(pnlFormulario, java.awt.BorderLayout.CENTER);

        pnlContenedor.add(pnlConfig, "config");

        pnlSimulacion.setLayout(new java.awt.BorderLayout());

        pnlContadores.setLayout(new java.awt.GridLayout(1, 5, 10, 0));

        lblTurno.setText("Turno: -");
        pnlContadores.add(lblTurno);

        lblClima.setText("Clima: -");
        pnlContadores.add(lblClima);

        lblPlantas.setText("Plantas: -");
        pnlContadores.add(lblPlantas);

        lblConejos.setText("Conejos: -");
        pnlContadores.add(lblConejos);

        lblLobos.setText("Lobos: -");
        pnlContadores.add(lblLobos);

        pnlSimulacion.add(pnlContadores, java.awt.BorderLayout.NORTH);

        tblEntidades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo", "Nombre", "Energia", "Edad", "Detalle"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblEntidades);

        pnlSimulacion.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(350, 30));

        lstEventos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstEventos);

        pnlSimulacion.add(jScrollPane2, java.awt.BorderLayout.EAST);

        pnlAcciones.setLayout(new java.awt.BorderLayout());

        lblIntervencion.setText("Próxima intervención: turno 3.");
        pnlAcciones.add(lblIntervencion, java.awt.BorderLayout.NORTH);

        pnlIntervencion.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel8.setText("Clima:");
        pnlIntervencion.add(jLabel8);

        cboClimaIntervencion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SOLEADO", "LLUVIOSO", "SEQUIA", "INVIERNO" }));
        pnlIntervencion.add(cboClimaIntervencion);

        btnCambiarClima.setText("Cambiar clima");
        btnCambiarClima.addActionListener(this::btnCambiarClimaActionPerformed);
        pnlIntervencion.add(btnCambiarClima);

        jLabel9.setText("Agregar:");
        pnlIntervencion.add(jLabel9);

        cboTipoEntidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Planta", "Conejo", "Lobo" }));
        pnlIntervencion.add(cboTipoEntidad);

        btnAgregarEntidad.setText("Agregar entidad");
        btnAgregarEntidad.addActionListener(this::btnAgregarEntidadActionPerformed);
        pnlIntervencion.add(btnAgregarEntidad);

        pnlAcciones.add(pnlIntervencion, java.awt.BorderLayout.CENTER);

        pnlBotonesSim.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnVerReporte.setText("Ver reporte final");
        btnVerReporte.addActionListener(this::btnVerReporteActionPerformed);
        pnlBotonesSim.add(btnVerReporte);

        btnAvanzar.setText("Avanzar turno");
        btnAvanzar.addActionListener(this::btnAvanzarActionPerformed);
        pnlBotonesSim.add(btnAvanzar);

        pnlAcciones.add(pnlBotonesSim, java.awt.BorderLayout.SOUTH);

        pnlSimulacion.add(pnlAcciones, java.awt.BorderLayout.SOUTH);

        pnlContenedor.add(pnlSimulacion, "simulacion");

        pnlReporte.setLayout(new java.awt.BorderLayout());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Reporte final de la simulación");
        pnlReporte.add(jLabel10, java.awt.BorderLayout.NORTH);

        pnlTablasReporte.setLayout(new java.awt.GridLayout(3, 1, 0, 10));

        tblResumen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Concepto", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblResumen);

        pnlTablasReporte.add(jScrollPane3);

        tblPorTipo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo", "Nacimientos", "Muertes", "Maxima Poblacion", "Minima Poblacion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblPorTipo);

        pnlTablasReporte.add(jScrollPane4);

        tblHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Turno", "Plantas", "Conejos", "Lobos"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tblHistorial);

        pnlTablasReporte.add(jScrollPane5);

        pnlReporte.add(pnlTablasReporte, java.awt.BorderLayout.CENTER);

        pnlBotonesReporte.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnVolver.setText("Volver a la simulación");
        btnVolver.addActionListener(this::btnVolverActionPerformed);
        pnlBotonesReporte.add(btnVolver);

        btnNuevaSimulacion.setText("Nueva simulación");
        btnNuevaSimulacion.addActionListener(this::btnNuevaSimulacionActionPerformed);
        pnlBotonesReporte.add(btnNuevaSimulacion);

        pnlReporte.add(pnlBotonesReporte, java.awt.BorderLayout.SOUTH);

        pnlContenedor.add(pnlReporte, "reporte");

        getContentPane().add(pnlContenedor, java.awt.BorderLayout.CENTER);

        lblEstado.setText("Listo.");
        getContentPane().add(lblEstado, java.awt.BorderLayout.SOUTH);

        menuSimulacion.setText("Simulación");

        itemNueva.setText("Nueva simulación");
        itemNueva.addActionListener(this::itemNuevaActionPerformed);
        menuSimulacion.add(itemNueva);

        itemSalir.setText("Salir");
        itemSalir.addActionListener(this::itemSalirActionPerformed);
        menuSimulacion.add(itemSalir);

        jMenuBar1.add(menuSimulacion);

        menuReportes.setText("Reportes");

        itemVerReporte.setText("Ver reporte final");
        itemVerReporte.addActionListener(this::itemVerReporteActionPerformed);
        menuReportes.add(itemVerReporte);

        jMenuBar1.add(menuReportes);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        controlador.iniciarSimulacion(txtPlantas.getText(), txtConejos.getText(), txtLobos.getText(), (String) cboClimaInicial.getSelectedItem(), txtTurnos.getText());  // TODO add your handling code here:
    }//GEN-LAST:event_btnIniciarActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        controlador.volverASimulacion();        // TODO add your handling code here:
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnNuevaSimulacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaSimulacionActionPerformed
        controlador.nuevaSimulacion();        // TODO add your handling code here:
    }//GEN-LAST:event_btnNuevaSimulacionActionPerformed

    private void itemNuevaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNuevaActionPerformed
        controlador.nuevaSimulacion();
    }//GEN-LAST:event_itemNuevaActionPerformed

    private void itemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSalirActionPerformed
        dispose();
    }//GEN-LAST:event_itemSalirActionPerformed

    private void itemVerReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemVerReporteActionPerformed
        controlador.verReporte();
    }//GEN-LAST:event_itemVerReporteActionPerformed

    private void btnAvanzarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvanzarActionPerformed
        controlador.avanzarTurno();    }//GEN-LAST:event_btnAvanzarActionPerformed

    private void btnCambiarClimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarClimaActionPerformed
        controlador.cambiarClima((String) cboClimaIntervencion.getSelectedItem());    }//GEN-LAST:event_btnCambiarClimaActionPerformed

    private void btnAgregarEntidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEntidadActionPerformed
        controlador.agregarEntidad((String) cboTipoEntidad.getSelectedItem());    }//GEN-LAST:event_btnAgregarEntidadActionPerformed

    private void btnVerReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerReporteActionPerformed
        controlador.verReporte();    }//GEN-LAST:event_btnVerReporteActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarEntidad;
    private javax.swing.JButton btnAvanzar;
    private javax.swing.JButton btnCambiarClima;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnNuevaSimulacion;
    private javax.swing.JButton btnVerReporte;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> cboClimaInicial;
    private javax.swing.JComboBox<String> cboClimaIntervencion;
    private javax.swing.JComboBox<String> cboTipoEntidad;
    private javax.swing.JMenuItem itemNueva;
    private javax.swing.JMenuItem itemSalir;
    private javax.swing.JMenuItem itemVerReporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblClima;
    private javax.swing.JLabel lblConejos;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblIntervencion;
    private javax.swing.JLabel lblLobos;
    private javax.swing.JLabel lblPlantas;
    private javax.swing.JLabel lblTurno;
    private javax.swing.JList<String> lstEventos;
    private javax.swing.JMenu menuReportes;
    private javax.swing.JMenu menuSimulacion;
    private javax.swing.JPanel pnlAcciones;
    private javax.swing.JPanel pnlBotonesReporte;
    private javax.swing.JPanel pnlBotonesSim;
    private javax.swing.JPanel pnlConfig;
    private javax.swing.JPanel pnlContadores;
    private javax.swing.JPanel pnlContenedor;
    private javax.swing.JPanel pnlFormulario;
    private javax.swing.JPanel pnlIntervencion;
    private javax.swing.JPanel pnlReporte;
    private javax.swing.JPanel pnlSimulacion;
    private javax.swing.JPanel pnlTablasReporte;
    private javax.swing.JTable tblEntidades;
    private javax.swing.JTable tblHistorial;
    private javax.swing.JTable tblPorTipo;
    private javax.swing.JTable tblResumen;
    private javax.swing.JTextField txtConejos;
    private javax.swing.JTextField txtLobos;
    private javax.swing.JTextField txtPlantas;
    private javax.swing.JTextField txtTurnos;
    // End of variables declaration//GEN-END:variables
}
