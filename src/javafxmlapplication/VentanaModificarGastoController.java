/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;



/**
 * FXML Controller class
 *
 * @author 34744
 */
public class VentanaModificarGastoController implements Initializable {
    @FXML
    private TextField cajaNombre;
    @FXML
    private ChoiceBox<Category> choiceCateg;
    @FXML
    private TextField cajaCantidad;
    @FXML
    private TextField CajaUnidades;
    @FXML
    private DatePicker cajaFecha;
    @FXML
    private TextArea cajaDesc;
    @FXML
    private Label nombreArchivo;
    @FXML
    private Button bSubirFoto;
    @FXML
    private Button bAceptar;
    @FXML
    private Button bCancelar;
    
    Image recibo = null;
    @FXML
    private Button bCrearCateg;
    
    private Charge cargoSelec;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        choiceCateg.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
        if (isNowShowing) {
            try {
                populateCategories();
            } catch (AcountDAOException | IOException e) {
                e.printStackTrace();
            }
        }
        });
        
        UnaryOperator<TextFormatter.Change> filterr = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormater = new TextFormatter<>(filterr);
        cajaCantidad.setTextFormatter(textFormater);
        
        try {
            populateCategories();
        } catch (AcountDAOException | IOException e) {
            e.printStackTrace();
        }
        rellenarCampos();
    }  
    
    private void checkFields() {
        // Check if all required fields are filled
        boolean isDisabled = cajaNombre.getText().isEmpty() ||
                             cajaCantidad.getText().isEmpty() ||
                             cajaFecha.getValue() == null ||
                             CajaUnidades.getText().isEmpty() ||
                             choiceCateg.getSelectionModel().getSelectedItem() == null;
        bAceptar.setDisable(isDisabled);
    }

    @FXML
    private void subirRecibo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.gif"),
        new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            // Guardar la imagen seleccionada en la variable recibo
            recibo = new Image(selectedFile.toURI().toString());
        
            // Mostrar el nombre del archivo en el Label nombreArchivo
            nombreArchivo.setText(selectedFile.getName());
            nombreArchivo.setVisible(true);
        }
    }
    
    private void populateCategories() throws AcountDAOException, IOException{
        List<Category> categorias = Acount.getInstance().getUserCategories();
        ObservableList<model.Category> listaCategorias = FXCollections.observableArrayList(categorias);
        choiceCateg.setItems(listaCategorias);
        choiceCateg.setConverter(new javafx.util.StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getName(): "";
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });
        
        if (cargoSelec != null) {
            choiceCateg.setValue(cargoSelec.getCategory());
        }
    }
    
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void crearCateg(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaCrearCateg.fxml"));
        Parent root = fxmlLoader.load();

        
        VentanaCrearCategController crearCategController = fxmlLoader.getController();
        crearCategController.setCategoryAddedCallback(category -> {
            choiceCateg.getItems().add(category);
            choiceCateg.getSelectionModel().select(category);
        });
        
            // Crear una nueva ventana (stage) para la ventana emergente
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal mientras la ventana emergente está abierta
        stage.setTitle("Crear Categoría");
        stage.setScene(new Scene(root));
        
        stage.setResizable(false);

            // Mostrar la ventana emergente y esperar a que se cierre antes de continuar
        stage.showAndWait();
    }
    
    public void setCargo(Charge c) {
        this.cargoSelec = c;
        rellenarCampos();
    }
    
    private void rellenarCampos() {
        if (cargoSelec != null) {
            cajaNombre.setText(cargoSelec.getName());
            choiceCateg.setValue(cargoSelec.getCategory());
            cajaCantidad.setText(String.valueOf(cargoSelec.getCost()));
            CajaUnidades.setText(String.valueOf(cargoSelec.getUnits()));
            cajaFecha.setValue(cargoSelec.getDate());
            // Suponiendo que la descripción y la imagen son parte de Charge
            cajaDesc.setText(cargoSelec.getDescription());
            recibo = cargoSelec.getImageScan();
            nombreArchivo.setVisible(false);
            
               
        }
    }

    @FXML
    private void guardarModificacion(ActionEvent event) {
        cargoSelec.setName(cajaNombre.getText());
        cargoSelec.setCategory(choiceCateg.getValue());
        cargoSelec.setCost(Double.parseDouble(cajaCantidad.getText()));
        cargoSelec.setDate(cajaFecha.getValue());
        cargoSelec.setDescription(cajaDesc.getText());
        cargoSelec.setUnits(Integer.parseInt(CajaUnidades.getText()));
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Gasto editado correctamente");
            alert.setHeaderText(null);
            alert.showAndWait();
            
            
                
            // Si se registra correctamente, cerrar la ventana actual
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        
        
        
        
    }
    
}
