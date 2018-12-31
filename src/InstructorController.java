import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Course;
import model.Grades;
import util.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    private  TableColumn<Grades,String> StudentsTWCourseID;
    @FXML
    private TableColumn<Grades,String> StudentsTWStudentID;
    @FXML
    private TableColumn<Grades,String> StudentsTWGrade;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        //Set cell value factory to tableview
        instCourseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        instCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        instQuota.setCellValueFactory(new PropertyValueFactory<>("courseQuota"));
        StudentsTWCourseID.setCellValueFactory(new PropertyValueFactory<>(choseeCourse.getSelectionModel().getSelectedItem()));
        StudentsTWStudentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        StudentsTWGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        updateTable();

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

        updateTable();

    }

    public void updateTable() {
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

}
