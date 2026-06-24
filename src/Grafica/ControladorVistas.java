package Grafica;

public interface ControladorVistas {
  public void accionarInicioJuego();

  public void accionarPantallaRanking();

  public void mostrarPantallaInicial();

  public void seleccionarModo(ModosDeJuego.ModoTipo modo);

  public void seleccionarDominio(Fabricas.Dominio dominio);

  public void actualizarRankings();
}
