package Grafica;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import ModosDeJuego.Ranking;

public class PanelGameOver extends PanelVista {
	private JLabel imagenFondo;
	private JButton volverMenu;
	private JButton verRankings;
	private JLabel mensajeRanking;
	private JLabel labelNombre;
	private JTextField inputNombre;
	private JButton confirmarNombre;

	private Ranking rankingActual;
	private int puntajeFinal;

	public PanelGameOver(ControladorVistas controladorVistas) {
		agregarControlador(controladorVistas);
		setSize(ConstantesVistas.PANEL_ANCHO, ConstantesVistas.PANEL_ALTO);
		setLayout(null);
		agregarFondoImagen();
		crearComponentes();
		if (imagenFondo != null) {
			setComponentZOrder(imagenFondo, getComponentCount() - 1);
		}
	}

	private void crearComponentes() {
		mensajeRanking = new JLabel();
		mensajeRanking.setFont(new Font("Arial", Font.BOLD, 24));
		mensajeRanking.setForeground(Color.YELLOW);
		mensajeRanking.setHorizontalAlignment(SwingConstants.CENTER);
		mensajeRanking.setBounds(200, 250, 800, 40);
		mensajeRanking.setVisible(false);
		add(mensajeRanking);

		labelNombre = new JLabel("Ingresa tu nombre:");
		labelNombre.setFont(new Font("Arial", Font.BOLD, 20));
		labelNombre.setForeground(Color.WHITE);
		labelNombre.setHorizontalAlignment(SwingConstants.CENTER);
		labelNombre.setBounds(400, 320, 400, 30);
		labelNombre.setVisible(false);
		add(labelNombre);

		inputNombre = new JTextField(15);
		inputNombre.setFont(new Font("Arial", Font.PLAIN, 18));
		inputNombre.setBounds(450, 360, 300, 35);
		inputNombre.setHorizontalAlignment(JTextField.CENTER);
		inputNombre.setVisible(false);
		add(inputNombre);

		confirmarNombre = new JButton("Confirmar");
		confirmarNombre.setFont(new Font("Arial", Font.BOLD, 16));
		confirmarNombre.setBounds(500, 410, 200, 40);
		confirmarNombre.setBackground(Color.GREEN);
		confirmarNombre.setForeground(Color.BLACK);
		confirmarNombre.setVisible(false);
		confirmarNombre.addActionListener(e -> confirmarNombreJugador());
		add(confirmarNombre);

		volverMenu = new JButton("Volver al Menú");
		volverMenu.setFont(new Font("Arial", Font.BOLD, 18));
		volverMenu.setBounds(350, 550, 250, 50);
		volverMenu.setBackground(Color.YELLOW);
		volverMenu.setForeground(Color.BLACK);
		volverMenu.setVisible(false);
		volverMenu.addActionListener(e -> {
			controladorVistas.mostrarPantallaInicial();
			actualizarPanelesDeRankingControlador();
		});
		add(volverMenu);

		verRankings = new JButton("Ver Rankings");
		verRankings.setFont(new Font("Arial", Font.BOLD, 18));
		verRankings.setBounds(650, 550, 250, 50);
		verRankings.setBackground(Color.CYAN);
		verRankings.setForeground(Color.BLACK);
		verRankings.setVisible(false);
		verRankings.addActionListener(e -> {
			controladorVistas.accionarPantallaRanking();
			actualizarPanelesDeRankingControlador();
		});
		add(verRankings);
	}

	public void configurarConRanking(Ranking ranking, int puntaje, String nombreActual) {
		this.rankingActual = ranking;
		this.puntajeFinal = puntaje;
		ocultarTodosLosComponentes();
		boolean califica = ranking.esTop5(puntaje);

		if (califica) {
			mensajeRanking.setText("¡Felicitaciones! Entraste al Top 5");
			mensajeRanking.setForeground(Color.RED);
			mensajeRanking.setVisible(true);
			labelNombre.setVisible(true);
			inputNombre.setVisible(true);
			if (nombreActual == null || nombreActual.trim().isEmpty() ||
					"jugador".equalsIgnoreCase(nombreActual.trim())) {
				inputNombre.setText("");
			} else {
				inputNombre.setText(nombreActual);
			}
			inputNombre.requestFocus();
			confirmarNombre.setVisible(true);
			volverMenu.setVisible(false);
			verRankings.setVisible(false);
		} else {
			mensajeRanking.setText("Puntaje Final: " + puntaje);
			mensajeRanking.setForeground(Color.RED);
			mensajeRanking.setVisible(true);
			labelNombre.setVisible(false);
			inputNombre.setVisible(false);
			confirmarNombre.setVisible(false);
			volverMenu.setVisible(true);
			verRankings.setVisible(true);
		}
		revalidate();
		repaint();
	}

	private void actualizarPanelesDeRankingControlador() {
		controladorVistas.actualizarRankings();
	}

	private void ocultarTodosLosComponentes() {
		mensajeRanking.setVisible(false);
		labelNombre.setVisible(false);
		inputNombre.setVisible(false);
		confirmarNombre.setVisible(false);
		volverMenu.setVisible(false);
		verRankings.setVisible(false);
	}

	private void confirmarNombreJugador() {
		String nombre = inputNombre.getText().trim();

		if (nombre.isEmpty()) {
			mensajeRanking.setText("Por favor, ingresa un nombre válido");
			mensajeRanking.setForeground(Color.RED);
			return;
		}

		if ("jugador".equalsIgnoreCase(nombre)) {
			mensajeRanking.setText("Ingresa un nombre distinto de 'Jugador'");
			mensajeRanking.setForeground(Color.RED);
			return;
		}

		ModosDeJuego.EntradaRanking entrada = new ModosDeJuego.EntradaRanking(nombre, puntajeFinal);

		if (rankingActual != null) {
			java.util.ArrayList<ModosDeJuego.EntradaRanking> top5 = rankingActual.getTop5();
			top5.add(entrada);
			top5.sort(java.util.Comparator.comparingInt(ModosDeJuego.EntradaRanking::getPuntaje).reversed());
			while (top5.size() > 5) {
				top5.remove(5);
			}
			rankingActual.guardarRankingDirecto(top5);
		}
		labelNombre.setVisible(false);
		inputNombre.setVisible(false);
		confirmarNombre.setVisible(false);
		mensajeRanking.setText("¡Registro guardado! " + nombre + " - " + puntajeFinal + " puntos");
		mensajeRanking.setForeground(Color.RED);
		volverMenu.setVisible(true);
		verRankings.setVisible(true);
	}

	protected void agregarFondoImagen() {
		imagenFondo = new JLabel();
		try {
			java.net.URL imgUrl = getClass().getResource("/Sprites/originales/pantallaGameOver.png");

			if (imgUrl == null) {
				throw new IllegalArgumentException(
						"Recurso no encontrado en classpath: /Sprites/originales/pantallaGameOver.png");
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
}
