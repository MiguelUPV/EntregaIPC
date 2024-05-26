/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;

/**
 * 
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {
    private Label labelMessage;
    private String Miguel;
    @FXML
    private TextField cajaUser;
    @FXML
    private Button bIniciar;
    @FXML
    
    
    private PasswordField cajaPassword;
    @FXML
    private Hyperlink linkReg;
    
    private static final int MAX_CHARACTERS = 20;
    @FXML
    private Text errorContrasena;
    @FXML
    private Text errorUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String normalStyle = "-fx-background-color: navy; -fx-text-fill: white;";
        String hoverStyle = "-fx-background-color: #3486eb; -fx-text-fill: white;";
        
        cajaUser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaUser.setText(oldValue);
            }
            if (newValue.length() > MAX_CHARACTERS) {
                cajaUser.setText(newValue.substring(0, MAX_CHARACTERS));
            }
        });

        cajaPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaPassword.setText(oldValue);
            }
            if (newValue.length() > MAX_CHARACTERS) {
                cajaPassword.setText(newValue.substring(0, MAX_CHARACTERS));
            }
        });
        
        BooleanBinding bb = Bindings.createBooleanBinding(
                () -> cajaUser.getText().isEmpty() || cajaPassword.getText().isEmpty(),
                cajaUser.textProperty(),
                cajaPassword.textProperty()
        );
        bIniciar.disableProperty().bind(bb);
    }

    @FXML
    private void IniciarSesion(ActionEvent event) throws IOException, AcountDAOException {
        String username = cajaUser.getText();
        String password = cajaPassword.getText();
        
        
        Acount acount = Acount.getInstance();
        
        if (!acount.existsLogin(username)) {
            errorUser.setVisible(true);
            cajaUser.clear();
        } else {
            errorUser.setVisible(false);
            boolean loggedIn = acount.logInUserByCredentials(username, password);
        
        if (loggedIn) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VentanaPrincipal.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) bIniciar.getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        } else {
            errorContrasena.setVisible(true);
            cajaPassword.clear();
        }
        }
        
    }

    @FXML
    private void ventanaReg(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VentanaRegistro.fxml"));
        Parent root = loader.load();

        VentanaRegistroController controller = loader.getController();

        Scene scene = new Scene(root);
        Stage stage = (Stage) linkReg.getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void accionarBotonIniciarConEnter(KeyEvent event) throws IOException {
        if (KeyCode.ENTER == event.getCode()) {
            try {
                IniciarSesion(null);
            } catch (IOException | AcountDAOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void noResaltar(MouseEvent event) {
        String normalStyle = "-fx-background-color: navy; -fx-text-fill: white;";
        bIniciar.setStyle(normalStyle);
    }

    @FXML
    private void resaltar(MouseEvent event) {
        String hoverStyle = "-fx-background-color: #3486eb; -fx-text-fill: white;";
        bIniciar.setStyle(hoverStyle);
    }
}

