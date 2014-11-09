package rs.ac.bg.etf.examiner.errors;

import rs.ac.bg.etf.examiner.student.*;

import org.apache.logging.log4j.*;

/**
 * @author Marko Milojevic
 *
 */
public class FileRemark extends Remark implements Comparable<FileRemark> {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(FileRemark.class.getName());
    public static final String DEFAULT_MARKED_CODE = "No code attached.";
    private SourceCodeFile sourceCodeFile;
    private String markedCode;
    private int startRow;
    private int endRow;
    
    public FileRemark(SourceCodeFile sourceCodeFile, String markedCode, int startRow,
    		int endRow, String explanation) {
		super(explanation);
		setSourceCodeFile(sourceCodeFile);
		setPosition(startRow, endRow);
		setMarkedCode(markedCode);
	}
    
	public FileRemark(SourceCodeFile sourceCodeFile, String markedCode, int startRow,
			int endRow, Error error, int errorPenaltyPercentage, String explanation) {
		super(error, errorPenaltyPercentage, explanation);
		setSourceCodeFile(sourceCodeFile);
		setPosition(startRow, endRow);
		setMarkedCode(markedCode);
	}
	
	public SourceCodeFile getSourceCodeFile() {
        return this.sourceCodeFile;
    }

    private void setSourceCodeFile(SourceCodeFile sourceCodeFile) {
    	if (sourceCodeFile == null) {
    		log.error(ErrorMessages.NULL_ARGUMENT);
    		throw new IllegalArgumentException();
    	}
    	
        this.sourceCodeFile = sourceCodeFile;
    }
    
    public String getMarkedCode() {
	    return this.markedCode;
	}

	private void setMarkedCode(String markedCode) {
	    this.markedCode = markedCode == null || "".equals(markedCode) ?
	    		DEFAULT_MARKED_CODE : markedCode;
	}

	public int getStartRow() {
    	return this.startRow;
    }
    
    public int getEndRow() {
    	return this.endRow;
    }
    
    private void setPosition(int startRow, int endRow) {
    	if (startRow < 0 || endRow < 0 || startRow > endRow) {
    		log.error(ErrorMessages.INVALID_ERROR_POSITION);
    		throw new IllegalArgumentException();
    	}
    	
    	this.startRow = startRow;
    	this.endRow = endRow;
    }

    public int getErrorPenaltyPercentage() {
    	StudentTask studentTask = this.sourceCodeFile.getParent();
    	return studentTask.isAbsoluteEvaluation() ? 0 : super.getErrorPenaltyPercentage();
    }
    
    public RemarkType getType() {
		return RemarkType.FILE_SPECIFIC;
	}

	public int compareTo(FileRemark remark) {
        if (remark == null) {
            return 1;
        } else if (this.startRow < remark.startRow) {
            return -1;
        } else if (this.startRow > remark.startRow) {
            return 1;
        } else if (this.endRow < remark.endRow) {
            return -1;
        } else if (this.endRow > remark.endRow) {
            return 1;
        } else {        	
        	return 0;
        }
    }
	
    public String toString() {
        String result = "";
        String newline = System.lineSeparator();
        result += "Error:" + newline + this.error + newline;
        result += "Marked code:" + newline + this.markedCode + newline + newline;
        result += "Explanation:" + newline + "\t" + this.explanation + newline;
        return result;
    }
}
