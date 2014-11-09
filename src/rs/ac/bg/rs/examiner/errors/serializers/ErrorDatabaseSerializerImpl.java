package rs.ac.bg.rs.examiner.errors.serializers;

import rs.ac.bg.rs.examiner.errors.*;
import java.io.*;
import org.apache.logging.log4j.*;

/**
 * @author Marko Milojevic
 *
 */
public interface ErrorDatabaseSerializerImpl {
	
	static final Logger log = LogManager.getLogger(ErrorDatabaseSerializerImpl.class.getName());
	
	public void save(String path) throws IOException;

    public ErrorDatabase open(String path) throws ClassNotFoundException, IOException;

    public ErrorDatabase open(File file) throws ClassNotFoundException, IOException;
}
