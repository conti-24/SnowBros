package Launcher;

import java.awt.EventQueue;
import Grafica.ControladorGrafica;
import Grafica.GUI;
import Logica.Juego;

public class Launcher {
    public static void main(String [] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ControladorGrafica controlador_grafica = new GUI();
                    Juego juego = new Juego(controlador_grafica);
                    controlador_grafica.registrarControladorJuego(juego);
                    juego.configurar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}