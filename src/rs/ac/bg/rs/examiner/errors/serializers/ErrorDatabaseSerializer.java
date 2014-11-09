package rs.ac.bg.rs.examiner.errors.serializers;

import rs.ac.bg.rs.examiner.errors.*;
import java.io.*;

/**
 * @author Marko Milojevic
 *
 */
public class ErrorDatabaseSerializer {
	
	private static ErrorDatabaseSerializerImpl impl = new ErrorDatabaseByteSerializerImpl();
    
    public static void setSerializer(ErrorDatabaseSerializerImpl impl) {
    	if (impl != null) {
    		ErrorDatabaseSerializer.impl = impl;
    	}
    }
    
	public static void save(String path) throws IOException {
		impl.save(path);
    }

    public static ErrorDatabase open(String path) throws ClassNotFoundException, IOException {
    	return impl.open(path);
    }

    public static ErrorDatabase open(File file) throws ClassNotFoundException, IOException {
    	return impl.open(file);
    }
}
