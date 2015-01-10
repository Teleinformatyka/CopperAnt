package pl.edu.pk.iti.copperAnt.simulation.generators.factory;

import cern.jet.random.AbstractContinousDistribution;
import cern.jet.random.Exponential;
import cern.jet.random.Normal;
import cern.jet.random.StudentT;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;

public class ContinousDistributionFactory implements DistributionFactory {

	public AbstractContinousDistribution create(String name, double arg0) {
		RandomEngine generator = new MersenneTwister();

		switch (name) {
		case "Exponential":
			return new Exponential(arg0, generator);

		case "Normal":
			return new Normal(arg0, 5, generator);

		case "StudentT":
			return new StudentT(arg0, generator);

		default:
			return new Exponential(arg0, generator);

		}

	}

	public AbstractContinousDistribution create(String name, int arg0) {
		RandomEngine generator = new MersenneTwister();

		switch (name) {
		case "Exponential":
			return new Exponential(arg0 * 1.0, generator);

		case "Normal":
			return new Normal(arg0 * 1.0, 5, generator);

		case "StudentT":
			return new StudentT(arg0 * 1., generator);

		default:
			return new Exponential(arg0 * 1., generator);

		}

	}
}
