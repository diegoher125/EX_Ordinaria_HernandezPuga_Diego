package main.java.ieseuropa;

public class Usuario {
	
	private int idUsuario;
	private String user;
	private String password;
	private int tipo;
	private float total;
	
	public Usuario(int idUsuario,String user, String password, int tipo) {
		this.idUsuario = idUsuario;
		this.user = user;
		this.password = password;
		this.tipo = tipo;
		this.total = 0;
	}
	
	public Usuario() {
		this.idUsuario = 0;
		this.user = "";
		this.password = "";
		this.tipo = 0;
		this.total = 0;
	}
	
	public Usuario(String user, String password) {
		this.user = user;
		this.password = password;
		this.total = 0;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}
	
	public void sumarTotalDiario(float cantidad) {
		this.total += cantidad;
	}
	
	public boolean superaObjetivo() {
		if(total > 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this.user.equals(((Usuario) o).getUser()) && this.password.equals(((Usuario) o).getPassword())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", user=" + user + ", password=" + password + ", tipo=" + tipo
				+ ", total=" + total + "]";
	}
	
}
