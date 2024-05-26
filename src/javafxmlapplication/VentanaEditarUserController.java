/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.Acount;
import model.AcountDAOException;
import model.User;

/**
 * FXML Controller class
 *
 * @author Rubén
 */
public class VentanaEditarUserController implements Initializable {

    @FXML
    private TextField cajaNombre;
    @FXML
    private TextField cajaCorreo;
    @FXML
    private TextField cajaUser;
    @FXML
    private TextField cajaPassword;
    @FXML
    private TextField cajaRepPassword;
    @FXML
    private Text textoFecha;
    @FXML
    private Button bGuardar;
    @FXML
    private Button bCancelar;
    @FXML
    private Button bFoto;
    
    String nombreAnt = null;
    String usernameAnt = null;
    String correoAnt = null;
    Image nuevaFoto = null;
    Image fotoAnt = null;
    
    
    User usuario = null;
    @FXML
    private ImageView imagenPerfil;
    @FXML
    private Label errorRepPassword;
    @FXML
    private Label errorPassword;
    @FXML
    private Label errorCorreo;
    @FXML
    private Button boton_foto_Nueva;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Acount cuenta = Acount.getInstance();
            usuario = cuenta.getLoggedUser();
        } catch (AcountDAOException ex) {
            Logger.getLogger(VentanaEditarUserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VentanaEditarUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        bGuardar.setDisable(true);
        usernameAnt = usuario.getNickName();
        cajaUser.setText(usernameAnt);
        cajaUser.setEditable(false);
        
        
        nombreAnt = usuario.getName();
        cajaNombre.setText(nombreAnt);
        
        correoAnt = usuario.getEmail();
        cajaCorreo.setText(correoAnt);
        
        textoFecha.setText(usuario.getRegisterDate().toString());
        
        fotoAnt = usuario.getImage();
        
        cajaCorreo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaCorreo.setText(oldValue);
            }
        });
        
        cajaNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaNombre.setText(oldValue);
            }
        });
        
        cajaPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaPassword.setText(oldValue);
            }
        });
        
        cajaRepPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                cajaRepPassword.setText(oldValue);
            }
        });
        
        Image imagenUsuario = usuario.getImage();
        if (imagenUsuario != null) {
            imagenPerfil.setImage(imagenUsuario);
        }
        
        cajaCorreo.textProperty().addListener((observable, oldValue, newValue) -> {
        if (!newValue.isEmpty()) {
            if (!validarCorreo(newValue)) {
                errorCorreo.setVisible(true);
            } else {
                errorCorreo.setVisible(false);
            }
        } else {
            errorCorreo.setVisible(false);
        }
        actualizarEstadoBotonGuardar();
        });
        
        cajaPassword.textProperty().addListener((observable, oldValue, newValue) -> {
        if (!newValue.isEmpty()) {
            if (!validarPassword(newValue)) {
                errorPassword.setVisible(true);
            } else {
                errorPassword.setVisible(false);
            }
        } else {
            errorPassword.setVisible(false);
        }
        actualizarEstadoBotonGuardar();
        });
        
        cajaRepPassword.textProperty().addListener((observable, oldValue, newValue) -> {
        if (!newValue.equals(cajaPassword.getText())) {
            errorRepPassword.setVisible(true);
        } else {
            errorRepPassword.setVisible(false);
        }
        actualizarEstadoBotonGuardar();
        });
        
        cajaNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            actualizarEstadoBotonGuardar();
        });
        
        
        
        
        
    }    

    @FXML
    private void guardarEdit(ActionEvent event) {
        usuario.setName(cajaNombre.getText());
        usuario.setEmail(cajaCorreo.getText());
        usuario.setImage(imagenPerfil.getImage());
        usuario.setPassword(cajaPassword.getText());
        
        
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setHeaderText("Cambios realizados correctamente.");
        alerta.showAndWait();
        
    }

    @FXML
    private void cancelarEdit(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
    alerta.setHeaderText(null);
    alerta.setContentText("¿Estás seguro?");
    ButtonType botonAceptar = new ButtonType("Aceptar", ButtonData.OK_DONE);
    ButtonType botonCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
    alerta.getButtonTypes().setAll(botonAceptar, botonCancelar);

    Optional<ButtonType> resultado = alerta.showAndWait();
    if (resultado.isPresent() && resultado.get() == botonAceptar) {
        // Si el usuario presionó "Aceptar"
        usernameAnt = usuario.getNickName();
        cajaUser.setText(usernameAnt);
        cajaUser.setEditable(false);

        nombreAnt = usuario.getName();
        cajaNombre.setText(nombreAnt);

        correoAnt = usuario.getEmail();
        cajaCorreo.setText(correoAnt);

        cajaPassword.clear();
        cajaRepPassword.clear();
    }
    }

    @FXML
    private void actualizarFotoPerfil(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen de Perfil");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        
        File selectedFile = fileChooser.showOpenDialog(bFoto.getScene().getWindow());
        if (selectedFile != null) {
            nuevaFoto = new Image(selectedFile.toURI().toString());
            imagenPerfil.setPreserveRatio(true);
            double WidthOrig = imagenPerfil.getFitWidth();
            double HeightOrig = imagenPerfil.getFitHeight();
            
            imagenPerfil.setImage(nuevaFoto);
            
            imagenPerfil.setFitWidth(WidthOrig);
            imagenPerfil.setFitHeight(HeightOrig);
        }
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
    
    private void actualizarEstadoBotonGuardar() {
    boolean camposVacios = cajaNombre.getText().isEmpty() ||
                           cajaCorreo.getText().isEmpty() ||
                           cajaPassword.getText().isEmpty() ||
                           cajaRepPassword.getText().isEmpty();

    boolean erroresVisibles = errorCorreo.isVisible() ||
                              errorPassword.isVisible() ||
                              errorRepPassword.isVisible();

    bGuardar.setDisable(camposVacios || erroresVisibles);
}

    @FXML
    private void nuevaFoto(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Seleccionar Imagen");
    fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
    );

    File file = fileChooser.showOpenDialog(boton_foto_Nueva.getScene().getWindow());
    if (file != null) {
        try {
            Image fotoPerfil = new Image(file.toURI().toString());
            imagenPerfil.setImage(fotoPerfil);
            // Verificar si la imagen se cargó correctamente
            if (fotoPerfil.isError()) {
                throw new IOException("No se pudo cargar la imagen");
            }
            //nombreImagen.setText(file.getName());
            //nombreImagen.setVisible(true);
            // Usa 'this.fotoPerfil' si 'fotoPerfil' es una variable de clase
            // this.fotoPerfil = fotoPerfil;
        } catch (IOException e) {
            // Manejar la excepción, por ejemplo, mostrar un mensaje de error
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
    }
    }
    
}
