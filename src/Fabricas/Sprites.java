package Fabricas;

import java.util.Map;

public class Sprites {
	
	protected Map<Integer,String> mapeo_imagen_estado;
	protected int estado_actual;
	
	public Sprites (Map<Integer,String> mapeo_estado_imagen, int estado_actual) {
		this.mapeo_imagen_estado = mapeo_estado_imagen;
		this.estado_actual = estado_actual;
	}
	
	public void set_estado_actual(int estado_actual){
		this.estado_actual = estado_actual;
	}

	public String get_ruta_imagen_actual() {
		return mapeo_imagen_estado.get(estado_actual);
	}

}