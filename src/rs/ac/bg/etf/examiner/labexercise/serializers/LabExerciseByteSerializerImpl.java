package rs.ac.bg.etf.examiner.labexercise.serializers;

import rs.ac.bg.etf.examiner.errors.*;
import rs.ac.bg.etf.examiner.labexercise.*;

import java.io.*;
import java.nio.file.*;

/**
 * 
 * @author Marko Milojevic
 *
 */
public class LabExerciseByteSerializerImpl implements LabExerciseSerializerImpl {

    public static String LAB_EXERCISE_FILE_EXTENSION = ".elx";
    
	public void save(String path) throws IOException {
		if (path == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        } else if (!path.endsWith(LAB_EXERCISE_FILE_EXTENSION)) {
            log.error(ErrorMessages.INVALID_FILE_EXTENSION);
            throw new IllegalArgumentException(ErrorMessages.INVALID_FILE_EXTENSION);
        }
        
		LabExercise labExercise = LabExercise.getInstance();
		ErrorDatabase errorDB = ErrorDatabase.getInstance();
        String errorMessage = ErrorMessages.errorMessage(labExercise);
        if (!errorMessage.equals("")) {
            log.error(ErrorMessages.INVALID_STATE + "\r\n" + errorMessage);
            throw new IllegalStateException(ErrorMessages.INVALID_STATE + "\r\n" + errorMessage);
        }

        try (FileOutputStream fileOut = new FileOutputStream(path);
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(labExercise);
            out.writeObject(errorDB);
        }
	}

	public LabExercise open(String path) throws ClassNotFoundException, IOException {
		if (path == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        } else if (!path.endsWith(LAB_EXERCISE_FILE_EXTENSION)) {
            log.error(ErrorMessages.INVALID_FILE_EXTENSION);
            throw new IllegalArgumentException(ErrorMessages.INVALID_FILE_EXTENSION);
        } else if (Files.notExists(Paths.get(path))) {
            log.error(ErrorMessages.FILE_DOES_NOT_EXIST);
            throw new FileNotFoundException(ErrorMessages.FILE_DOES_NOT_EXIST);
        }

    	LabExercise labExercise = null;
    	ErrorDatabase errorDB = null;
        try (FileInputStream fileIn = new FileInputStream(path);
                ObjectInputStream in = new ObjectInputStream(fileIn)) {
        	labExercise = (LabExercise) in.readObject();
        	errorDB = (ErrorDatabase) in.readObject();
        	LabExercise.setInstance(labExercise);
            ErrorDatabase.setInstance(errorDB);
        }

        return labExercise;
	}

	public LabExercise open(File file) throws ClassNotFoundException, IOException {
		if (file == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        }
        
        return open(file.getPath());
	}
}
