package dataModel;

import java.io.Serializable;

public final class SimpleTask extends Task implements Serializable {

    private static final long serialVersionUID = 1L;
    private int startHour;
    private int endHour;

    public SimpleTask(int idTask, String statusTask, int startHour, int endHour) {
        super(idTask, statusTask);
        this.startHour = startHour;
        this.endHour = endHour;
        if(startHour > 24 || endHour > 24 || startHour < 0 || endHour < 0) {
            throw new IllegalArgumentException("Invalid hour specified.");
        }
    }

    @Override
    public int estimateDuration() {
        if(this.getStatusTask().equalsIgnoreCase("Completed")) {
            if (endHour < startHour) {
                return endHour + (24 - startHour);
            } else {
                if (startHour == endHour) {
                    throw new IllegalArgumentException("Invalid hour interval.");
                }
            }
            return endHour - startHour;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "SimpleTask " + getIdTask();
    }

}
