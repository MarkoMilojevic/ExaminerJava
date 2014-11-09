package rs.ac.bg.rs.examiner.errors;

/**
 * Note can be bind to a student's task file, in which case it is FILE_SPECIFIC.
 * If binding is FILE_SPECIFIC, note can contain marked code from that file.
 * 
 * If note is related to a student's implementation of a task in general, it is
 * TASK_SPECIFIC.
 * 
 * If note is related to a student's implementation of an assignment in general,
 * it is ASSIGNMENT_SPECIFIC.
 * 
 * @author Marko Milojevic
 * 
 */
public enum RemarkType {
    FILE_SPECIFIC, TASK_SPECIFIC, ASSIGNMENT_SPECIFIC
}
