package pl.edu.pk.iti.copperAnt.simulation.generators.factory;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Binomial;
import cern.jet.random.HyperGeometric;
import cern.jet.random.Poisson;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;

public class DiscreteDistributionFactory implements DistributionFactory {

	public AbstractDistribution create(String name, double arg0) {
		return DiscreteDistributionFactory.createStatic(name, arg0);
	}

	public static AbstractDistribution createStatic(String name, double arg0) {
		RandomEngine generator = new MersenneTwister();

		int arg0Int = (int) arg0;
		switch (name) {
		case "Binomial":
			return new Binomial(arg0Int, 0.5, generator);

		case "HyperGeometric":
			return new HyperGeometric(arg0Int, arg0Int / 2, arg0Int / 3,
					generator);

		case "Poisson":
			return new Poisson(arg0 * 1.0, generator);

		default:
			return new Poisson(arg0 * 1.0, generator);

		}
	}
}
