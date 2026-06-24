package ParserArchivo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import Enemigos.DemonioRojo;
import Enemigos.Kamakichi;
import Enemigos.Moghera;
import Enemigos.RanaDeFuego;
import Enemigos.TrollAmarillo;
import Fabricas.CreadorEntidades;
import Logica.Jugador;
import Logica.Nivel;
import Obstaculos.Escalera;
import Obstaculos.ParedComun;
import Obstaculos.ParedDestructible;
import Plataformas.Piso;
import Plataformas.PlataformaNormal;
import Plataformas.PlataformaQuebradiza;
import Powerups.Fruta;
import Powerups.PocionAzul;
import Powerups.PocionRoja;
import Powerups.PocionVerde;
import Powerups.VidaExtra;
import Obstaculos.Pinchos;
import Obstaculos.SueloResbaladizo;

public class LectorNiveles {

    private final CreadorEntidades creadorEntidades;

    public LectorNiveles(CreadorEntidades creador) {
        this.creadorEntidades = Objects.requireNonNull(creador, "El CreadorEntidades no puede ser nulo.");
    }

    public boolean cargarNivel(String rutaArchivo, Nivel nivel) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String lineaLimpia = linea.trim();
                if (lineaLimpia.startsWith("//") || lineaLimpia.isEmpty()) {
                    continue;
                }
                String[] partes = lineaLimpia.split("\\s+");
                if (partes.length != 3) {
                    System.err.println("ADVERTENCIA: Línea mal formada, se ignora -> '" + linea + "'");
                    continue;
                }

