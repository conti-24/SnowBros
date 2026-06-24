package Logica;

/**
 * Clase centralizada para todas las constantes de física del juego.
 * Permite ajustar velocidades y física desde un solo lugar.
 */
public class ConfiguracionFisica {

    // ========== CONFIGURACIÓN GLOBAL ==========
    /**
     * Factor de ajuste global para todas las velocidades.
     * Cambiar este valor escala todas las velocidades del juego.
     * 1.0 = velocidades para 120 FPS
     * 2.0 = velocidades para 60 FPS
     */
    public static final float FACTOR_VELOCIDAD = 2.0f;

    // ========== FÍSICA GENERAL ==========
    public static final float GRAVEDAD = -0.6f * FACTOR_VELOCIDAD;

    // ========== ENEMIGOS ==========
    public static final float VELOCIDAD_ENEMIGO_BASE = 1f * FACTOR_VELOCIDAD;
    public static final float FUERZA_SALTO_ENEMIGO = 10f * FACTOR_VELOCIDAD;
    public static final float VELOCIDAD_BOLA_NIEVE = 8f * FACTOR_VELOCIDAD;
    public static final float FRICCION_BOLA = 0.98f; 

    // ========== ENEMIGOS ESPECÍFICOS ==========
    public static final float VELOCIDAD_TROLL_AMARILLO = 2.5f * FACTOR_VELOCIDAD;
    public static final float VELOCIDAD_RANA_DE_FUEGO = 1.5f * FACTOR_VELOCIDAD;

    // ========== JUGADOR ==========
    public static final float ACELERACION_JUGADOR = 0.8f * FACTOR_VELOCIDAD;
    public static final float VELOCIDAD_MAX_JUGADOR = 6f * FACTOR_VELOCIDAD;
    public static final float GRAVEDAD_JUGADOR = -0.6f * FACTOR_VELOCIDAD;
    public static final float FUERZA_SALTO_JUGADOR = 10f * FACTOR_VELOCIDAD;
    public static final float VELOCIDAD_CAIDA_MAX_JUGADOR = -15f * FACTOR_VELOCIDAD;
    public static final float FRICCION_JUGADOR = 0.85f; 

    // ========== LÍMITES DEL MAPA ==========
    public static final int LIMITE_PISO = 80;
    public static final int LIMITE_TECHO = 760;
    public static final int LIMITE_PARED1 = 0;
    public static final int LIMITE_PARED2 = 1200 - 60 - 40;

    // Constructor privado para evitar instanciación
    private ConfiguracionFisica() {
        throw new AssertionError("No se debe instanciar ConfiguracionFisica");
    }
}
