package Grafica;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import ModosDeJuego.EntradaRanking;
import ModosDeJuego.ModoDeJuego;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PanelRanking extends PanelVista {

    private ModoDeJuego modoClasico;
    private ModoDeJuego modoSupervivencia;
    private ModoDeJuego modoContrarreloj;

    private JPanel panelClasicoUI;
    private JPanel panelSupervivenciaUI;
    private JPanel panelContrarrelojUI;
    private JButton botonVolver;
    public PanelRanking(ControladorVistas c, ModoDeJuego clasico, ModoDeJuego supervivencia, ModoDeJuego contrarreloj) {
        agregarControlador(c);
        this.modoClasico = clasico;
        this.modoSupervivencia = supervivencia;
        this.modoContrarreloj = contrarreloj;
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelCentral = new JPanel(new GridLayout(1, 3, 20, 0)); // 1 fila, 3 columnas, 20px de espacio horizontal
        configurarPaneles(panelCentral);
        botonVolver = new JButton("Volver al Menú");
        botonVolver.setFont(new Font("Arial", Font.BOLD, 16));
        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controladorVistas.mostrarPantallaInicial();
            }
        });
        this.add(panelCentral, BorderLayout.CENTER);
        this.add(botonVolver, BorderLayout.SOUTH);
        actualizarPanelesDeRanking();
    }

    private void configurarPaneles(JPanel panelCentral) {
        panelCentral.setOpaque(false); 
        panelClasicoUI = crearPanelRankingVisual("Ranking Clásico");
        panelSupervivenciaUI = crearPanelRankingVisual("Ranking Supervivencia");
        panelContrarrelojUI = crearPanelRankingVisual("Ranking Contrarreloj");
        panelCentral.add(panelClasicoUI);
        panelCentral.add(panelSupervivenciaUI);
        panelCentral.add(panelContrarrelojUI);
    }

    private JPanel crearPanelRankingVisual(String titulo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Border bordeVacio = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        TitledBorder bordeConTitulo = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2), 
                titulo);
        bordeConTitulo.setTitleFont(new Font("Arial", Font.BOLD, 18));
        bordeConTitulo.setTitleJustification(TitledBorder.CENTER);
        panel.setBorder(BorderFactory.createCompoundBorder(bordeConTitulo, bordeVacio));
        panel.setOpaque(false); 
        return panel;
    }

    public void actualizarPanelesDeRanking() {
        recargarRankings();
        List<EntradaRanking> topClasico = modoClasico.getRanking().getTop5();
        List<EntradaRanking> topSupervivencia = modoSupervivencia.getRanking().getTop5();
        List<EntradaRanking> topContrarreloj = modoContrarreloj.getRanking().getTop5();
        actualizarCadaPanel(topClasico, topSupervivencia, topContrarreloj);
        this.revalidate();
        this.repaint();
    }

    private void actualizarCadaPanel(List<EntradaRanking> topClasico, List<EntradaRanking> topSupervivencia,
            List<EntradaRanking> topContrarreloj) {
        popularPanelConLista(panelClasicoUI, topClasico);
        popularPanelConLista(panelSupervivenciaUI, topSupervivencia);
        popularPanelConLista(panelContrarrelojUI, topContrarreloj);
    }

    private void recargarRankings() {
        modoClasico.getRanking().recargarRanking();
        modoSupervivencia.getRanking().recargarRanking();
        modoContrarreloj.getRanking().recargarRanking();
    }

    private void popularPanelConLista(JPanel panel, List<EntradaRanking> listaEntradas) {
        panel.removeAll();
        if (listaEntradas.isEmpty()) {
            JLabel vacio = new JLabel("Aún no hay puntajes");
            vacio.setFont(new Font("Arial", Font.ITALIC, 14));
            vacio.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar
            panel.add(vacio);
        } else {
            for (int i = 0; i < listaEntradas.size(); i++) {
                EntradaRanking entrada = listaEntradas.get(i);
                String texto = (i + 1) + ". " + entrada.getNombre() + " - " + entrada.getPuntaje() + " pts";

                JLabel labelPuntaje = new JLabel(texto);
                labelPuntaje.setFont(new Font("Arial", Font.PLAIN, 16));
                labelPuntaje.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar
                panel.add(labelPuntaje);
                if (i < listaEntradas.size() - 1) {
                    panel.add(Box.createRigidArea(new Dimension(0, 5)));
                }
            }
        }
    }
}