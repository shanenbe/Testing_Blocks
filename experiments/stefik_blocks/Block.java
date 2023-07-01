package experiments.stefik_blocks;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Block {

    static List<Color> colors = Arrays.asList(new Color[]{Color.orange, Color.cyan, Color.yellow, Color.red, Color.green, Color.gray});

    static int LINE_HEIGHT = 20;
    static int INDENTATION_WIDTH = 20;

    RecBlock recBlock;

    int position_of_marked_element;
    List<Integer> indentations = new ArrayList<Integer>();

    boolean has_color = false;
    boolean surrounds_children = false;

    public Block() {
        this.indentations = new ArrayList<>();
        this.position_of_marked_element = 0;
    }

    public Block(List<Integer> indentation, int position_of_marked_element) {
        this();
        this.indentations = indentation;
        this.position_of_marked_element = position_of_marked_element;
    }

    public void createRecBlock() {

        recBlock = new RecBlock();
        RecBlock currentRecBlock = recBlock;
        int line = 1;

        int currentLevel = 1;

        for (int indentation : indentations) {

            RecBlock new_block = new RecBlock();
            new_block.line = line;
            if (indentation == currentLevel) {
                currentRecBlock.children.add(new_block);
                new_block.parent = currentRecBlock;
                currentRecBlock = new_block;
                currentLevel++;
            } else if (indentation < currentLevel) {
                while (indentation < currentLevel) {
                    currentRecBlock = currentRecBlock.parent;
                    currentLevel--;
                }
                currentRecBlock.children.add(new_block);
                new_block.parent = currentRecBlock;
                currentRecBlock = new_block;
                currentLevel++;
            } else if (indentation > currentLevel) {
                throw new RuntimeException("Something is wrong");
            }
            line ++;
        }
        recBlock.init();
    }


    /**
     * Generate a random statement with given loc and depth. A statement is just a recursive data-structure - not really a statement ;)
     */
    static Block generate_random_block ( boolean has_color, boolean surrounds_children, int loc, int depth, int position_of_marked_element){

        if (depth > loc)
            throw new RuntimeException("LOC must be <= depth:  depth=" + depth + " loc=" + loc + ".....but loc<=depth should hold");
        if (depth <= 0) throw new RuntimeException("Depth must be at least 1");

        List<Integer> indentation = new ArrayList<Integer>();
        Block ret = new Block(indentation, position_of_marked_element);

        // The first lines are the required depth
        for (int default_indentation = 1; default_indentation <= depth; default_indentation++)
            indentation.add(default_indentation);

        int current_size = depth;

        // Now generate additional lines
        for (int to_insert = 1; to_insert <= loc - depth; to_insert++) {
            int insert_pos = Globals.random.nextInt(current_size + 1);
            int insert_indentation;

            if (insert_pos == 0) {
                insert_indentation = 1;
            } else if (insert_pos == current_size) {
                insert_indentation = Globals.random.nextInt(Math.min(indentation.get(current_size - 1) + 2, depth + 1));
                insert_indentation = Math.max(1, insert_indentation);
            } else {
                int min;
                int max;
                if (indentation.get(insert_pos - 1) > indentation.get(insert_pos)) {
                    min = Math.max(indentation.get(insert_pos) - 1, 1);
                    max = Math.min(indentation.get(insert_pos - 1) + 1, depth);
                } else if (indentation.get(insert_pos - 1) == (indentation.get(insert_pos) - 1)) {
                    min = indentation.get(insert_pos - 1);
                    max = indentation.get(insert_pos);
                } else if (indentation.get(insert_pos - 1) == (indentation.get(insert_pos))) {
                    min = Math.max(indentation.get(insert_pos) - 1, 1);
                    max = Math.min(indentation.get(insert_pos) + 1, depth);
                } else {
                    min = indentation.get(insert_pos) - 1;
                    max = indentation.get(insert_pos) + 1;
                }

                int bound = max - min + 1;
                int random = Globals.random.nextInt(bound);
                insert_indentation = min + random;
            }

            indentation.add(insert_pos, insert_indentation);
            current_size++;
        }
        ret.surrounds_children = surrounds_children;
        ret.has_color = has_color;
        ret.createRecBlock();

        return ret;

    }


    void doPrint () {
        for (int num = 0; num < indentations.size(); num++) {
            System.out.println(num + "." + " ".repeat(indentations.get(num) * 4) + "" + indentations.get(num));
        }
    }

    public void set_any_position_of_depth ( int position_in_depth){
        List<Integer> positions = new ArrayList<>();

        int line = 1;
        for (int an_indentation : indentations) {

            if (an_indentation == position_in_depth)
                positions.add(line);

            line++;
        }
        if(positions.size()==0) throw new RuntimeException("No combination found");
        Collections.shuffle(positions);

        position_of_marked_element = positions.get(0);

    }

    public void draw_on(Graphics g){
        doPrint();
        if(recBlock!=null)
            recBlock.draw_on(g);
    }

    class RecBlock {

        public RecBlock parent=null;
        public ArrayList<RecBlock> children = new ArrayList<RecBlock>();

        public Color color = null;
        public int line;

        public void init() {
            for(RecBlock child: children) {
                child.init();
            }

            if(children.size()==0) {
                color = colors.get(colors.size() - 1);
                return;
            }


            if(has_color) {
                    color = colors.get(Globals.random.nextInt(colors.size()-1));
            } else {
                color = Color.lightGray;
            }
        }

        public void draw_on (Graphics g){
            this.draw_on_height(g, 0);
        }

        public int draw_on_height(Graphics g, int y_pos) {
            int indentation = this.ind_level();
            int offset = y_pos + Block.LINE_HEIGHT;

            g.setColor(color);
            g.fillRect(indentation * INDENTATION_WIDTH, y_pos, width(), height());

            g.setColor(Color.black);
            g.drawRect(indentation * INDENTATION_WIDTH, y_pos, width(), height());

            for(RecBlock child: children) {
                child.draw_on_height(g, offset);
                offset = offset + child.height();
            }

            if (position_of_marked_element == line) {
                g.setColor(Color.white);
                g.fillRect(indentation * INDENTATION_WIDTH + width() - INDENTATION_WIDTH, y_pos+2,
                        INDENTATION_WIDTH - 4,
                        INDENTATION_WIDTH - 4);
                g.setColor(Color.black);
                g.drawRect(indentation * INDENTATION_WIDTH + width() - INDENTATION_WIDTH, y_pos+2,
                        INDENTATION_WIDTH - 4,
                        INDENTATION_WIDTH - 4);

                g.drawLine(indentation * INDENTATION_WIDTH + width() - INDENTATION_WIDTH, y_pos+2,
                        indentation * INDENTATION_WIDTH + width() - INDENTATION_WIDTH + INDENTATION_WIDTH - 4,
                        y_pos+2 + INDENTATION_WIDTH - 4);

                g.drawLine(indentation * INDENTATION_WIDTH + width() - INDENTATION_WIDTH + INDENTATION_WIDTH - 4,
                        y_pos+2,
                        indentation * INDENTATION_WIDTH + width() - INDENTATION_WIDTH,
                        y_pos+2 + INDENTATION_WIDTH - 4);
            }

            return offset + children.size()>0? 4: 0;
        }

        int height() {
            int ret = Block.LINE_HEIGHT;

            for(RecBlock child: children) {
                ret = ret + child.height();
            }

            return ret + (children.size()>0? 4: 0);
        }

        int width() {
            if(children.size()==0)
                return 3 * Block.INDENTATION_WIDTH;

            if (!surrounds_children) {
                return 2 * Block.INDENTATION_WIDTH;
            }

            int width = Block.INDENTATION_WIDTH + 6;

            for(RecBlock child: children) {
                int child_with = child.width();
                if(child_with + Block.INDENTATION_WIDTH + 6> width) {
                    width = child_with + Block.INDENTATION_WIDTH + 6;
                }
            }

            return width;
        }

        int ind_level() {
            if(parent==null)
                return 0;
            else
                return 1 + parent.ind_level();
        }

    }
}

