/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Category;

/**
 * FXML Controller class
 *
 * @author 34744
 */
public class VentanaModificarGastoController implements Initializable {

    @FXML
    private TextField nuevo_Nombre;
    @FXML
    private ChoiceBox<Category> nueva_Categoria;
    @FXML
    private TextField nueva_Cantidad;
    @FXML
    private TextField nuevas_unidades;
    @FXML
    private TextField nueva_fecha;
    @FXML
    private TextArea nueva_descripción;
    @FXML
    private Button nueva_Foto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
