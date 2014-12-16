package pl.edu.pk.iti.copperAnt.network;
// ICMP plus other protocols
public enum PackageType {
	/*
	 * 
	 *  DHCP package  haven't source IP and destination mac because it is  sent to all devices in network
	 *  to get IP
	 */
	DHCP, // to get IP; request without content and response with content IP
	
	ECHO_REQUEST, // ping
	ECHO_REPLY,  // pong
	DESTINATION_UNREACHABLE, // without content, send when host is unknown, eg when ttl reach 0
	
	// to get MAC address from IP
	ARP_REQ, // request to resolve IP address to MAC
	
	ARP_REP // response with MAC for IP
	
	
}
