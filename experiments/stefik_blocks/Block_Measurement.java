package experiments.stefik_blocks;

public class Block_Measurement {
    public long timestamp = System.currentTimeMillis();
    public long finish;


    public void doMeasurement(Block_Task task, Block_CSVWriter writer, Object response) {
        finish = System.currentTimeMillis() - timestamp;
        writer.writeResults(task, this, response);
    }

}
