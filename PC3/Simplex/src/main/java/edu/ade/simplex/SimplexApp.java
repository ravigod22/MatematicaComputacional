package edu.ade.simplex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimplexApp extends JFrame {
    private JTextField txtVariables, txtRestricciones;
    private JButton btnAceptar;

    public SimplexApp() {
        setTitle("Simplex Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblVariables = new JLabel("Número de variables:");
        txtVariables = new JTextField(10);
        panel.add(lblVariables);
        panel.add(txtVariables);

        JLabel lblRestricciones = new JLabel("Número de restricciones:");
        txtRestricciones = new JTextField(10);
        panel.add(lblRestricciones);
        panel.add(txtRestricciones);

        btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int nroVariables = Integer.parseInt(txtVariables.getText());
                int nroRestricciones = Integer.parseInt(txtRestricciones.getText());
                crearVentanaFuncionObjetivo(nroVariables, nroRestricciones);
            }
        });
        panel.add(btnAceptar);

        add(panel);
        pack();
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    private void crearVentanaFuncionObjetivo(int nroVariables, int nroRestricciones) {
        double[] array_Objetivo = new double[nroVariables];
        double[][] tabla_Restricciones = new double[nroRestricciones][nroVariables + 1];
        JPanel panelFuncionObjetivo = new JPanel();
        panelFuncionObjetivo.setLayout(new FlowLayout());

        JLabel lblFuncionObjetivo = new JLabel("Función objetivo:");
        panelFuncionObjetivo.add(lblFuncionObjetivo);

        JTextField[] txtVariablesArray = new JTextField[nroVariables];
        for (int i = 0; i < nroVariables; i++) {
            JTextField txtVariable = new JTextField(5);
            panelFuncionObjetivo.add(txtVariable);
            txtVariablesArray[i] = txtVariable;
        }
        // Crear matriz de restricciones
        JPanel panelRestricciones = new JPanel();
        panelRestricciones.setLayout(new GridLayout(nroRestricciones, nroVariables + 1)); // +1 para el resultado
        for (int i = 0; i < nroRestricciones; i++) {
            for (int j = 0; j < nroVariables + 1; j++) {
                JTextField txtCampo = new JTextField(5);
                panelRestricciones.add(txtCampo);
            }
        }
        JButton btnCalcular = new JButton("Calcular Simplex");
        btnCalcular.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < nroVariables; i++) {
                    array_Objetivo[i] = Double.parseDouble(txtVariablesArray[i].getText());
                }
                Component[] components = panelRestricciones.getComponents();
                int index = 0;
                for (int i = 0; i < nroRestricciones; i++) {
                    for (int j = 0; j < nroVariables + 1; j++) {
                        JTextField txtCampo = (JTextField) components[index];
                        tabla_Restricciones[i][j] = Double.parseDouble(txtCampo.getText());
                        index++;
                    }
                }
                Simplex simplex = new Simplex(nroVariables, nroRestricciones, array_Objetivo, tabla_Restricciones);
                simplex.simplexMaximizar();
                JOptionPane.showMessageDialog(null, "Simplex calculado y resultados mostrados.");
                simplex.printValues();
            }
        });
        JFrame ventanaFuncionObjetivo = new JFrame("Ingresar Función Objetivo y Restricciones");
        ventanaFuncionObjetivo.setLayout(new BoxLayout(ventanaFuncionObjetivo.getContentPane(), BoxLayout.Y_AXIS));
        ventanaFuncionObjetivo.add(panelFuncionObjetivo);
        ventanaFuncionObjetivo.add(panelRestricciones);
        ventanaFuncionObjetivo.add(btnCalcular); 
        ventanaFuncionObjetivo.pack();
        ventanaFuncionObjetivo.setLocationRelativeTo(null);
        ventanaFuncionObjetivo.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SimplexApp();
            }
        });
    }
}

