package experiments.stefik_blocks;

import java.util.Arrays;
import java.util.List;

public class Block_Main {
	
	public final Block_TestUI testui = new Block_TestUI();
	{
		int newSeed=-1;
		if((newSeed=testui.seed())!=-1) {
			System.out.println("You added your own seed: " + newSeed);
			Globals.setSeed(newSeed);
		}
	}
	
	public final String username = testui.userName();

	public void runExperiment(String welcomeText, List<Block_Task> taskList) throws Exception {

		Block_ExperimentExecution run = createExperimentExecution(taskList);
		
		run.startAllTasks();

	}

	public Block_ExperimentExecution createExperimentExecution(List<Block_Task> taskList, String ... parameters ) throws Exception {
		return new Block_ExperimentExecution(
				testui,
				taskList,
				username,
				parameters
		);
	}

}
