package experiments.stefik_blocks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

public class Block_TestUI {

	public JFrame frame;
	Block_DrawJPanel labelsPanel;

	public final SubjectObserver<Consumer<KeyEvent>> keyListeners = new SubjectObserver<Consumer<KeyEvent>>();
	
	public void show_block(Block aBlock) {
		labelsPanel.text = null;
		labelsPanel.set_block(aBlock);
	}

	public void show_block_with_string(Block aBlock, String aString) {
		labelsPanel.text = aString;
		labelsPanel.set_block(aBlock);
	}
	
	public void createAndShowFrame(String introductionText) {
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			frame.setMinimumSize(frame.getMaximumSize());
	        frame.setBackground(Color.white);

			labelsPanel = new Block_DrawJPanel(new BorderLayout(), new Block());
			labelsPanel.text = introductionText;
	        labelsPanel.setBackground(Color.white);
	        frame.add(labelsPanel);
	        labelsPanel.updateUI();
	        frame.pack();
			frame.setResizable(false);

			frame.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
				}
				
				@Override
				public void keyReleased(KeyEvent e) {
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					keyListeners.notifyObservers((subjectCommand)->subjectCommand.accept(e));
				}
			});
	        

			frame.setVisible(true);

	}
	
	public String userName() {
        String result = (String)JOptionPane.showInputDialog(
                frame,
                "Your username (where the data is stored)", 
                "Username",            
                JOptionPane.PLAIN_MESSAGE,
                null,            
                null, 
                "DummyUser"
             );
       return result;
	}

	public int seed() {
        String result = (String)JOptionPane.showInputDialog(
                frame,
                "What seed (positive number) should be used?\n"
                + "Just press return if you don't want your own seed.\n"
                + "\"X\" just prints out the structure of the experiment", 
                "Seed",            
                JOptionPane.PLAIN_MESSAGE,
                null,  
                null,
                ""
             );
        if (result==null) System.exit(-1);
        
        if(result.equals("X")) {
        	Globals.currentAction = Globals.action.valueOf("printoutStructure");
        	return -1;
        }
       return result.equals("")?
    		   		-1:
    		   		Integer.valueOf(result);
	}

}
