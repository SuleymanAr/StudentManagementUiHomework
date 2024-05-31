package application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseManagement {
    private static List<Course> courses = new ArrayList<>();
    private static List<Student> students = new ArrayList<>();
    public static Map<Student, Map<Course, List<Integer>>> studentGrades = new HashMap<>(); // Student -> (Course -> List of Grades)

    

    
    public static Map<Course, List<Integer>> getGrades(Student student) {
    	return studentGrades.get(student);
    }
    public static void addCourse(String courseCode, String name, int maxCapacity) {
    	if (isCourseCodeExists(courseCode)) {
            System.out.println("Course with code '" + courseCode + "' already exists.");
            return; 
        }
    	Course newCourse = new Course(courseCode, name, maxCapacity);
        courses.add(newCourse);
        System.out.println("Course added successfully!");
    }
    
    private static boolean isCourseCodeExists(String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equals(courseCode)) {
                return true; // Course code already exists
            }
        }
        return false; // Course code does not exist
    }
    
    public static void addStudent(String name) {
        Student newStudent = new Student(name);
        students.add(newStudent);
        System.out.println("Student added successfully with ID: " + newStudent.getId());
    }
    
    public static Student getStudentById(int studentId) {
        for (Student student : students) {
            if (student.getId() == studentId) {
                return student; 
            }
        }
        return null; 
    }
    

    public static Course getCourseByCode(String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equals(courseCode)) {
                return course; 
            }
        }
        return null; 
    }
    
    public static List<Student> getStudents() {
        return students;
    }
    
    public static List<Course> getCourses() {
        return courses;
    }
    
    public static void assignGrade(Student student, Course course, int grade) {
        if (!studentGrades.containsKey(student)) {
            studentGrades.put(student, new HashMap<>());
        }
        
        Map<Course, List<Integer>> studentCourseGrades = studentGrades.get(student);
        if (!studentCourseGrades.containsKey(course)) {
            studentCourseGrades.put(course, new ArrayList<>());
        }
        
        studentCourseGrades.get(course).add(grade);
        System.out.println("Grade " + grade + " assigned to " + student.getName() + " for course " + course.getName());
    }
    
    public static List<Integer> getGrades(Student student, Course course) {
        if (studentGrades.containsKey(student)) {
            Map<Course, List<Integer>> studentCourseGrades = studentGrades.get(student);
            if (studentCourseGrades.containsKey(course)) {
                return studentCourseGrades.get(course);
            }
        }
        return new ArrayList<>(); // Return an empty list if no grades recorded for the student in the course
    }
    
    public static double calculateAverageGrade(Student student, Course course) {
        List<Integer> grades = getGrades(student, course);
        if (!grades.isEmpty()) {
            int total = 0;
            for (int grade : grades) {
                total += grade;
            }
            return (double) total / grades.size();
        }
        return 0.0; // Return 0.0 if no grades recorded for the student in the course
    }


}

