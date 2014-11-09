package rs.ac.bg.rs.examiner.labexercise.serializers;

import java.io.*;

import rs.ac.bg.rs.examiner.labexercise.*;

/**
 * 
 * @author Marko Milojevic
 *
 */
public class LabExerciseSerializer {
	
    private static LabExerciseSerializerImpl impl = new LabExerciseByteSerializerImpl();
    
    public static void setSerializer(LabExerciseSerializerImpl impl) {
    	if (impl != null) {
    		LabExerciseSerializer.impl = impl;
    	}
    }
    
	public static void save(String path) throws IOException {
		impl.save(path);
    }

    public static LabExercise open(String path) throws ClassNotFoundException, IOException {
    	return impl.open(path);
    }

    public static LabExercise open(File file) throws ClassNotFoundException, IOException {
    	return impl.open(file);
    }
}
