import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Course;
import model.Grades;
import util.DBUtil;

import java.sql.*;

public class InstructorController {
    Connection conn;

    @FXML
    private TextField txCourseID;
    @FXML
    private TextField txCourseName;
    @FXML
    private TextField txQuota;
    @FXML
    private TableView<Course> twInstructor;
    @FXML
    private TableColumn<Course, String> instCourseID;
    @FXML
    private TableColumn<Course, String> instCourseName;
    @FXML
    private TableColumn<Course, String> instQuota;
    @FXML
    private ComboBox<String> choseeCourse;

    @FXML
    private TableView<Grades> tableViewStudents;
    @FXML
    private TableColumn<Grades, String> StudentsTWCourseID;
    @FXML
    private TableColumn<Grades, String> StudentsTWStudentID;
    @FXML
    private TableColumn<Grades, String> StudentsTWGrade;

    @FXML
    private TextField txStudentID;
    @FXML
    private TextField txGrade;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        //Set cell value factory to tableview
        instCourseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        instCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        instQuota.setCellValueFactory(new PropertyValueFactory<>("courseQuota"));
        StudentsTWCourseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        StudentsTWStudentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        StudentsTWGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        updateCourseTable();
        updateStudentTable();

        choseeCourse.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateStudentTable();
            }
        });

    }

    public void btnCourseAdding(ActionEvent event) throws SQLException, ClassNotFoundException {
        String courseID = txCourseID.getText();
        String courseName = txCourseName.getText();
        String quota = txQuota.getText();

        if (!txCourseID.getText().isEmpty() && !txCourseName.getText().isEmpty() && !txQuota.getText().isEmpty()) {
            conn = DBUtil.dbConnect();

            String hasIt = "SELECT * FROM courses WHERE CourseID = '" + courseID + "'";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(hasIt);
            if (resultSet.next()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("No allowed duplicate courses.");
                alert.showAndWait();
                txCourseID.setText("");
                txCourseName.setText("");
                txQuota.setText("");
                resultSet.close();
                statement.close();
            } else {
                String query = "INSERT INTO courses (CourseID, CourseName, Quota) VALUES('" + courseID + "', '" + courseName + "', '" + quota + "')";
                Statement stmt = conn.createStatement();
                stmt.execute(query);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Eklendi");
                alert.showAndWait();
                txCourseID.setText("");
                txCourseName.setText("");
                txQuota.setText("");
                stmt.close();
            }
            conn.close();
        } else

        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Field must be filled.");
            alert.showAndWait();

        }

        updateCourseTable();

    }

    public void updateCourseTable() {
        ObservableList<Course> data = FXCollections.observableArrayList();
        ObservableList<String> courses = FXCollections.observableArrayList();
        try {

            conn = DBUtil.dbConnect();
            String query = "SELECT * FROM courses";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                data.add(new Course(rs.getString(1), rs.getString(2), rs.getString(3)));
                courses.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        twInstructor.setItems(data);
        choseeCourse.setItems(courses);
        choseeCourse.getSelectionModel().selectFirst();
    }

    public void updateStudentTable() {
        ObservableList<Grades> data = FXCollections.observableArrayList();
        try {

            conn = DBUtil.dbConnect();
            String query = "SELECT * FROM grades WHERE CourseID ='" + choseeCourse.getSelectionModel().getSelectedItem() + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                data.add(new Grades(rs.getString(1), rs.getString(2), rs.getInt(3)));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tableViewStudents.setItems(data);
    }

    public void btnUpdateGrade(ActionEvent event) throws SQLException, ClassNotFoundException {
        conn = DBUtil.dbConnect();
        if (!txStudentID.getText().isEmpty() && !txGrade.getText().isEmpty()) {
            String query = "SELECT * FROM grades WHERE CourseID = ? AND StudentID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, choseeCourse.getSelectionModel().getSelectedItem());
            ps.setString(2, txStudentID.getText());
            ResultSet rs1 = ps.executeQuery();
            if (rs1.next()) {
                String query2 = "UPDATE grades SET Grade = ? WHERE StudentID = ? AND CourseID = ?";
                PreparedStatement ps2 = conn.prepareStatement(query2);
                ps2.setString(1, txGrade.getText());
                ps2.setString(2, txStudentID.getText());
                ps2.setString(3, choseeCourse.getSelectionModel().getSelectedItem());
                int rs2 = ps2.executeUpdate();
                if (rs2 == 1) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Grade is updated.");
                    alert.showAndWait();
                }
                ps2.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("This student doesn't take this course.");
                alert.showAndWait();
            }
            ps.close();
            rs1.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Field must be filled.");
            alert.showAndWait();
        }
        conn.close();
        updateStudentTable();


    }


}
