package Grafica;

import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class PanelVista extends JPanel {
  protected JLabel fondoImagen;
  protected ControladorVistas controladorVistas;

  public void agregarControlador(ControladorVistas c) {
    controladorVistas = c;
  }
}
