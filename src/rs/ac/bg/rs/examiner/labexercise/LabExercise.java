package rs.ac.bg.rs.examiner.labexercise;

import rs.ac.bg.rs.examiner.errors.*;
import rs.ac.bg.rs.examiner.labexercise.serializers.*;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.*;

/**
 * Represents laboratory exercise. It contains set of
 * <tt>Assignment</tt>s students need to implement. Singleton Design Pattern.
 * 
 * @author Marko Milojevic
 * 
 */
public class LabExercise implements Serializable {

    private static final long serialVersionUID = 1L;
    static final Logger log = LogManager.getLogger(LabExercise.class.getName());
    private static LabExercise instance;
    private Date date;
    private List<Assignment> assignments;

    {
    	this.assignments = new ArrayList<Assignment>();
        this.date = new Date();
    }
    
    protected LabExercise() {}
    
    public static LabExercise getInstance() {
        if (instance == null) {
            instance = new LabExercise();
        }

        return instance;
    }
    
    public static void setInstance(LabExercise instance) {
    	if (instance != null) {
            LabExercise.instance = instance;
        }
    }

    public Date getDate() {
        return this.date;
    }

    public List<Assignment> getAssignments() {
        return Collections.unmodifiableList(this.assignments);
    }

    public Assignment getAssignmentByName(String assignmentName) {
        if (assignmentName == null) {
            return null;
        }
        
        for (Assignment assignment : this.assignments) {
            if (assignment.getName().equals(assignmentName)) {
                return assignment;
            }
        }

        return null;
    }

    public boolean addAssignment(Assignment assignment) {
        if (assignment == null) {
            return false;
        }

        boolean opResult = this.assignments.add(assignment);
        if (opResult == true) {
            assignment.setParent(this);
        }

        return opResult;
    }

    public boolean removeAssignment(Assignment assignment) {
        if (assignment == null) {
            return false;
        }

        boolean opResult = this.assignments.remove(assignment);
        if (opResult == true) {
            assignment.setParent(null);
        }

        return opResult;
    }
    
    public boolean removeAssignmentByName(String assignmentName) {
    	Assignment assignment = getAssignmentByName(assignmentName);
    	return removeAssignment(assignment);
    }

    public boolean containsAssignment(Assignment assignment) {
        if (assignment == null) {
            return false;
        }

        return this.assignments.contains(assignment);
    }

    public boolean containsAssignmentByName(String assignmentName) {
        if (assignmentName == null) {
            return false;
        }

        for (Assignment assignment : this.assignments) {
            if (assignment.getName().equals(assignmentName)) {
                return true;
            }
        }

        return false;
    }
    
    public boolean equals(Object obj) {
    	if (obj == null) {
	        return false;
	    } else if (obj == this) {
	        return true;
	    } else if (!(obj instanceof LabExercise)) {
	        return false;
	    }
	
    	LabExercise labExercise = (LabExercise) obj;
	    return this.date.equals(labExercise.date);
    }

    public static void main(String[] args) {
        LabExercise e = LabExercise.getInstance();

        Assignment a1 = new Assignment("group1");

        Task t1 = new Task("RemoteFileMessageBox");
        Item i1 = new Item("host", "Remote File MessageBox's host.", 5);
        Item i2 = new Item("port", "Remote File MessageBox's port", 5);
        Item i3 = new Item("constructor", "Object constructor", 5);
        Item i4 = new Item("send()", "Method send()", 5);
        Item i5 = new Item("receive()", "Method receive()", 5);
        t1.addItem(i1);
        t1.addItem(i2);
        t1.addItem(i3);
        t1.addItem(i4);
        t1.addItem(i5);

        Task t2 = new Task("RemoteMessageBox");
        Item i18 = new Item("host", "Remote MessageBox's host.", 5);
        Item i19 = new Item("port", "Remote MessageBox's port", 5);
        Item i20 = new Item("constructor", "Object constructor", 5);
        Item i21 = new Item("send()", "Method send()", 5);
        Item i22 = new Item("receive()", "Method receive()", 5);
        t2.addItem(i18);
        t2.addItem(i19);
        t2.addItem(i20);
        t2.addItem(i21);
        t2.addItem(i22);

        Task t3 = new Task("Server");
        Item i6 = new Item("M", "Buffer capacity", 5);
        Item i7 = new Item("port", "Server's port", 5);
        Item i8 = new Item("buffer", "Server's buffer", 5);
        Item i9 = new Item("pool", "Server's pool of threads", 5);
        Item i10 = new Item("constructor", "Object constructor", 5);
        Item i11 = new Item("work()", "Method work()", 5);
        Item i12 = new Item("processRequest()", "Method processRequest()", 5);
        Item i13 = new Item("main()", "Method main()", 5);
        t3.addItem(i6);
        t3.addItem(i7);
        t3.addItem(i8);
        t3.addItem(i9);
        t3.addItem(i10);
        t3.addItem(i11);
        t3.addItem(i12);
        t3.addItem(i13);

        Task t4 = new Task("WorkingThread");
        Item i14 = new Item("client", "Client's socket", 2);
        Item i15 = new Item("buffer", "Client's buffer", 3);
        Item i16 = new Item("constructor", "Object constructor", 2);
        Item i17 = new Item("run()", "Method run()", 3);
        t4.addItem(i14);
        t4.addItem(i15);
        t4.addItem(i16);
        t4.addItem(i17);

        a1.addTask(t1);
        a1.addTask(t2);
        a1.addTask(t3);
        a1.addTask(t4);

        e.addAssignment(a1);

        ErrorDatabase.main(null);

        try {
        	LabExerciseSerializer.save("LabExercise.elx");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
