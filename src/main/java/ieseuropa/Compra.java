package main.java.ieseuropa;

import java.util.ArrayList;
import java.util.Collections;
import java.time.LocalDateTime;

public class Compra {

	private Ticket ticket;
	private ArrayList<Producto> productos;

	public Compra(Ticket ticket, ArrayList<Producto> productos) {
		this.productos = productos;
		this.ticket = ticket;
	}
	
	public Compra() {
		this.productos = new ArrayList<>();
		this.ticket = new Ticket();
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public ArrayList<Producto> getProductos() {
		return productos;
	}

	public void setProductos(ArrayList<Producto> productos) {
		this.productos = productos;
	}

	public void addProducto(Producto producto) {
		productos.add(producto);
	}

	public int devolverCantidad(Producto producto) {
		return productos.get(productos.indexOf(producto)).getUnidadesStock();
	}

	public String devolverPorcentaje(String nombre) {
		return "select porcentaje from p16.impuestos where idImpuesto = "
				+ productos.get(productos.indexOf(new Producto(nombre))).getIdImpuesto() + ";";
	}

	public String hacerTicket(int idUsuario) {
		this.ticket = new Ticket(calcImporteTotal(1), calcImporteTotal(2), idUsuario);
		return this.ticket.insertTicket();
	}
	
	public String prodsTicket() {
		Collections.sort(this.productos);
		String prodsTicket = "";
		for(Producto producto: this.productos) {
			prodsTicket += producto.ticketProd();
		}
		return prodsTicket;
	}
	
	public float calcImporteTotal(int opc) {
		float total = 0;
		for(Producto producto: this.productos) {
			if(opc == 1) {
				total += producto.calcSubTotal();
			}else {
				total += producto.calcImporteIVA();
			}
		}
		return total;
	}
	
	public String totalImporte() {
		return "TOTAL: " + (Math.round(this.ticket.getImporte() * 100.0) / 100.0) + "\n\n";
	}
	
	public String borrarUnidades(String nombre) {
		return productos.get(productos.indexOf(new Producto(nombre))).borrarUnidadesCompra();
	}
	
	public String reducirUnidades(String nombre, int unidades) {
		return productos.get(productos.indexOf(new Producto(nombre))).reducirUnidadesCompra(unidades);
	}
	
	public boolean sePuedeReducirUnidades(String nombre, int unidades) {
		if(productos.get(productos.indexOf(new Producto(nombre))).diferenciaUnidades(unidades) > 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Compra [ticket=" + ticket + ", productos=" + productos + "]";
	}

}
