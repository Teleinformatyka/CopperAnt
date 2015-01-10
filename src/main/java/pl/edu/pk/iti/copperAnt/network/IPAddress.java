package pl.edu.pk.iti.copperAnt.network;

import java.util.StringTokenizer;

import org.apache.commons.net.util.SubnetUtils;

public class IPAddress {
	private int[] ipParts = { 192, 168, 0, 1 };
	private int[] netmaskParts = { 255, 255, 255, 0 };
	public static final String NETMASK = "255.255.255.0";
	private SubnetUtils subnet;
	private int broadcast = 255;

	private IPAddress(String ip, String netmask) {
		String[] ipParts = ip.split("\\.");
		for (int i = 0; i < 4; ++i) {
			this.ipParts[i] = Integer.parseInt(ipParts[i]);
		}
		String[] netmaskParts = netmask.split("\\.");
		for (int i = 0; i < 4; ++i) {
			this.netmaskParts[i] = Integer.parseInt(netmaskParts[i]);

		}
		subnet = new SubnetUtils(ip, netmask);

	}

	public IPAddress copy() {
		IPAddress clone = new IPAddress(this);
		return clone;

	}

	public IPAddress(String ip) {
		this(ip, "255.255.255.0");
	}

	public IPAddress(IPAddress ipObj) {
		for (int i = 0; i < 4; i++) {
			ipParts[i] = ipObj.ipParts[i];
			netmaskParts[i] = ipObj.netmaskParts[i];
		}
		subnet = new SubnetUtils(ipObj.toString(), ipObj.getNetwork());

	}

	public String toString() {
		StringBuilder ip = new StringBuilder();
		for (int i = 0, len = 4; i < len; ++i) {
			ip.append(ipParts[i] + ".");
		}
		return new String(ip.deleteCharAt(ip.length() - 1));
	}

	private String createSting(int[] parts) {
		StringBuilder ip = new StringBuilder();
		for (int i = 0, len = 4; i < len; ++i) {
			ip.append(parts[i] + ".");
		}
		return new String(ip.deleteCharAt(ip.length() - 1));
	}

	public String increment(int index) {
		ipParts[index - 1]++;
		subnet = new SubnetUtils(this.toString(), IPAddress.NETMASK);
		return this.toString();
	}

	public String increment() {
		ipParts[3]++;
		subnet = new SubnetUtils(this.toString(), IPAddress.NETMASK);

		return this.toString();
	}

	public String decrement() {
		if (ipParts[3] > 0)
			ipParts[3]--;
		subnet = new SubnetUtils(this.toString(), IPAddress.NETMASK);

		return this.toString();
	}

	public void set(int index, int value) {

		ipParts[index - 1] = value;
		subnet = new SubnetUtils(this.toString(), IPAddress.NETMASK);

	}

	public String getNetwork() {
		return subnet.getInfo().getNetworkAddress();
	}

	public String getBroadcastAddress() {
		return subnet.getInfo().getBroadcastAddress();
	}

	public boolean isInRange(String ip) {
		return subnet.getInfo().isInRange(ip);
	}

	public String getBrodcast() {
		int[] brodcatParts = ipParts;
		brodcatParts[3] = broadcast;
		return createSting(brodcatParts);

	}

	/**
	 * TCP/IP Address Utility Class
	 *
	 * @author gkspencer
	 */
	/**
	 * Check if the specified address is within the required subnet
	 * 
	 * @param ipaddr
	 *            String
	 * @param subnet
	 *            String
	 * @param mask
	 *            String
	 * @return boolean
	 */
	public final static boolean isInSubnet(String ipaddr, String subnet,
			String mask) {

		// Convert the addresses to integer values

		int ipaddrInt = parseNumericAddress(ipaddr);
		if (ipaddrInt == 0)
			return false;

		int subnetInt = parseNumericAddress(subnet);
		if (subnetInt == 0)
			return false;

		int maskInt = parseNumericAddress(mask);
		if (maskInt == 0)
			return false;

		// Check if the address is part of the subnet

		if ((ipaddrInt & maskInt) == subnetInt)
			return true;
		return false;
	}

	/**
	 * Check if the specified address is a valid numeric TCP/IP address and
	 * return as an integer value
	 * 
	 * @param ipaddr
	 *            String
	 * @return int
	 */
	public final static int parseNumericAddress(String ipaddr) {

		// Check if the string is valid

		if (ipaddr == null || ipaddr.length() < 7 || ipaddr.length() > 15)
			return 0;

		// Check the address string, should be n.n.n.n format

		StringTokenizer token = new StringTokenizer(ipaddr, ".");
		if (token.countTokens() != 4)
			return 0;

		int ipInt = 0;

		while (token.hasMoreTokens()) {

			// Get the current token and convert to an integer value

			String ipNum = token.nextToken();

			try {

				// Validate the current address part

				int ipVal = Integer.valueOf(ipNum).intValue();
				if (ipVal < 0 || ipVal > 255)
					return 0;

				// Add to the integer address

				ipInt = (ipInt << 8) + ipVal;
			} catch (NumberFormatException ex) {
				return 0;
			}
		}

		// Return the integer address

		return ipInt;
	}

}
