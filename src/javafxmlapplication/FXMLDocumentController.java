/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private Label textError;
    @FXML
    private PasswordField cajaPassword;
    @FXML
    private Hyperlink linkReg;
    //=========================================================
    // event handler, fired when button is clicked or 
    //                      when the button has the focus and enter is pressed
    private void handleButtonAction(ActionEvent event) {
        labelMessage.setText("Hello, this is your first JavaFX project - IPC");
    }
    
    //=========================================================
    // you must initialize here all related with the object 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
      
    
        
      cajaUser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaUser.setText(oldValue);
            }
        });

        cajaPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaPassword.setText(oldValue);
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
        boolean loggedIn = acount.logInUserByCredentials(username, password);
         
        
        // Llamar al método logInUserByCredentials para verificar las credenciales
        
        
        if (loggedIn) {
            // Abrir la ventana principal si el inicio de sesión es exitoso
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VentanaPrincipal.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) bIniciar.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            // Mostrar mensaje de error si el inicio de sesión falla
            textError.setText("El usuario o contraseña son incorrectos");
            textError.setVisible(true);
            // Vaciar los campos de texto y contraseña
            cajaUser.clear();
            cajaPassword.clear();
        }
    }    


    @FXML
    private void ventanaReg(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VentanaRegistro.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la nueva ventana
        VentanaRegistroController controller = loader.getController();
        // Realizar cualquier configuración adicional si es necesario

        // Crear una nueva escena con la raíz de la nueva ventana
        Scene scene = new Scene(root);
        
        // Obtener la etapa actual
        Stage stage = (Stage) linkReg.getScene().getWindow();

        // Mostrar la nueva ventana
        stage.setScene(scene);
        stage.show();
    
    }
    
}
