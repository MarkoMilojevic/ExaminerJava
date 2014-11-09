package rs.ac.bg.etf.examiner.student;

import rs.ac.bg.etf.examiner.errors.*;
import rs.ac.bg.etf.examiner.labexercise.*;

import java.io.*;

import org.apache.logging.log4j.*;

/**
 * @author Marko Milojevic
 * 
 */
public class StudentItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(StudentItem.class.getName());
    private String name;
    private String description;
    private int value;
    private int absoluteScore;
    private ReviewStatus reviewStatus;
    private StudentTask parent;

    {
    	absoluteScore = 0;
    	reviewStatus = ReviewStatus.NOT_STARTED;
    }
    
    StudentItem(Item item, StudentTask parent) {
        if (item == null) {
        	log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        }
        
        setName(item.getName());
        setDescription(item.getDescription());
        setValue(item.getValue());
        setParent(parent);
    }

    public String getName() {
        return this.name;
    }

    private void setName(String name) {
        if (name == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        }
        
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    private void setDescription(String description) {
        if (description == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        }
        
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    private void setValue(int value) {
        if (value <= 0) {
            log.error(ErrorMessages.NON_POSITIVE_VALUE);
            throw new IllegalArgumentException(ErrorMessages.NON_POSITIVE_VALUE);
        }
        
        this.value = value;
    }
    
    public int getScoreAsAbsolute() {
        return this.absoluteScore;
    }

    public void setScoreAsAbsolute(int score) {
        if (score < 0 || score > value) {
            log.error(ErrorMessages.INVALID_SCORE_VALUE);
            throw new IllegalArgumentException();
        }

        this.absoluteScore = score;
    }

    public int getScoreAsPercentage() {
        return (int) (this.absoluteScore / this.value * 100);
    }

    public void setScoreAsPercentage(int percentage) {
        if (percentage < 0 || percentage > 100) {
            log.error(ErrorMessages.INVALID_PERCENTAGE_VALUE);
            throw new IllegalArgumentException();
        }

        this.absoluteScore = Math.round(((float) (this.value * percentage)) / 100);
    }

    public ReviewStatus getReviewStatus() {
	    return this.reviewStatus;
	}

	public void setReviewStatus(ReviewStatus status) {
        this.reviewStatus = status;
    }

    public boolean isReviewed() {
        return this.reviewStatus == ReviewStatus.FINISHED ? true : false;
    }
    
    public StudentTask getParent() {
		return this.parent;
	}

	private void setParent(StudentTask parent) {
		if (parent == null) {
	    	log.error(ErrorMessages.NULL_ARGUMENT);
	        throw new IllegalArgumentException();
	    }
		
		this.parent = parent;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
	        return false;
	    } else if (obj == this) {
	        return true;
	    } else if (!(obj instanceof StudentItem)) {
	        return false;
	    }
	
		StudentItem studentItem = (StudentItem) obj;
	    boolean haveSameParent = this.parent.equals(studentItem.parent);
	    return this.name.equals(studentItem.name) && haveSameParent;
	}
}
