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
import io.jenetics.engine.Codec;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;


public class Genetics_5 {

	// The fitness function.
	private static double fitness(final Genotype<DoubleGene> gt) {
		final double [] x = gt.chromosome().as(DoubleChromosome.class).toArray();
		return sin(5/(2*PI)*x[0])*sin(5/(2*PI)*x[1])*exp(-(x[0]+x[1])/20);
	}

	static Phenotype<DoubleGene,Double> best = null;
	@SuppressWarnings("unused")
	private static void update(
			final EvolutionResult<DoubleGene,Double> result
			) {
		if(best == null ||
				best.compareTo(result.bestPhenotype()) < 0)
		{
			best = result.bestPhenotype();
			System.out.print(result.generation() + ": ");
			System.out.println("Fount best phenotype: " + best);
		}
	}
	
	static final Genotype<DoubleGene> genotype = Genotype.of(
			DoubleChromosome.of(-5,5,1),
			DoubleChromosome.of(-5,5,1)
			);
	static final Codec<double [], DoubleGene> codec = Codec.of(
			genotype,gt -> gt.chromosome().as(DoubleChromosome.class).toArray());

	static final Engine<DoubleGene, Double> engine = Engine
			.builder(Genetics_5::fitness,genotype)
			.populationSize(500)
			.optimize(Optimize.MINIMUM)
			.alterers(
					new Mutator<>(0.03),
					new MeanAlterer<>(0.6))
			.build();
	
	
/*      
	public static void main(final String[] args) {
		
		Phenotype<DoubleGene, Double> x = engine.stream()
		//.peek(Genetics_5::update)
		.limit(10)
		.collect(EvolutionResult.toBestPhenotype()
		);
*/		
/*	
		final double [] result = codec.decode(
				engine.stream()
				.peek(Genetics_5::update)
				.limit(10)
				.collect(EvolutionResult.toBestGenotype())
				);
*/
		//System.out.println(statistics);
		//System.out.println(best);
//		System.out.println(x);


//	}
}