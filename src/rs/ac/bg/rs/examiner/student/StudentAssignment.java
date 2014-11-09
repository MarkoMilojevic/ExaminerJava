package rs.ac.bg.rs.examiner.student;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rs.ac.bg.rs.examiner.errors.*;
import rs.ac.bg.rs.examiner.labexercise.*;

/**
 * @author Marko Milojevic
 * 
 */
public class StudentAssignment implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(StudentAssignment.class.getName());
    private File directory;
    private List<StudentTask> studentTasks;
    private List<Remark> remarks;
    private List<SourceCodeFile> unmatchedSourceCodeFiles;

    {
    	studentTasks = new ArrayList<StudentTask>();
    	remarks = new ArrayList<Remark>();
    	unmatchedSourceCodeFiles = new ArrayList<SourceCodeFile>();
    }
    
    StudentAssignment(String directoryPath) throws FileNotFoundException {
    	if (directoryPath == null) {
    		log.error(ErrorMessages.NULL_ARGUMENT);
    		throw new IllegalArgumentException();
    	}
    	
    	File directory = new File(directoryPath);
    	initializeStudentAssignment(directory);
    }

    StudentAssignment(File directory) throws FileNotFoundException {
        initializeStudentAssignment(directory);
    }

    private void initializeStudentAssignment(File directory) throws FileNotFoundException {
	    if (directory == null) {
	        log.error(ErrorMessages.NULL_ARGUMENT);
	        throw new IllegalArgumentException();
	    } else if (!directory.exists()) {
	        log.error(ErrorMessages.FILE_DOES_NOT_EXIST);
	        throw new FileNotFoundException();
	    }
	
	    this.directory = directory;
	    Assignment assignment = getAssignment(directory);
	    if (assignment == null) {
	    	log.error(ErrorMessages.INVALID_ASSIGNMENT_DIRECTORY);
	    	throw new IllegalArgumentException();
	    }
	    
	    initializeStudentTasks(assignment);
	}

	private Assignment getAssignment(File directory) {
		LabExercise labExercise = LabExercise.getInstance();
	    File assignmentGroupDirectory = directory.getParentFile();
	    if (assignmentGroupDirectory == null) {
	        log.error(ErrorMessages.INVALID_ASSIGNMENT_DIRECTORY);
	        throw new IllegalArgumentException();
	    }
	
	    String assignmentName = assignmentGroupDirectory.getName();
	    return labExercise.getAssignmentByName(assignmentName);
	}

	private void initializeStudentTasks(Assignment assignment) {
		this.unmatchedSourceCodeFiles = loadSourceCodeFiles(this.directory);	    
	    for (Task task : assignment.getTasks()) {
	        StudentTask studentTask = new StudentTask(task, this);	
	        Iterator<SourceCodeFile> it = this.unmatchedSourceCodeFiles.iterator();
		    while (it.hasNext()) {
		    	SourceCodeFile sourceCodeFile = it.next();
		    	if (sourceCodeFile.getNameWithoutExtension().equals(task.getName())) {
	                studentTask.addSourceCodeFile(sourceCodeFile);
	            }
		    	
		    	it.remove();
	    	}
	
	        this.studentTasks.add(studentTask);
	    }
	}

	private List<SourceCodeFile> loadSourceCodeFiles(File directory) {	
		List<SourceCodeFile> sourceCodeFiles = new ArrayList<SourceCodeFile>();
	    Stack<File> stack = new Stack<>();
	    stack.push(directory);	
	    while (stack.size() > 0) {
	        File file = stack.pop();
	        for (File f : file.listFiles()) {
	            String fileName = f.getName();	
	            if (f.isDirectory()) {
	                stack.push(f);
	            } else if (isSourceCodeFile(fileName) && !fileName.contains("(Pregledano)")) {
	                sourceCodeFiles.add(new SourceCodeFile(f));
	            }
	        }
	    }
	    
	    return sourceCodeFiles;
	}

	private boolean isSourceCodeFile(String fileName) {
		return fileName.endsWith(".java")|| fileName.endsWith(".cpp") ||
				fileName.endsWith(".h") || fileName.endsWith(".c");
	}

	public String getId() {
        return this.directory.getName();
    }

    public File getDirectory() {
        return this.directory;
    }

    public List<StudentTask> getStudentTasks() {
	    return Collections.unmodifiableList(this.studentTasks);
	}

	public StudentTask getStudentTaskByName(String studentTaskName) {
		if (studentTaskName == null) {
			return null;
		}
		
		for (StudentTask studentTask : this.studentTasks) {
			if (studentTask.getName().equals(studentTaskName)) {
				return studentTask;
			}
		}
		
		return null;
	}
	
	public boolean containsStudentTask(StudentTask studentTask) {
		if (studentTask == null) {
			return false;
		}
		
		return this.studentTasks.contains(studentTask);
	}
	
	public boolean containsStudentTaskByName(String studentTaskName) {
		if (studentTaskName == null) {
			return false;
		}
		
		for (StudentTask studentTask : this.studentTasks) {
			if (studentTask.getName().equals(studentTaskName)) {
				return true;
			}
		}
		
		return false;
	}

	public List<Remark> getRemarks() {
        return Collections.unmodifiableList(this.remarks);
    }

    public boolean addRemark(Remark remark) {
        if (remark == null) {
        	return false;
        }

        return this.remarks.add(remark);
    }

    public boolean removeRemark(Remark remark) {
        if (remark == null) {
            return false;
        }

        return this.remarks.remove(remark);
    }

    public boolean containsRemark(Remark note) {
        if (note == null) {
            return false;
        }

        return this.remarks.contains(note);
    }

    public List<SourceCodeFile> getUnmatchedSourceCodeFiles() {
        return Collections.unmodifiableList(this.unmatchedSourceCodeFiles);
    }

    public SourceCodeFile getUnmatchedSourceCodeFileByName(String fileName) {
    	if (fileName == null) {
    		return null;
    	}
    	
    	for (SourceCodeFile sourceCodeFile : this.unmatchedSourceCodeFiles) {
    		if (sourceCodeFile.getName().equals(fileName)) {
    			return sourceCodeFile;
    		}
    	}
    	
    	return null;
    }
    
    boolean addUnmatachedSourceCodeFile(SourceCodeFile sourceCodeFile) {
		if (sourceCodeFile == null) {
			return false;
		}
		
		return this.unmatchedSourceCodeFiles.add(sourceCodeFile);
	}

	boolean removeUnmatachedSourceCodeFile(SourceCodeFile sourceCodeFile) {
		if (sourceCodeFile == null) {
			return false;
		}
		
		return this.unmatchedSourceCodeFiles.remove(sourceCodeFile);
	}

	public int getScoreAsAbsolute() {
        int score = 0;
        for (StudentTask studentTask : studentTasks) {
            score += studentTask.getScoreAsAbsolute();
        }

        return score;
    }

    public ReviewStatus getReviewStatus() {
	    if (notStarted()) {
	        return ReviewStatus.NOT_STARTED;
	    } else if (isReviewFinished()) {
	        return ReviewStatus.FINISHED;
	    } else {
	        return ReviewStatus.IN_PROGRESS;
	    }
	}

    private boolean notStarted() {
        for (StudentTask studentTask : this.studentTasks) {
            if (studentTask.getReviewStatus() != ReviewStatus.NOT_STARTED) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isReviewFinished() {
        for (StudentTask studentTask : this.studentTasks) {
            if (studentTask.getReviewStatus() != ReviewStatus.FINISHED) {
                return false;
            }
        }
        
        return true;
    }
    
	public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof StudentAssignment)) {
            return false;
        }

        StudentAssignment studentAssignment = (StudentAssignment) obj;
        return getId().equals(studentAssignment.getId());
    }
}
    /*public void generateReview() throws FileNotFoundException, IOException {
	    String reviewDirName = "Izvestaj";
	    File reviewDir = new File(directory.getPath() + "/" + reviewDirName);
	
	    if (!reviewDir.exists()) {
	        reviewDir.mkdirs();
	    }
	
	    for (StudentTask studentTask : studentTasks) {
	        for (SourceCodeFile taskFile : studentTask.getSourceCodeFiles()) {
	            generateReviewTaskFile(reviewDir, studentTask,
	                    taskFile.getFile());
	        }
	    }
	
	    String reviewFilePath = reviewDir.getPath() + "/Izvestaj.txt";
	    try (Writer reviewFile = new BufferedWriter(new OutputStreamWriter(
	            new FileOutputStream(reviewFilePath)))) {
	        String report = getReport();
	        reviewFile.write(report);
	    }
	}

	Map<Error, Integer> getAllErrorsWithCount() {
        Map<Error, Integer> errors = new HashMap<Error, Integer>();
        for (Remark note : remarks) {
            Error error = note.getError();
            if (error != null) {
                if (!errors.containsKey(error)) {
                    errors.put(error, 1);
                } else {
                    errors.put(error, errors.get(error) + 1);
                }
            }
        }

        return errors;
    }

    Set<Error> getNewErrors() {
        ErrorDatabase database = ErrorDatabase.getInstance();
        Set<Error> newErrors = new HashSet<Error>();

        for (Remark note : remarks) {
            Error error = note.getError();
            if (error != null && !database.containsError(error)) {
                newErrors.add(error);
            }
        }

        return newErrors;
    }

    void evaluate(Error error, int occurrenceThreshold, int lowValue,
            int highValue) {
        Map<Error, Integer> map = getAllErrorsWithCount();
        for (Remark note : remarks) {
            Error e = note.getError();
            if (e != null && e.equals(error)) {
                int occurrenceCount = map.get(error);
                if (occurrenceCount <= occurrenceThreshold) {
                    // note.setErrorValue(lowValue);
                } else {
                    // note.setErrorValue(highValue);
                }
            }
        }
    }

    void evaluate(ErrorSeverity severity, int occurrenceThreshold, int lowValue,
            int highValue) {
        Map<Error, Integer> map = getAllErrorsWithCount();
        for (Remark note : remarks) {
            Error error = note.getError();
            if (error != null && error.getSeverity() == severity) {
                int occurrenceCount = map.get(error);
                if (occurrenceCount <= occurrenceThreshold) {
                    // note.setErrorValue(lowValue);
                } else {
                    // note.setErrorValue(highValue);
                }
            }
        }
    }

    void refactor(StudentAssignment student) {
        if (student == null) {
            log.error("student must not be null");
        } else if (!getId().equals(student.getId())) {
            log.error("student's ids do not match");
            throw new IllegalArgumentException();
        }

        this.directory = student.directory;
        this.studentTasks = student.studentTasks;
        this.remarks = student.remarks;
        this.unmatchedSourceCodeFiles = student.unmatchedSourceCodeFiles;
    }

    void refactorErrorCategory(Error error, ErrorCategory category) {
        if (error == null || category == null) {
            log.error("error and category must not be null");
            throw new NullPointerException();
        }

        for (Remark note : remarks) {
            Error e = note.getError();
            if (e != null && e.equals(error)) {
            	category.addError(e);
            }
        }
    }

    void refactorError(Error oldError, Error newError) {
        if (oldError == null || newError == null) {
            log.error("oldError and newError must not be null");
            throw new NullPointerException();
        }

        
        for (Remark note : remarks) {
            Error e = note.getError();
            if (e != null && e.equals(oldError)) {
                e.refactor(newError);
            }
        }
        
    }

    private File createReviewFile(File reviewDir, File taskFile)
            throws FileNotFoundException, IOException {
        if (taskFile == null) {
            return null;
        }

        String taskFileName = taskFile.getName();
        String taskFileNameNoExtension = Utils.removeExtension(taskFileName);
        String taskFileExtension = Utils.getExtension(taskFileName);
        String reviewed = " (Pregledano)";
        String reviewFilePath = reviewDir.getPath() + "/"
                + taskFileNameNoExtension + reviewed + taskFileExtension;

        Utils.copyFile(taskFile, reviewFilePath);

        File reviewFile = new File(reviewFilePath);
        return reviewFile;
    }

    private void insertComments(StudentTask studentTask, File reviewFile)
            throws IOException {
        if (studentTask == null || reviewFile == null) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(reviewFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        fis))) {
            int lineNumber = 0;
            String line;

            StringBuilder editedFileText = new StringBuilder();

            List<Remark> studentNotes;

            // Read reviewFile Line By Line
            while ((line = br.readLine()) != null) {
                studentNotes = getNotes(studentTask, lineNumber);

                for (Remark note : studentNotes) {
                    editedFileText.append("/*\r\n");
                    editedFileText.append(note);
                    editedFileText.append("\r\n/\r\n");
                }

                editedFileText.append(line);
                editedFileText.append("\r\n");
                lineNumber++;
            }
            br.close();

            // Now editedFileText will have updated content , which we override
            // into file
            FileWriter fw = new FileWriter(reviewFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(editedFileText.toString());
            bw.close();
        }
    }

    private void generateReviewTaskFile(File reviewDir,
            StudentTask studentTask, File taskFile)
            throws FileNotFoundException, IOException {
        if (taskFile == null) {
            return;
        }

        File reviewFile = createReviewFile(reviewDir, taskFile);
        insertComments(studentTask, reviewFile);
    }

    @SuppressWarnings("unused")
	private List<Remark> getNotes(StudentTask studentTask, int lineNumber) {
        List<Remark> resultNotes = new ArrayList<Remark>();

        for (Remark note : remarks) {
            if (note.getStudentTask().equals(studentTask)
                    && note.getStartRow() == lineNumber) {
                resultNotes.add(note);
            }
        }

        return resultNotes;
    }

    private String getReport() {
        String report = "";
        for (StudentTask studentTask : studentTasks) {
            report += "*** " + studentTask.getName() + "\r\n";
            report += studentTask.getScoreAsAbsolute() + "/" + studentTask.getValue() + "\r\n\r\n";
        }
        
        report += "===============\r\n";        
        report += "##Poena: " + getScoreAsAbsolute() + "/" + 100;
        
        return report;
    }
}*/
