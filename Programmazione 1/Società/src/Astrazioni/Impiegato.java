package Astrazioni;

public class Impiegato extends Dipendente {

	public Impiegato(String nomeCognome, int matricola, double stipendioMensile) throws Exception {
		super(nomeCognome, matricola, stipendioMensile);
		
	}

	@Override
	public double calcolaRAL() {
		return 13 * getStipendioMensile();
	}

}
