package experiments.stefik_blocks;

import java.util.Random;

public class Globals {
	
	public static final boolean debug = true;
	
	public static long seed = 123456789;
	public static Random random = new Random(seed);
	
	public static void setSeed(long newSeed) {
		System.out.println("new seed: " + newSeed);
		seed = newSeed;
		random = new Random(seed);
	}
	
	public static enum action {doExperiment, printoutStructure};
	public static action currentAction = action.valueOf("doExperiment");
	
}	
