package main.java.ieseuropa;

import java.time.LocalDateTime;

public class Ticket {

	private int idTicket;
	private LocalDateTime fechaTicket;
	private float importe;
	private float importeIVA;
	private int idUsuario;

	public Ticket(int idTicket, LocalDateTime fechaTicket, float importe, float importeIVA, int idUsuario) {
		this.idTicket = idTicket;
		this.fechaTicket = fechaTicket;
		this.importe = importe;
		this.importeIVA = importeIVA;
		this.idUsuario = idUsuario;
	}

	public Ticket(float importe, float importeIVA, int idUsuario) {
		this.importe = importe;
		this.importeIVA = importeIVA;
		this.idUsuario = idUsuario;
	}

	public Ticket() {
		this.idTicket = 0;
		this.fechaTicket = LocalDateTime.now();
		this.importe = 0;
		this.importeIVA = 0;
	}

	public int getIdTicket() {
		return idTicket;
	}

	public void setIdTicket(int idTicket) {
		this.idTicket = idTicket;
	}

	public LocalDateTime getFechaTicket() {
		return fechaTicket;
	}

	public void setFechaTicket(LocalDateTime fechaTicket) {
		this.fechaTicket = fechaTicket;
	}

	public float getImporte() {
		return importe;
	}

	public void setImporte(float importe) {
		this.importe = importe;
	}

	public float getImporteIVA() {
		return importeIVA;
	}

	public void setImporteIVA(float importeIVA) {
		this.importeIVA = importeIVA;
	}

	public String insertTicket() {
		return "insert into p16.tickets(fecha,importe,totalIVA,idUsuario) values(now()," + this.importe
				+ "," + this.importeIVA + "," + this.idUsuario + ");";
	}

	@Override
	public String toString() {
		return "Ticket [idTicket=" + idTicket + ", fechaTicket=" + fechaTicket + ", importe=" + importe
				+ ", importeIVA=" + importeIVA + "]";
	}

}
