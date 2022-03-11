package msc.ga;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Math.exp;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.IntStream;

import javax.swing.text.html.HTMLDocument.Iterator;

import com.opencsv.CSVWriter;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import static io.jenetics.engine.Limits.bySteadyFitness;

import io.jenetics.Alterer;
import io.jenetics.CombineAlterer;
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
import io.jenetics.engine.Limits;
import io.jenetics.engine.Problem;
import io.jenetics.util.DoubleRange;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;


public class Genetics_4 {
	public static final String filePath_population = ".//population.csv"; 
	public static final String filePath_phenotype = ".//phenotype.csv"; 
	private static Alterer alterer = null;
	
	static final Problem<double[], DoubleGene, Double>
	PROBLEM = Problem.of(
			Genetics_4::fitness,
			Codecs.ofVector(DoubleRange.of(-4, 4), 2)
	);

	public static void init(int sel) {
		final RandomGenerator random = RandomRegistry.random();
		final int length = 8;
		final double ps = 0.5;
		initCSVPhenotype();
		final int[] indexes = IntStream.range(0, length)
				.filter(i -> random.nextDouble() < ps)
				.toArray();
		alterer = new CombineAlterer<DoubleGene, Double>(
				(g1, g2) -> g1.newInstance(g1.doubleValue()/g2.doubleValue()));
	}
	
	// The fitness function.
	private static double fitness(final double [] x) {

		return sin(5/(2*PI)*x[0])*sin(5/(2*PI)*x[1])*exp(-(x[0]+x[1])/20);

	}


	public static void writeCSVPopulation( EvolutionResult<DoubleGene,Double> result)
	{
	  
	    // first create file object for file placed at location
	    // specified by filepath
	    File file = new File(filePath_population);
	  
	    try {
	        // create FileWriter object with file as parameter
	        FileWriter outputfile = new FileWriter(file);
	  
	        // create CSVWriter object filewriter object as parameter
	        CSVWriter writer = new CSVWriter(outputfile);
	        
			ISeq<Phenotype<DoubleGene, Double>> seq = result.population();
			//System.out.println("size of the population : " + seq.length());
			java.util.Iterator<Phenotype<DoubleGene, Double>> it = seq.iterator();
			//List<String[]> line = new ArrayList<String[]>();
			//String[] line = new String[]();
			System.out.print("<");
			int i = 0;
			String str = "";
			while(it.hasNext()) {
				double [] x = it.next().genotype().chromosome().as(DoubleChromosome.class).toArray();
				double f = it.next().fitness();
				String  str1= String.format("%,.010f %,.010f %,.010f ,", x[0],x[1],f);
				str += str1;
				System.out.print("-" + f + "-");
			}  
			String [] line = str.split(",");
			//line.add(stra);
			System.out.println(">");
			//System.out.println(str);
	        writer.writeNext(line);
	  
	        // closing writer connection
	        writer.close();
	    }
	    catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}

	public static void writeCSVPhenotype(Phenotype<DoubleGene,Double> best,int gen )
	{
	  
	    // first create file object for file placed at location
	    // specified by filepath
	    File file = new File(filePath_phenotype);
	  
	    try {
	        // create FileWriter object with file as parameter
	        FileWriter outputfile = new FileWriter(file,true);
	  
	        // create CSVWriter object filewriter object as parameter
	        CSVWriter writer = new CSVWriter(outputfile);
	        
			System.out.print("<");
			int i = 0;

			double [] x = best.genotype().chromosome().as(DoubleChromosome.class).toArray();
			double f = best.fitness();
			String  str1= String.format(" %d , %,.010f , %,.010f , %,.010f ", gen, x[0],x[1],f);
			System.out.print("-" + str1 + "-");
  
			String [] line = str1.split(",");
			System.out.println(">");
	        writer.writeNext(line);
	  
	        // closing writer connection
	        writer.close();
	    }
	    catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}

	public static void initCSVPhenotype()
	{
	  
	    // first create file object for file placed at location
	    // specified by filepath
	    File file = new File(filePath_phenotype);
	  
	    try {
	        // create FileWriter object with file as parameter
	        FileWriter outputfile = new FileWriter(file);
	  
	        // create CSVWriter object filewriter object as parameter
	        CSVWriter writer = new CSVWriter(outputfile);
	        
	        String[] data2 = { "Generation", "X", "Y","Fitness" };
	        writer.writeNext(data2);
	  
	        // closing writer connection
	        writer.close();
	    }
	    catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}

	
	static Phenotype<DoubleGene,Double> best = null;
	@SuppressWarnings("unused")
	private static void update(
			final EvolutionResult<DoubleGene,Double> result
			) 
	{

		int gen = (int) result.generation();
		if(best == null ||
				best.compareTo(result.bestPhenotype()) > 0)
		{
			best = result.bestPhenotype();
			System.out.print(result.generation() + ": ");
			System.out.println("Found best phenotype: " + best);

		}
		writeCSVPhenotype(best,gen);
		//writeCSVData(result);
		//System.out.println(gen + ": ");
		//System.out.println("Fount phenotype: " + result.bestPhenotype());
	}
	
	static final Genotype<DoubleGene> genotype = Genotype.of(
			DoubleChromosome.of(-4,4,1),
			DoubleChromosome.of(-4,4,1)
			);
	static final Codec<double [], DoubleGene> codec = Codec.of(
			genotype,gt -> gt.chromosome().as(DoubleChromosome.class).toArray());


	
	
      
	public static void main(final String[] args) {

		init(1);
		final Engine<DoubleGene, Double> engine = 
				Engine.builder(PROBLEM)
				//.builder(Genetics_4::fitness,codec)
				.populationSize(100)
				.optimize(Optimize.MINIMUM)
				.alterers(
						new Mutator<>(0.03),
						new MeanAlterer<>(0.5))
				.survivorsSelector(new RouletteWheelSelector<>())
				.offspringSelector(new TournamentSelector<>())
				.build();
		
		final double[] result = codec.decode(
				engine.stream()
				.limit(Limits.bySteadyFitness(100))
				.peek(Genetics_4::update)
				//.limit(300)
				.collect(EvolutionResult.toBestGenotype())
				);
		//System.out.println(statistics);
		//System.out.println(best);
		System.out.println(result.toString());


	}

}