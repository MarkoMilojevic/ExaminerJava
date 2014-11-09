package rs.ac.bg.etf.examiner.labexercise;

import rs.ac.bg.etf.examiner.errors.*;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.*;

/**
 * Represents an assignment student needs to implement during laboratory
 * exercise. It has a name, list of <tt>Task</tt>s student needs
 * implement within this assignment and a parent <tt>LabExercise</tt>.
 * 
 * @author Marko Milojevic
 * 
 */
public class Assignment implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(Assignment.class.getName());
    private String name;
    private List<Task> tasks;
    private LabExercise parent;

    {
        tasks = new ArrayList<Task>();
    }
    
    public Assignment(String name) {
        setName(name);
    }

    public Assignment(Assignment assignment) {
        if (assignment == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        }

        setName(assignment.name);
        for (Task task : assignment.tasks) {
            addTask(new Task(task));
        }
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        if (name == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        } else if (hasParent() && this.parent.containsAssignmentByName(name)) {
        	log.error(ErrorMessages.ALREADY_IN_COLLECTION);
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(this.tasks);
    }

    public Task getTaskByName(String taskName) {
    	if (taskName == null) {
    		return null;
    	}
    	
    	for (Task task : this.tasks) {
    		if (task.getName().equals(taskName)) {
    			return task;
    		}
    	}
    	
    	return null;
    }
    
    public boolean addTask(Task task) {
        if (task == null) {
            return false;
        } else if (containsTaskByName(task.getName())) {
            log.error(ErrorMessages.ALREADY_IN_COLLECTION);
            throw new IllegalArgumentException();
        }

        boolean opResult = this.tasks.add(task);
        if (opResult == true) {
            task.setParent(this);
        }

        return opResult;
    }

    public boolean removeTask(Task task) {
        if (task == null) {
            return false;
        }

        boolean opResult = this.tasks.remove(task);
        if (opResult == true) {
            task.setParent(null);
        }

        return opResult;
    }
    
    public boolean removeTaskByName(String taskName) {
        Task task = getTaskByName(taskName);
        return removeTask(task);
    }

    public boolean containsTask(Task task) {
        if (task == null) {
            return false;
        }

        return this.tasks.contains(task);
    }
    
    public boolean containsTaskByName(String taskName) {
        if (taskName == null) {
            return false;
        }

        for (Task task : this.tasks) {
        	if (task.getName().equals(taskName)) {
        		return true;
        	}
        }
        
        return false;
    }

    public LabExercise getParent() {
	    return this.parent;
	}

	void setParent(LabExercise parent) {
	    if (this.parent == parent) {
	        return;
	    }
	
	    if (hasParent()) {
	        removeFromCurrentParent();
	    }
	
	    this.parent = parent;
	}
	
	private boolean hasParent() {
		return this.parent != null;
	}

	private void removeFromCurrentParent() {
		this.parent.removeAssignment(this);
	}

	public int getValue() {
	    int value = 0;
	    for (Task task : this.tasks) {
	        value += task.getValue();
	    }
	
	    return value;
	}

	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    } else if (obj == this) {
	        return true;
	    } else if (!(obj instanceof Assignment)) {
	        return false;
	    }
	
	    Assignment assignment = (Assignment) obj;
	    boolean haveSameParent = hasParent() && assignment.hasParent() ?
	    		this.parent.equals(assignment.parent) : false;
	    		
	    return this.name.equals(assignment.name) && haveSameParent;
	}
}
