package ro.ace.dnad.server.model;

/**
 * Created by Paul on 22-Jun-15.
 */
public class Task {

    private long mTaskId;
    private int mTaskComplexity;
    private long mStartInterval;
    private long mEndInterval;

    public Task(long mTaskId) {
        this.mTaskId = mTaskId;
    }

    public Task(long mTaskId, int mTaskComplexity, long mStartInterval, long mEndInterval) {
        this.mTaskId = mTaskId;
        this.mTaskComplexity = mTaskComplexity;
        this.mStartInterval = mStartInterval;
        this.mEndInterval = mEndInterval;
    }

    public long getTaskId() {
        return mTaskId;
    }

    public int getTaskComplexity() {
        return mTaskComplexity;
    }

    public long getStartInterval() {
        return mStartInterval;
    }

    public long getEndInterval() {
        return mEndInterval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (mTaskId != task.mTaskId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (mTaskId ^ (mTaskId >>> 32));
    }
}
