package experiments.stefik_blocks;

import java.util.*;

public class stefik_neighbors_with_same_color extends Block_Main{

    public static final int repetitions = 30;
    public static final int LOC = 25;
    public static final int DEPTH = 9;



    public static void main(String[] args) throws Exception  {
        new stefik_neighbors_with_same_color().main();
    }


    private void generate_task_with_all_treatments(List<Block_Task> tasks) {

        for(int depth = 3; depth <= 6; depth++) {

            Block a_block = Block.generate_random_block(true, false, false, LOC, DEPTH, 1);
            a_block.set_any_position_of_depth(depth);
            Block_Task task = new Block_Task(a_block, "" + depth);
            task.treatmentCombination.put("depth", depth);
            task.treatmentCombination.put("correctAnswer", "" + depth);
            task.treatmentCombination.put("hasColor", "" + true);
            task.treatmentCombination.put("surroundsChildren", "" + false);
            task.treatmentCombination.put("similarColor", "" + false);
            tasks.add(task);

            a_block = Block.generate_random_block(true, false, true, LOC, DEPTH, 1);
            a_block.set_any_position_of_depth(depth);
            a_block.has_similar_color = true;
            task = new Block_Task(a_block, "" + depth);
            task.treatmentCombination.put("depth", depth);
            task.treatmentCombination.put("correctAnswer", "" + depth);
            task.treatmentCombination.put("hasColor", "" + true);
            task.treatmentCombination.put("surroundsChildren", "" + false);
            task.treatmentCombination.put("similarColor", "" + true);            
            tasks.add(task);


        }
    }


    public void main() throws Exception {
        System.out.println("current seed: " + Globals.seed);

        List<Block_Task> tasks = new Vector<>();

        for(int repetion_no = 1; repetion_no <= repetitions; repetion_no++) {
            generate_task_with_all_treatments(tasks);
        }

        Collections.shuffle(tasks, Globals.random);

        Block_ExperimentExecution run = this.createExperimentExecution(
                tasks,
                "depth", "hasColor", "surroundsChildren", "similarColor"
        );

        run.startAllTasks();

    }
}


