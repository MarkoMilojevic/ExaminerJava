package rs.ac.bg.rs.examiner.errors.serializers;

import rs.ac.bg.rs.examiner.errors.*;

import java.io.*;
import java.nio.file.*;

/**
 * @author Marko Milojevic
 *
 */
public class ErrorDatabaseByteSerializerImpl implements 
		ErrorDatabaseSerializerImpl {

    public static final String ERRORDATABASE_FILE_EXTENSION = ".edb";
    
	public void save(String path) throws IOException {
		if (path == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        } else if (!path.endsWith(ERRORDATABASE_FILE_EXTENSION)) {
            log.error(ErrorMessages.INVALID_FILE_EXTENSION);
            throw new IllegalArgumentException(ErrorMessages.INVALID_FILE_EXTENSION);
        }

        try (FileOutputStream fileOut = new FileOutputStream(path);
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
        	ErrorDatabase errorDatabase = ErrorDatabase.getInstance();
            out.writeObject(errorDatabase);
        }
		
	}

	public ErrorDatabase open(String path) throws ClassNotFoundException,
			IOException {
		if (path == null) {
        	log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        } else if (!path.endsWith(ERRORDATABASE_FILE_EXTENSION)) {
            log.error(ErrorMessages.INVALID_FILE_EXTENSION);
            throw new IllegalArgumentException(ErrorMessages.INVALID_FILE_EXTENSION);
        } else if (Files.notExists(Paths.get(path))) {
        	log.error(ErrorMessages.FILE_DOES_NOT_EXIST);
            throw new FileNotFoundException(ErrorMessages.FILE_DOES_NOT_EXIST);
        }

		ErrorDatabase errorDatabase = null;
        try (FileInputStream fileIn = new FileInputStream(path);
                ObjectInputStream in = new ObjectInputStream(fileIn)) {
        	errorDatabase = (ErrorDatabase) in.readObject();
        	ErrorDatabase.setInstance(errorDatabase);
        }

        return errorDatabase;
	}

	public ErrorDatabase open(File file) throws ClassNotFoundException, IOException {
		if (file == null) {
			log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
		}
		
		return open(file.getPath());
	}	
}
