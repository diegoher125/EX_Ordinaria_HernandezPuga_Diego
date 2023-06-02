package main.java.ieseuropa;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class EX_Ordinaria_HernandezPuga_Diego {

	private static Usuario usuario;

	private static int pedirInt(String texto) {
		Scanner teclado = new Scanner(System.in);
		System.out.println(texto);
		int numero = 0;
		do {
			while (!teclado.hasNextInt()) {
				teclado.next();
				System.out.println("Valor incorrecto");
			}
			numero = teclado.nextInt();
		} while (numero < 0);
		return numero;
	}

	private static String pedirString(String texto) {
		Scanner teclado = new Scanner(System.in);
		System.out.println(texto);
		return teclado.nextLine();
	}

	private static float pedirFloat(String texto) {
		Scanner teclado = new Scanner(System.in);
		System.out.println(texto);
		float numero = 0;
		do {
			while (!teclado.hasNextFloat()) {
				teclado.next();
				System.out.println("Valor incorrecto");
			}
			numero = teclado.nextFloat();
		} while (numero < 0);
		return numero;
	}

	private static void mostrarProductos(Conexion conexion) {
		for (Producto producto : conexion.cargarProductos("SELECT * FROM p16.productos;")) {
			System.out.println(producto.infoProd());
		}
	}

	private static void eliminarProd(Conexion conexion, Producto producto) {
		if (conexion.existeProd(producto.queryExisteProducto())) {
			conexion.ejecutarQuery(producto.deleteProd());
			System.out.println("Producto eliminado correctamente");
		} else {
			System.out.println("ERROR: El producto no existe");
		}
	}

	private static void modProd(Conexion conexion, Producto producto) {
		if (conexion.existeProd(producto.queryExisteProducto())) {
			producto.setIdProd(conexion.cargarInt(producto.queryIdProd()));
			conexion.ejecutarQuery(producto.actualizarProducto());
			System.out.println("Producto modificado correctamente");
		} else {
			System.out.println("ERROR: El producto no existe");
		}
	}

	private static void inicio(Conexion conexion) {
		mostrarProductos(conexion);
		if (usuario instanceof Administrador) {
			int opcion = 0;
			do {
				opcion = menuSuper(conexion);
			} while (opcion != 0);
		}
		menuCompra(conexion);
	}

	private static int menuSuper(Conexion conexion) {
		int opcion = -1;
		while (opcion < 0 || opcion > 3) {
			opcion = pedirInt("¿Qué desea hacer? (1: añadir nuevo producto, 2: modificar un"
					+ " producto,3: eliminar un producto, 0: seguir)");
		}
		menuProd(opcion, conexion);
		return opcion;
	}

	private static void menuProd(int opcion, Conexion conexion) {
		switch (opcion) {
		case 1:
			addProd(conexion, nuevoProducto());
			break;
		case 2:
			modProd(conexion, nuevoProducto());
			break;
		case 3:
			eliminarProd(conexion, conexion.cargarProducto("select * from p16.productos where nombre = '"
					+ pedirString("Introduzca el nombre del producto:") + "';"));
			break;
		}
	}

	private static void addProd(Conexion conexion, Producto producto) {
		if (conexion.existeProd(producto.queryExisteProducto())) {
			System.out.println("ERROR: El producto ya existe");
		} else {
			conexion.ejecutarQuery(producto.nuevoProd());
		}
	}

	private static Producto nuevoProducto() {
		return new Producto(pedirString("Introduzca el nombre del producto:"), pedirFloat("Introduzca su precio:"),
				pedirInt("Introduzca el número de unidades en stock:"), pedirIdIVA());
	}

	private static int pedirIdIVA() {
		int tipIVA = 0;
		while (tipIVA < 1 || tipIVA > 3) {
			tipIVA = pedirInt("Introduzca el tipo de IVA(1.IVA Superreducido, 2.IVA reducido, 3.IVA General):");
		}
		return tipIVA;
	}

	private static void menuCompra(Conexion conextion) {
		String seguir = "";
		do {
			hacerCompra(conextion);
			seguir = pedirString("¿Desea atender a otro cliente?");
		} while (seguir.equalsIgnoreCase("Si"));
	}

	private static void hacerCompra(Conexion conexion) {
		System.out.println("Empecemos con las compras!!");
		Compra carrito = new Compra();
		String nombre = "";
		while (!nombre.equals("0")) {
			nombre = pedirString("Introduzca el producto:");
			if (nombre.equals("0")) {
			} else if (conexion.existeProd(new Producto(nombre).queryExisteProducto())) {
				carrito = addCarrito(conexion, nombre, carrito);
			} else {
				System.out.println("ERROR: No tenemos ese producto");
			}
		}
		borrarUnidadesProducto(conexion, carrito);
		redicirUnidadesProducto(conexion, carrito);
		imprimirTicket(conexion, carrito);
	}

	private static Compra borrarUnidadesProducto(Conexion conexion, Compra carrito) {
		String nombre = "";
		while (!nombre.equals("0")) {
			nombre = pedirString("¿Quieres borrar algún producto?");
			if (nombre.equals("0")) {
			} else if (conexion.existeProd(new Producto(nombre).queryExisteProducto())) {
				conexion.ejecutarQuery(carrito.borrarUnidades(nombre));
			} else {
				System.out.println("ERROR: No tenemos ese producto");
			}
		}
		return carrito;
	}

	private static Compra redicirUnidadesProducto(Conexion conexion, Compra carrito) {
		String nombre = "";
		while (!nombre.equals("0")) {
			nombre = pedirString("¿Quieres reducir unidades de algún producto?");
			if (nombre.equals("0")) {
			} else if (conexion.existeProd(new Producto(nombre).queryExisteProducto())) {
				int unidades = pedirInt("¿Cuántas unidades?");
				if (carrito.sePuedeReducirUnidades(nombre, unidades)) {
					conexion.ejecutarQuery(carrito.reducirUnidades(nombre, unidades));
				} else {
					System.out.println("No se pueden reducir " + unidades + " de " + nombre);
				}
			} else {
				System.out.println("ERROR: No tenemos ese producto");
			}
		}
		return carrito;
	}

	private static Compra addCarrito(Conexion conexion, String nombre, Compra carrito) {
		int cant = pedirInt("Introduzca la cantidad");
		Producto producto = conexion.cargarProducto(new Producto(nombre).selectProd());
		if (cant < producto.getUnidadesStock()) {
			producto.setUnidadesStock(cant);
			conexion.ejecutarQuery(producto.restarStock());
			carrito.addProducto(producto);
		} else {
			System.out.println("No hay tanto stock");
		}
		return carrito;
	}

	private static void imprimirTicket(Conexion conexion, Compra carrito) {
		System.out.println("Generando ticket...\n\n" + "Producto\t\t\tPrecio\tCantidad\tSubtotal\tTipo\n"
				+ "--------------------------------------------------------------");
		System.out.println(carrito.prodsTicket());
		conexion.ejecutarQuery(carrito.hacerTicket(usuario.getIdUsuario()));
		usuario.sumarTotalDiario(carrito.calcImporteTotal(1));
		System.out.println(carrito.totalImporte() + "\t\t\t\tTipo\tBase\tIVATotal"
				+ "--------------------------------------------------------------");
	}

	private static void fin(Conexion conexion) throws IOException {
		if (usuario instanceof Administrador) {
			imprimirFacturadoAdmin(conexion);
			ficherosDineroRecaudado(conexion);
		} else {
			imprimirFacturadoTrabajador(conexion);
		}
		System.out.println("\nTu stock queda asi:\n");
		mostrarProductos(conexion);
	}
	
	private static void ficherosDineroRecaudado(Conexion conexion) throws IOException {
		ficheroMasDineroRecaudado(conexion);
		ficheroMenosDineroRecaudado(conexion);
	}
	
	private static void ficheroMasDineroRecaudado(Conexion conexion) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(usuario.getUser() + "_top3.txt"));
		bw.write(conexion.cargarNombresUsuarios("select usuarios.user, sum(tickets.importe) as total, usuarios.idUsuario \n"
				+ "from p16.tickets\n"
				+ "right join p16.usuarios on tickets.idUsuario = usuarios.idUsuario \n"
				+ "group by usuarios.idUsuario order by total desc;"));
		bw.close();
	}
	
	private static void ficheroMenosDineroRecaudado(Conexion conexion) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(usuario.getUser() + "_worst3.txt"));
		bw.write(conexion.cargarNombresUsuarios("select usuarios.user, sum(tickets.importe) as total, usuarios.idUsuario \n"
				+ "from p16.tickets\n"
				+ "right join p16.usuarios on tickets.idUsuario = usuarios.idUsuario \n"
				+ "group by usuarios.idUsuario order by total;"));
		bw.close();
	}

	private static void imprimirFacturadoAdmin(Conexion conexion) {
		System.out.println("\nEl total facturado el día de hoy es: "
				+ (Math.round(sumarImporte(cargarTicketsHoy(conexion), 1) * 100.0) / 100.0));
		System.out.println("El total de IVA el día de hoy es: "
				+ (Math.round(sumarImporte(cargarTicketsHoy(conexion), 2) * 100.0) / 100.0));
		System.out.println("El total facturado este mes es: "
				+ (Math.round(sumarImporte(cargarTicketsEsteMes(conexion), 1) * 100.0) / 100.0));
		System.out.println("El total de IVA este mes es: "
				+ (Math.round(sumarImporte(cargarTicketsEsteMes(conexion), 2) * 100.0) / 100.0));
		System.out.println("El ticket medio ha sido "
				+ (Math.round(mediaTicketsAdministrador(conexion) * 100.0) / 100.0));
	}

	private static void imprimirFacturadoTrabajador(Conexion conexion) {
		System.out.println("\nEl total facturado el día de hoy es: "
				+ (Math.round(sumarImporte(cargarTicketsHoyTrabajador(conexion), 1) * 100.0) / 100.0));
		System.out.println("El total facturado este mes es: "
				+ (Math.round(sumarImporte(cargarTicketsEsteMesTrabajador(conexion), 1) * 100.0) / 100.0));
		System.out.println("El ticket medio ha sido "
				+ (Math.round(mediaTicketsTrabajador(conexion) * 100.0) / 100.0));
	}

	private static float mediaTicketsTrabajador(Conexion conexion) {
		return conexion.cargarFloat("select sum(importe) from p16.tickets where idUsuario = " 
				+ usuario.getIdUsuario() + ";") / conexion.cargarInt("select count(idcompra) from p16.tickets where idUsuario = " 
						+ usuario.getIdUsuario() + ";");
	}
	
	private static float mediaTicketsAdministrador(Conexion conexion) {
		return conexion.cargarFloat("select sum(importe) from p16.tickets;") / 
				conexion.cargarInt("select count(idcompra) from p16.tickets" + ";");
	}

	private static ArrayList<Ticket> cargarTicketsHoy(Conexion conexion) {
		ArrayList<Ticket> tickets = conexion.cargarTickets("select importe, totalIVA, idUsuario from p16.tickets"
				+ " where extract(year from fecha) = " + LocalDateTime.now().getYear()
				+ " and extract(month from fecha) = " + LocalDateTime.now().getMonthValue()
				+ " and extract(day from fecha) = " + LocalDateTime.now().getDayOfMonth() + ";");
		return tickets;
	}

	private static ArrayList<Ticket> cargarTicketsEsteMes(Conexion conexion) {
		ArrayList<Ticket> tickets = conexion.cargarTickets("select importe, totalIVA, idUsuario from p16.tickets"
				+ " where extract(year from fecha) = " + LocalDateTime.now().getYear()
				+ " and extract(month from fecha) = " + LocalDateTime.now().getMonthValue() + ";");
		return tickets;
	}

	private static ArrayList<Ticket> cargarTicketsHoyTrabajador(Conexion conexion) {
		ArrayList<Ticket> tickets = conexion.cargarTickets(
				"select importe, totalIVA, idUsuario from p16.tickets" + " where extract(year from fecha) = "
						+ LocalDateTime.now().getYear() + " and extract(month from fecha) = "
						+ LocalDateTime.now().getMonthValue() + " and extract(day from fecha) = "
						+ LocalDateTime.now().getDayOfMonth() + " and idUsuario = '" + usuario.getIdUsuario() + "';");
		return tickets;
	}

	private static ArrayList<Ticket> cargarTicketsEsteMesTrabajador(Conexion conexion) {
		ArrayList<Ticket> tickets = conexion.cargarTickets(
				"select importe, totalIVA, idUsuario from p16.tickets" + " where extract(year from fecha) = "
						+ LocalDateTime.now().getYear() + " and extract(month from fecha) = "
						+ LocalDateTime.now().getMonthValue() + " and idUsuario = '" + usuario.getIdUsuario() + "';");
		return tickets;
	}

	private static float sumarImporte(ArrayList<Ticket> tickets, int opcion) {
		float importeTotal = 0;
		for (Ticket ticket : tickets) {
			if (opcion == 1) {
				importeTotal += ticket.getImporte();
			} else {
				importeTotal += ticket.getImporteIVA();
			}
		}
		return importeTotal;
	}

	private static boolean registro(Conexion conexion) {
		String user = "", password = "";
		int cont = 0;
		while (!existeUsuario(conexion, user, password) && cont < 3) {
			user = pedirString("Introduzca su usuario:");
			password = pedirString("Introduzca la contraseña:");
			if (!existeUsuario(conexion, user, password)) {
				cont++;
			}else {
				usuario = conexion.cargarUsuario(
						"select * from p16.usuarios where user = '" + user + "' and password = '" + password + "';");
			}
		}
		if(!existeUsuario(conexion, user, password) && cont >= 3) {
			return false;
		}else {
			return true;
		}
	}

	private static boolean existeUsuario(Conexion conexion, String user, String password) {
		if (conexion.cargarUsuarios("SELECT * FROM p16.usuarios;").contains(new Usuario(user, password))) {
			return true;
		} else {
			return false;
		}
	}
	
	private static void imprimirResultadoObjetivo() {
		if(usuario.superaObjetivo()) {
			System.out.println("\nHas superado el objetivo de ventas");
		}else {
			System.out.println("\nNo has superado el objetivo de ventas");
		}
	}
	
	private static void ejecucion(Conexion conexion) throws IOException {
		System.out.println("Bienvenido!!");
		if(registro(conexion)) {
			inicio(conexion);
			fin(conexion);
			imprimirResultadoObjetivo();
		}
		System.out.println("Adios!!");
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Conexion conexion = new Conexion();
		ejecucion(conexion);
	}

}
