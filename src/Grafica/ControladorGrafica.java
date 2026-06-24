package Grafica;

import Logica.ControladorJuego;
import Logica.EntidadLogica;
import Logica.InfoJugador;

public interface ControladorGrafica {

  public void mostrarPantallaInicial();

  public void mostrarPantallaGameOver();

  public void mostrarPantallaRanking();

  public void mostrarPantallaPartida();

  public void registrarControladorJuego(ControladorJuego c);

  public Observer registrarEntidad(EntidadLogica e);

  public void registrarInfoJugador(InfoJugador info);

  public void refrescarVista();

  public void limpiarLienzo();

  public void mostrarOverlayNivel(int nivel, int piso, int duracionMs);

  public void actualizarNivel(int nivel, int piso);

  public void actualizarTiempoRestante(int segundos);

  public void mostrarGameOverConRanking(ModosDeJuego.Ranking ranking, int puntaje, String nombreActual);

}
