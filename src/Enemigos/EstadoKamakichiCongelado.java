package Enemigos;

public class EstadoKamakichiCongelado extends EstadoCongeladoEstatico {

    public EstadoKamakichiCongelado(Kamakichi kamakichi) {
        super(kamakichi);
    }

    @Override
    public void actualizarSprite() {
        if (enemigo.getSprites() != null) {
            enemigo.getSprites().set_estado_actual(1);
        }
        
        
        if (enemigo.spritesCongelamiento != null) {
            enemigo.spritesCongelamiento.set_estado_actual(0); 
        }
        
        enemigo.notificar();
    }
}
