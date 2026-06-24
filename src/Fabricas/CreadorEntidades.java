package Fabricas;

import Enemigos.Calabaza;
import Enemigos.DemonioRojo;
import Enemigos.Kamakichi;
import Enemigos.RanaDeFuego;
import Enemigos.TrollAmarillo;
import Logica.Jugador;
import Obstaculos.Escalera;
import Obstaculos.ParedComun;
import Obstaculos.Pinchos;
import Obstaculos.SueloResbaladizo;
import Plataformas.Piso;
import Plataformas.PlataformaNormal;
import Plataformas.PlataformaQuebradiza;
import Powerups.Fruta;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.BolaDeFuego;
import Obstaculos.ParedDestructible;
import Enemigos.Fantasma;

public class CreadorEntidades {
    protected Dominio fabricaSprites;

    public CreadorEntidades(Dominio nuevafabrica) {
        fabricaSprites = nuevafabrica;
    }

    public Jugador getJugador() {
        Sprites spritesBros = fabricaSprites.getSpritesBros();
        Jugador retorno = new Jugador(spritesBros, this);
        return retorno;
    }

    public Piso getPiso(int x, int y) {
        Sprites spritesPiso = fabricaSprites.getSpritePiso();
        Piso retorno = new Piso(spritesPiso, x, y);
        return retorno;
    }

    public DemonioRojo getDemonioRojo(int x, int y) {
        Sprites spritesDemRojo = fabricaSprites.getSpriteDemonioRojo();
        Sprites spritesCongelamiento = fabricaSprites.getSpriteCongelamiento();
        Sprites spritesBolaRodando = fabricaSprites.getSpriteCongelamientoKamakichi(); // MobSnow3/4
        DemonioRojo retorno = new DemonioRojo(spritesDemRojo, x, y, generarPowerUp());
        retorno.setSpritesCongelamiento(spritesCongelamiento);
        retorno.setSpritesBolaRodando(spritesBolaRodando);
        return retorno;
    }

    public RanaDeFuego getRanaDeFuego(int x, int y) {
        Sprites spritesRanaFuego = fabricaSprites.getSpriteRanaDeFuego();
        Sprites spritesCongelamiento = fabricaSprites.getSpriteCongelamiento();
        Sprites spritesBolaRodando = fabricaSprites.getSpriteCongelamientoKamakichi(); // MobSnow3/4
        RanaDeFuego retorno = new RanaDeFuego(spritesRanaFuego, x, y, new CreadorEntidades(fabricaSprites),
                generarPowerUp());
        retorno.setSpritesCongelamiento(spritesCongelamiento);
        retorno.setSpritesBolaRodando(spritesBolaRodando);
        return retorno;
    }

    private PowerUp generarPowerUp() {
        double chance = Math.random();
        if (chance < 0.25) {
            int tipo = (int) (Math.random() * 4); // 0: roja, 1: azul, 2: verde, 3: fruta
            switch (tipo) {
                case 0:
                    return getPocionRoja(0, 0);
                case 1:
                    return getPocionAzul(0, 0);
                case 2:
                    return getPocionVerde(0, 0);
                case 3:
                    return getFruta(0, 0);
                default:
                    return null;
            }
        }
        return null;
    }

    public PlataformaNormal getPlataformaNormal(int x, int y) {
        Sprites spritesPlataforma = fabricaSprites.getSpritePlataformaNormal();
        PlataformaNormal retorno = new PlataformaNormal(spritesPlataforma, x, y);
        return retorno;
    }

    public TrollAmarillo getTrollAmarillo(int x, int y) {
        Sprites spritesTroll = fabricaSprites.getSpriteTrollAmarillo();
        Sprites spritesCongelamiento = fabricaSprites.getSpriteCongelamiento();
        Sprites spritesBolaRodando = fabricaSprites.getSpriteCongelamientoKamakichi(); // MobSnow3/4
        TrollAmarillo retorno = new TrollAmarillo(spritesTroll, x, y, generarPowerUp());
        retorno.setSpritesCongelamiento(spritesCongelamiento);
        retorno.setSpritesBolaRodando(spritesBolaRodando);
        return retorno;
    }

    public ParedComun getParedComun(int x, int y) {
        Sprites spritesPared = fabricaSprites.getSpriteParedNormal();
        ParedComun retorno = new ParedComun(spritesPared, x, y);
        return retorno;
    }

    public ParedDestructible getParedDestructible(int x, int y) {
        Sprites spritesPared = fabricaSprites.getSpriteParedDestructible();
        ParedDestructible retorno = new ParedDestructible(spritesPared, x, y);
        return retorno;
    }

    public Pinchos getPinchos(int x, int y) {
        Sprites spritesPinchos = fabricaSprites.getSpritePinchos();
        Pinchos retorno = new Pinchos(spritesPinchos, x, y);
        return retorno;
    }

    public Calabaza getCalabaza(int x, int y) {
        Sprites calabazaSprites = fabricaSprites.getSpriteCalabaza();
        Calabaza retorno = new Calabaza(calabazaSprites, x, y, null, this);
        return retorno;
    }

    public Fantasma getFantasma(int x, int y) {
        Sprites spritesFantasma = fabricaSprites.getSpriteFantasma();
        Fantasma retorno = new Fantasma(spritesFantasma, x, y, null, this);
        return retorno;
    }

