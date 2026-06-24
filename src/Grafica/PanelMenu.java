package Grafica;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import ModosDeJuego.ModoTipo;
import Fabricas.Dominio;
import Fabricas.DominioClasico;
import Fabricas.DominioAlternativo;

public class PanelMenu extends PanelVista {
    private static final long serialVersionUID = -3884090044789061650L;
    private JLabel imagenFondo;
    private JButton botonIniciar;
    private JButton botonPuntajes;
    private static final Color MODE_BG = new Color(230, 230, 230);
    private static final Color MODE_SELECTED_BG = new Color(255, 200, 0);
    private static final Color DOMAIN_BG = MODE_BG;
    private static final Color DOMAIN_SELECTED_BG = MODE_SELECTED_BG;

    private JButton btnClasico;
    private JButton btnContrarreloj;
    private JButton btnSupervivencia;
    private JButton btnDominioClasico;
    private JButton btnDominioAlternativo;

    public PanelMenu(ControladorVistas controladorVistas) {
        agregarControlador(controladorVistas);
        setSize(ConstantesVistas.PANEL_ANCHO, ConstantesVistas.PANEL_ALTO);
        setLayout(null);
        agregarFondoImagen();
        agregarBotonIniciar();
        agregarBotonPuntaje();
        agregarBotonesModo();
        agregarBotonesDominio();
        seleccionarModoButton(btnClasico);
        seleccionarDominioButton(btnDominioClasico);
        if (imagenFondo != null) {
            setComponentZOrder(imagenFondo, getComponentCount() - 1);
        }
    }

    protected void agregarFondoImagen() {
        imagenFondo = new JLabel();
        try {
            java.net.URL imgUrl = getClass().getResource("/Sprites/originales/inicioSnowBros.png");

            if (imgUrl == null) {
                throw new IllegalArgumentException(
                        "Recurso no encontrado en classpath: /Sprites/originales/inicioSnowBros.png");
            }

            ImageIcon icono_imagen = new ImageIcon(imgUrl);
            Image imagen_escalada = icono_imagen.getImage().getScaledInstance(ConstantesVistas.PANEL_ANCHO,
                    ConstantesVistas.PANEL_ALTO, Image.SCALE_SMOOTH);
            Icon icono_imagen_escalado = new ImageIcon(imagen_escalada);
            imagenFondo.setIcon(icono_imagen_escalado);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            imagenFondo.setText("Error al cargar la imagen");
        }
        imagenFondo.setBounds(0, 0, ConstantesVistas.PANEL_ANCHO, ConstantesVistas.PANEL_ALTO);
        add(imagenFondo);
    }

    protected void agregarBotonIniciar() {
        botonIniciar = new PixelButton("Iniciar");
        decorarBotonIniciar();
        registrarOyenteBotonIniciar();
        add(botonIniciar);
    }

    protected void agregarBotonPuntaje() {
        botonPuntajes = new PixelButton("Puntajes");
        decorarBotonPuntajes();
        registrarOyenteBotonPuntajes();
        add(botonPuntajes);
    }

    protected void decorarBotonIniciar() {
        botonIniciar.setText("Iniciar");
        botonIniciar.setBounds((ConstantesVistas.PANEL_ANCHO / 2) - 100, ConstantesVistas.PANEL_ALTO - 150, 200, 50);
        botonIniciar.setBackground(Color.GREEN);
        botonIniciar.setForeground(Color.BLACK);

        // estilo pixel y bordes curvos
        botonIniciar.setFont(new Font("Monospaced", Font.BOLD, 16));
        botonIniciar.setOpaque(false);
        botonIniciar.setContentAreaFilled(false);
        botonIniciar.setBorderPainted(false);
        botonIniciar.setFocusPainted(false);
        botonIniciar.repaint();
    }

    protected void decorarBotonPuntajes() {
        botonPuntajes.setText("Puntajes");
        botonPuntajes.setBounds((ConstantesVistas.PANEL_ANCHO / 2) - 130, ConstantesVistas.PANEL_ALTO - 90, 260, 50);
        botonPuntajes.setBackground(new Color(0, 128, 255));
        botonPuntajes.setForeground(Color.WHITE);

        botonPuntajes.setFont(new Font("Monospaced", Font.BOLD, 16));
        botonPuntajes.setOpaque(false);
        botonPuntajes.setContentAreaFilled(false);
        botonPuntajes.setBorderPainted(false);
        botonPuntajes.setFocusPainted(false);
        botonPuntajes.repaint();
    }

