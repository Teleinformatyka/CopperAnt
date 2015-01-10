package pl.edu.pk.iti.copperAnt.simulation;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pl.edu.pk.iti.copperAnt.TestUtils;
import pl.edu.pk.iti.copperAnt.network.Cable;
import pl.edu.pk.iti.copperAnt.network.Computer;
import pl.edu.pk.iti.copperAnt.network.IPAddress;
import pl.edu.pk.iti.copperAnt.network.Package;
import pl.edu.pk.iti.copperAnt.network.PackageType;
import pl.edu.pk.iti.copperAnt.network.Port;
import pl.edu.pk.iti.copperAnt.network.Router;
import pl.edu.pk.iti.copperAnt.network.Switch;

public class MediumNetworkNonUnitScenario {
	final static String ipPrefix = "192.168";
	private List<List<Computer>> networks;
	private Router router;

	@Test
	public void computersCanPingEachViaNetworkWithRouterAndSwitches() {
		// given
		final int numberOfComputersInOneNetwork = 3;
		final int numberOfNetworks = 5;
		prepareNetworks(numberOfNetworks, numberOfComputersInOneNetwork);

		for (int networkIndexFrom = 1; networkIndexFrom <= numberOfNetworks; networkIndexFrom++) {
			for (int networkIndexTo = 1; networkIndexTo <= numberOfNetworks; networkIndexTo++) {
				for (int computerIndexFrom = 1; computerIndexFrom <= numberOfComputersInOneNetwork; computerIndexFrom++) {
					for (int computerIndexTo = 1; computerIndexTo <= numberOfComputersInOneNetwork; computerIndexTo++) {
						if (networkIndexFrom == networkIndexTo
								&& computerIndexFrom == computerIndexTo) {
							continue;
						}
						// when
						sendPing(networkIndexFrom, computerIndexFrom,
								networkIndexTo, computerIndexTo);
						Clock.getInstance().run();
						// then
						ArgumentCaptor<Package> packageCaptor = ArgumentCaptor
								.forClass(Package.class);
						Computer computerFrom = networks.get(networkIndexFrom)
								.get(computerIndexFrom);
						Computer computerTo = networks.get(networkIndexTo).get(
								computerIndexTo);
						verify(computerFrom.getPort(), atLeastOnce())
								.receivePackage(packageCaptor.capture());
						boolean expectedPackageWasPresent = packageCaptor//
								.getAllValues()//
								.stream()
								//
								.anyMatch(
										p -> (TestUtils
												.checkExpectedParametersOfPackage(
														p,//
														computerTo.getIP(),//
														computerFrom.getIP(),//
														PackageType.ECHO_REPLY)));
						if (!expectedPackageWasPresent) {
							packageCaptor.getAllValues().stream()
									.forEach(p -> System.out.println(p));
						}
						assertTrue("Condition not satisfied for "
								+ networkIndexFrom + ", " + computerIndexFrom
								+ ", " + networkIndexTo + ", "
								+ computerIndexTo, expectedPackageWasPresent);
					}
				}

			}
		}

	}

	private void sendPing(int networkIndexA, int computerIndexA,
			int networkIndexB, int computerIndexB) {
		Computer computerA = networks.get(networkIndexA).get(computerIndexA);
		Computer computerB = networks.get(networkIndexB).get(computerIndexB);
		Package pack = new Package();
		pack.setDestinationIP(computerB.getIP());
		computerA.sendPackage(pack);
	}

	private void prepareNetworks(final int numberOfNetworks,
			final int numberOfComputersInOneNetwork) {
		router = new Router(numberOfNetworks + 1);
		networks = new ArrayList<List<Computer>>(numberOfNetworks + 1);
		networks.add(null);// dirty hack to increase size of list
		for (int networkNumber = 1; networkNumber <= numberOfNetworks; networkNumber++) {
			String addressOfRouterPort = ipPrefix + "." + networkNumber + "."
					+ 254;
			router.setIpForPort(networkNumber, new IPAddress(
					addressOfRouterPort));
			Switch _switch = new Switch(numberOfComputersInOneNetwork + 1);
			connectPortsByCable(_switch.getPort(0),
					router.getPort(networkNumber));
			List<Computer> computers = new ArrayList<Computer>(
					numberOfComputersInOneNetwork + 1);
			computers.add(null);// dirty hack to increase size of list
			for (int computerNumber = 1; computerNumber <= numberOfComputersInOneNetwork; computerNumber++) {
				String ipString = ipPrefix + "." + networkNumber + "."
						+ computerNumber;
				Computer computer = new Computer(new IPAddress(ipString));
				computer.setDefaultGateway(new IPAddress(addressOfRouterPort));
				computer.setPort(spy(computer.getPort()));
				computers.add(computerNumber, computer);
				connectPortsByCable(computer.getPort(),
						_switch.getPort(computerNumber));

			}
			networks.add(networkNumber, computers);
		}
	}

	private void connectPortsByCable(Port portA, Port portB) {
		Cable cable = new Cable();
		cable.insertInto(portA);
		cable.insertInto(portB);
	}
}
