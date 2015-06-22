package ro.ace.dnad.server;

import ro.ace.dnad.server.model.Task;
import ro.ace.dnad.server.utils.Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskHandler {

    private static TaskHandler ourInstance;

    public static TaskHandler getInstance() {
        if (ourInstance == null) {
            ourInstance = new TaskHandler();
        }
        return ourInstance;
    }

    public static final String ORIGINAL_HASH = "ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f";

    private static final long MAX_INTERVAL = 99999999;
    private static final long MIN_INTERVAL = 10000000;

    private static final String INSTRUCTION_ORIGINAL_HASH = "originalHash";
    private static final String INSTRUCTION_MIN_INTERVAL = "minInterval";
    private static final String INSTRUCTION_MAX_INTERVAL = "maxInterval";

    private Random mRand = new Random();
    private final ArrayList<Task> mTasks = new ArrayList<>();

    private TaskHandler() {
        startTaskGeneration();
    }

    private void startTaskGeneration() {
        long taskId = 0;
        long prevEnd = MIN_INTERVAL - 1;
        Task task = generateTask(taskId, prevEnd);
        mTasks.add(task);
        while ((prevEnd = task.getEndInterval()) <= MAX_INTERVAL) {
            task = generateTask(++taskId, prevEnd);
            mTasks.add(task);
        }
    }

    private Task generateTask(long taskId, long prevInterval) {
        final long minInterval = prevInterval + 1;
        final long maxInterval = minInterval + (long) (mRand.nextDouble() * (Math.min(prevInterval + 2 * MIN_INTERVAL, MAX_INTERVAL) - minInterval));
        int taskComplexity = (int) ((maxInterval - minInterval) / 899999);
        return new Task(taskId, taskComplexity, minInterval, maxInterval);
    }

    public List<Task> getCurrentTaskList() {
        return mTasks;
    }

    public void clearTaskList() {
        mTasks.clear();
    }

    public String getTaskInstructions(long taskId) {
        Task task = mTasks.get(mTasks.indexOf(new Task(taskId)));
        if (task != null) {
            //get file input stream
            InputStream fileStream = TaskHandler.class.getResourceAsStream("/default.java");
            //convert file to text
            String text = Utils.getStringFromInputStream(fileStream);
            if (text != null) {
                text = text.replaceFirst(INSTRUCTION_ORIGINAL_HASH, ORIGINAL_HASH);
                text = text.replaceFirst(INSTRUCTION_MIN_INTERVAL, String.valueOf(task.getStartInterval()));
                text = text.replaceFirst(INSTRUCTION_MAX_INTERVAL, String.valueOf(task.getEndInterval()));
                return text;
            }
        }
        return null;
    }
}
