/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Category;

/**
 * FXML Controller class
 *
 * @author Rubén
 */
public class VentanaAnadirGastoController implements Initializable {

    @FXML
    private TextField cajaNombre;
    @FXML
    private ChoiceBox<String> choiceCateg;
    @FXML
    private TextField cajaCantidad;
    @FXML
    private DatePicker cajaFecha;
    @FXML
    private TextArea cajaDesc;
    @FXML
    private Button bSubirFoto;
    @FXML
    private Button bAnadir;
    @FXML
    private Button bCancelar;
    @FXML
    private Button bCrearCateg;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bAnadir.setDisable(true);

        // Add listeners to text fields, date picker and choice box
        cajaNombre.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        cajaCantidad.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        cajaFecha.valueProperty().addListener((observable, oldValue, newValue) -> checkFields());
        choiceCateg.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> checkFields());
        
        populateCategories();
        
        
    }   
    
    private void populateCategories() {
        Acount acount;
        try {
            acount = Acount.getInstance();
            List<Category> userCategories = acount.getUserCategories();
            if (userCategories != null) {
                List<String> categoryNames = userCategories.stream().map(Category::getName).collect(Collectors.toList());
                choiceCateg.getItems().setAll(categoryNames);
            }
        } catch (AcountDAOException | IOException ex) {
            Logger.getLogger(VentanaAnadirGastoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private void checkFields() {
        // Check if all required fields are filled
        boolean isDisabled = cajaNombre.getText().isEmpty() ||
                             cajaCantidad.getText().isEmpty() ||
                             cajaFecha.getValue() == null ||
                             choiceCateg.getSelectionModel().getSelectedItem() == null;
        bAnadir.setDisable(isDisabled);
    }

    @FXML
    private void subirRecibo(ActionEvent event) {
    }

    @FXML
    private void anadirGasto(ActionEvent event) {
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void crearCateg(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaCrearCateg.fxml"));
        Parent root = fxmlLoader.load();

        
        VentanaCrearCategController crearCategController = fxmlLoader.getController();
        crearCategController.setCategoryAddedCallback(categoryName -> {
            choiceCateg.getItems().add(categoryName);
            choiceCateg.getSelectionModel().select(categoryName);
        });
        
            // Crear una nueva ventana (stage) para la ventana emergente
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal mientras la ventana emergente está abierta
        stage.setTitle("Crear Categoría");
        stage.setScene(new Scene(root));

            // Mostrar la ventana emergente y esperar a que se cierre antes de continuar
        stage.showAndWait();
    }

    private void desplegarCateg(MouseDragEvent event) {
        populateCategories();
    }

    @FXML
    private void elegirCategoria(MouseEvent event) {
    }
    
}
