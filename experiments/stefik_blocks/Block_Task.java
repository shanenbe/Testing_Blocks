package experiments.stefik_blocks;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Block_Task {

	public final Block block;
	public final Map<String, Object> treatmentCombination = new HashMap<String, Object>();
	final String correctAnwer;
	final boolean requiresUniqueAnswer;
	final List<Character> answers = new Vector<Character>();
	final List<Long> responseTimes = new Vector<Long>();
	public String textAfterTask;


	public Block_Task(Block codeSnippet, String correctAnwer, boolean requiresUniqueAnswer) {
		super();
		this.block = codeSnippet;
		this.correctAnwer = correctAnwer;
		this.requiresUniqueAnswer = requiresUniqueAnswer;
		this.textAfterTask = codeSnippet +
				"\n\nThe correct answer was: " + correctAnwer +
				"\n\nPress [Return] for the next task ([ESC] stops the experiment).";
	}

	public Block_Task(Block codeSnippet, String correctAnwer, boolean requiresUniqueAnswer, String textAfterTask) {
		this(codeSnippet, correctAnwer, requiresUniqueAnswer);
		this.textAfterTask = textAfterTask;
	}

	public Block_Task(Block codeSnippet, String correctAnwer) {
		this(codeSnippet, correctAnwer, false);
	}

	
	synchronized public void doTask(
										Block_TestUI testUI,
										Supplier<Consumer<KeyEvent>> startUpCode_EndTaskCodeCreation
	) throws Exception {
		
		Consumer<KeyEvent> event_command = startUpCode_EndTaskCodeCreation.get();		

		testUI.keyListeners.registerObserver(event_command);
		testUI.show_block(block);
		wait();
		testUI.keyListeners.unregisterObserver(event_command);


		event_command = (KeyEvent k) -> {this.switchToNext(k, ()->{});};
		testUI.show_block_with_string(block, "Press [RETURN] for next task. The correct answer was " + correctAnwer);
		testUI.keyListeners.registerObserver(event_command);
		wait();
		testUI.keyListeners.unregisterObserver(event_command);
		
	}
	
	
	public synchronized void getResponse(KeyEvent k, Runnable responseCommand) {
		k.consume();
		responseCommand.run();

		if (!requiresUniqueAnswer || (correctAnwer.equals(String.valueOf(k.getKeyChar())))) {
				this.notifyAll();
		}
	}
	
	public synchronized void switchToNext(KeyEvent k, Runnable xxx) {
		if (k.getKeyCode() == KeyEvent.VK_ENTER)
			this.notifyAll();
		else if (k.getKeyCode()==KeyEvent.VK_ESCAPE)
			System.exit(-1);
	}
	

	
	
	
	

}
