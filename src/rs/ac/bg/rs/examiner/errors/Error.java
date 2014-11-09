package rs.ac.bg.rs.examiner.errors;

import java.io.*;

import org.apache.logging.log4j.*;

/**
 * Represents an error that can be attached to student's assignment. <tt>Error</tt>s are
 * stored in error database which is represented by <tt>ErrorDatabase</tt>.
 * Errors are divided into categories represented by <tt>ErrorCategory</tt>.
 * 
 * @author Marko Milojevic
 * 
 */
public class Error implements Serializable, Comparable<Error> {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(Error.class.getName());
    public static final String DEFAULT_DESCRIPTION = "No description.";
    public static final ErrorSeverity DEFAULT_SEVERITY = ErrorSeverity.LOW;
    public static final String DEFAULT_EXAMPLE_CODE = "No example code.";
    private String name;
    private String description;
    private ErrorSeverity severity;
    private String exampleCode;
    private ErrorCategory parent;

    public Error(String name, String description, ErrorSeverity severity,
            String exampleCode) {
        setName(name);
        setDescription(description);
        setSeverity(severity);
        setExampleCode(exampleCode);
    }

    public Error(Error error) {
        if (error == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        }

        setName(error.name);
        setDescription(error.description);
        setSeverity(error.severity);
        setExampleCode(error.exampleCode);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null) {
        	log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        } else if (hasParent() && parent.containsErrorByName(name)) {
        	log.error(ErrorMessages.ALREADY_IN_COLLECTION);
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description == null || "".equals(description) ?
        		DEFAULT_DESCRIPTION : description;
    }

    public ErrorSeverity getSeverity() {
        return this.severity;
    }

    public void setSeverity(ErrorSeverity severity) {
        this.severity = severity == null ? DEFAULT_SEVERITY : severity;
    }

    public String getExampleCode() {
        return this.exampleCode;
    }

    public void setExampleCode(String exampleCode) {
        this.exampleCode = exampleCode == null || "".equals(exampleCode) ?
        		DEFAULT_EXAMPLE_CODE : exampleCode;
    }

    public ErrorCategory getParent() {
	    return this.parent;
	}

	void setParent(ErrorCategory category) {
	    if (this.parent == category) {
	        return;
	    }
	
	    if (hasParent()) {
	        removeFromCurrentParent();
	    }
	
	    this.parent = category;
	}

	private boolean hasParent() {
		return this.parent != null;
	}

	private void removeFromCurrentParent() {
		this.parent.removeError(this);
	}

	public int compareTo(Error o) {
        return name.compareTo(o.name);
    }
    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof Error)) {
            return false;
        }

        Error error = (Error) obj;
        boolean haveSameParent = hasParent() && error.hasParent() ?
        		parent.equals(error.parent) : false;

        return name.equals(error.name) && haveSameParent;
    }
    
    public String toString() {
    	String errorString = "\tname: " + this.name + "\r\n";
    	errorString += "\tdescription: " + this.description + "\r\n";   
    	return errorString;
    }
}