                try {
                    String caracter = partes[0];
                    // AQUI VAN TODAS LAS ENTIDADES
                    int x = Integer.parseInt(partes[1]);
                    int y = Integer.parseInt(partes[2]);
                    if ("_".equals(caracter)) {
                        Piso nuevoPiso = creadorEntidades.getPiso(x, y);
                        nuevoPiso.setPosX(x);
                        nuevoPiso.setPosY(y);
                        nivel.agregarEntidad(nuevoPiso);
                        nivel.agregarSuperficieSolida(nuevoPiso);
                    }
                    if ("=".equals(caracter)) {
                        PlataformaNormal p = creadorEntidades.getPlataformaNormal(x, y);
                        p.setPosX(x);
                        p.setPosY(y);
                        nivel.agregarEntidad(p);
                        nivel.agregarSuperficieSolida(p);
                    }

                    if (">".equals(caracter) || "<".equals(caracter)) {
                        boolean iniciaDerecha = ">".equals(caracter);
                        Plataformas.PlataformaMovil pm = creadorEntidades.getPlataformaMovil(x, y, iniciaDerecha);
                        pm.setPosX(x);
                        pm.setPosY(y);
                        int izq = Math.max(50, x - 200);
                        int der = Math.min(1100, x + 200);
                        pm.setLimites(izq, der);
                        nivel.agregarEntidad(pm);
                        nivel.agregarMovible(pm);
                        nivel.agregarEntidad(pm.getCajaSuperior());
                        nivel.agregarSuperficieSolida(pm);
                    }

                    if ("v".equals(caracter)) {
                        Plataformas.PlataformaMovil pm = creadorEntidades.getPlataformaMovil(x, y, true);
                        pm.setPosX(x);
                        pm.setPosY(y);
                        pm.setVelocidadY(1.0f);
                        pm.setVelocidad(0f); 
                        int arriba = Math.max(80, y - 150);
                        int abajo = Math.min(830, y + 150);
                        pm.setLimitesVerticales(arriba, abajo);
                        pm.setInicioDireccion(true);
                        nivel.agregarEntidad(pm);
                        nivel.agregarMovible(pm);
                        nivel.agregarEntidad(pm.getCajaSuperior());
                        nivel.agregarSuperficieSolida(pm);
                    }

                    if ("D".equals(caracter)) {
                        DemonioRojo dr = creadorEntidades.getDemonioRojo(x, y);
                        dr.setPosX(x);
                        dr.setPosY(y);
                        nivel.agregarEntidad(dr);
                        nivel.agregarEnemigo(dr);
                        nivel.agregarMovible(dr);
                    }
                    if ("R".equals(caracter)) {
                        RanaDeFuego rf = creadorEntidades.getRanaDeFuego(x, y);
                        rf.setPosX(x);
                        rf.setPosY(y);
                        nivel.agregarEntidad(rf);
                        nivel.agregarEnemigo(rf);
                        nivel.agregarMovible(rf);
                    }
                    if ("J".equals(caracter)) {
                        Jugador rf = creadorEntidades.getJugador();
                        rf.setPosX(x);
                        rf.setPosY(y);
                        nivel.agregarEntidad(rf);
                        nivel.agregarMovible(rf);
                    }
                    if ("T".equals(caracter)) {
                        TrollAmarillo ta = creadorEntidades.getTrollAmarillo(x, y);
                        ta.setPosX(x);
                        ta.setPosY(y);
                        nivel.agregarEntidad(ta);
                        nivel.agregarMovible(ta);
                        nivel.agregarEnemigo(ta);
                    }
                    if ("|".equals(caracter)) {
                        ParedComun pcomun = creadorEntidades.getParedComun(x, y);
                        pcomun.setPosX(x);
                        pcomun.setPosY(y);
                        nivel.agregarEntidad(pcomun);
                    }
                    if ("#".equals(caracter)) {
                        ParedDestructible pdestructible = creadorEntidades.getParedDestructible(x, y);
                        pdestructible.setPosX(x);
                        pdestructible.setPosY(y);
                        nivel.agregarEntidad(pdestructible);
                    }
                    if ("K".equals(caracter)) {
                        Kamakichi kamakichi = creadorEntidades.getKamakichi(x, y);
                        kamakichi.setPosX(x);
                        kamakichi.setPosY(y);
                        nivel.agregarEnemigo(kamakichi);
                        nivel.agregarEntidad(kamakichi);
                        nivel.agregarMovible(kamakichi);
                    }
                    if ("C".equals(caracter)) {
                        Enemigos.Calabaza calabaza = creadorEntidades.getCalabaza(x, y);
                        calabaza.setPosX(x);
                        calabaza.setPosY(y);
                        nivel.agregarEntidad(calabaza);
                        nivel.agregarEnemigo(calabaza);
                        nivel.agregarMovible(calabaza);
                    }
                    if ("~".equals(caracter)) {
                        PlataformaQuebradiza plataformaQuebradiza = creadorEntidades.getPlataformaQuebradiza(x, y);
                        plataformaQuebradiza.setPosX(x);
                        plataformaQuebradiza.setPosY(y);
                        nivel.agregarEntidad(plataformaQuebradiza);
                        nivel.agregarEntidad(plataformaQuebradiza.getCajaSuperior());
                        nivel.agregarSuperficieSolida(plataformaQuebradiza);
                    }
                    if ("f".equals(caracter)) {
                        Fruta fruta = creadorEntidades.getFruta(x, y);
                        fruta.setPosX(x);
                        fruta.setPosY(y);
                        nivel.agregarPowerUp(fruta);
                    }
                    if ("H".equals(caracter)) {
                        Escalera escalera = creadorEntidades.getEscalera(x, y);
                        escalera.setPosX(x);
                        escalera.setPosY(y);
                        nivel.agregarEntidad(escalera);
                        nivel.agregarEscalera(escalera);
                    }
                    if ("P".equals(caracter)) {
                        PocionRoja pocionRoja = creadorEntidades.getPocionRoja(x, y);
                        pocionRoja.setPosX(x);
                        pocionRoja.setPosY(y);
                        nivel.agregarPowerUp(pocionRoja);
                    }
                    if ("A".equals(caracter)) {
                        PocionAzul pocionAzul = creadorEntidades.getPocionAzul(x, y);
                        pocionAzul.setPosX(x);
                        pocionAzul.setPosY(y);
                        nivel.agregarPowerUp(pocionAzul);
                    }
                    if ("V".equals(caracter)) {
                        PocionVerde pocionVerde = creadorEntidades.getPocionVerde(x, y);
                        pocionVerde.setPosX(x);
                        pocionVerde.setPosY(y);
                        nivel.agregarPowerUp(pocionVerde);
                    }
                    if ("+".equals(caracter)) {
                        VidaExtra vidaExtra = creadorEntidades.getVidaExtra(x, y);
                        vidaExtra.setPosX(x);
                        vidaExtra.setPosY(y);
                        nivel.agregarPowerUp(vidaExtra);
                    }
                    if ("^".equals(caracter)) {
                        Pinchos pinchos = creadorEntidades.getPinchos(x, y);
                        pinchos.setPosX(x);
                        pinchos.setPosY(y);
                        nivel.agregarEntidad(pinchos);
                    }

                    if ("s".equals(caracter)) {
                        SueloResbaladizo sueloResbaladizo = creadorEntidades.getSueloResbaladizo(x, y);
                        sueloResbaladizo.setPosX(x);
                        sueloResbaladizo.setPosY(y);
                        nivel.agregarEntidad(sueloResbaladizo);
                        nivel.agregarEntidad(sueloResbaladizo.getCajaSuperior());
                    }
                    if ("M".equals(caracter)) {
                        Moghera moghera = creadorEntidades.getMoghera(x, y);
                        moghera.setPosX(x);
                        moghera.setPosY(y);
                        nivel.agregarMovible(moghera);
                        nivel.agregarEnemigo(moghera);
                        nivel.agregarEntidad(moghera);
                    }
                    if ("#".equals(caracter)) {
                        ParedDestructible paredDest = creadorEntidades.getParedDestructible(x, y);
                        paredDest.setPosX(x);
                        paredDest.setPosY(y);
                        nivel.agregarEntidad(paredDest);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("ADVERTENCIA: Coordenadas inválidas, se ignora -> '" + linea + "'");
                }
            }
            return true;

        } catch (IOException e) {
            System.err.println("ERROR: No se pudo leer el archivo de nivel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
