import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.User;
import util.DBUtil;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminController {
    Connection conn;
    @FXML
    private TableView<User> tblUsers;
    @FXML
    private TableColumn<User, Integer> colId;
    @FXML
    private TableColumn<User,String> colRole;
    @FXML
    private TableColumn<User, String> colUserName;
    @FXML
    private TableColumn<User, String> colLastName;
    @FXML
    private TableColumn<User, String> colFirstName;
    @FXML
    private TableColumn<User, String> colEmail;
    @FXML
    private TableColumn<User, String> colTelephone;



    @FXML
    private TextField txID;
    @FXML
    private ComboBox<String> cbRole;
    @FXML
    private TextField txtSurname;
    @FXML
    private TextField txtUserName;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelephone;
    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUpdateıd;
    @FXML
    private TextField txtUpdateTelephone;

    @FXML
    private TextField txtUpdateEmail;
    @FXML
    private TextArea txtResultConsole;


    private ObservableList<User> data;
    private Main mainApp;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        //Set cell value factory to tableview
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        cbRole.setItems(FXCollections.observableArrayList(new String[]{"Admin","Instructor","Student"}));
        cbRole.getSelectionModel().selectFirst();
    }


    public void btnShowAllUser(ActionEvent actionEvent) {

        data = FXCollections.observableArrayList();


        try {

            conn = DBUtil.dbConnect();
            String query = "SELECT * FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);


            while (rs.next()) {

                data.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
            }


            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        tblUsers.setItems(data);


    }

    public void tblColClick(MouseEvent mouseEvent) {

        if (!tblUsers.getSelectionModel().getSelectedIndices().isEmpty()) {

            txtUpdateEmail.setText(tblUsers.getSelectionModel().getSelectedItem().getEmail().toString());
            txtUpdateTelephone.setText(tblUsers.getSelectionModel().getSelectedItem().getTelephone().toString());
            txtUpdateıd.setText(String.valueOf(tblUsers.getSelectionModel().getSelectedItem().getId()));
        }
    }


    public void btnAddClick(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String ID = txID.getText();
        String role = cbRole.getSelectionModel().getSelectedItem();
        String username = txtUserName.getText();
        String password = txtPassword.getText();
        String firstname = txtName.getText();
        String lastname = txtSurname.getText();

        String email = txtEmail.getText();
        String telephone = txtTelephone.getText();

        if (!txtUserName.getText().isEmpty() && !txtPassword.getText().isEmpty()) {
            conn = DBUtil.dbConnect();
            String isUnique = "SELECT * FROM users WHERE ID = " + ID;
            Statement q2 = conn.createStatement();
            ResultSet resultSet = q2.executeQuery(isUnique);
            if (!resultSet.next()){
                String query = "INSERT INTO users (ID, role, UserName, Password, Name, Surname, Email, Telephone) VALUES ('" + ID + "', '" + role + "', '" + username + "','" + password + "','" + firstname + "','" + lastname + "','" + email + "','" + telephone + "')";
                Statement stmt = conn.createStatement();
                int result = stmt.executeUpdate(query);
                if (result == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Eklendi");
                    alert.showAndWait();
                    txtResultConsole.setText("");
                    txtResultConsole.setText("Kullanıcı Eklendi");
                    txID.setText("");
                    cbRole.getSelectionModel().selectFirst();
                    txtUserName.setText("");
                    txtPassword.setText("");
                    txtName.setText("");
                    txtSurname.setText("");
                    txtEmail.setText("");
                    txtTelephone.setText("");
                    showAllUser();
                }
                stmt.close();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No allowed to duplicates.");
                alert.showAndWait();

            }
            q2.close();
            conn.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Username and password not null");
            alert.showAndWait();

        }


    }


    public void btnSearchClick(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        tblUsers.getItems().clear();


        conn = DBUtil.dbConnect();
        String query = "SELECT * FROM users WHERE ID=" + txtUpdateıd.getText();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {

            data.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
        }


        rs.close();
        stmt.close();
        conn.close();


        tblUsers.setItems(data);

    }


    public void btnUpdateClick(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String email = txtUpdateEmail.getText();
        String telephone = txtUpdateTelephone.getText();

        if (!txtUpdateıd.getText().isEmpty()) {
            conn = DBUtil.dbConnect();
            String query = "UPDATE users SET Email='" + email + "' , Telephone='" + telephone + "' WHERE ID=" + txtUpdateıd.getText();
            Statement stmt = conn.createStatement();
            int result = stmt.executeUpdate(query);
            if (result == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Güncellendi");
                alert.showAndWait();
                showAllUser();
                txtResultConsole.setText("");
                txtResultConsole.setText("Güncelleme yapıldı");
            }


            stmt.close();
            DBUtil.dbDisconnect(conn);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("id not null");
            alert.showAndWait();

        }

    }


    public void btnDeleteClick(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (!txtUpdateıd.getText().isEmpty()) {
            conn = DBUtil.dbConnect();
            String query = "DELETE FROM users  WHERE ID=" + txtUpdateıd.getText();
            Statement stmt = conn.createStatement();
            int result = stmt.executeUpdate(query);
            if (result == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Silindi");
                alert.showAndWait();
                showAllUser();
                txtResultConsole.setText("");
                txtResultConsole.setText("Kullanıcı Silindi");
            }


            stmt.close();
            DBUtil.dbDisconnect(conn);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("id not null");
            alert.showAndWait();
        }
    }


    public void showAllUser() throws SQLException, ClassNotFoundException {

        tblUsers.getItems().clear();

        conn = DBUtil.dbConnect();
        String query = "SELECT * FROM users";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {

            data.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
        }


        rs.close();
        stmt.close();
        DBUtil.dbDisconnect(conn);
        tblUsers.setItems(data);
    }

    public void btnShowInstructors() throws SQLException, ClassNotFoundException {

        tblUsers.getItems().clear();

        conn = DBUtil.dbConnect();
        String query = "SELECT * FROM users WHERE Role='Instructor'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {

            data.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
        }


        rs.close();
        stmt.close();
        tblUsers.setItems(data);
    }

    public void btnShowStudents() throws SQLException, ClassNotFoundException {

        tblUsers.getItems().clear();

        conn = DBUtil.dbConnect();
        String query = "SELECT * FROM users WHERE Role='Student'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {

            data.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
        }


        rs.close();
        stmt.close();
        tblUsers.setItems(data);
    }


    public void editUser(ActionEvent actionEvent) {
        if (!tblUsers.getSelectionModel().getSelectedIndices().isEmpty()) {
            User selectedPerson = tblUsers.getSelectionModel().getSelectedItem();
            showUserEditDialog(selectedPerson);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select user");
            alert.showAndWait();
        }
    }


    public void showUserEditDialog(User user) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/editUser.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.setTitle("Edit Person");
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(page);
            stage.setScene(scene);

            // Set the user into the controller.
            EditUserController controller = loader.getController();

            controller.setUser(user);

            stage.show();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
