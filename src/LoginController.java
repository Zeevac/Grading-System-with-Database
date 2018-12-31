
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {

    PreparedStatement pst = null;
    ResultSet resultSet = null;
    Stage prevStage;
    static String userName;
    static String password;

    @FXML
    private ComboBox<String> roles;
    @FXML
    private TextField txtUserName;
    @FXML
    private PasswordField txtPassword;
    static String role;

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }

    public void initialize() {
        roles.setItems(FXCollections.observableArrayList(new String[]{"Admin", "Instructor", "Student"}));
        roles.getSelectionModel().selectFirst();
    }

    public void login(ActionEvent actionEvent) {
        userName = txtUserName.getText();
        password = txtPassword.getText();
        try {
            Connection conn = DBUtil.dbConnect();

           /* String SQL = "SELECT * FROM users WHERE UserName="+userName+" AND Password="+password+"";
            Statement stmt = (Statement) conn.createStatement();
            ResultSet resultSet = (ResultSet) stmt.executeQuery(SQL);

            while (resultSet.next()) {

            }*/
            role = roles.getSelectionModel().getSelectedItem();
            String query = "SELECT * FROM users WHERE Username=? AND Password=? AND role=?";
            pst = conn.prepareStatement(query);
            pst.setString(1, userName);
            pst.setString(2, password);
            pst.setString(3, role);
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString(2).equals("Admin")) {
                    System.out.println("Login Successful");
                    showAdminView();

                } else if (resultSet.getString(2).equals("Instructor")) {
                    System.out.println("Login Successful");
                    showInstructorView();
                } else if (resultSet.getString(2).equals("Student")) {
                    System.out.println("Login Successful");
                    showStudentView();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Form Error!");
                alert.setHeaderText(null);
                alert.setContentText("Username or password or role is wrong");
                alert.show();
            }


            pst.close();
            resultSet.close();
            DBUtil.dbDisconnect(conn);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void showAdminView() {
        try {

            Pane myPane = FXMLLoader.load(getClass().getResource("view/adminController.fxml"));
            Scene scene = new Scene(myPane);
            Stage stage = new Stage();
            stage.setTitle("Admin Page");

            stage.setScene(scene);

            prevStage.close();
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showInstructorView() {
        try {

            Pane myPane = FXMLLoader.load(getClass().getResource("view/InstructorController.fxml"));
            Scene scene = new Scene(myPane);
            Stage stage = new Stage();
            stage.setTitle("Instructor Page");

            stage.setScene(scene);

            prevStage.close();
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showStudentView() {
        try {

            Pane myPane = FXMLLoader.load(getClass().getResource("view/StudentController.fxml"));
            Scene scene = new Scene(myPane);
            Stage stage = new Stage();
            stage.setTitle("Student Page");

            stage.setScene(scene);

            prevStage.close();
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void btn(ActionEvent actionEvent) throws IOException {
//
//        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("view/adminController.fxml"));
//
//        Pane myPane = myLoader.load();
//
//        Stage stage = new Stage();
//
//
//
//        stage.setTitle("Login");
//        stage.setScene(new Scene(myPane));
//        stage.show();

      /*  Parent blah = FXMLLoader.load(getClass().getResource("view/adminController.fxml"));
        Scene scene = new Scene(blah);
        Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();*/


    }

}
