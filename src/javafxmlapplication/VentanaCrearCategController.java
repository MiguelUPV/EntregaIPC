/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Category;

/**
 * FXML Controller class
 *
 * @author Rubén
 */
public class VentanaCrearCategController implements Initializable {

    @FXML
    private TextField cajaNombre;
    @FXML
    private TextArea cajaDesc;
    @FXML
    private Button bAnadir;
    @FXML
    private Button Cancelar;
    @FXML
    private Label erroNombre;
    
    private Consumer<Category> categoryAddedCallback;
private static final int MAX_CHARACTERS = 20;
private static final int MAX_CHARACTERSDescripcion = 20;
    String normalStyle = "-fx-background-color: navy; -fx-text-fill: white;";
    String hoverStyle = "-fx-background-color: #3486eb; -fx-text-fill: white;";
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initially disable the add button
        bAnadir.setDisable(true);

        // Add listener to the cajaNombre text field
        cajaNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            checkFields();
            if (newValue.length() > MAX_CHARACTERS) {
                cajaNombre.setText(newValue.substring(0, MAX_CHARACTERS));
            }
        });
        
          cajaDesc.textProperty().addListener((observable, oldValue, newValue) -> {
            checkFields();
            if (newValue.length() > MAX_CHARACTERSDescripcion) {
                cajaDesc.setText(newValue.substring(0, MAX_CHARACTERSDescripcion));
            }
        });
    }
    
    public void setCategoryAddedCallback(Consumer<Category> callback) {
        this.categoryAddedCallback = callback;
    }

    private void checkFields() {
        // Enable bAnadir if cajaNombre is not empty
        boolean isDisabled = cajaNombre.getText().isEmpty();
        bAnadir.setDisable(isDisabled);
    }   

    @FXML
    private void anadirCategoria(ActionEvent event) throws AcountDAOException, IOException {
        String nombre = cajaNombre.getText();
        String descripcion = cajaDesc.getText();
        
        Acount acount = Acount.getInstance();
        boolean registrado = acount.registerCategory(nombre, descripcion);

        if (registrado) {
            // Cerrar la ventana emergente si se registra correctamente
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Categoría creada correctamente");
            alert.setHeaderText(null);
            alert.showAndWait();
            
            Stage stage = (Stage) bAnadir.getScene().getWindow();
            stage.close(); 
        } else {
            // Mostrar mensaje de error y vaciar campos si no se registra correctamente
            erroNombre.setVisible(true);
            cajaNombre.clear();
            cajaDesc.clear();
        }
    
    
}

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) Cancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void noAñ(MouseEvent event) {
        bAnadir.setStyle(normalStyle);
    }

    @FXML
    private void resaltarAñ(MouseEvent event) {
        bAnadir.setStyle(hoverStyle);
    }

    @FXML
    private void resaltarCan(MouseEvent event) {
        Cancelar.setStyle(hoverStyle);
    }

    @FXML
    private void noCan(MouseEvent event) {
        Cancelar.setStyle(normalStyle);
    }
    
}
