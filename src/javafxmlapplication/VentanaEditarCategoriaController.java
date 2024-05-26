/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Category;

/**
 * FXML Controller class
 *
 * @author Rubén
 */
public class VentanaEditarCategoriaController implements Initializable {

    @FXML
    private TextField cajaNombre;
    @FXML
    private TextArea cajaDesc;
    @FXML
    private Button bConfirmar;
    @FXML
    private Button Cancelar;
    
    private Category category;
    private static final int MAX_CHARACTERS = 20;
    private static final int MAX_CHARACTERSDescripcion = 20;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bConfirmar.setDisable(true);

        
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

    @FXML
    private void editarCategoria(ActionEvent event) {
        category.setName(cajaNombre.getText());
        category.setDescription(cajaDesc.getText());
        
        Stage stage = (Stage) cajaNombre.getScene().getWindow();
        stage.close();
        
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) Cancelar.getScene().getWindow();
        stage.close();
    }
    
    private void checkFields() {
        
        boolean isDisabled = cajaNombre.getText().isEmpty();
        bConfirmar.setDisable(isDisabled);
    } 
    
    public void setCategory(Category category) {
        this.category = category;
        cajaNombre.setText(category.getName());
        cajaDesc.setText(category.getDescription());
    }
    
}