    protected void registrarOyenteBotonIniciar() {
        botonIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controladorVistas.accionarInicioJuego();
            }
        });
    }

    protected void registrarOyenteBotonPuntajes() {
        botonPuntajes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controladorVistas.accionarPantallaRanking();
            }
        });
    }

    private void agregarBotonesModo() {
        btnClasico = crearBoton("Clásico", (ConstantesVistas.PANEL_ANCHO / 2) - 320, ConstantesVistas.PANEL_ALTO - 240,
                180, 40, MODE_BG, Color.BLACK);
        btnContrarreloj = crearBoton("Contrarreloj", (ConstantesVistas.PANEL_ANCHO / 2) - 90,
                ConstantesVistas.PANEL_ALTO - 240, 180, 40, MODE_BG, Color.BLACK);
        btnSupervivencia = crearBoton("Supervivencia", (ConstantesVistas.PANEL_ANCHO / 2) + 140,
                ConstantesVistas.PANEL_ALTO - 240, 180, 40, MODE_BG, Color.BLACK);

        btnClasico.addActionListener(e -> {
            controladorVistas.seleccionarModo(ModoTipo.CLASICO);
            seleccionarModoButton(btnClasico);
        });
        btnContrarreloj.addActionListener(e -> {
            controladorVistas.seleccionarModo(ModoTipo.CONTRARRELOJ);
            seleccionarModoButton(btnContrarreloj);
        });
        btnSupervivencia.addActionListener(e -> {
            controladorVistas.seleccionarModo(ModoTipo.SUPERVIVENCIA);
            seleccionarModoButton(btnSupervivencia);
        });

        add(btnClasico);
        add(btnContrarreloj);
        add(btnSupervivencia);
    }

    private void agregarBotonesDominio() {
        btnDominioClasico = crearBoton("Color", (ConstantesVistas.PANEL_ANCHO / 2) - 200,
                ConstantesVistas.PANEL_ALTO - 190, 180, 40, DOMAIN_BG, Color.BLACK);
        btnDominioAlternativo = crearBoton("Minecraft", (ConstantesVistas.PANEL_ANCHO / 2) + 20,
                ConstantesVistas.PANEL_ALTO - 190, 180, 40, DOMAIN_BG, Color.BLACK);

        Dominio d1 = new DominioClasico();
        Dominio d2 = new DominioAlternativo();

        btnDominioClasico.addActionListener(e -> {
            controladorVistas.seleccionarDominio(d1);
            seleccionarDominioButton(btnDominioClasico);
        });
        btnDominioAlternativo.addActionListener(e -> {
            controladorVistas.seleccionarDominio(d2);
            seleccionarDominioButton(btnDominioAlternativo);
        });

        add(btnDominioClasico);
        add(btnDominioAlternativo);
    }

    private JButton crearBoton(String texto, int x, int y, int w, int h, Color fondo, Color textoColor) {
        JButton b = new PixelButton(texto);
        b.setBounds(x, y, w, h);
        b.setBackground(fondo);
        b.setForeground(textoColor);
        b.setFont(new Font("Monospaced", Font.BOLD, 16));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        return b;
    }

    private void seleccionarModoButton(JButton btn) {
        resetModoButtons();
        btn.setBackground(MODE_SELECTED_BG);
        btn.repaint();
    }

    private void resetModoButtons() {
        if (btnClasico != null)
            btnClasico.setBackground(MODE_BG);
        if (btnContrarreloj != null)
            btnContrarreloj.setBackground(MODE_BG);
        if (btnSupervivencia != null)
            btnSupervivencia.setBackground(MODE_BG);
    }

    private void seleccionarDominioButton(JButton btn) {
        resetDominioButtons();
        btn.setBackground(DOMAIN_SELECTED_BG);
        btn.repaint();
    }

    private void resetDominioButtons() {
        if (btnDominioClasico != null)
            btnDominioClasico.setBackground(DOMAIN_BG);
        if (btnDominioAlternativo != null)
            btnDominioAlternativo.setBackground(DOMAIN_BG);
    }

    private static class PixelButton extends JButton {
        private static final long serialVersionUID = 1L;
        private static final int ARC = 18;

        public PixelButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth();
            int h = getHeight();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            Color bg = getBackground();
            RoundRectangle2D.Float rr = new RoundRectangle2D.Float(0, 0, w, h, ARC, ARC);
            g2.setColor(bg);
            g2.fill(rr);
            g2.setColor(getForeground().darker());
            g2.draw(rr);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // El borde ya se dibuja en paintComponent sobre el rectángulo redondeado
        }
    }

}
