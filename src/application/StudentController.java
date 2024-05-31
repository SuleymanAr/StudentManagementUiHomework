package application;


import javafx.scene.control.TextField;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class StudentController {

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, Integer> idColumn;

    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, Integer> courseCodeColumn;

    @FXML
    private TableView<Grade> gradeTable;
    @FXML
    private TableColumn<Grade, Integer> gradeColumn;
    
    
    @FXML
    private TextField studentName;
    
    @FXML
    private TextField courseCode;
    @FXML
    private TextField courseName;
    @FXML
    private TextField courseMaxStudents;
    
    @FXML 
    private Text errortext;
    
    @FXML
    private ChoiceBox<String> courseBox;
    
    @FXML
    private TextField studentGradeInput;
    
    @FXML ChoiceBox<String> courseBoxGrades;
    
    
    private int courseMaxStudentsInt;
    
    private ObservableList<String> courseNames = FXCollections.observableArrayList();

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private ObservableList<Grade> gradeList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize the student table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Initialize the course table columns
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));

        // Initialize the grade table columns
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Set the student data to the table
        studentTable.setItems(studentList);

        // Add a listener to update the course and grade tables when a student is selected
        studentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCoursesForStudent(newValue));
        courseTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showGradesForCourse(newValue));
    }
    
    @FXML
    private void handleStudentSubmit() {
        try {
            String studentNameText = studentName.getText();
            if (studentNameText.isEmpty()) {
                throw new IllegalArgumentException("Student name cannot be empty");
            }
            Student newStudent = new Student(studentNameText);
            studentList.add(newStudent);
        } catch (IllegalArgumentException ex) {
            errortext.setText("Error: " + ex.getMessage());
        }
    }

    
    @FXML
    private void handleCourseSubmit() {
        try {
            String courseNameText = courseName.getText();
            String courseCodeText = courseCode.getText();
            if (courseNameText.isEmpty() || courseCodeText.isEmpty()) {
                throw new IllegalArgumentException("Course name and code cannot be empty");
            }
            courseMaxStudentsInt = Integer.parseInt(courseMaxStudents.getText());
            Course newCourse = new Course(courseCodeText, courseNameText, courseMaxStudentsInt);
            courseList.add(newCourse);
            errortext.setText("Course Created");
            courseNames.clear();
            courseList.forEach(course -> courseNames.add(course.getName()));
            courseBox.setItems(courseNames);
        } catch (NumberFormatException ex) {
            errortext.setText("Error: Invalid maximum number of students");
        } catch (IllegalArgumentException ex) {
            errortext.setText("Error: " + ex.getMessage());
        }
    }


    
    @FXML
    private void assignToCourse() {
        try {
            String courseName = courseBox.getValue();
            if (courseName == null) {
                throw new IllegalArgumentException("Please select a course");
            }

            Course foundCourse = Course.findCourseByName(courseName, courseList);
            if (foundCourse == null) {
                throw new IllegalArgumentException("Selected course does not exist");
            }
            Student foundStudent = studentTable.getSelectionModel().getSelectedItem();
            if (foundStudent == null) {
                throw new IllegalArgumentException("Student with the provided ID not found");
            }
            Student.enrollStudent(foundStudent, foundCourse);
            courseNames.clear();
            
            courseList.forEach(course -> courseNames.add(course.getName()));
            courseBoxGrades.setItems(courseNames);
        } catch (NumberFormatException ex) {
            errortext.setText("Error: Invalid student ID");
        } catch (IllegalArgumentException ex) {
            errortext.setText("Error: " + ex.getMessage());
        }
    }

    
    @FXML
    private void assignGrade() {
        try {
            String courseName = courseBoxGrades.getValue();
            if (courseName == null) {
                throw new IllegalArgumentException("Please select a course");
            }
            Student studentId = studentTable.getSelectionModel().getSelectedItem();
            Course foundCourse = Course.findCourseByName(courseName, courseList);
            if (foundCourse == null) {
                throw new IllegalArgumentException("Selected course does not exist");
            }
            Student foundStudent = studentTable.getSelectionModel().getSelectedItem();
            if (foundStudent == null) {
                throw new IllegalArgumentException("Please select a student");
            }
            int courseGrade = Integer.parseInt(studentGradeInput.getText());
            if (courseGrade < 0 || courseGrade > 100) {
                throw new IllegalArgumentException("Grade must be between 0 and 100");
            }
            foundCourse.addGrade(foundStudent, courseGrade);
            courseNames.clear();
            
            courseList.forEach(course -> courseNames.add(course.getName()));
            courseBoxGrades.setItems(courseNames);
            showGradesForCourse(foundCourse);
        } catch (NumberFormatException ex) {
            errortext.setText("Error: Invalid input for grade");
        } catch (IllegalArgumentException ex) {
            errortext.setText("Error: " + ex.getMessage());
        }
    }



    private void showCoursesForStudent(Student student) {
        if (student != null) {
            ObservableList<Course> studentCourses = FXCollections.observableArrayList(student.getCourses());
            studentTable.getSelectionModel().getSelectedItem().setCourses(studentCourses);
            courseTable.setItems(studentCourses);
            
            courseList.forEach(course -> courseNames.add(course.getName()));


            
            courseBoxGrades.setItems(courseNames);
            gradeTable.setItems(null); // Clear grades when student selection changes
        } else {
            courseTable.setItems(null);
            gradeTable.setItems(null);
        }
    }
    
    


    private void showGradesForCourse(Course course) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (course != null && selectedStudent != null) {
            List<Integer> grades = course.getGrades(selectedStudent);
            ObservableList<Grade> gradeObjects = FXCollections.observableArrayList();

            // Convert grades to Grade objects
            for (Integer grade : grades) {
                gradeObjects.add(new Grade(grade));
            }

            // Set the items directly without replacing the entire list
            gradeTable.setItems(gradeObjects);
        } else {
            gradeTable.setItems(null);
        }
    }

    // Inner class to represent grades
    public static class Grade {
        private final Integer grade;

        public Grade(Integer grade) {
            this.grade = grade;
        }

        public Integer getGrade() {
            return grade;
        }
    }
}
