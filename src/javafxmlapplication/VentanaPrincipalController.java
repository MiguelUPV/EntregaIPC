/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    private Button boton_Inicio;
    @FXML
    private Button boton_AñadirGasto;
    @FXML
    private Button boton_Historial;
    @FXML
    private Button boton_AjustesDeCuenta;
    @FXML
    private Button boton_CerrarSesion;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button boton_AnadirCateg;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
        FXMLLoader inicioLoader = new FXMLLoader(getClass().getResource("/javafxmlapplication/VentanaInicio.fxml"));
        try {
            Parent inicio;
            inicio = inicioLoader.load();
            borderPane.setCenter(inicio);
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
      
        
         boton_CerrarSesion.setOnAction(actionEvent -> {
            try {
                cerrar_sesión();
            } catch (IOException ex) {
                Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
         
        boton_Historial.setOnAction(actionEvent -> {
             abrir_historial();
        }); 
        
        boton_AñadirGasto.setOnAction(actionEvent -> {
            try {
                anadirGasto();
            } catch (IOException ex) {
                Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        
        boton_Inicio.setOnAction(actionEvent -> {
            abrirInicio();
        });
        
        boton_AjustesDeCuenta.setOnAction((actionEvent) -> {
            abrirAjustesDeCuenta();
        });
        
        boton_AnadirCateg.setOnAction(((t) -> {
            try {
                anadirCateg();
            } catch (IOException ex) {
                Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));
         
         
       
        
        
    }    

    private void cerrar_sesión() throws IOException {
       Alert alert = new Alert(AlertType.CONFIRMATION);
       alert.setTitle("Cerrar sesión");
       alert.setHeaderText("¿Está a punto de cerrar la sesión?");
       alert.setContentText("¿Seguro que quiere continuar?");
       Optional<ButtonType> result = alert.showAndWait();
       if (result.isPresent() && result.get() == ButtonType.OK){
       System.out.println("OK");
       FXMLLoader inicio = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = inicio.load();
        Stage stage = (Stage) boton_CerrarSesion.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        } else {
        System.out.println("CANCELAR");
        
}
    }

   private void anadirGasto() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("VentanaAnadirGasto.fxml"));
    Parent root = loader.load();

    // Create a new stage for the popup window
    Stage stage = new Stage();
    stage.setTitle("Añadir Gasto");
    stage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
    stage.setScene(new Scene(root));
    stage.setResizable(false);

    // Set event handler for when the window is closed
    stage.setOnHidden(a -> {
        FXMLLoader historialLoader = new FXMLLoader(getClass().getResource("/javafxmlapplication/VentanaHistorial.fxml"));
        try {
            boton_Historial.requestFocus();
            Parent inicio = historialLoader.load();
            borderPane.setCenter(inicio);
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    });

    // Show the window and wait for it to be closed
    stage.showAndWait();
}

    private void abrir_historial() {
       FXMLLoader historialLoader = new FXMLLoader(getClass().getResource("/javafxmlapplication/VentanaHistorial.fxml"));
        try {
            Parent inicio;
            inicio = historialLoader.load();
            borderPane.setCenter(inicio);
            
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void abrirInicio() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxmlapplication/VentanaInicio.fxml"));
        try {
            Parent inicio;
            inicio = loader.load();
            borderPane.setCenter(inicio);
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void abrirAjustesDeCuenta() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxmlapplication/VentanaEditarUser.fxml"));
        try {
            Parent inicio;
            inicio = loader.load();
            borderPane.setCenter(inicio);
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void anadirCateg() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VentanaCrearCateg.fxml"));
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
