/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Account;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;
 

/**
 * FXML Controller class
 *
 * @author 34744
 */
public class VentanaHistorialController implements Initializable {

    @FXML
    private Button modificar_Elemento;
    @FXML
    private Button eliminar_Elemento;
    @FXML
    private VBox imprimir_Historial;
    
    private ObservableList<Charge> obsLista = null;
    @FXML
    private TableView<Charge> tableView;
    @FXML
    private TableColumn<Charge, String> colNombre;
    @FXML
    private TableColumn<Charge, Category> colCateg;
    @FXML
    private TableColumn<Charge, Double> colCant;
    @FXML
    private TableColumn<Charge, Integer> colUnidades;
    @FXML
    private TableColumn<Charge, String> colFecha;
    @FXML
    private Button modificar_Elemento1;
    
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        refreshTable();


        // Set up the cell factory for the category column
        colCateg.setCellFactory(column -> {
            return new TableCell<Charge, Category>() {
                @Override
                protected void updateItem(Category category, boolean empty) {
                    super.updateItem(category, empty);
                    if (empty || category == null) {
                        setText(null);
                    } else {
                        setText(category.getName());
                    }
                }
            };
        });

        // Set up the cell value factories
        colNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCateg.setCellValueFactory(new PropertyValueFactory<>("category"));
        colCant.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colUnidades.setCellValueFactory(new PropertyValueFactory<>("units"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
        // Código para actualizar la TableView aquí
                refreshTable();
            }
        };
// Actualizar cada 5 segundos (5000 milisegundos)
        timer.schedule(task, 0, 2000);
    }
    
    public void refreshTable() {
        Acount acount;
        try {
            acount = Acount.getInstance();
            List<Charge> miscargos = acount.getUserCharges();
            obsLista = FXCollections.observableList(miscargos);
            tableView.setItems(obsLista);
        } catch (AcountDAOException ex) {
            Logger.getLogger(VentanaHistorialController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VentanaHistorialController.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

    @FXML
    private void modificar_Gasto(ActionEvent event) throws IOException {
        if (tableView.getSelectionModel().getSelectedItem() == null) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText("No se ha seleccionado ningún elemento");
        alert.setContentText("Por favor, seleccione un elemento de la tabla.");
        alert.showAndWait();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaModificarGasto.fxml"));
        Parent root = fxmlLoader.load();
        
            // Crear una nueva ventana (stage) para la ventana emergente
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal mientras la ventana emergente está abierta
        stage.setTitle("Modificar Gasto");
        stage.setScene(new Scene(root));
        
        stage.setResizable(false);
        
          // Mostrar la ventana emergente y esperar a que se cierre antes de continuar
        stage.showAndWait();
        }
      
        
       
    }

    @FXML
    private void eliminar_Gasto(ActionEvent event) throws AcountDAOException {
        Charge cargoEliminar = tableView.getSelectionModel().getSelectedItem();
        if (cargoEliminar == null) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText("No se ha seleccionado ningún elemento");
        alert.setContentText("Por favor, seleccione un elemento de la tabla.");
        alert.showAndWait();
        } else {
            Charge selectedCharge = tableView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText("Está a punto de eliminar el siguiente producto:");
            alert.setContentText("Nombre: " + selectedCharge.getName() + "\nCategoría: " + selectedCharge.getCategory().getName());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Acount cuenta;
                try {
                    cuenta = Acount.getInstance();
                     boolean confirmacion = cuenta.removeCharge(cargoEliminar);
                     if ( confirmacion = true){
                         Alert alertaConf = new Alert(Alert.AlertType.INFORMATION);
                         alertaConf.setTitle("Categoria eliminada");
                         alertaConf.setHeaderText("El gasto ha sido eliminado correctamente");
                         alertaConf.showAndWait();
                     }
                } catch (IOException ex) {
                    Logger.getLogger(VentanaHistorialController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
       
        

    }
    @FXML
    private void imprimir_PDF(ActionEvent event) {
    }
    
    
    
    
    
    
}
