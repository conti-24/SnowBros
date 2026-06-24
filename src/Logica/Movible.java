package Logica;
import java.awt.Rectangle;

import Visitor.Colisionable;
import Visitor.Colisionador;

public interface Movible extends Colisionable, Colisionador{
    public void mover();
    public Rectangle getRectangulo();
}
