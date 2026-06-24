package Grafica;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import Enemigos.Enemigo;

public class ObserverGraficoEnemigo extends ObserverGrafico {
    private Enemigo enemigo;
    private ImageIcon iconoCongelamiento;
    private ImageIcon iconoBolaRodando;

    public ObserverGraficoEnemigo(Enemigo enemigo) {
        super(enemigo);
        this.enemigo = enemigo;
    }

    @Override
    public void actualizar() {
        super.actualizar();
        actualizarImagenCongelamiento();
        actualizarImagenBolaRodando();
    }

    private void actualizarImagenCongelamiento() {
        if (enemigo != null && enemigo.getSpritesCongelamiento() != null) {
            String rutaImagen = enemigo.getSpritesCongelamiento().get_ruta_imagen_actual();
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                try {
                    ImageIcon iconoOriginal = new ImageIcon(getClass().getClassLoader().getResource(rutaImagen));
                    int nuevoAncho = enemigo.getAncho();
                    int nuevoAlto = enemigo.getAlto();
                    Image imagenOriginal = iconoOriginal.getImage();
                    Image imagenReescalada = imagenOriginal.getScaledInstance(nuevoAncho, nuevoAlto,
                            Image.SCALE_FAST);
                    this.iconoCongelamiento = new ImageIcon(imagenReescalada);
                } catch (Exception e) {
                    this.iconoCongelamiento = null;
                }
            } else {
                this.iconoCongelamiento = null;
            }
        }
        repaint();
    }

    private void actualizarImagenBolaRodando() {
        if (enemigo != null && enemigo.getSpritesBolaRodando() != null) {
            String rutaImagen = enemigo.getSpritesBolaRodando().get_ruta_imagen_actual();
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                try {
                    ImageIcon iconoOriginal = new ImageIcon(getClass().getClassLoader().getResource(rutaImagen));
                    int nuevoAncho = enemigo.getAncho();
                    int nuevoAlto = enemigo.getAlto();
                    Image imagenOriginal = iconoOriginal.getImage();
                    Image imagenReescalada = imagenOriginal.getScaledInstance(nuevoAncho, nuevoAlto,
                            Image.SCALE_FAST);
                    this.iconoBolaRodando = new ImageIcon(imagenReescalada);
                } catch (Exception e) {
                    this.iconoBolaRodando = null;
                }
            } else {
                this.iconoBolaRodando = null;
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (enemigo != null && enemigo.esBolaDeNieve() &&
                iconoBolaRodando != null && iconoBolaRodando.getImage() != null) {
            g.drawImage(iconoBolaRodando.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        else if (enemigo != null && enemigo.estaCongelado() &&
                iconoCongelamiento != null && iconoCongelamiento.getImage() != null) {
            g.drawImage(iconoCongelamiento.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
}
