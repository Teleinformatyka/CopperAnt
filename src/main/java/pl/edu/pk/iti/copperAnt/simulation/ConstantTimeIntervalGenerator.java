package pl.edu.pk.iti.copperAnt.simulation;

public class ConstantTimeIntervalGenerator implements TimeIntervalGenerator {

	public static final int DEFAULT_INTERVAL = 10;
	private final int constantInterval;

	public ConstantTimeIntervalGenerator() {
		this(DEFAULT_INTERVAL);
	}

	public ConstantTimeIntervalGenerator(int interval) {
		this.constantInterval = interval;
	}

	@Override
	public int getTimeInterval() {
		return constantInterval;
	}

}
