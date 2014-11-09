package rs.ac.bg.rs.examiner.errors;

import rs.ac.bg.rs.examiner.student.*;
import org.apache.logging.log4j.*;

/**
 * @author Marko Milojevic
 *
 */
public class AssignmentRemark extends Remark {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AssignmentRemark.class.getName());
	private StudentAssignment studentAssignment;
	
	public AssignmentRemark(StudentAssignment studentAssignment, String explanation) {
		super(explanation);
		setStudentAssignment(studentAssignment);
	}
	
	public AssignmentRemark(StudentAssignment studentAssignment, Error error,
			int errorPenaltyPercentage, String explanation) {
		super(error, errorPenaltyPercentage, explanation);
		setStudentAssignment(studentAssignment);
	}

	public StudentAssignment getStudentAssignment() {
        return this.studentAssignment;
    }
    
    private void setStudentAssignment(StudentAssignment studentAssignment) {
        if (studentAssignment == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        }
        
        this.studentAssignment = studentAssignment;
    }

	public RemarkType getType() {
		return RemarkType.ASSIGNMENT_SPECIFIC;
	}	
}
