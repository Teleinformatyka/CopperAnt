package pl.edu.pk.iti.copperAnt;

import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.PackageType;

public class TestUtils {

	public static boolean checkExpectedParametersOfPackage(Package pack,
			String sourceMac, String sourceIp, String destinationMac,
			String destinationIp, PackageType packageType) {
		return packageType.equals(pack.getType())//
				&& destinationMac.equals(pack.getDestinationMAC())//
				&& destinationIp.equals(pack.getDestinationIP())//
				&& sourceMac.equals(pack.getSourceMAC())//
				&& sourceIp.equals(pack.getSourceIP());
	}

	public static boolean checkExpectedParametersOfPackage(Package pack,
			String sourceIp, String destinationIp, PackageType packageType) {
		return packageType.equals(pack.getType())//
				&& destinationIp.equals(pack.getDestinationIP())//
				&& sourceIp.equals(pack.getSourceIP());
	}

}
