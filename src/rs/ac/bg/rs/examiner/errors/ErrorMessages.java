package rs.ac.bg.rs.examiner.errors;

import java.util.HashSet;

import rs.ac.bg.rs.examiner.labexercise.*;

public class ErrorMessages {
	private static String newline = System.lineSeparator();
	public static String NULL_ARGUMENT = "Argmument must not be null.";
	public static String NON_POSITIVE_VALUE = "Value must be greater than zero.";
	public static String ALREADY_IN_COLLECTION = "Object is already contained in collection.";
	public static String INVALID_PERCENTAGE_VALUE = "Percentage value must be in interval [0, 100].";
	public static String INVALID_SCORE_VALUE = "Score cannot be less then zero, or bigger then total value.";
	public static String INVALID_FILE_EXTENSION = "Filepath does not end with required extension.";
	public static String INVALID_STATE = "Object's state is not valid.";
	public static String INVALID_ASSIGNMENT_DIRECTORY = "Student's assignment directory must have a parent"
														+ " directory which denotes student's group.";
	public static String FILE_DOES_NOT_EXIST = "File does not exist.";
	public static String INVALID_ERROR_POSITION = "Error cannot be marked due to invalid code selection.";
	
	public static String errorMessage(Task task) {
		if (task == null) {
			return "";
		}
		
		String message = "";
        if (task.getItems().size() <= 0) {
            message += "Task \'" + task.getName() + "\'";
            message += task.getParent() != null ? " in Assignment \'" + task.getParent().getName() + "\'" : "";
            message += " contains no items." + newline;
        }

        if (task.getValue() <= 0) {
        	message += "Task \'" + task.getName() + "\'";
            message += task.getParent() != null ? " in Assignment \'" + task.getParent().getName() + "\'" : "";
            message += "\' has non-positive value." + newline;
        }

        return message;
	}
	
	public static String errorMessage(Assignment assignment) {
		String message = "";
        if (assignment.getTasks().size() <= 0) {
            message += "Assignment \'" + assignment.getName() + "\' contains no tasks." + newline;
        }

        if (assignment.getValue() != 100) {
            message += "Assignment \'" + assignment.getName()
                    + "\' does not have a value of 100p." + newline;
        }

        for (Task task : assignment.getTasks()) {
            message += ErrorMessages.errorMessage(task);
        }

        return message;
	}
	
	public static String errorMessage(LabExercise labExercise) {
		String message = "";
        if (labExercise.getAssignments().size() <= 0) {
            message += "LabExercise contains no assignments." + newline;
        }

        for (Assignment assignment : labExercise.getAssignments()) {
            message += ErrorMessages.errorMessage(assignment);
        }

        // checking for duplicates
        HashSet<Assignment> assignmentSet = new HashSet<Assignment>();
        for (Assignment assignment : labExercise.getAssignments()) {
            if (!assignmentSet.add(assignment)) {
                message += "Assignment \'"
                        + assignment.getName()
                        + "\' is a duplicate. Please rename it, or remove it from LabExercise." + newline;
            }
        }

        return message;
	}
}
