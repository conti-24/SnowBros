package Grafica;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import Logica.ConstantesDireccion;
import Logica.ControladorJuego;
import Logica.EntidadLogica;
import ModosDeJuego.Clasico;
import ModosDeJuego.Contrarreloj;
import ModosDeJuego.Supervivencia;
import ModosDeJuego.ModoTipo;
import Fabricas.Dominio;
import Fabricas.DominioClasico;

public class GUI implements ControladorVistas, ControladorGrafica {
  protected PanelMenu panelMenu;
  protected PanelGameOver panelGameOver;
  protected PanelPartida panelPartida;
  protected PanelRanking panelRanking;
  protected JFrame ventana;
  protected ControladorJuego controlador;
  private ModoTipo modoSeleccionado = ModoTipo.CLASICO;
  private Dominio dominioSeleccionado = new DominioClasico();

  public GUI() {
    panelPartida = new PanelPartida(this);
    panelMenu = new PanelMenu(this);
    panelGameOver = new PanelGameOver(this);
    Clasico modoClasico = new Clasico(null, null);
    Contrarreloj modoContrarreloj = new Contrarreloj(null, null);
    Supervivencia modoSupervivencia = new Supervivencia(null, null);
    panelRanking = new PanelRanking(this, modoClasico, modoSupervivencia, modoContrarreloj);

    configurar();
    configurarKeyBindings();
  }

  public void configurar() {
    ventana = new JFrame("Snow Bros");
    ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ventana.setSize(new Dimension(ConstantesVistas.VENTANA_ANCHO, ConstantesVistas.PANEL_ALTO));
    ventana.setResizable(false);
    ventana.setLocationRelativeTo(null);
    ventana.setVisible(true);
    ventana.setFocusable(true);

    System.setProperty("sun.java2d.opengl", "true");
  }

  protected void configurarKeyBindings() {
    InputMap inputMap = panelPartida.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = panelPartida.getActionMap();

    inputMap.put(KeyStroke.getKeyStroke("pressed A"), "izquierdaPressed");
    inputMap.put(KeyStroke.getKeyStroke("released A"), "izquierdaReleased");

    actionMap.put("izquierdaPressed", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (controlador != null) {
          controlador.teclaApretada(ConstantesDireccion.IZQUIERDA);
        }
      }
    });

    actionMap.put("izquierdaReleased", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (controlador != null) {
          controlador.teclaSoltada(ConstantesDireccion.IZQUIERDA);
        }
      }
    });

    inputMap.put(KeyStroke.getKeyStroke("pressed D"), "derechaPressed");
    inputMap.put(KeyStroke.getKeyStroke("released D"), "derechaReleased");

    actionMap.put("derechaPressed", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (controlador != null) {
          controlador.teclaApretada(ConstantesDireccion.DERECHA);
        }
      }
    });

    actionMap.put("derechaReleased", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (controlador != null) {
          controlador.teclaSoltada(ConstantesDireccion.DERECHA);
        }
      }
    });

    inputMap.put(KeyStroke.getKeyStroke("pressed W"), "saltoPressed");
    inputMap.put(KeyStroke.getKeyStroke("released W"), "saltoReleased");

    actionMap.put("saltoPressed", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (controlador != null) {
          controlador.teclaApretada(ConstantesDireccion.ARRIBA);
        }
      }
    });

    actionMap.put("saltoReleased", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (controlador != null) {
          controlador.teclaSoltada(ConstantesDireccion.ARRIBA);
        }
      }
    });

    inputMap.put(KeyStroke.getKeyStroke("pressed S"), "abajoPressed");
    inputMap.put(KeyStroke.getKeyStroke("released S"), "abajoReleased");

    actionMap.put("abajoPressed", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (controlador != null) {
          controlador.teclaApretada(ConstantesDireccion.ABAJO);
        }
      }
    });

    actionMap.put("abajoReleased", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (controlador != null) {
          controlador.teclaSoltada(ConstantesDireccion.ABAJO);
        }
      }
    });

    inputMap.put(KeyStroke.getKeyStroke("pressed SPACE"), "espacioPressed");

    actionMap.put("espacioPressed", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (controlador != null) {
          controlador.teclaApretada(ConstantesDireccion.ESPACIO);
        }
      }
    });
  }

  public void refrescarVista() {
    ventana.revalidate();
    ventana.repaint();
  }

  public void mostrarPantallaPartida() {
    ventana.setContentPane(panelPartida);
    refrescarVista();
  }

  public void mostrarPantallaInicial() {
    ventana.setContentPane(panelMenu);
    refrescarVista();
  }

  @Override
  public void mostrarPantallaGameOver() {
    ventana.setContentPane(panelGameOver);
    refrescarVista();
  }

  public void mostrarGameOverConRanking(ModosDeJuego.Ranking ranking, int puntaje, String nombreActual) {
    panelGameOver.configurarConRanking(ranking, puntaje, nombreActual);
    ventana.setContentPane(panelGameOver);
    refrescarVista();
  }

  @Override
  public void mostrarPantallaRanking() {
    ventana.setContentPane(panelRanking);
    refrescarVista();
  }

  @Override
  public void registrarControladorJuego(ControladorJuego c) {
    controlador = c;
  }

  public Observer registrarEntidad(EntidadLogica e) {
    Observer o = panelPartida.incorporarEntidad(e);
    return o;
  }

  @Override
  public void registrarInfoJugador(Logica.InfoJugador info) {
    panelPartida.registrarInfoJugador(info);
  }

  @Override
  public void limpiarLienzo() {
    if (panelPartida != null) {
      panelPartida.limpiarLienzo();
    }
  }

  @Override
  public void mostrarOverlayNivel(int nivel, int piso, int duracionMs) {
    if (panelPartida != null) {
      panelPartida.mostrarOverlayNivel(nivel, piso, duracionMs);
    }
  }

  @Override
  public void actualizarNivel(int nivel, int piso) {
    if (panelPartida != null) {
      panelPartida.actualizarNivel(nivel, piso);
    }
  }

  @Override
  public void actualizarTiempoRestante(int segundos) {
    if (panelPartida != null) {
      panelPartida.actualizarTiempoRestante(segundos);
    }
  }

  @Override
  public void accionarInicioJuego() {
    controlador.seleccionarDominio(dominioSeleccionado);
    controlador.seleccionarModo(modoSeleccionado);
    controlador.iniciar();
  }

  @Override
  public void accionarPantallaRanking() {
    mostrarPantallaRanking();
  }

  @Override
  public void seleccionarModo(ModoTipo modo) {
    this.modoSeleccionado = modo;
  }

  @Override
  public void seleccionarDominio(Dominio dominio) {
    this.dominioSeleccionado = dominio;
  }

  @Override
  public void actualizarRankings() {
    panelRanking.actualizarPanelesDeRanking();
  }
}