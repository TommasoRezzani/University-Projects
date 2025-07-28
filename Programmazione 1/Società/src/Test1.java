
public class Test1 {

	public static void main(String[] args) {
		Azienda azienda = new Azienda("Bicocca", 3);
		//azienda.nome = "bicocca";
		//azienda.dipendenti = new Dipendente[3];
		
		Dipendente d1 = new Dipendente("Pippo", 100.0, 1);
		//d1.nome = "pippo";
		//d1.setNome("Pippo");
		//d1.costo = 100.0;
		//d1.setCosto(100.0);
		//d1.matricola = 1;
		//d1.setMatricola(1);
		System.out.println(d1.toString());
		
		System.out.println(d1.equals(d1));
		
		azienda.assumi(d1);
		System.out.println(azienda);
		
		azienda.assumi("pluto", 150.0);
		System.out.println(azienda);
		
		
		/**
		 *Dipendente[] nuoviDipendenti = new Dipendente[4];
		 
		for (int i = 0; i < azienda.dipendenti.length; i++) {
			nuoviDipendenti[i] = azienda.dipendenti[i];
		}
		
		
		azienda.dipendenti = nuoviDipendenti;
		*/
		//System.out.println("Costo totale: " + azienda.calcolaCosto());
		
		
		azienda.assumi("Paperino", 130.0, 3);
		System.out.println(azienda);
		
		Dipendente d2 = azienda.licenzia(2);
		System.out.println(azienda);
		
		azienda.assumi("Topolino", 180.0, 5);
		System.out.println(azienda);
		
		System.out.println(azienda.contains(d1));
		System.out.println(azienda.contains(d2));
		System.out.println(azienda.contains(5));
	}
		
	
}