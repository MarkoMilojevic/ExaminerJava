package rs.ac.bg.rs.examiner.errors;

import rs.ac.bg.rs.examiner.student.*;
import org.apache.logging.log4j.*;

/**
 * @author Marko Milojevic
 *
 */
public class TaskRemark extends Remark {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(TaskRemark.class.getName());
    private StudentTask studentTask;
    
    public TaskRemark(StudentTask task, String explanation) {
		super(explanation);
		setStudentTask(task);
	}
    
	public TaskRemark(StudentTask task, Error error, int errorPenaltyPercentage, String explanation) {
		super(error, errorPenaltyPercentage, explanation);
		setStudentTask(task);
	}

    public StudentTask getStudentTask() {
        return studentTask;
    }

    private void setStudentTask(StudentTask studentTask) {
    	if (studentTask == null) {
    		log.error(ErrorMessages.NULL_ARGUMENT);
    		throw new IllegalArgumentException();
    	}
    	
        this.studentTask = studentTask;
    }
    
    public int getErrorPenaltyPercentage() {
    	return this.studentTask.isAbsoluteEvaluation() ? 0 : super.getErrorPenaltyPercentage();
    }

	public RemarkType getType() {
		return RemarkType.TASK_SPECIFIC;
	}
}
