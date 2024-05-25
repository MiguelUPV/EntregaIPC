/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.converter.StringConverter;
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
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DateTimeStringConverter;
import javafx.util.converter.LocalDateStringConverter;
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
    private ChoiceBox<Category> choiceCateg;
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
    @FXML
    private Label nombreArchivo;
    private Image recibo = null;
    @FXML
    private TextField cajaUnidades;
    
    
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
        
        UnaryOperator<TextFormatter.Change> filter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("[0-9/]*") && newText.length() <= 10) { // Solo permite números y "/" y máximo 10 caracteres
            return change;
        }
        return null; // Rechazar el cambio si contiene caracteres no permitidos o excede la longitud máxima
    };
    TextFormatter<String> textFormatter = new TextFormatter<>(filter);
    ((TextField) cajaFecha.getEditor()).setTextFormatter(textFormatter);
        
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

    @FXML
    private void anadirGasto(ActionEvent event) {
    // Obtener los valores de los campos de entrada
    String nombre = cajaNombre.getText();
    Category categoria = choiceCateg.getValue();
    
    double cantidad = Double.parseDouble(cajaCantidad.getText());
    LocalDate fecha = cajaFecha.getValue();
    String descripcion = cajaDesc.getText();
    int unidades = Integer.parseInt(cajaUnidades.getText());
    
    // Intentar registrar el cargo/gasto
    try {
        Acount acount = Acount.getInstance();
        // Utilizar el método registerCharge de la clase Acount
        boolean registrado = acount.registerCharge(nombre, descripcion, cantidad, unidades, recibo, fecha, categoria);
        
        if (registrado) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Gasto añadido correctamente");
            alert.setHeaderText(null);
            alert.showAndWait();
            
            
                
            // Si se registra correctamente, cerrar la ventana actual
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            
            
        } else {
            // Si no se registra correctamente, borrar el texto en cajaNombre
            cajaNombre.clear();
        }
    } catch (AcountDAOException | IOException ex) {
        Logger.getLogger(VentanaAnadirGastoController.class.getName()).log(Level.SEVERE, null, ex);
    }
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

    private void desplegarCateg(MouseDragEvent event) throws AcountDAOException, IOException{
        populateCategories();
    }
    
}
    
    
    
    
    
    
    
    

    

