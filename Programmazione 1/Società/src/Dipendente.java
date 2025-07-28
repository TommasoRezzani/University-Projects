
public class Dipendente {
	private String nome;
	private double costo;
	private int matricola;
	
	//get e set nome
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	
	//get e set costo
	
	public double getCosto() {
		return costo;
	}
	public void setCosto(double costo) {
		this.costo = costo;
	}
	
	
	//get e set matricola
	public int getMatricola() {
		return matricola;
	}
	private void setMatricola(int matricola) {
		this.matricola = matricola;
	}
	
	
	//toString dipendente
	public String toString() {
		return "Dipendente [nome=" + nome + ", costo=" + costo + ", matricola=" + matricola + "]";
	}
	
	public Dipendente(String nome, double costo, int matricola) {
		setNome(nome);
		setCosto(costo);
		setMatricola(matricola);
	}
	
	public Dipendente(String nome, int matricola) {
		this(nome, 100.0, matricola);
	}
	
	public Dipendente() {
		this("prova", 0);
	}
	
	
	public boolean equals(Dipendente dip) {
		if (this == dip)
			return true;
		if (dip == null)
			return false;
		if (getClass() != dip.getClass())
			return false;
		Dipendente other = (Dipendente) dip;
		if (Double.doubleToLongBits(costo) != Double.doubleToLongBits(other.costo))
			return false;
		if (getMatricola() == dip.getMatricola())
			return true;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	
}


