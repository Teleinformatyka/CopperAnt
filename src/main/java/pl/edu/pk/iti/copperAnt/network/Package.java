package pl.edu.pk.iti.copperAnt.network;

public class Package {
	

	public String getFromIp() {
		return "no jet implemented";
	}

	public String getToIp() {
		return "no jet implemented";
	}

	public String getFromMac() {
		return "no jet implemented";
	}

	public String getToMac() {
		return "no jet implemented";
	}
	
	public void setToIp(String ip) {
		m_toIp = ip;
	}

	public int getSize() {
		// TODO wykorzystać do obliczania opóźnień
		return 0;
	}
	
	private String m_fromIp;
	private String m_toIp;
		
	

}
