package Grafica;

public interface Observer {
  public void actualizar();

  public default void eliminarDePantalla() {
    // No hace nada por defecto
  }
}
