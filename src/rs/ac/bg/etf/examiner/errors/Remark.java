package rs.ac.bg.etf.examiner.errors;

import java.io.*;
import org.apache.logging.log4j.*;

/**
 * 
 * @author Marko Milojevic
 * 
 */
public abstract class Remark implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(Remark.class.getName());
    public static final String DEFAULT_EXPLANATION = "No explanation.";
    protected Error error;
    protected int errorPenaltyPercentage;
    protected String explanation;
    
    protected Remark(String explanation) {
    	setExplanation(explanation);
    }
    
    protected Remark(Error error, int errorPenaltyPercentage, String explanation) {
        setError(error);
        setErrorPenaltyPercentage(errorPenaltyPercentage);
        setExplanation(explanation);
    }
    
    public Error getError() {
        return this.error;
    }

    public void setError(Error error) {
        if (error == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        }

        this.error = error;
    }
    
    public String getExplanation() {
        return this.explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation == null || "".equals(explanation) ?
        		DEFAULT_EXPLANATION : explanation;
    }
    
    public int getErrorPenaltyPercentage() {
    	return getSeverity() == RemarkSeverity.ERROR ? this.errorPenaltyPercentage : 0;
    }

    public void setErrorPenaltyPercentage(int errorPenaltyPercentage) {
        if (errorPenaltyPercentage < 0 || errorPenaltyPercentage > 100) {
            log.error(ErrorMessages.INVALID_PERCENTAGE_VALUE);
            throw new IllegalArgumentException();
        }

        this.errorPenaltyPercentage = errorPenaltyPercentage;
    }

    public RemarkSeverity getSeverity() {
        return this.error != null ? RemarkSeverity.ERROR : RemarkSeverity.WARNING;
    }
    
    public abstract RemarkType getType();

    public String toString() {
        String result = "";
        String newline = System.lineSeparator();
        result += "Error:" + newline + this.error + newline;
        result += "Explanation:" + newline + "\t" + this.explanation + newline;
        return result;
    }
}
