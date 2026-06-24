package Logica;

import Fabricas.Dominio;
import ModosDeJuego.ModoDeJuego;
import ModosDeJuego.ModoTipo;

public interface ControladorJuego {

    public void iniciar();

    public void seleccionarModoDeJuego(ModoDeJuego m);

    public void seleccionarModo(ModoTipo modo);

    public void seleccionarDominio(Dominio d);

    public void teclaApretada(int p);

    public void teclaSoltada(int p);

}