package Logica;

public class Efectos {
    protected boolean efectoVelocidad;
    protected boolean efectoPotencia;
    protected int velExtra;
    protected int potExtra;
    protected long tiempoExpiracionVelocidad;
    protected long tiempoExpiracionPotencia;
    protected Jugador jugador;

    public Efectos(Jugador j) {
        efectoPotencia = false;
        efectoVelocidad = false;
        velExtra = 0;
        potExtra = 0;
        tiempoExpiracionVelocidad = 0;
        tiempoExpiracionPotencia = 0;
        jugador = j;
    }

    public void actualizarEfectos() {
        long tiempoActual = System.currentTimeMillis();

        // Verificar expiración de velocidad
        if (efectoVelocidad && tiempoActual > tiempoExpiracionVelocidad) {
            velExtra = 0;
            efectoVelocidad = false;
            tiempoExpiracionVelocidad = 0;
        }

        // Verificar expiración de potencia
        if (efectoPotencia && tiempoActual > tiempoExpiracionPotencia) {
            potExtra = 0;
            efectoPotencia = false;
            tiempoExpiracionPotencia = 0;
        }
    }

    public boolean hayVelocidad() {
        return efectoVelocidad && System.currentTimeMillis() <= tiempoExpiracionVelocidad;
    }

    public boolean hayPotencia() {
        return efectoPotencia && System.currentTimeMillis() <= tiempoExpiracionPotencia;
    }

    public int getVelExtra() {
        return velExtra;
    }

    public int getPotExtra() {
        return potExtra;
    }

    public void ajustarVelExtra(int n) {
        velExtra = velExtra + n;
        if (velExtra > 0) {
            efectoVelocidad = true;
            tiempoExpiracionVelocidad = System.currentTimeMillis() + 10000; // 10 segundos
        } else {
            efectoVelocidad = false;
            tiempoExpiracionVelocidad = 0;
        }
    }

    public void ajustarPotExtra(int n) {
        potExtra = potExtra + n;
        if (potExtra > 0) {
            efectoPotencia = true;
            tiempoExpiracionPotencia = System.currentTimeMillis() + 10000; // 10 segundos
        } else {
            efectoPotencia = false;
            tiempoExpiracionPotencia = 0;
        }
    }
}
