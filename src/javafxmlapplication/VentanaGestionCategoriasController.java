/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;

/**
 * FXML Controller class
 *
 * @author Rubén
 */
public class VentanaGestionCategoriasController implements Initializable {

    @FXML
    private Text textoBienvenido;
    @FXML
    private TableView<Category> tableView;
    @FXML
    private Button bEditar;
    @FXML
    private Button bEliminar;
    @FXML
    private TableColumn<Category, String> colNombre;
    @FXML
    private TableColumn<Category, String> colDesc;
    String normalStyle = "-fx-background-color: navy; -fx-text-fill: white;";
    String hoverStyle = "-fx-background-color: #3486eb; -fx-text-fill: white;";
    
    private Acount cuenta;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        
        try {
            cuenta = Acount.getInstance();
            List<Category> listaCateg = cuenta.getUserCategories();
            ObservableList<Category> obsLista = FXCollections.observableList(listaCateg);
            tableView.setItems(obsLista);
        } catch (AcountDAOException ex) {
            Logger.getLogger(VentanaGestionCategoriasController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VentanaGestionCategoriasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    private void anadirCateg(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaCrearCateg.fxml"));
        Parent root = fxmlLoader.load();

        
        
        
            // Crear una nueva ventana (stage) para la ventana emergente
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal mientras la ventana emergente está abierta
        stage.setTitle("Crear Categoría");
        stage.setScene(new Scene(root));
        
        stage.setResizable(false);

            // Mostrar la ventana emergente y esperar a que se cierre antes de continuar
        stage.showAndWait();
        tableView.refresh();
    }

    @FXML
    private void editarCateg(ActionEvent event) throws IOException {
        if (tableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No se ha seleccionado ningún elemento");
            alert.setContentText("Por favor, seleccione un elemento de la tabla.");
            alert.showAndWait();
        } else {
            Category selectedCategory = tableView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaEditarCategoria.fxml"));
            Parent root = fxmlLoader.load();
            
            VentanaEditarCategoriaController controller = fxmlLoader.getController();
            controller.setCategory(selectedCategory);

        
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar Categoría");
            stage.setScene(new Scene(root));
            stage.setResizable(false);

        // Mostrar la ventana emergente y esperar a que se cierre antes de continuar
        stage.showAndWait();

        // Actualizar la tabla después de editar la categoría
        tableView.refresh();
        }
        
    }

    @FXML
    private void eliminarCateg(ActionEvent event) throws AcountDAOException {
        Category catEliminar = tableView.getSelectionModel().getSelectedItem();
        if (catEliminar == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No se ha seleccionado ningún elemento");
            alert.setContentText("Por favor, seleccione un elemento de la tabla.");
            alert.showAndWait();
        } else {
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar categoría");
            alert.setHeaderText("¿Está seguro de querer continuar?");
            alert.setContentText("Al eliminar la categoría " + catEliminar.getName() + " se eliminarán todos\n los gastos asociados a esta categoria" );

            Optional<ButtonType> result = alert.showAndWait();
            
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean borrado =  cuenta.removeCategory(catEliminar);
                if (borrado) {
                        Alert alertaConf = new Alert(Alert.AlertType.INFORMATION);
                         alertaConf.setTitle("Categoria eliminada");
                         alertaConf.setHeaderText("La categoría ha sido eliminado correctamente");
                         alertaConf.showAndWait();
                }
            }
        }
        tableView.refresh();
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void noEditar(MouseEvent event) {
        bEditar.setStyle(normalStyle);
    }

    @FXML
    private void resaltarEditar(MouseEvent event) {
        bEditar.setStyle(hoverStyle);
    }

    @FXML
    private void noElim(MouseEvent event) {
        bEliminar.setStyle(normalStyle);
    }

    

    @FXML
    private void resaltarElim(MouseEvent event) {
        bEliminar.setStyle(hoverStyle);
    }


}
