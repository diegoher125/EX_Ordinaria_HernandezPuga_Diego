package main.java.ieseuropa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Conexion {
	
	private static String db_ = "p16";
	private static String login_ = "root";
	private static String pwd_ = "root";
	private static String url_ = "jdbc:mysql://localhost/" + db_ + "?serverTimezone=UTC";
	private static Connection conection_;
	private static Statement st_;
	
	public Conexion() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conection_ = DriverManager.getConnection(url_, login_, pwd_);
			if(conection_ != null) {
				st_ = conection_.createStatement();
				System.out.println("Conexion exitosa a la base de datos " + db_);
			}else {
				System.out.println("Conexion fallida. Muerte y destruccion");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void ejecutarQuery(String query) {
		try {
			Statement st = conection_.createStatement();
			
			st.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Producto> cargarProductos(String query){
		ArrayList<Producto> productos = new ArrayList<>();
		try {
			Statement st = conection_.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				int idProd, unidadesStock, idImpuesto;
				String nombre;
				float precio;
				idProd = rs.getInt("idprod");
				nombre = rs.getString("nombre");
				precio = rs.getFloat("precio");
				unidadesStock = rs.getInt("unidadesStock");
				idImpuesto = rs.getInt("idImpuesto");
				productos.add(new Producto(idProd, nombre, precio, unidadesStock, idImpuesto));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productos;
	}
	
	public ArrayList<Usuario> cargarUsuarios(String query){
		ArrayList<Usuario> usuarios = new ArrayList<>();
		try {
			Statement st = conection_.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				int idUsuario, tipo;
				String user,password;
				idUsuario = rs.getInt("idUsuario");
				user = rs.getString("user");
				password = rs.getString("password");
				tipo = rs.getInt("tipo");
				switch(tipo) {
				case 0:
					usuarios.add(new Trabajador(idUsuario, user, password));
					break;
				case 1:
					usuarios.add(new Administrador(idUsuario, user, password));
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usuarios;
	}
	
	public Usuario cargarUsuario(String query){
		Usuario usuario = new Usuario();
		try {
			Statement st = conection_.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				int idUsuario, tipo;
				String user,password;
				idUsuario = rs.getInt("idUsuario");
				user = rs.getString("user");
				password = rs.getString("password");
				tipo = rs.getInt("tipo");
				switch(tipo) {
				case 0:
					usuario = new Trabajador(idUsuario, user, password);
					break;
				case 1:
					usuario = new Administrador(idUsuario, user, password);
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usuario;
	}
	
	public Producto cargarProducto(String query){
		Producto producto = new Producto();
		try {
			Statement st = conection_.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				int idProd, unidadesStock, idImpuesto;
				String nombre;
				float precio;
				idProd = rs.getInt("idprod");
				nombre = rs.getString("nombre");
				precio = rs.getFloat("precio");
				unidadesStock = rs.getInt("unidadesStock");
				idImpuesto = rs.getInt("idImpuesto");
				producto = new Producto(idProd, nombre, precio, unidadesStock, idImpuesto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return producto;
	}
	
	public ArrayList<Ticket> cargarTickets(String query){
		ArrayList<Ticket> tickets = new ArrayList<>();
		try {
			Statement st = conection_.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				float importe, importeIVA;
				int idUsuario;
				importe = rs.getFloat("importe");
				importeIVA = rs.getFloat("totalIVA");
				idUsuario = rs.getInt("idUsuario");
				tickets.add(new Ticket(importe, importeIVA,idUsuario));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tickets;
	}
	
	public float cargarFloat(String query){
		float calculo = 0;
		try {
			Statement st = conection_.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				calculo = rs.getFloat(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return calculo;
	}
	
	public int cargarInt(String query){
		int calculo = 0;
		try {
			Statement st = conection_.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				calculo = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return calculo;
	}
	
	public boolean existeProd(String query){
		boolean comp = false;
		try {
			Statement st = conection_.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				if(rs.getString(1).isEmpty()) {
					comp = false;
				}else {
					comp = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comp;
	}
	
	public String cargarString(String query){
		String cadena = "";
		try {
			Statement st = conection_.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				cadena = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cadena;
	}
	
	public String cargarNombresUsuarios(String query){
		String cadena = "";
		try {
			Statement st = conection_.createStatement();
			ResultSet rs = st.executeQuery(query);
			int cont = 0;
			while(rs.next() && cont<3) {
				cadena += rs.getString(1) + "\n";
				cont++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cadena;
	}

}