package us.ridiculousbakery.espressoexpress.Model;

/**
 * Created by teddywyly on 6/15/15.
 */
public class PickupPhase {

    private long targetTime;
    private String task;
    private String actionTask;

    public PickupPhase(long targetTime, String task, String actionTask) {
        this.targetTime = targetTime;
        this.task = task;
        this.actionTask = actionTask;
    }

    public long getTargetTime() {
        return targetTime;
    }

    public String getTask() {
        return task;
    }

    public String getActionTask() {
        return actionTask;
    }
}
