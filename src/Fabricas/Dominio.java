package Fabricas;

import java.util.HashMap;
import java.util.Map;

import Estados.EstadoVisualBros;

public class Dominio {
	protected String ruta_a_carpeta;

	protected Dominio(String ruta_a_carpeta) {
		this.ruta_a_carpeta = ruta_a_carpeta;
	}

	private Map<Integer, String> generar_mapeo_estados(String[] nombres_imagenes) {
		Map<Integer, String> mapeo_estado_imagen = new HashMap<>();
		for (int i = 0; i < nombres_imagenes.length; i++) {
			String ruta_completa = this.ruta_a_carpeta + "/" + nombres_imagenes[i];
			mapeo_estado_imagen.put(i, ruta_completa);
		}
		return mapeo_estado_imagen;
	}

	public Sprites getSpritesBros() {
		Map<Integer, String> mapa = new HashMap<>();
		String r = this.ruta_a_carpeta; 
		mapa.put(EstadoVisualBros.DISPARANDO_DERECHA, r + "/BrosDisparaDrc.png");
		mapa.put(EstadoVisualBros.DISPARANDO_IZQUIERDA, r + "/BrosDisparaIzq.png");
		mapa.put(EstadoVisualBros.CAMINANDO_DERECHA, r + "/BrosDrc.png");
		mapa.put(EstadoVisualBros.CAMINANDO_DERECHA_1, r + "/BrosDrc1.png");
		mapa.put(EstadoVisualBros.CAMINANDO_DERECHA_2, r + "/BrosDrc2.png");
		mapa.put(EstadoVisualBros.EMPUJANDO_DERECHA, r + "/BrosEmpujaDrc.png");
		mapa.put(EstadoVisualBros.EMPUJANDO_IZQUIERDA, r + "/BrosEmpujaIzq.png");
		mapa.put(EstadoVisualBros.CAMINANDO_IZQUIERDA, r + "/BrosIzq.png");
		mapa.put(EstadoVisualBros.CAMINANDO_IZQUIERDA_1, r + "/BrosIzq1.png");
		mapa.put(EstadoVisualBros.CAMINANDO_IZQUIERDA_2, r + "/BrosIzq2.png");
		mapa.put(EstadoVisualBros.MURIENDO, r + "/BrosMuere.png");
		mapa.put(EstadoVisualBros.SALTANDO_DERECHA, r + "/BrosSaltaDrc.png");
		mapa.put(EstadoVisualBros.SALTANDO_IZQUIERDA, r + "/BrosSaltaIzq.png");
		mapa.put(EstadoVisualBros.SUBIENDO_ESCALERA, r + "/BrosMuere.png");
		mapa.put(EstadoVisualBros.BAJANDO_ESCALERA, r + "/BrosMuere.png");

		return new Sprites(mapa, EstadoVisualBros.CAMINANDO_DERECHA);
	}

	// =================================================================
	// == ENEMIGOS ==
	// =================================================================

	public Sprites getSpriteMoghera() {
		String[] imagenes = { "mogheraDerecha.png", "mogheraIzq.png", "mogheraMuere.png", "mogheraSaltandoDer.png",
				"mogheraSaltandoIzq.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteKamakichi() {
		String[] imagenes = { "kamakichiDisparo.png", "KamakichiNormal.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteCongelamientoKamakichi() {
		String[] imagenes = { "MobSnow3.png", "MobSnow4.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteCongelamiento() {
		String[] imagenes = { "MobSnow1.png", "MobSnow2.png", "MobSnow3.png", "MobSnow4.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteRanaDeFuego() {
		String[] imagenes = { "ranaDeFuegoDerecha1.png", "ranaDeFuegoDerecha2.png","ranaDeFuegoIzquierda1.png","ranaDeFuegoIzquierda2.png" ,"ranaDeFuegoDisparaDerecha.png",
				"ranaDeFuegoDisparaIzquierda.png", "ranaDeFuegoMuere.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteTrollAmarillo() {
		String[] imagenes = { "trollAmarilloDerecha.png", "trollAmarilloMueveDerecha.png",
				"trollAmarilloIzquierda.png",
				"trollAmarilloMueveIzquierda.png", "trollAmarilloMuere.png",
				"trollAmarilloSaltaDerecha.png", "trollAmarilloSaltaIzquierda.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteDemonioRojo() {
		String[] imagenes = { "demonioRojoDerecha.png", "demonioRojoDerecha2.png", "demonioRojoIzquierda.png",
				"demonioRojoIzquierda2.png", "demonioRojoMuere.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteCalabaza() {
		String[] imagenes = { "calabazaAturdido1.png", "calabazaDerecha1.png", "calabazaDerecha2.png",
				"calabazaIzquierda1.png", "calabazaIzquierda2.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteFantasma() {
		String[] imagenes = { "fantasmaDer.png", "fantasmaDer2.png", "fantasmaIzq.png", "fantasmaIzq2.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteBombaKamakichi() {
		String[] imagenes = { "bombaDer1.png", "bombaDer2.png", "bombaIzq1.png", "bombaIzq2.png", "bombaExpl1.png",
				"bombaExpl2.png", "bombaExplotandoDer.png", "bombaExplotandoIzq.png", "bombaVolando.png",
				"bombaMuereDer.png", "bombaMuereIzq.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	// =================================================================
	// == PLATAFORMAS Y ESCENARIO ==
	// =================================================================

	public Sprites getSpritePlataformaNormal() {
		String[] imagenes = { "plataformaComun.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpritePlataformaQuebradiza() {
		// Ejemplo de un sprite con dos estados: 0=sana, 1=rota
		String[] imagenes = { "plataformaQuebradiza.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpritePiso() {
		String[] imagenes = { "Piso.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpritePlataformaMovil() {
		String[] imagenes = { "plataformaMovil.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteEscalera() {
		String[] imagenes = { "escalera.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteParedNormal() {
		String[] imagenes = { "paredComun.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteParedDestructible() {
		String[] imagenes = { "paredDestructible.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteSueloResbaladizo() {
		String[] imagenes = { "sueloResbaladizo.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpritePinchos() {
		String[] imagenes = { "pinchos.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	// =================================================================
	// == ITEMS Y POWER-UPS ==
	// =================================================================

	public Sprites getSpriteVidaExtra() {
		String[] imagenes = { "vidaExtra.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpritePocionRoja() {
		String[] imagenes = { "pocionRoja.png" }; // Aumenta velocidad
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpritePocionAzul() {
		String[] imagenes = { "pocionAzul.png" }; // Aumenta poder
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpritePocionVerde() {
		String[] imagenes = { "pocionVerde.png" }; // Aumenta rango/invencibilidad
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteFruta() {
		String[] imagenes = { "fruta.png" }; // Puntos
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	// =================================================================
	// == PROYECTILES ==
	// =================================================================

	public Sprites getSpriteBolaDeNieve() {
		String[] imagenes = { "snowDrc.png", "snowIzq.png", "snowPotDrc.png", "snowPotIzq.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteProyectilMoghera() {
		String[] imagenes = { "fuegoMoghera.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteProyectilRanaDeFuego() {
		String[] imagenes = { "proyectil_rana_de_fuego.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

	public Sprites getSpriteBolaDeFuego() {
		String[] imagenes = { "disparoDrcRfuego.png", "disparoIzqRFuego.png" };
		return new Sprites(generar_mapeo_estados(imagenes), 0);
	}

}
