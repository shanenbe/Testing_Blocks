package experiments.stefik_blocks;

import javax.swing.*;
import java.awt.*;

public class Block_DrawJPanel extends JPanel {

	String text = null;

	public Block_DrawJPanel(LayoutManager layout, boolean isDoubleBuffered, Block a_block) {
		super(layout, isDoubleBuffered);
		this.a_block = a_block;
	}

	public Block_DrawJPanel(LayoutManager layout, Block a_block) {
		super(layout);
		this.a_block = a_block;
	}

	public Block_DrawJPanel(boolean isDoubleBuffered, Block a_block) {
		super(isDoubleBuffered);
		this.a_block = a_block;
	}

	Block a_block;

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (a_block != null) {
			a_block.draw_on(g);
		}

		g.setColor(Color.black);
		if (text!=null) {
			if(a_block!=null)
				g.drawString(text, 0, a_block.recBlock.height() + 20);
			else
				g.drawString(text, 0, 20);
		}
	}

	public void set_block(Block aBlock) {
		a_block = aBlock;
		this.setVisible(true);
		this.updateUI();
	}
}
