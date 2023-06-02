package main.java.ieseuropa;

public class Producto implements Comparable<Producto> {

	private int idProd;
	private String nombre;
	private float precio;
	private int unidadesStock;
	private int idImpuesto;

	public Producto(int idProd, String nombre, float precio, int unidadesStock, int idImpuesto) {
		this.idProd = idProd;
		this.nombre = nombre;
		this.precio = precio;
		this.unidadesStock = unidadesStock;
		this.idImpuesto = idImpuesto;
	}

	public Producto(String nombre, float precio, int unidadesStock, int idImpuesto) {
		this.idProd = 0;
		this.nombre = nombre;
		this.precio = precio;
		this.unidadesStock = unidadesStock;
		this.idImpuesto = idImpuesto;
	}

	public Producto(String nombre) {
		this.idProd = 0;
		this.nombre = nombre;
		this.precio = 0;
		this.unidadesStock = 0;
		this.idImpuesto = 0;
	}

	public Producto() {
		this.idProd = 0;
		this.nombre = "";
		this.precio = 0;
		this.unidadesStock = 0;
		this.idImpuesto = 0;
	}

	public int getIdProd() {
		return idProd;
	}

	public void setIdProd(int idProd) {
		this.idProd = idProd;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public int getUnidadesStock() {
		return unidadesStock;
	}

	public void setUnidadesStock(int unidadesStock) {
		this.unidadesStock = unidadesStock;
	}

	public int getIdImpuesto() {
		return idImpuesto;
	}

	public void setIdImpuesto(int idImpuesto) {
		this.idImpuesto = idImpuesto;
	}

	public String infoProd() {
		return this.nombre + " - Precio: " + this.precio + " - Stock: " + this.unidadesStock + " unidades - "
				+ tipoIVA();
	}

	public String ticketProd() {
		return this.nombre + "\t\t\t\t\t" + this.precio + "\t" + this.unidadesStock + "\t"
				+ (Math.round(calcSubTotal() * 100.0) / 100.0) + "\t\t" + this.idImpuesto + "\n";
	}

	public float calcSubTotal() {
		return this.precio * this.unidadesStock;
	}
	
	public float calcImporteIVA() {
		return calcSubTotal() * (1 + (porcentajeIVA() / 100));
	}

	private String tipoIVA() {
		switch (this.idImpuesto) {
		case 1:
			return "IVA Superreducido";
		case 2:
			return "IVA Reducido";
		case 3:
			return "IVA General";
		default:
			return "";

		}
	}
	
	private int porcentajeIVA() {
		switch (this.idImpuesto) {
		case 1:
			return 4;
		case 2:
			return 10;
		case 3:
			return 21;
		default:
			return 0;

		}
	}

	public String selectProd() {
		return "select * from p16.productos where nombre = '" + this.nombre + "';";
	}

	public String restarStock() {
		return "update p16.productos set unidadesStock = unidadesStock - " + this.unidadesStock + " where idprod = "
				+ this.idProd + ";";
	}

	public String nuevoProd() {
		return "insert into p16.productos(nombre,precio,unidadesStock,idImpuesto) values('" + this.nombre + "',"
				+ this.precio + "," + this.unidadesStock + "," + this.idImpuesto + ");";
	}

	public String actualizarProducto() {
		return "update p16.productos set nombre = '" + this.nombre + "', precio = " + this.precio + ", unidadesStock = "
				+ this.unidadesStock + ", idImpuesto = " + this.idImpuesto + " where idprod = " + this.idProd + ";";
	}

	public String deleteProd() {
		return "delete from p16.productos where idprod = " + this.idProd + ";";
	}
	
	public String queryExisteProducto() {
		return "select nombre from p16.productos where nombre = '" + this.nombre + "';";
	}
	
	public String queryIdProd() {
		return "select idprod from p16.productos where nombre = '" + this.nombre + "';";
	}
	
	public String reducirUnidadesCompra(int unidades) {
		this.unidadesStock -= unidades;
		return "update p16.productos set unidadesStock = unidadesStock +"
				+ unidades + " where idprod = " + this.idProd + ";";
	}
	
	public String borrarUnidadesCompra() {
		return "update p16.productos set unidadesStock = unidadesStock +"
				+ this.unidadesStock + " where idprod = " + this.idProd + ";";
	}
	
	public int diferenciaUnidades(int unidades) {
		return this.unidadesStock - unidades;
	}

	@Override
	public int compareTo(Producto prod) {
		return (this.unidadesStock < prod.getUnidadesStock() ? 1
				: this.unidadesStock == prod.getUnidadesStock() ? 0 : -1);
	}

	@Override
	public boolean equals(Object o) {
		if (nombre.equalsIgnoreCase(((Producto) o).nombre)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Producto [idProd=" + idProd + ", nombre=" + nombre + ", precio=" + precio + ", unidadesStock="
				+ unidadesStock + ", idImpuesto=" + idImpuesto + "]";
	}

}
