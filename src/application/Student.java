package application;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

public class Student {
	private String name;
	private int id;  // Non-static instance variable
    private List<Course> enrolledCourses;
    
    private static int nextId = 1;  // Static variable to track the next available ID
    
	
	
	public Student(String name) {
		this.name = name;
	    this.id = nextId; 
	    nextId++; 
		this.enrolledCourses = new ArrayList<>();

		
	}
	
	
	//Methods to return private variables
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public List<Course> getCourses() {
        return enrolledCourses;
    }

    public void setCourses(List<Course> courses) {
        this.enrolledCourses = courses;
    }

    public static void enrollStudent(Student student, Course course) {
        course.enrollStudent(student);
        student.enrolledCourses.add(course); // Update the list of enrolled courses for the student
    }

    // Method to find a student by ID
    public static Student findStudentById(int id, ObservableList<Student> students) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null; // Return null if no student with the given ID is found
    }
}