    public Kamakichi getKamakichi(int x, int y) {
        Sprites kamakichiSprites = fabricaSprites.getSpriteKamakichi();
        Sprites spritesCongelamiento = fabricaSprites.getSpriteCongelamientoKamakichi();
        Sprites spritesBolaRodando = fabricaSprites.getSpriteCongelamientoKamakichi(); // MobSnow3/4
        Kamakichi retorno = new Kamakichi(kamakichiSprites, x, y, this, null);
        retorno.setSpritesCongelamiento(spritesCongelamiento);
        retorno.setSpritesBolaRodando(spritesBolaRodando);
        return retorno;
    }

    public PlataformaQuebradiza getPlataformaQuebradiza(int x, int y) {
        Sprites spritesPQuebradiza = fabricaSprites.getSpritePlataformaQuebradiza();
        PlataformaQuebradiza retorno = new PlataformaQuebradiza(spritesPQuebradiza, x, y);
        return retorno;
    }

    public Plataformas.PlataformaMovil getPlataformaMovil(int x, int y, boolean iniciaDerecha) {
        Sprites spritesPMovil = fabricaSprites.getSpritePlataformaMovil();
        Plataformas.PlataformaMovil retorno = new Plataformas.PlataformaMovil(spritesPMovil, x, y, iniciaDerecha);
        return retorno;
    }

    public BolaDeNieve getBolaDeNieve(int x, int y) {
        Sprites spritesBolaNieve = fabricaSprites.getSpriteBolaDeNieve();
        BolaDeNieve retorno = new BolaDeNieve(spritesBolaNieve, x, y);
        return retorno;
    }

    public Fruta getFruta(int x, int y) {
        Sprites frutaSprites = fabricaSprites.getSpriteFruta();
        Fruta retorno = new Fruta(frutaSprites, x, y);
        return retorno;
    }

    public Escalera getEscalera(int x, int y) {
        Sprites spritesEscalera = fabricaSprites.getSpriteEscalera();
        Escalera retorno = new Escalera(spritesEscalera, x, y);
        return retorno;
    }

    public Powerups.PocionRoja getPocionRoja(int x, int y) {
        Sprites spritesPocionRoja = fabricaSprites.getSpritePocionRoja();
        Powerups.PocionRoja retorno = new Powerups.PocionRoja(spritesPocionRoja, x, y);
        return retorno;
    }

    public Powerups.PocionAzul getPocionAzul(int x, int y) {
        Sprites spritesPocionAzul = fabricaSprites.getSpritePocionAzul();
        Powerups.PocionAzul retorno = new Powerups.PocionAzul(spritesPocionAzul, x, y);
        return retorno;
    }

    public Powerups.PocionVerde getPocionVerde(int x, int y) {
        Sprites spritesPocionVerde = fabricaSprites.getSpritePocionVerde();
        Powerups.PocionVerde retorno = new Powerups.PocionVerde(spritesPocionVerde, x, y);
        return retorno;
    }

    public Powerups.VidaExtra getVidaExtra(int x, int y) {
        Sprites spritesVidaExtra = fabricaSprites.getSpriteVidaExtra();
        Powerups.VidaExtra retorno = new Powerups.VidaExtra(spritesVidaExtra, x, y);
        return retorno;
    }

    public BolaDeFuego crearBolaDeFuego(int x, int y, boolean direccionDerecha) {
        Sprites spritesBolaDeFuego = fabricaSprites.getSpriteBolaDeFuego();
        BolaDeFuego retorno = new BolaDeFuego(spritesBolaDeFuego, x, y, direccionDerecha);
        return retorno;
    }

    public SueloResbaladizo getSueloResbaladizo(int x, int y) {
        Sprites spritesSueloResbaladizo = fabricaSprites.getSpriteSueloResbaladizo();
        SueloResbaladizo retorno = new SueloResbaladizo(spritesSueloResbaladizo, x, y);
        return retorno;
    }

    public Enemigos.Bomba crearBomba(int x, int y, float velX, float velY) {
        Sprites spritesBomba = fabricaSprites.getSpriteBombaKamakichi();
        Enemigos.Bomba retorno = new Enemigos.Bomba(spritesBomba, x, y, this, velX, velY);
        return retorno;
    }

    public Enemigos.Moghera getMoghera(int x, int y) {
        Sprites spritesMoghera = fabricaSprites.getSpriteMoghera();
        Sprites spritesCongelamiento = fabricaSprites.getSpriteCongelamientoKamakichi();
        Sprites spritesBolaRodando = fabricaSprites.getSpriteCongelamientoKamakichi(); // MobSnow3/4
        Enemigos.Moghera retorno = new Enemigos.Moghera(spritesMoghera, x, y, this, generarPowerUp());
        retorno.setSpritesCongelamiento(spritesCongelamiento);
        retorno.setSpritesBolaRodando(spritesBolaRodando);
        return retorno;
    }

    public Proyectiles.ProyectilMoghera crearProyectilMoghera(int x, int y, boolean direccionDerecha) {
        Sprites spritesProyectilMoghera = fabricaSprites.getSpriteProyectilMoghera();
        Proyectiles.ProyectilMoghera retorno = new Proyectiles.ProyectilMoghera(spritesProyectilMoghera, x, y,
                direccionDerecha);
        return retorno;
    }
}
