package rs.ac.bg.etf.examiner.errors;

import rs.ac.bg.etf.examiner.errors.serializers.*;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.*;

/**
 * Contains all errors, divided into error categories, that can
 * appear in student's assignment. Singleton Design Pattern.
 * 
 * @author Marko Milojevic
 * 
 */
public class ErrorDatabase implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(ErrorDatabase.class.getName());
    private static ErrorDatabase instance;
    private List<ErrorCategory> categories;

    {
    	categories = new ArrayList<ErrorCategory>();
    }
    
    protected ErrorDatabase() {}

    public static ErrorDatabase getInstance() {
        if (instance == null) {
            instance = new ErrorDatabase();
        }

        return instance;
    }
    
    public static void setInstance(ErrorDatabase instance) {
    	if (instance != null) {
    		ErrorDatabase.instance = instance;
    	}
    }

    public List<ErrorCategory> getCategories() {
        return Collections.unmodifiableList(this.categories);
    }

    public ErrorCategory getCategoryByName(String categoryName) {
        if (categoryName == null) {
            return null;
        }

        for (ErrorCategory category : this.categories) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }

        return null;
    }

    public boolean addCategory(ErrorCategory category) {
        if (category == null) {
            return false;
        } else if (containsCategory(category)) {
            log.error(ErrorMessages.ALREADY_IN_COLLECTION);
            throw new IllegalArgumentException();
        }
        
        boolean opResult = this.categories.add(category);
        if (opResult == true) {
        	category.setParent(this);
        }

        return opResult;
    }

    public boolean removeCategory(ErrorCategory category) {
        if (category == null) {
            return false;
        }

        boolean opResult = this.categories.remove(category);
        if (opResult) {
        	category.setParent(null);
        }

        return opResult;
    }
    
    public boolean removeCategoryByName(String categoryName) {
    	ErrorCategory category = getCategoryByName(categoryName);
    	return removeCategory(category);
    }

    public boolean containsCategory(ErrorCategory category) {
        if (category == null) {
            return false;
        }

        return this.categories.contains(category);
    }

    public boolean containsCategoryByName(String categoryName) {
        if (categoryName == null) {
            return false;
        }

        for (ErrorCategory category : this.categories) {
            if (category.getName().equals(categoryName)) {
                return true;
            }
        }

        return false;
    }

    public boolean containsError(Error error) {
        if (error == null || error.getParent() == null) {
            return false;
        }
        
        ErrorCategory category = getCategoryByName(error.getParent().getName());
        return category.containsError(error);
    }
    
    public void sort() {
        Collections.sort(this.categories);

        for (ErrorCategory category : this.categories) {
            category.sort();
        }
    }

    public static void main(String[] args) {
        ErrorDatabase database = ErrorDatabase.getInstance();

        ErrorCategory c1 = new ErrorCategory("Syntax errors");
        Error e1 = new Error(
                "...expected",
                "This error occurs, for example, if you forget a semicolon at the end of a statement or don’t balance parentheses.",
                ErrorSeverity.LOW,
                "if (i > j // Error, unbalanced parentheses\r\n\tmax = i // Error, missing semicolon\r\nelse\r\n\tmax = j;");
        Error e2 = new Error(
                "unclosed string literal",
                "This error occurs if you fail to terminate the literal with quotation marks. Fortunately, the syntax of JAVA requires that a string literal appear entirely on one line so the error message appears on the same line as the mistake. If you need a string literal that is longer than a single line, create two or more literals and concatenate them with +.",
                ErrorSeverity.LOW,
                "String longString =\r\n\t\"This is first half of a long string\" +\r\n\t\"and this is the second half.; //Error, string literal is not properly closed by a double-quote");
        Error e3 = new Error(
                "illegal start of expression",
                "This error occurs when an expression is expected but not found.",
                ErrorSeverity.LOW,
                "if (i > j) ) // Error, extra parenthesis\r\n\tmax = i;");
        Error e4 = new Error(
                "not a statement",
                "This error occurs when a syntactically correct statement does not appear where it should.",
                ErrorSeverity.LOW, "max ; // Error, missing =");
        c1.addError(e1);
        c1.addError(e2);
        c1.addError(e3);
        c1.addError(e4);

        ErrorCategory c2 = new ErrorCategory("Identifiers");
        Error e5 = new Error(
                "cannot find symbol",
                "All identifiers in JAVA must be declared before being used and an inconsistency between the declaration of an identifier and its use will give rise to this error.",
                ErrorSeverity.LOW,
                "int[] a = {1, 2, 3};\r\nint sum = 0;\r\nfor (int i = 0; i < a.length; i++)\r\n\tsum = sum + a[i];\r\nSystem.out.println(\"Last = \" + i); // Error, i not in scope");
        Error e6 = new Error("...is already defined in",
                "An identifier can only be declared once in the same scope.",
                ErrorSeverity.LOW,
                "int sum = 0;\r\ndouble sum = 0.0; // Error, sum already defined");
        Error e7 = new Error(
                "array requiered but... found",
                "This error is caused by attempting to index a variable which is not an array.",
                ErrorSeverity.LOW,
                "int max(int i, int j) {\r\n\tif (i > j) return i;\r\n\telse return j[i]; // Error, j is not an array\r\n}");
        Error e8 = new Error(
                "...has private access in...",
                "It is illegal to access a variable declared private outside its class.",
                ErrorSeverity.LOW, "");
        c2.addError(e5);
        c2.addError(e6);
        c2.addError(e7);
        c2.addError(e8);

        ErrorCategory c3 = new ErrorCategory("Computation");
        Error e9 = new Error(
                "variable... might not have been initialized",
                "An instance variable declared in a class has a default initial value.5 However, a local variable declared within a method does not, so you are required to initialize it before use, either in its declaration or in an assignment statement.",
                ErrorSeverity.LOW,
                "void m(int n) { // n is initialized from the actual parameter\r\n\tint i, j;\r\n\ti = 2; // i is initialized by the assignment\r\n\tint k = 1; // k is initialized in its declaration\r\n\tif (i == n) // OK\r\n\t\tk = j; // Error, j is not initialized\r\n\telse\r\n\t\tj = k;\r\n}");
        Error e10 = new Error(
                "...in... cannot be applied to...",
                "This error message is very common and results from an incompatibility between a method call and the method’s declaration.",
                ErrorSeverity.LOW,
                "void m(int i) { ... }\r\nm(5.5); // Error, the literal is of type double, not int");
        Error e11 = new Error(
                "operator... cannot be applied to ...",
                "Operators are only defined for certain types, although implicit type conversion is allowed between certain numeric types.",
                ErrorSeverity.LOW,
                "int a = 5;\r\nboolean b = true;\r\nint c = a + b; // Error, can’t add a boolean value\r\ndouble d = a + 1.4; // OK, int is implicitly converted to double");
        Error e12 = new Error(
                "possible loss of precision",
                "This error arises when trying to assign a value of higher precision to a variable of lower precision without an explicit type cast.",
                ErrorSeverity.LOW,
                "float sum = 0.0; // Error, literal is not of type float");
        Error e13 = new Error(
                "incompatible types",
                "JAVA checks that the type of an expression is compatible with the type of the variable in an assignment statement and this error will result if they are incompatible.",
                ErrorSeverity.LOW,
                "boolean b = true;\r\nint a = b; // Error, can’t assign boolean to int");
        Error e14 = new Error("inconvertible types",
                "Not every type conversion is legal.", ErrorSeverity.LOW,
                "boolean b = true;\r\nint x = (int) b; // Error, can’t convert boolean to int");
        Error e25 = new Error(
                "InputMismatchException",
                "This exception is thrown by the class Scanner, which is a class introduced into version 5 of JAVA to simplify character-based input to programs.",
                ErrorSeverity.HIGH, "");
        Error e26 = new Error(
                "IllegalFormatException",
                "This exception is thrown by the method format in class String that enables output using format specifiers as in the C language.",
                ErrorSeverity.HIGH, "");
        Error e27 = new Error(
                "NumberFormatException",
                "This exception is thrown by methods declared in the numeric “wrapper” classes such as Integer and Double, in particular by the methods parseInt and parseDouble that convert from strings to the primitive numeric types.",
                ErrorSeverity.HIGH, "");
        Error e28 = new Error("ArithmeticException",
                "This exception is thrown if you attempt to divide by zero.",
                ErrorSeverity.HIGH, "");
        c3.addError(e9);
        c3.addError(e10);
        c3.addError(e11);
        c3.addError(e12);
        c3.addError(e13);
        c3.addError(e14);
        c3.addError(e25);
        c3.addError(e26);
        c3.addError(e27);
        c3.addError(e28);

        ErrorCategory c4 = new ErrorCategory("Return statements");
        Error e15 = new Error(
                "missing return statement",
                "When a method returns a non-void type, every path that leaves the method must have a return-statement, even if there is no way that the path can be executed",
                ErrorSeverity.LOW,
                "int max(int i, int j) {\r\n\tif (i > j) return i;\r\n\telse if (i <= j) return j;\r\n\t// Error: what about the path when i>j and i<=j are both false?!!\r\n}");
        Error e16 = new Error(
                "missing return value",
                "A method returning a type must have a return-statement that includes an expression of the correct type.",
                ErrorSeverity.LOW,
                "int max(int i, int j) {\r\n\treturn; // Error, missing int expression\r\n}");
        Error e17 = new Error(
                "cannot return a value from method whose result type is void",
                "Conversely, a return-statement in a void method must not have an expression.",
                ErrorSeverity.LOW,
                "void m(int i, int j) {\r\n\treturn i + j; // Error, the method was declared void\r\n}");
        Error e18 = new Error(
                "invalid method declaration; return type required",
                "Every method except constructors must have a return type or void specified; if not, this error will arise.",
                ErrorSeverity.LOW, "max(int i, int j) {\r\n\t...\r\n}");
        Error e19 = new Error(
                "unreachable statement",
                "The error can occur if you write a statement after a return statement.",
                ErrorSeverity.LOW,
                "void m(int j) {\r\n\tSystem.out.println(\"Value is \" + j);\r\n\treturn;\r\n\tj++;\r\n}");
        c4.addError(e15);
        c4.addError(e16);
        c4.addError(e17);
        c4.addError(e18);
        c4.addError(e19);

        ErrorCategory c5 = new ErrorCategory("Access to static entities");
        Error e20 = new Error(
                "non-static variable cannot be referenced from a static context",
                "Since the method main is (required to be) static, so must any variable declared in the class that is accessed by the method. Omitting the modifier results in a compile-time error.",
                ErrorSeverity.LOW,
                "class MyClass1 {\r\n\tint field; // Forgot \"static\"\r\n\tstatic void m(int parm) {\r\n\tfield = parm;\r\n}\r\npublic static void main(String[] args) {\r\n\tSystem.out.println(field); // Error, which field?\r\n}");
        Error e21 = new Error(
                "non-static method cannot be referenced from a static context",
                "A non-static method cannot be called from a static method like main; the reason is a bit subtle. When a non-static method like m is executed, it receives as an implicit parameter the reference to an object. (The reference can be explicitly referred to using this.) Therefore, when it accesses variables declared in the class like field, it is clear that the variable is the one associated with the object referenced by this. Thus, in the absence of an object, it is meaningless to call a non-static method from a static method.",
                ErrorSeverity.LOW,
                "class MyClass1 {\r\n\tstatic int field;\r\n\tvoid m(int parm) { // Forgot \"static\"\r\n\tfield = parm; // Error, which field?\r\n}\r\npublic static void main(String[] args) {\r\n\tm(5);\r\n}");
        c5.addError(e20);
        c5.addError(e21);

        ErrorCategory c6 = new ErrorCategory("Out of range");
        Error e22 = new Error(
                "ArrayIndexOutOfRange",
                "This exception is thrown when attempting the index an array a[i] where the index i is not within the values 0 and a.length-1, inclusive.",
                ErrorSeverity.HIGH,
                "final static int SIZE = 10;\r\nint a = new int[SIZE];\r\nfor (int i = 0; i <= SIZE; i++) // Error, <= should be <\r\nfor (int i = 0; i <= a.length; i++) // Better, but still an error");
        Error e23 = new Error(
                "StringIndexOutOfRange",
                "This exception is thrown by many methods of the class String if an index is not within the values 0 and s.length(), inclusive. Note that s.length() is valid and means the position just after the last character in the string.",
                ErrorSeverity.HIGH, "");
        c6.addError(e22);
        c6.addError(e23);

        ErrorCategory c7 = new ErrorCategory("Dereferencing null");
        Error e24 = new Error(
                "NullPointerException",
                "When declaring a variable of a reference type, you only get a variable that can hold a reference to an object of that class. At this point, attempting to access a field or method of the class will cause the exception NullPointerException to be thrown.",
                ErrorSeverity.HIGH,
                "public static void main(String[] args) {\r\n\tMyClass my; // Can hold a reference but doesn’t yet\r\n\tmy.m(5); // Throws an exception\r\n}");
        c7.addError(e24);

        ErrorCategory c8 = new ErrorCategory("Insufficient memory");
        Error e29 = new Error("outOfMemoryError",
                "This exception can be thrown if you run out of memory.",
                ErrorSeverity.HIGH, "int a = new int[1000000000];");
        Error e30 = new Error(
                "StackOverFlowArea",
                "A stack is used to store the activation record of each method that is called; it contains the parameters and the local variables as well as other information such as the return address. Unbounded recursion can cause the Java Virtual Machine to run out of space in the stack.",
                ErrorSeverity.HIGH,
                "int factorial(int n) {\r\n\tif (n == 0) return 1;\r\n\telse return n * factorial(n + 1); // Error, you meant n - 1\r\n}");
        c8.addError(e29);
        c8.addError(e30);

        ErrorCategory c9 = new ErrorCategory("Program execution");
        Error e31 = new Error(
                "NoClassDefFoundError",
                "The interpreter must be able to find the file containing a class with the main method, for example, Test.class. If packages are not used, this must be in the directory where the interpreter is executed. Check that the name of the class is the same as the name of the file without the extension. Case is significant!",
                ErrorSeverity.HIGH, "");
        Error e32 = new Error(
                "NoSuchMethodFoundError: main",
                "This error will occur if there is no method main in the class, or if the declaration is not precisely correct.",
                ErrorSeverity.HIGH, "");
        c9.addError(e31);
        c9.addError(e32);

        ErrorCategory c10 = new ErrorCategory("Assignment and equality");
        Error e33 = new Error(
                "String equality",
                "Always use equals rather than == to compare strings, unless you can explain why the latter is needed.",
                ErrorSeverity.HIGH, "");
        Error e34 = new Error(
                "Assignment of references",
                "To copy an object pointed to by a reference, you can create a new object and pass the old object as a parameter to a constructor.",
                ErrorSeverity.HIGH, "");
        c10.addError(e33);
        c10.addError(e34);

        database.addCategory(c1);
        database.addCategory(c2);
        database.addCategory(c3);
        database.addCategory(c4);
        database.addCategory(c5);
        database.addCategory(c6);
        database.addCategory(c7);
        database.addCategory(c8);
        database.addCategory(c9);
        database.addCategory(c10);

        try {
            ErrorDatabaseSerializer.save("ErrorDB.edb");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
