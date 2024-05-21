/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;

/**
 * FXML Controller class
 *
 * @author Rubén
 */
public class VentanaRegistroController implements Initializable {

    @FXML
    private TextField cajaName;
    @FXML
    private TextField cajaUser;
    @FXML
    private Text errorUser;
    @FXML
    private TextField cajaCorreo;
    @FXML
    private Text errorCorreo;
    @FXML
    private PasswordField cajaPassword;
    @FXML
    private PasswordField cajaRepetir;
    @FXML
    private Button bRegist;
    @FXML
    private Button bCancel;
    @FXML
    private Button bFoto;
    @FXML
    private TextField cajaApellido;
    @FXML
    private Text errorPassword;
    @FXML
    private Text errorRepetir;
    private Text nombreImagen;
    
    public Image fotoPerfil = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cajaUser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaUser.setText(oldValue);
            }
        });
        
        cajaName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaName.setText(oldValue);
            }
        });
        
        cajaApellido.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaApellido.setText(oldValue);
            }
        });

        cajaPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaPassword.setText(oldValue);
            }
        });
        
        // Listeners para evitar espacios en cajaCorreo y cajaRepetir
        cajaCorreo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaCorreo.setText(oldValue);
            }
        });

        cajaRepetir.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaRepetir.setText(oldValue);
            }
        });
        
        // BooleanBinding para habilitar/deshabilitar el botón bRegist
        BooleanBinding bb = Bindings.createBooleanBinding(
                () -> cajaName.getText().isEmpty() ||
                      cajaApellido.getText().isEmpty() || // Añadido para cajaApellido
                      cajaUser.getText().isEmpty() ||
                      cajaCorreo.getText().isEmpty() ||
                      cajaPassword.getText().isEmpty() ||
                      cajaRepetir.getText().isEmpty(),
                cajaName.textProperty(),
                cajaApellido.textProperty(), // Añadido para cajaApellido
                cajaUser.textProperty(),
                cajaCorreo.textProperty(),
                cajaPassword.textProperty(),
                cajaRepetir.textProperty()
        );
        bRegist.disableProperty().bind(bb);    }

    @FXML
    private void registrarse(ActionEvent event) throws AcountDAOException {
        
        try {
            String name = cajaName.getText();
            String apellido = cajaApellido.getText();
            String user = cajaUser.getText();
            String password = cajaPassword.getText();
            String repetida = cajaRepetir.getText();
            String correo = cajaCorreo.getText();
            LocalDate fechaActual = LocalDate.now();
        
            Acount acount = Acount.getInstance();
            
            if (acount.existsLogin(user)) {
                errorUser.setVisible(true);
                cajaUser.setText("");
            } else {
                errorUser.setVisible(false);
                
                if (!validarCorreo(correo)) {
                    errorCorreo.setVisible(true);
                    cajaCorreo.setText("");
                } else {
                    errorCorreo.setVisible(false);
                    if (!validarPassword(password)) {
                        errorPassword.setVisible(true);
                        cajaPassword.setText("");
                    } else {
                        errorPassword.setVisible(false);
                        
                        if (!password.equals(repetida)) {
                            errorRepetir.setVisible(true);
                            cajaRepetir.setText("");
                        } else {
                            errorRepetir.setVisible(false);
                            
                            if (acount.registerUser(name, apellido, correo, user, password, fotoPerfil, fechaActual)) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText("Usuario creado correctamente");
                                alert.setContentText("Inicie sesión para acceder a la aplicación");
                                alert.showAndWait();
                                
                                
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
                                Parent root = loader.load();
                                Stage stage = (Stage) bRegist.getScene().getWindow();
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();
                            }           
                        }
                    }
                
                
                }
                
                    
                
                
            }
        } catch (IOException ex) {
            Logger.getLogger(VentanaRegistroController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        
         
    }

    @FXML
    private void cancelar(ActionEvent event) throws IOException {
        
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
            Parent root = loader.load();

            // Obtener el escenario actual (Stage)
            Stage stage = (Stage) bCancel.getScene().getWindow();

            // Configurar la nueva escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
    }
    
    private boolean validarCorreo(String s) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (s == null)
            return false;
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
    
    private boolean validarPassword(String password) {
        return 6 <= password.length();
    }

    @FXML
    private void subirFoto(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Seleccionar Imagen");
    fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
    );

    File file = fileChooser.showOpenDialog(bFoto.getScene().getWindow());
    if (file != null) {
        try {
            Image fotoPerfil = new Image(file.toURI().toString());
            // Verificar si la imagen se cargó correctamente
            if (fotoPerfil.isError()) {
                throw new IOException("No se pudo cargar la imagen");
            }
            nombreImagen.setText(file.getName());
            nombreImagen.setVisible(true);
            // Usa 'this.fotoPerfil' si 'fotoPerfil' es una variable de clase
            // this.fotoPerfil = fotoPerfil;
        } catch (IOException e) {
            // Manejar la excepción, por ejemplo, mostrar un mensaje de error
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
    }
}
    
   
    
    
}
