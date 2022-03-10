package msc.ga;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.exp;

import java.util.List;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import static io.jenetics.engine.Limits.bySteadyFitness;

import io.jenetics.DoubleChromosome;
import io.jenetics.DoubleGene;
import io.jenetics.Genotype;
import io.jenetics.MeanAlterer;
import io.jenetics.Mutator;
import io.jenetics.Optimize;
import io.jenetics.Phenotype;
import io.jenetics.RouletteWheelSelector;
import io.jenetics.TournamentSelector;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Problem;
import io.jenetics.util.DoubleRange;


public class Genetics_4 {
	
	static final Problem<double[], DoubleGene, Double>
	PROBLEM = Problem.of(
			Genetics_4::fitness,
			Codecs.ofVector(DoubleRange.of(-4, 4), 2)
	);

	// The fitness function.
	private static double fitness(final double [] x) {
		//return cos(0.5 + sin(x))*cos(x);
		return sin(5/(2*PI)*x[0])*sin(5/(2*PI)*x[1])*exp(-(x[0]+x[1])/20);
		//sin(5/(2*pi)*x).*sin(5/(2*pi)*y).*exp(-(x+y)/20);
	}

	static Phenotype<DoubleGene,Double> best = null;
	@SuppressWarnings("unused")
	private static void update(
			final EvolutionResult<DoubleGene,Double> result
			) 
	{
		int gen = (int) result.generation();
		if(best == null ||
				best.compareTo(result.bestPhenotype()) < 0)
		{
			best = result.bestPhenotype();
			System.out.print(result.generation() + ": ");
			System.out.println("Fount best phenotype: " + best);
		}
		
		System.out.println(gen + ": ");
		System.out.println("Fount phenotype: " + result.bestPhenotype());
	}
	
	static final Genotype<DoubleGene> genotype = Genotype.of(
			DoubleChromosome.of(-5,5,1),
			DoubleChromosome.of(-5,5,1)
			);
	static final Codec<double [], DoubleGene> codec = Codec.of(
			genotype,gt -> gt.chromosome().as(DoubleChromosome.class).toArray());

	static final Engine<DoubleGene, Double> engine = 
			Engine.builder(PROBLEM)
			//.builder(Genetics_4::fitness,codec)
			.populationSize(500)
			.optimize(Optimize.MINIMUM)
			.alterers(
					new Mutator<>(0.03),
					new MeanAlterer<>(0.6))
			.survivorsSelector(new RouletteWheelSelector<>())
			.offspringSelector(new TournamentSelector<>())
			.build();
	
	
      
	public static void main(final String[] args) {

		final double[] result = codec.decode(
				engine.stream()
				.peek(Genetics_4::update)
				.limit(500)
				.collect(EvolutionResult.toBestGenotype())
				);
		//System.out.println(statistics);
		//System.out.println(best);
		System.out.println(result.toString());


	}

}