
public class ParcoAuto {
	
	private Auto[] autoAziendali;
	
	public ParcoAuto(int MaxAuto) {
		autoAziendali = new Auto[MaxAuto];
	}
	
	public Auto trovaAuto(Auto auto) {
		if (auto == null)
			return null;
		for (Auto autoNelParco : autoAziendali) {
		if (auto.equals(autoNelParco))
			return autoNelParco;
		}
		return null;
	}
	
	public boolean inserisciNuovaAuto(String targa) {
		if (targa == null)
			return false;
		Auto autoNelParco = trovaAuto(new Auto(targa));
		if (autoNelParco != null)
			return false;
		for (int i = 0; i < autoAziendali.length; i++) {
			if (autoAziendali[i] == null) {
				autoAziendali[i] = new Auto(targa);
			return true;
			}
		}	
		return false;
	}
	
	
	public boolean rientroAuto(Auto AutoRientrata) {
		if (AutoRientrata == null)
		return false;
		
		Auto autoNelParco = trovaAuto(AutoRientrata);
		if (autoNelParco == null)
			return false;
		
		return autoNelParco.aggiornaKm(AutoRientrata.getKmPercorsi() - autoNelParco.getKmPercorsi());
	}

	public boolean rientroAuto(String targa, int kmalRientro) {
		return rientroAuto(new Auto(targa, kmalRientro));
	}
	
	
}	
