package rs.ac.bg.etf.examiner.errors;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.*;

/**
 * Contains list of <tt>Error</tt>s.
 * 
 * @author Marko Milojevic
 * 
 */
public class ErrorCategory implements Serializable, Comparable<ErrorCategory> {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(ErrorCategory.class.getName());
    public static final String DEFAULT_CATEGORY_NAME = "NEW";
    private String name;
    private List<Error> errors;
    private ErrorDatabase parent;

    {
    	errors = new ArrayList<Error>();
    }
    
    public ErrorCategory() {
        setName(DEFAULT_CATEGORY_NAME);
    }
    
    public ErrorCategory(String name) {
        setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
    	if (hasParent() && this.parent.containsCategoryByName(name)) {
    		log.error(ErrorMessages.ALREADY_IN_COLLECTION);
            throw new IllegalArgumentException();
    	}
    	
        this.name = name == null || "".equals(name) ? DEFAULT_CATEGORY_NAME : name;
    }

    public List<Error> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }
    
    public Error getErrorByName(String errorName) {
    	if (errorName == null) {
    		return null;
    	}
    	
    	for (Error error : this.errors) {
    		if (error.getName().equals(errorName)) {
    			return error;
    		}
    	}
    	
    	return null;
    }

    public boolean addError(Error error) {
        if (error == null) {
        	return false;
        } else if (containsError(error)) {
            log.error(ErrorMessages.ALREADY_IN_COLLECTION);
            throw new IllegalArgumentException();
        }

        boolean opResult = this.errors.add(error);
        if (opResult == true) {
            error.setParent(this);
        }

        return opResult;
    }

    public boolean removeError(Error error) {
        if (error == null) {
        	return false;
        }

        boolean opResult = this.errors.remove(error);
        if (opResult) {
            error.setParent(null);
        }

        return opResult;
    }
    
    public boolean removeErrorByName(String errorName) {
        Error error = getErrorByName(errorName);
        return removeError(error);
    }

    public boolean containsError(Error error) {
        if (error == null) {
        	return false;
        }

        return errors.contains(error);
    }
    
    public boolean containsErrorByName(String errorName) {
    	if (errorName == null) {
    		return false;
    	}
    	
    	for (Error error : this.errors) {
    		if (error.getName().equals(errorName)) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public ErrorDatabase getParent() {
    	return this.parent;
    }
    
    void setParent(ErrorDatabase parent) {
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
		this.parent.removeCategory(this);
	}

    public void sort() {
        Collections.sort(errors);
    }

    public int compareTo(ErrorCategory category) {
        return category != null ? name.compareTo(category.name) : 1;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof ErrorCategory)) {
            return false;
        }

        ErrorCategory category = (ErrorCategory) obj;
        return name.equals(category.name);
    }
}
