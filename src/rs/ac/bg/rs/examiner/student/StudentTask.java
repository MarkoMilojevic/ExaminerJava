package rs.ac.bg.rs.examiner.student;

import rs.ac.bg.rs.examiner.errors.*;
import rs.ac.bg.rs.examiner.labexercise.*;
import java.io.*;
import java.util.*;
import org.apache.logging.log4j.*;

/**
 * @author Marko Milojevic
 * 
 */
public class StudentTask implements Serializable {
	
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(StudentTask.class.getName());
    private String name;
    private List<StudentItem> studentItems;
    private List<SourceCodeFile> sourceCodeFiles;
    private boolean isAbsoluteEvaluation;
    private int absoluteValuePercentage;
    private StudentAssignment parent;

    {
    	studentItems = new ArrayList<StudentItem>();
    	sourceCodeFiles = new ArrayList<SourceCodeFile>();
    	isAbsoluteEvaluation = false;
    	absoluteValuePercentage = 0;
    }
    
    StudentTask(Task task, StudentAssignment parent) {
    	if (task == null) {
    		log.error(ErrorMessages.NULL_ARGUMENT);
    		throw new IllegalArgumentException();
    	}
    	
        setName(task.getName());
        setParent(parent);
        for (Item item : task.getItems()) {
            this.studentItems.add(new StudentItem(item, this));
        }        
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

    public List<StudentItem> getStudentItems() {
	    return Collections.unmodifiableList(this.studentItems);
	}
    
    public StudentItem getStudentItemByName(String studentItemName) {
    	if (studentItemName == null) {
    		return null;
    	}
    	
    	for (StudentItem item : this.studentItems) {
    		if (item.getName().equals(studentItemName)) {
    			return item;
    		}
    	}
    	
    	return null;
    }
    
    public boolean containsStudentItem(StudentItem item) {
        if (item == null) {
            return false;
        }

        return this.studentItems.contains(item);
    }
    
    public boolean containsStudentItemByName(String itemName) {
        if (itemName == null) {
            return false;
        }

        for (StudentItem item : this.studentItems) {
        	if (item.getName().equals(itemName)) {
        		return true;
        	}
        }
        
        return false;
    }
    
	public List<SourceCodeFile> getSourceCodeFiles() {
	    return Collections.unmodifiableList(this.sourceCodeFiles);
	}

	public SourceCodeFile getSourceCodeFileByName(String fileName) {
		if (fileName == null) {
			return null;
		}
		
		for (SourceCodeFile sourceCodeFile : this.sourceCodeFiles) {
			if (sourceCodeFile.getName().equals(fileName)) {
				return sourceCodeFile;
			}
		}
		
		return null;
	}
	
	public boolean addSourceCodeFile(SourceCodeFile sourceCodeFile) {
	    if (sourceCodeFile == null) {
	        return false;
	    }
	
	    boolean opResult = this.sourceCodeFiles.add(sourceCodeFile);
	    if (opResult) {
	        sourceCodeFile.setParent(this);
	        parent.removeUnmatachedSourceCodeFile(sourceCodeFile);
	    }
	
	    return opResult;
	}

	public boolean removeSourceCodeFile(SourceCodeFile sourceCodeFile) {
	    if (sourceCodeFile == null) {
	        return false;
	    }
	
	    boolean opResult = this.sourceCodeFiles.remove(sourceCodeFile);
	    if (opResult) {
	        sourceCodeFile.setParent(null);
	        parent.addUnmatachedSourceCodeFile(sourceCodeFile);
	    }
	
	    return opResult;
	}
	
	public boolean removeSourceCodeFileByName(String fileName) {
		SourceCodeFile sourceCodeFile = getSourceCodeFileByName(fileName);
		return removeSourceCodeFile(sourceCodeFile);
	}

	public boolean containsSourceCodeFile(SourceCodeFile sourceCodeFile) {
	    if (sourceCodeFile == null) {
	        return false;
	    }
	
	    return this.sourceCodeFiles.contains(sourceCodeFile);
	}
	
	public boolean containsSourceCodeFileByName(String fileName) {
	    if (fileName == null) {
	        return false;
	    }
	
	    for (SourceCodeFile sourceCodeFile : this.sourceCodeFiles) {
	    	if (sourceCodeFile.getName().equals(fileName)) {
	    		return true;
	    	}
	    }
	    
	    return false;
	}

	public boolean isAbsoluteEvaluation() {
	    return this.isAbsoluteEvaluation;
	}

	public void setAbsoluteEvaluation(boolean isAbsoluteEvaluation) {
	    this.isAbsoluteEvaluation = isAbsoluteEvaluation;
	}

	public int getAbsoluteValuePercentage() {
	    return this.absoluteValuePercentage;
	}

	public void setAbsoluteValuePercentage(int percentage) {
	    if (percentage < 0 || percentage > 100) {
	        log.error(ErrorMessages.INVALID_PERCENTAGE_VALUE);
	        throw new IllegalArgumentException();
	    }
	
	    this.absoluteValuePercentage = percentage;
	}

	public StudentAssignment getParent() {
	    return this.parent;
	}

	private void setParent(StudentAssignment parent) {
		if (this.parent == null) {
			log.error(ErrorMessages.NULL_ARGUMENT);
			throw new IllegalArgumentException();
		}
		
		this.parent = parent;
	}

	public int getValue() {
        int value = 0;
        
        for (StudentItem item : this.studentItems) {
        	value += item.getValue();
        }
        
        return value;
    }

    public int getScoreAsAbsolute() {
        if (this.isAbsoluteEvaluation) {
            return Math.round((float) (getValue() * this.absoluteValuePercentage) / 100);
        }

        int score = 0;
        for (StudentItem item : this.studentItems) {
            score += item.getScoreAsAbsolute();
        }
        
        return score;
    }

    public int getScoreAsPercentage() {
        if (this.isAbsoluteEvaluation) {
            return this.absoluteValuePercentage;
        }

        return (int) (getScoreAsAbsolute() / getValue() * 100);
    }

    public ReviewStatus getReviewStatus() {
	    if (reviewNotStarted()) {
	        return ReviewStatus.NOT_STARTED;
	    } else if (isReviewFinished()) {
	        return ReviewStatus.FINISHED;
	    } else {
	        return ReviewStatus.IN_PROGRESS;
	    }
	}

	private boolean reviewNotStarted() {
        for (StudentItem item : this.studentItems) {
            if (item.getReviewStatus() != ReviewStatus.NOT_STARTED) {
                return false;
            }
        }
    
        return true;
    }

    private boolean isReviewFinished() {
        for (StudentItem item : this.studentItems) {
            if (!item.isReviewed()) {
                return false;
            }
        }
    
        return true;
    }

	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    } else if (obj == this) {
	        return true;
	    } else if (!(obj instanceof StudentTask)) {
	        return false;
	    }
	
	    StudentTask studentTask = (StudentTask) obj;
	    boolean haveSameParent = this.parent.equals(studentTask.parent);
	    return this.name.equals(studentTask.name) && haveSameParent;
	}
}
