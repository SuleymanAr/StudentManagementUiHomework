package application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;

public class Course {
    private String courseCode;
    private String name;
    private int maxCapacity;
    private List<Student> enrolledStudents;
    private Map<Student, List<Integer>> studentGrades;  // Map to store grades for each student
    private static int totalEnrolledStudents = 0;
    
    public Course(String courseCode, String name, int maxCapacity) {
        this.name = name;
        this.courseCode = courseCode;
        this.maxCapacity = maxCapacity;
        this.enrolledStudents = new ArrayList<>();
        this.studentGrades = new HashMap<>();  // Initialize the map
    }
    
    // Get methods
    public String getCourseCode() {
        return courseCode;
    }
    
    public String getName() {
        return name;
    }
    
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }
    
    // Static method to retrieve total number of enrolled students
    public static int getTotalEnrolledStudents() {
        return totalEnrolledStudents;
    }
    
    // Method to enroll a student in the course
    public void enrollStudent(Student student) {
        if (enrolledStudents.size() < maxCapacity) { //If the course has enough places.
            enrolledStudents.add(student);
            studentGrades.put(student, new ArrayList<>());  // Initialize an empty grade list for the student
            totalEnrolledStudents++;//Increase the total enrolled students
            
            System.out.println("Student " + student.getName() + " enrolled in " + this.name);
        } else {
            System.out.println("Course " + this.name + " has reached its maximum capacity.");
        }
    }

    // Method to add a grade for a student
    public void addGrade(Student student, int grade) {
        if (studentGrades.containsKey(student)) {
           if(grade>100 || grade<0 ) {
        	   System.out.println("Invalid Input");
           }
           else {
        	   studentGrades.get(student).add(grade);
           }
        } else {
            System.out.println("Student " + student.getName() + " is not enrolled in " + this.name);
        }
    }

    public List<Integer> getGrades(Student student) {
        return studentGrades.getOrDefault(student, new ArrayList<>());
    }

    public boolean isStudentEnrolled(Student student) {
        return enrolledStudents.contains(student);
    }
    
    public static Course findCourseByName(String name, ObservableList<Course> courses) {
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }
        return null; // Course not found
    }
}
