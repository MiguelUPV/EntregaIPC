/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Button imprimir_Historial;
    
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
    
    
    
    
    
    
}
