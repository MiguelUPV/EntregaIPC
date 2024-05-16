/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
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
public class VentanaPrincipalController implements Initializable {

    @FXML
    private Text nombreUsuario;
    @FXML
    private Text NickName;
    
    String nombreUsuario1 = null;
    String nombreNickName1 = null;
    @FXML
    private Button boton_Inicio;
    @FXML
    private Button boton_AñadirGasto;
    @FXML
    private Button boton_Historial;
    @FXML
    private Button boton_AjustesDeCuenta;
    @FXML
    private Button boton_CerrarSesion;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        try {
            nombreUsuario1 = Acount.getInstance().getLoggedUser().getName();
        } catch (AcountDAOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            nombreNickName1 = Acount.getInstance().getLoggedUser().getNickName();
        } catch (AcountDAOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         boton_CerrarSesion.setOnAction(actionEvent -> cerrar_sesión());
        nombreUsuario.setText(nombreUsuario1);
        NickName.setText(nombreNickName1);
        
        
    }    

    private void cerrar_sesión() {
       Alert alert = new Alert(AlertType.CONFIRMATION);
       alert.setTitle("Cerrar sesión");
       alert.setHeaderText("¿Está a punto de cerrar la sesión?");
       alert.setContentText("¿Seguro que quiere continuar?");
       Optional<ButtonType> result = alert.showAndWait();
       if (result.isPresent() && result.get() == ButtonType.OK){
       System.out.println("OK");
        } else {
        System.out.println("CANCELAR");
}
    }

    @FXML
    private void anadirGasto(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VentanaAnadirGasto.fxml"));
            Parent root = loader.load();
            
            // Create a new stage for the popup window
            Stage stage = new Stage();
            stage.setTitle("Añadir Gasto");
            stage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
    }
    
}
