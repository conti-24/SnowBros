package Grafica;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import Logica.EntidadLogica;

public class ObserverGrafico extends JLabel implements Observer {
    protected EntidadLogica observado;
    private ImageIcon iconoActual;

    public ObserverGrafico(EntidadLogica e) {
        super();
        this.observado = e;
        actualizar();
    }

    @Override
    public void actualizar() {
        actualizarImagen();
        actualizarPosTamano();
    }

    public void actualizarImagen() {
        String rutaImagen = observado.getSprites().get_ruta_imagen_actual();
        if (rutaImagen != null) {
            try {
                ImageIcon iconoOriginal = new ImageIcon(getClass().getClassLoader().getResource(rutaImagen));
                int nuevoAncho = observado.getAncho();
                int nuevoAlto = observado.getAlto();
                Image imagenOriginal = iconoOriginal.getImage();
                Image imagenReescalada = imagenOriginal.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_FAST);
                this.iconoActual = new ImageIcon(imagenReescalada);

                setIcon(this.iconoActual);

            } catch (Exception e) {
                setIcon(null);
                setText("IMG?");
                setForeground(Color.WHITE);
                System.err.println("No se pudo cargar o reescalar la imagen: " + rutaImagen);
            }
        }
    }

    protected void actualizarPosTamano() {
        int x = AdaptadorPosicionPixel.transformar_x(observado.getPosX());
        int yLogico = observado.getPosY() + observado.getAlto();
        int y = AdaptadorPosicionPixel.transformar_y(yLogico);
        int ancho = (iconoActual != null) ? iconoActual.getIconWidth() : observado.getAlto();
        int alto = (iconoActual != null) ? iconoActual.getIconHeight() : observado.getAlto();

        setBounds(x, y, ancho, alto);
    }
    public void eliminarDePantalla() {
        setVisible(false);
        if (getParent() != null) {
            getParent().remove(this);
        }
    }
}