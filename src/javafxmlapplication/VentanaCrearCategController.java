/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;

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
    
    private Consumer<String> categoryAddedCallback;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initially disable the add button
        bAnadir.setDisable(true);

        // Add listener to the cajaNombre text field
        cajaNombre.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            checkFields();
        });
    }
    
    public void setCategoryAddedCallback(Consumer<String> callback) {
        this.categoryAddedCallback = callback;
    }

    private void checkFields() {
        // Enable bAnadir if cajaNombre is not empty
        boolean isDisabled = cajaNombre.getText().isEmpty();
        bAnadir.setDisable(isDisabled);
    }   

    @FXML
    private void anadirCategoria(ActionEvent event) throws AcountDAOException, IOException{
        String nombre = cajaNombre.getText().trim();
        String descripcion = cajaDesc.getText().trim();
        Acount acount = Acount.getInstance();
        // Intentar registrar la categoría
        boolean success = acount.registerCategory(nombre, descripcion);

        if (success) {
            if (categoryAddedCallback != null) {
                categoryAddedCallback.accept(nombre);
            }
            Stage stage = (Stage) bAnadir.getScene().getWindow();
            stage.close();
        } else {
            // Mostrar el error y vaciar la cajaNombre
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
    
}
