package rs.ac.bg.rs.examiner.student;

import rs.ac.bg.rs.examiner.errors.*;
import java.io.*;
import org.apache.logging.log4j.*;

/**
 * @author Marko Milojevic
 *
 */
public class SourceCodeFile implements Serializable {
	
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(SourceCodeFile.class.getName());
    private File file;
    private StudentTask parent;

    SourceCodeFile(File file) {
    	setFile(file);
    }

    public File getFile() {
	    return this.file;
	}

	public void setFile(File file) {
	    if (file == null) {
	        log.error(ErrorMessages.NULL_ARGUMENT);
	        throw new IllegalArgumentException();
	    }
	    
	    this.file = file;
	}

	public String getName() {
        return this.file.getName();
    }
    
    public String getNameWithoutExtension() {
    	String fileName = this.file.getName();
        int lastPeriodPos = fileName.lastIndexOf('.');
        return lastPeriodPos > 0 ? fileName.substring(0, lastPeriodPos) : fileName;
    }

    public StudentTask getParent() {
        return this.parent;
    }

    void setParent(StudentTask parent) {
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

    private boolean removeFromCurrentParent() {
		return this.parent.removeSourceCodeFile(this);
	}

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof SourceCodeFile)) {
            return false;
        }

        SourceCodeFile sourceCodeFile = (SourceCodeFile) obj;
        boolean haveSameParent = hasParent() && sourceCodeFile.hasParent() ?
        		this.parent.equals(sourceCodeFile.parent) : false;
        		
        return getName().equals(sourceCodeFile.getName()) && haveSameParent;
    }
}
