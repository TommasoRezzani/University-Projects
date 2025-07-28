
public class Auto {
	
	private String targa;
	private int km;
	
	
	public String getTarga() {
		return targa;
	}
	

	public int getKmPercorsi() {
		return km;
	}
	
	public boolean aggiornaKm(int kmAggiuntivi) {
		if (kmAggiuntivi < 0)
			return false;
		km += kmAggiuntivi;
		return true;
	}
	
	public Auto(String targa, int km) {
		super();
		this.targa = targa;
		this.km = km;
	}
	
	
	public Auto(String targa) {
		this(targa, 0);
	}

	public boolean equals(Auto altra) {
		if (altra == null)
			return false;
		if (getTarga().equals(altra.getTarga()))
			return true;
		return false;
	}

	
	public String toString() {
		return "Auto [targa=" + targa + ", km=" + km + "]";
	}
	
	
	
}


	
	

	
	


	
	
	
	
	
	
	
	
	


	
	
	
	
	
	
	
	
