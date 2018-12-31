package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Grades {
    private final StringProperty courseID;
    private final StringProperty studentID;
    private final IntegerProperty grade;

    public Grades(String courseID, String studentID, Integer grade) {
        this.courseID = new SimpleStringProperty(courseID);
        this.studentID = new SimpleStringProperty(studentID);
        this.grade = new SimpleIntegerProperty(grade);
    }

    public String getCourseID() {
        return courseID.get();
    }

    public StringProperty courseIDProperty() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID.set(courseID);
    }

    public String getStudentID() {
        return studentID.get();
    }

    public StringProperty studentIDProperty() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID.set(studentID);
    }

    public int getGrade() {
        return grade.get();
    }

    public IntegerProperty gradeProperty() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade.set(grade);
    }
}
