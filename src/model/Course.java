package model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {
    private final StringProperty courseID;
    private final StringProperty courseName;
    private final StringProperty courseQuota;

    public Course(String courseID, String courseName, String courseQuota) {
        this.courseID = new SimpleStringProperty(courseID);
        this.courseName = new SimpleStringProperty(courseName);
        this.courseQuota = new SimpleStringProperty(courseQuota);
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

    public String getCourseName() {
        return courseName.get();
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public String getCourseQuota() {
        return courseQuota.get();
    }

    public StringProperty courseQuotaProperty() {
        return courseQuota;
    }

    public void setCourseQuota(String courseQuota) {
        this.courseQuota.set(courseQuota);
    }
}
