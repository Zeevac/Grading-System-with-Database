import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Course;
import model.Grades;
import model.User;
import util.DBUtil;

import java.sql.*;

public class StudentController {
    Connection conn;
    private ObservableList<Course> data;

    @FXML
    private TableView<Course> twOpenedCourses;
    @FXML
    private TableColumn<Course, String> tcCourseID;
    @FXML
    private TableColumn<Course, String> tcCourseName;
    @FXML
    private TableColumn<Course, String> tcQuota;
    @FXML
    private TextField txCourseID;
    @FXML
    private TableView<Grades> twEnrolled;
    @FXML
    private TableColumn<Grades, String> EnrolledCN;
    @FXML
    private TableColumn<Grades, Integer> tcGrade;

    private String userID;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        //Set cell value factory to tableview
        tcCourseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        tcCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tcQuota.setCellValueFactory(new PropertyValueFactory<>("courseQuota"));
        EnrolledCN.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        tcGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        data = FXCollections.observableArrayList();
        try {

            conn = DBUtil.dbConnect();
            String query = "SELECT * FROM courses";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);


            while (rs.next()) {

                data.add(new Course(rs.getString(1), rs.getString(2), rs.getString(3)));
            }


            rs.close();
            stmt.close();

            String query2 = "SELECT ID FROM users WHERE Username=? AND Password=?";
            PreparedStatement stmt2 = conn.prepareStatement(query2);
            stmt2.setString(1, LoginController.userName);
            stmt2.setString(2, LoginController.password);
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next()) {
                userID = rs2.getString(1);
            }
            rs2.close();
            stmt2.close();

            ObservableList<Grades> data2 = FXCollections.observableArrayList();
            String query4 = "SELECT * FROM grades WHERE StudentID=" + userID;
            Statement stmt4 = conn.createStatement();
            ResultSet rs4 = stmt4.executeQuery(query4);
            while (rs4.next()) {
                data2.add(new Grades(rs4.getString(1),rs4.getString(2), rs4.getInt(3)));
            }
            twEnrolled.setItems(data2);

            rs4.close();
            stmt4.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        twOpenedCourses.setItems(data);
    }

    public void btnRegister(ActionEvent event) throws SQLException, ClassNotFoundException {
        conn = DBUtil.dbConnect();
        String courseID = txCourseID.getText();
        if (!txCourseID.getText().isEmpty()){
            try {
                String query2 = "SELECT * FROM grades WHERE CourseID=? AND StudentID=?";
                PreparedStatement stmt2 = conn.prepareStatement(query2);
                stmt2.setString(1, courseID);
                stmt2.setString(2, userID);
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("This user has already taken this course.");
                    alert.showAndWait();
                }
                else {
                    String query3 = "INSERT INTO grades (CourseID,StudentID,Grade) VALUES('" + courseID + "', '" + userID + "', '" + -1 + "')";
                    Statement stmt3 = conn.createStatement();
                    int result = stmt3.executeUpdate(query3);
                    if (result == 1) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Eklendi");
                        alert.showAndWait();
                        txCourseID.setText("");
                    }
                    stmt3.close();

                }
                stmt2.close();
                ObservableList<Grades> data2 = FXCollections.observableArrayList();
                String query4 = "SELECT * FROM grades WHERE StudentID=" + userID;
                Statement stmt4 = conn.createStatement();
                ResultSet rs4 = stmt4.executeQuery(query4);
                while (rs4.next()) {
                    data2.add(new Grades(rs4.getString(1),rs4.getString(2), rs4.getInt(3)));
                }
                twEnrolled.setItems(data2);

                rs4.close();
                stmt4.close();
                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must enter the course ID.");
            alert.showAndWait();
        }

    }

    public void updateTable() {

    }


}
