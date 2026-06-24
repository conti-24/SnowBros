package Enemigos;

public class EstadoMogheraCongelado extends EstadoCongeladoEstatico {

    public EstadoMogheraCongelado(Moghera moghera) {
        super(moghera);
    }

    @Override
    public void actualizarSprite() {
        if (enemigo.getSprites() != null) {
            enemigo.getSprites().set_estado_actual(2);
        }
        
        if (enemigo.spritesCongelamiento != null) {
            enemigo.spritesCongelamiento.set_estado_actual(0); 
        }
        
        enemigo.notificar();
    }
}