package Grafica;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import Logica.EntidadLogica;
import Logica.InfoJugador;

public class PanelPartida extends PanelVista implements Observer {
    private JLabel lienzoJuego;
    private JPanel statsPanel;
    private JLabel puntaje, vidas, nivel;
    private JLabel tiempoRestante;
    private InfoJugador infoJugador;

    public PanelPartida(ControladorVistas c) {
        agregarControlador(c);
        this.setDoubleBuffered(true);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1200, 800));

        agregarFondoImagen("Sprites/originales/background.png");
        agregarLabelsInformacion();
    }

    public void mostrarOverlayNivel(int numNivel, int numPiso, int duracionMs) {
        if (lienzoJuego == null)
            return;

        String texto = "Nivel: " + numNivel + "  Piso: " + numPiso;
        final PixelLabel overlay = new PixelLabel(texto);
        overlay.setOpaque(false); 
        overlay.setForeground(Color.WHITE);
        overlay.setFont(new Font("Monospaced", Font.BOLD, 52));

        int ancho = 700;
        int alto = 180;
        int x = (1200 - ancho) / 2;
        int y = (800 - alto) / 2;
        overlay.setBounds(x, y, ancho, alto);
        lienzoJuego.add(overlay);
        lienzoJuego.setComponentZOrder(overlay, 0);
        lienzoJuego.revalidate();
        lienzoJuego.repaint();
        Thread remover = new Thread(() -> {
            try {
                Thread.sleep(duracionMs);
                SwingUtilities.invokeLater(() -> {
                    if (lienzoJuego != null) {
                        lienzoJuego.remove(overlay);
                        lienzoJuego.revalidate();
                        lienzoJuego.repaint();
                    }
                });
            } catch (InterruptedException ex) {
                SwingUtilities.invokeLater(() -> {
                    if (lienzoJuego != null) {
                        lienzoJuego.remove(overlay);
                        lienzoJuego.revalidate();
                        lienzoJuego.repaint();
                    }
                });
            }
        });
        remover.setDaemon(true);
        remover.start();
    }

    private static class PixelLabel extends JLabel {
        public PixelLabel(String text) {
            super(text);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public void agregarFondoImagen(String ruta) {
        crearLienzoJuego(ruta);
    }

    private void crearLienzoJuego(String ruta) {
        lienzoJuego = new JLabel();
        lienzoJuego.setLayout(null);
        lienzoJuego.setBounds(0, 0, 1200, 800);

        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getClassLoader().getResource(ruta));
            int anchoVentana = 1200;
            int altoVentana = 800;
            Image imagenOriginal = iconoOriginal.getImage();
            Image imagenReescalada = imagenOriginal.getScaledInstance(anchoVentana, altoVentana, Image.SCALE_SMOOTH);
            ImageIcon iconoReescalado = new ImageIcon(imagenReescalada);
            lienzoJuego.setIcon(iconoReescalado);

        } catch (Exception e) {
            System.err.println("IMAGEN DE FONDO NO ENCONTRADA. El lienzo será gris.");
            lienzoJuego.setOpaque(true);
            lienzoJuego.setBackground(Color.DARK_GRAY);
        }
        this.add(lienzoJuego);
    }

    public Observer incorporarEntidad(EntidadLogica e) {
        ObserverGrafico observerGrafico = e.crearObserverGrafico();

        lienzoJuego.add(observerGrafico);
        lienzoJuego.setComponentZOrder(observerGrafico, 0);
        return observerGrafico;
    }

    private void agregarLabelsInformacion() {
        statsPanel = new JPanel();
        statsPanel.setBackground(new Color(10, 10, 10, 255));
        statsPanel.setBounds(0, 0, 1200, 40);

        puntaje = new JLabel("Puntaje: 0");
        vidas = new JLabel("Vidas: 0");
        nivel = new JLabel("Nivel: 0");
        tiempoRestante = new JLabel("");

        Font font = new Font("Arial", Font.BOLD, 18);
        Color colorTexto = Color.WHITE;
        puntaje.setForeground(Color.YELLOW);
        vidas.setForeground(Color.GREEN);
        nivel.setForeground(colorTexto);
        tiempoRestante.setForeground(colorTexto);
        puntaje.setFont(font);
        vidas.setFont(font);
        nivel.setFont(font);
        tiempoRestante.setFont(font);

        statsPanel.add(puntaje);
        statsPanel.add(vidas);
        statsPanel.add(nivel);
        statsPanel.add(tiempoRestante);

        this.add(statsPanel);
        this.setComponentZOrder(statsPanel, 0);
    }

    public void actualizarNivel(int numNivel, int numPiso) {
        if (nivel != null) {
            nivel.setText("Nivel: " + numNivel + " Piso: " + numPiso);
        }
    }

    public void actualizarTiempoRestante(int segundos) {
        if (tiempoRestante == null)
            return;
        if (segundos < 0) {
            tiempoRestante.setText("");
        } else {
            tiempoRestante.setText("Tiempo: " + segundos + "s");
        }
    }

    public void limpiarLienzo() {
        if (lienzoJuego != null) {
            lienzoJuego.removeAll();
            lienzoJuego.repaint();
        }
    }

    public void registrarInfoJugador(InfoJugador info) {
        this.infoJugador = info;
        if (info != null) {
            info.agregarObserver(this);
            actualizarStats();
        }
    }

    @Override
    public void actualizar() {
        actualizarStats();
    }

    private void actualizarStats() {
        if (infoJugador != null) {
            puntaje.setText("Puntaje: " + infoJugador.getPuntaje());
            vidas.setText("Vidas: " + infoJugador.getVidas());
        }
    }
}