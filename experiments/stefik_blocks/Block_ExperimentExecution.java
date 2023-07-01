package experiments.stefik_blocks;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Block_ExperimentExecution {
	public final Block_Task welcome;
	public final List<Block_Task> tasks;
	public final List<Long> resultsInMS = new Vector<Long>();
	public final Block_TestUI testUI;
	public final Block_CSVWriter writer;

	public int currentTask = 0;

	public Block_ExperimentExecution(
			Block_TestUI 		testUI,
			List<Block_Task> 	tasks,
			String 		fileName,
			String[] 	csvHeaders) throws Exception
	{
		this(testUI, tasks, fileName, Arrays.asList(csvHeaders));
	}

	public Block_ExperimentExecution(
				Block_TestUI testUI,
				List<Block_Task> tasks,
				String fileName,
				List<String> csvHeaders) throws Exception 
	{
		super();
		this.welcome = new Block_Task(new Block(), "\n", true);
		this.testUI = testUI;
		this.tasks = tasks;
		this.writer = new Block_CSVWriter(fileName, csvHeaders);
	}

	synchronized public void startAllTasks() throws Exception 
	{
		
		if(Globals.currentAction==Globals.action.valueOf("printoutStructure")) {
			this.printCSVWithoutReactionTime();
			System.exit(-1);
		}

		testUI.createAndShowFrame("This is an experiment - and this is just a dummy text");

		welcome.doTask(testUI, welcome_Command(welcome));

		while (currentTask < tasks.size()) {
			Supplier<Consumer<KeyEvent>> startUpCode_EndTaskCodeCreation = 
					task_Command(tasks.get(currentTask));
			
			testUI.frame.setTitle("Task: " + (currentTask + 1) + " / " + tasks.size());
			tasks.get(currentTask).doTask(testUI, startUpCode_EndTaskCodeCreation);
			currentTask++;
		}

		testUI.frame.dispose();
	}
	
	
	/** You probably wanna measure time in your task. 
		This method creates a function that
			1. reacts on a response
			2. the performs time measurements
	 * @param task */
	private Supplier<Consumer<KeyEvent>> task_Command(Block_Task task) {
		Supplier<Consumer<KeyEvent>> startUpCode_EndTaskCodeCreation = () -> {
			final Block_Measurement measurement = new Block_Measurement();
			
			return (KeyEvent e) -> {
				if (e.getKeyChar()>='1' && e.getKeyChar()<='9')
					task.getResponse(e, ()->measurement.doMeasurement(task, writer, e.getKeyChar()));
			};

		};
		return startUpCode_EndTaskCodeCreation;
	}
	
	private Supplier<Consumer<KeyEvent>> welcome_Command(Block_Task task) {
		Supplier<Consumer<KeyEvent>> startUpCode_EndTaskCodeCreation = () -> {
			final Block_Measurement measurement = new Block_Measurement();
			return (KeyEvent e) -> task.getResponse(e, ()->{});

		};

		return startUpCode_EndTaskCodeCreation;
		
	}
	public void printCSVWithoutReactionTime() throws Exception {
		writer.writeCSVHeader();
		tasks.forEach((task)->writer.writeResultTemplate(task));
	}
}
