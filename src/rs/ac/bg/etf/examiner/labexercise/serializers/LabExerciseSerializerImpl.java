package rs.ac.bg.etf.examiner.labexercise.serializers;

import rs.ac.bg.etf.examiner.labexercise.*;

import java.io.*;

import org.apache.logging.log4j.*;


/**
 * 
 * @author Marko Milojevic
 *
 */
public interface LabExerciseSerializerImpl {
	static final Logger log = LogManager.getLogger(LabExerciseSerializerImpl.class.getName());
    
	public abstract void save(String path) throws IOException;
	
	public abstract LabExercise open(String path) throws FileNotFoundException, ClassNotFoundException, IOException;
	
	public abstract LabExercise open(File file) throws ClassNotFoundException, IOException;
}
