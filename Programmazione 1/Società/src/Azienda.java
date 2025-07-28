import java.util.Arrays;

public class Azienda {
	private String nome;
	private Dipendente[] dipendenti;
	
	public String getNome() {
		return nome;
	}


	public double calcolaCosto( ) {
		double costoTotale = 0.0;
		if (dipendenti != null); {
		for (int i = 0; i < dipendenti.length; i++) {
			if (dipendenti[i] != null) {
			costoTotale += dipendenti[i].getCosto();
				}
			}
		}
		
	   return costoTotale;	
			
	}

	
	public String toString() {
		return "Azienda [nome=" + nome + ", dipendenti=" + Arrays.toString(dipendenti) + ", calcolaCosto()="
				+ calcolaCosto() + "]";
	}
	
	public boolean assumi(Dipendente nuovoDipendente) {
		if (dipendenti != null); {
			for (int i = 0; i < dipendenti.length; i++) {
				if (dipendenti[i] == null) {
				dipendenti[i] = nuovoDipendente;
				return true;
					}
				}
			}
		return false;
	}
	
	public boolean assumi(String nome, double costo, int matricola) {
		Dipendente nuovoDipendente = new Dipendente(nome, costo, matricola);
		//nuovoDipendente.nome = nome;
		//nuovoDipendente.costo = costo;
		//nuovoDipendente.matricola = matricola;
		return assumi(nuovoDipendente);
		
	}
	
	public boolean assumi(String nome, double costo) {
		return assumi(nome, costo, getNewMatricola());
	}
	
	public Dipendente licenzia(int matricola) {
		Dipendente licenziato = null;
		if (dipendenti != null); {
			for (int i = 0; i < dipendenti.length; i++) {
				if (dipendenti[i] != null && dipendenti[i].getMatricola() == matricola) {
					licenziato = dipendenti[i];
					dipendenti[i] = null;
					break;
				}
			}
		}
		
		return licenziato;
	}
	
	public Dipendente licenzia(Dipendente daLicenziare) {
		return licenzia(daLicenziare.getMatricola());
	}
	
	public boolean contains(int matricola) {
		if (dipendenti != null); {
			for (int i = 0; i < dipendenti.length; i++) {
				if (dipendenti[i] != null && dipendenti[i].getMatricola() == matricola) {					
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean contains(Dipendente d) {
		//return contains(d.getMatricola());
		for (int i = 0; i < dipendenti.length; i++) {
			if (dipendenti[i] != null && dipendenti[i].equals(d)) {
				return true;
			}
		}
		return false;
	}

	public int numDip() {
		return dipendenti.length;
	}


	public Azienda(String nome, int numDip) {
		this.nome = nome;
		dipendenti = new Dipendente[numDip];
	}


	public Azienda() {
		this("prova", 1);
	}

	//per aumentare dimensione array
	
	public boolean aumentaDimensione(int nuovaDimensione) {
		
		if (nuovaDimensione <= numDip())  {
			return false;
		}
		Dipendente[] nuoviDipendenti = new Dipendente[nuovaDimensione];
		for (int i = 0; i < this.dipendenti.length; i++) {
		nuoviDipendenti[i] = this.dipendenti[i];
		}
		
		this.dipendenti	= nuoviDipendenti;
		return true;
	}


	
	private int getNewMatricola() {
		int max = 0;
		for (int i = 0; i < dipendenti.length; i++)  {
			if(dipendenti[i] != null && dipendenti[i].getMatricola() > max)
				max = dipendenti[i].getMatricola();
		}
		return max + 1;
	}
	
	
	
}
