package experiments.stefik_blocks;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class SubjectObserver<CommandType> {
	
	public final Set<CommandType> callbacks = new HashSet<CommandType>();
	
	public void registerObserver(CommandType c) {
		callbacks.add(c);
	}
	
	public  void unregisterObserver(CommandType c) {
		callbacks.remove(c);
	}

	public  void notifyObservers(Consumer<CommandType> subjectCommand) {
		for (CommandType c : callbacks) {
			subjectCommand.accept(c);
		}
	}

	
	
}
