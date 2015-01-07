package pl.edu.pk.iti.copperAnt.network;

public class Package {
	private String sourceMAC = "";
	private String destinationMAC = "";
	private String sourceIP = "";
	private String destinationIP = "";
	private int ttl = 255;
	private String content = "";

	private String header;
	private PackageType type = PackageType.ECHO_REQUEST;
	public final static String MAC_BROADCAST = "00:00:00:00:00:00";

	public Package(PackageType type, String header) {
		this.type = type;
		this.content = header;
		this.header = header;

	}

	public Package(PackageType type, String header, String content) {
		this.type = type;
		this.content = content;
		this.header = header;

	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHeader() {
		return header;
	}

	public PackageType getType() {
		return type;
	}

	public void setType(PackageType type) {
		this.type = type;
	}

	public Package() {

	}

	public Package(PackageType packageType) {
		this(packageType, "");
	}

	public boolean validTTL() {
		return --ttl > 0;
	}

	public String getSourceMAC() {
		return sourceMAC;
	}

	public String getDestinationMAC() {
		return destinationMAC;
	}

	public String getSourceIP() {
		return sourceIP;
	}

	public String getDestinationIP() {
		return destinationIP;
	}

	public void setSourceMAC(String sMAC) {
		this.sourceMAC = sMAC;
	}

	public void setDestinationMAC(String dMAC) {
		this.destinationMAC = dMAC;
	}

	public void setSourceIP(String sIP) {
		this.sourceIP = sIP;
	}

	public void setDestinationIP(String dIP) {
		this.destinationIP = dIP;
	}

	public int getSize() {
		// + 1 Because it always has 'header'
		return content.length() + 1;

	}

	@Override
	public String toString() {
		return "Package [sourceMAC=" + sourceMAC + ", destinationMAC="
				+ destinationMAC + ", sourceIP=" + sourceIP
				+ ", destinationIP=" + destinationIP + ", ttl=" + ttl
				+ ", content=" + content + ", header=" + header + ", type="
				+ type + "]";
	}

	public Package copy() {
		Package clone = new Package();
		clone.sourceMAC = sourceMAC;
		clone.destinationMAC = destinationMAC;
		clone.sourceIP = sourceIP;
		clone.destinationIP = destinationIP;
		clone.ttl = ttl;
		clone.content = content;
		clone.header = header;
		clone.type = type;
		return clone;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((destinationIP == null) ? 0 : destinationIP.hashCode());
		result = prime * result
				+ ((destinationMAC == null) ? 0 : destinationMAC.hashCode());
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		result = prime * result
				+ ((sourceIP == null) ? 0 : sourceIP.hashCode());
		result = prime * result
				+ ((sourceMAC == null) ? 0 : sourceMAC.hashCode());
		result = prime * result + ttl;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Package other = (Package) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (destinationIP == null) {
			if (other.destinationIP != null)
				return false;
		} else if (!destinationIP.equals(other.destinationIP))
			return false;
		if (destinationMAC == null) {
			if (other.destinationMAC != null)
				return false;
		} else if (!destinationMAC.equals(other.destinationMAC))
			return false;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		if (sourceIP == null) {
			if (other.sourceIP != null)
				return false;
		} else if (!sourceIP.equals(other.sourceIP))
			return false;
		if (sourceMAC == null) {
			if (other.sourceMAC != null)
				return false;
		} else if (!sourceMAC.equals(other.sourceMAC))
			return false;
		if (ttl != other.ttl)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
