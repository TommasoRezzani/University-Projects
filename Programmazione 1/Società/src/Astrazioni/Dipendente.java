package Astrazioni;

public abstract class Dipendente {
	
	private String nomeCognome;
	private int matricola;
	private double stipendioMensile;
	
	public String getNomeCognome() {
		return nomeCognome;
	}
	
	public void setNomeCognome(String nomeCognome) {
		this.nomeCognome = nomeCognome;
	}
	
	public int getMatricola() {
		return matricola;
	}

	
	public void setMatricola(int matricola) {
		this.matricola = matricola;
	}

	
	public double getStipendioMensile() {
		return stipendioMensile;
	}

	
	public void setStipendioMensile(double stipendioMensile) {
		this.stipendioMensile = stipendioMensile;
	}

	public Dipendente(String nomeCognome, int matricola, double stipendioMensile) throws Exception {
		super();
		if (nomeCognome != "" && matricola > 0 && stipendioMensile > 0.0) {
		this.nomeCognome = nomeCognome;
		this.matricola = matricola;
		this.stipendioMensile = stipendioMensile;
	}	else {
		throw new Exception("Invalid parameters");
	}
	}
	
	public abstract double calcolaRAL();

	public String toString() {
		return "Dipendente [nomeCognome=" + nomeCognome + ", matricola=" + matricola + ", stipendioMensile="
				+ stipendioMensile + ", calcolaRAL()=" + calcolaRAL() + "]";
	}
	
	
	
}
