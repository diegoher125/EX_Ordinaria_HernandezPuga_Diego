package main.java.ieseuropa;

public class Administrador extends Usuario {

	public Administrador(int idUsuario, String user, String password, int tipo) {
		super(idUsuario, user, password, tipo);
	}
	
	public Administrador(int idUsuario, String user, String password) {
		super(idUsuario, user, password, 1);
	}
	
	@Override
	public boolean superaObjetivo() {
		if(getTotal() > (100 * 1.25)) {
			return true;
		}else {
			return false;
		}
	}

}
