package dataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ComplexTask extends Task implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Task> tasks;

    public ComplexTask(int idTask) {
        super(idTask, "Uncompleted");
        this.tasks = new ArrayList<>();
    }

    @Override
    public int estimateDuration() {
        int totalDuration = 0;
        for(Task t: tasks) {
            totalDuration += t.estimateDuration();
        }
        return totalDuration;
    }

    public void addTask(Task t) {
        for(Task task : tasks) {
            if(task.getIdTask() == t.getIdTask()) {
                System.out.println("Task ID already exists.");
                return;
            }
        }
        tasks.add(t);
        updateStatus();
        System.out.println("Task " + t.getIdTask() + " added.");
    }

    public void deleteTask(Task t) {
        Iterator<Task> it = tasks.iterator();
        while(it.hasNext()) {
            Task i = it.next();
            if(i.getIdTask() == t.getIdTask()) {
                it.remove();
                updateStatus();
                System.out.println("Task " + t.getIdTask() + " removed.");
                return;
            }
        }
        System.out.println("Task " + t.getIdTask() + " not found.");
    }

    public void updateStatus() {
        boolean allCompleted = true;

        for (Task t : tasks) {
            if (t instanceof ComplexTask) {
                ComplexTask complexSubtask = (ComplexTask) t;
                complexSubtask.updateStatus();
            }

            if (!t.getStatusTask().equals("Completed")) {
                allCompleted = false;
                break;
            }
        }

        this.setStatusTask(allCompleted ? "Completed" : "Uncompleted");
    }

    public List<Task> getSubtasks() {
        return tasks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ComplexTask ").append(getIdTask()).append(" [ ");
        for (Task t : tasks) {
            sb.append("  ").append(t.toString());
        }
        sb.append(" ] ");
        return sb.toString();
    }
}
