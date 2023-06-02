package main.java.ieseuropa;

public class Trabajador extends Usuario {

	public Trabajador(int idUsuario, String user, String password, int tipo) {
		super(idUsuario, user, password, tipo);
	}
	
	public Trabajador(int idUsuario, String user, String password) {
		super(idUsuario, user, password, 0);
	}
	
	@Override
	public boolean superaObjetivo() {
		if(getTotal() > 100) {
			return true;
		}else {
			return false;
		}
	}

}
