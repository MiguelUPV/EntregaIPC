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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
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
    private ToggleButton boton_Inicio;
    @FXML
    private ToggleButton boton_AñadirGasto;
    @FXML
    private ToggleButton boton_Historial;
    @FXML
    private ToggleButton boton_AjustesDeCuenta;
    @FXML
    private Button boton_CerrarSesion;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ToggleButton boton_AnadirCateg;
    String normalStyle = "-fx-background-color: white; -fx-text-fill: navy;";
    String hoverStyle = "-fx-background-color: #97a5b8; -fx-text-fill: white;";
    @FXML
    private ToggleButton boton_VisualizarCategorias;
    private ToggleGroup toggle;
     
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        toggle= new ToggleGroup();
        boton_AjustesDeCuenta.setToggleGroup(toggle);
        boton_AnadirCateg.setToggleGroup(toggle);
        boton_AñadirGasto.setToggleGroup(toggle);
        boton_Historial.setToggleGroup(toggle);
        boton_Inicio.setToggleGroup(toggle);
        boton_VisualizarCategorias.setToggleGroup(toggle);
        
        FXMLLoader inicioLoader = new FXMLLoader(getClass().getResource("/javafxmlapplication/VentanaInicio.fxml"));
        try {
            Parent inicio;
            inicio = inicioLoader.load();
            borderPane.setCenter(inicio);
            boton_Inicio.setStyle(hoverStyle);
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
      
        
         boton_CerrarSesion.setOnAction(actionEvent -> {
            try {
                boton_Inicio.setStyle(normalStyle);
                boton_AjustesDeCuenta.setStyle(normalStyle);
                boton_AnadirCateg.setStyle(normalStyle);
                boton_AñadirGasto.setStyle(normalStyle);
                boton_Historial.setStyle(normalStyle);
                boton_Inicio.setStyle(normalStyle);
                boton_VisualizarCategorias.setStyle(normalStyle);
                boton_CerrarSesion.setStyle(hoverStyle);
                cerrar_sesión();
            } catch (IOException ex) {
                Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
         
        boton_VisualizarCategorias.setOnAction((t) -> {
            boton_Inicio.setStyle(normalStyle);
                boton_AjustesDeCuenta.setStyle(normalStyle);
                boton_AnadirCateg.setStyle(normalStyle);
                boton_AñadirGasto.setStyle(normalStyle);
                boton_Historial.setStyle(normalStyle);
                boton_Inicio.setStyle(normalStyle);
                boton_VisualizarCategorias.setStyle(hoverStyle);
                boton_CerrarSesion.setStyle(normalStyle);
                visualizarCategorias();
        });
         
        boton_Historial.setOnAction(actionEvent -> {
            boton_Inicio.setStyle(normalStyle);
                boton_AjustesDeCuenta.setStyle(normalStyle);
                boton_AnadirCateg.setStyle(normalStyle);
                boton_AñadirGasto.setStyle(normalStyle);
                boton_Inicio.setStyle(normalStyle);
                boton_VisualizarCategorias.setStyle(normalStyle);
            boton_Historial.setStyle(hoverStyle);
             abrir_historial();
        }); 
        
        boton_AñadirGasto.setOnAction(actionEvent -> {
            try {
                boton_Inicio.setStyle(normalStyle);
                boton_AjustesDeCuenta.setStyle(normalStyle);
                boton_AnadirCateg.setStyle(normalStyle);
                boton_Historial.setStyle(normalStyle);
                boton_Inicio.setStyle(normalStyle);
                boton_VisualizarCategorias.setStyle(normalStyle);
                boton_AñadirGasto.setStyle(hoverStyle);
                anadirGasto();
                boton_Historial.setStyle(hoverStyle);
                boton_Historial.isSelected();
                boton_AñadirGasto.setStyle(normalStyle);
            } catch (IOException ex) {
                Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        
        boton_Inicio.setOnAction(actionEvent -> {
                boton_AjustesDeCuenta.setStyle(normalStyle);
                boton_AnadirCateg.setStyle(normalStyle);
                boton_AñadirGasto.setStyle(normalStyle);
                boton_Historial.setStyle(normalStyle);
                boton_Inicio.setStyle(normalStyle);
                boton_VisualizarCategorias.setStyle(normalStyle);
            boton_Inicio.setStyle(hoverStyle);
            abrirInicio();
        });
        
        boton_AjustesDeCuenta.setOnAction((actionEvent) -> {
            boton_AjustesDeCuenta.setStyle(hoverStyle);
                boton_AnadirCateg.setStyle(normalStyle);
                boton_AñadirGasto.setStyle(normalStyle);
                boton_Historial.setStyle(normalStyle);
                boton_Inicio.setStyle(normalStyle);
                boton_VisualizarCategorias.setStyle(normalStyle);
            abrirAjustesDeCuenta();
        });
        
        boton_AnadirCateg.setOnAction(((t) -> {
            try {
                 boton_AjustesDeCuenta.setStyle(normalStyle);
                boton_AnadirCateg.setStyle(normalStyle);
                boton_AñadirGasto.setStyle(normalStyle);
                boton_Historial.setStyle(normalStyle);
                boton_Inicio.setStyle(normalStyle);
                boton_VisualizarCategorias.setStyle(normalStyle);
                boton_AnadirCateg.setStyle(hoverStyle);
                anadirCateg();
                boton_VisualizarCategorias.setStyle(hoverStyle);
                boton_VisualizarCategorias.isSelected();
                boton_AnadirCateg.setStyle(normalStyle);
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
        Scene scene = new Scene(root, 850, 525); // Establecer el tamaño de la escena
        stage.setScene(scene);
        stage.setResizable(false);
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
    
        stage.setOnHidden(a -> {
        FXMLLoader historialLoader = new FXMLLoader(getClass().getResource("/javafxmlapplication/VentanaGestionCategorias.fxml"));
        try {
            boton_VisualizarCategorias.requestFocus();
            Parent inicio = historialLoader.load();
            borderPane.setCenter(inicio);
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    });
        
        stage.showAndWait();
        
        
    }

    @FXML
    private void noInicio(MouseEvent event) {
        if(!boton_Inicio.isSelected()){boton_Inicio.setStyle(normalStyle);}
       
    }

    @FXML
    private void resaltarInicio(MouseEvent event) {
        boton_Inicio.setStyle(hoverStyle);
    }

    @FXML
    private void noGasto(MouseEvent event) {
        if(!boton_AñadirGasto.isSelected()){boton_AñadirGasto.setStyle(normalStyle);}
    }

    @FXML
    private void resaltarGasto(MouseEvent event) {
        boton_AñadirGasto.setStyle(hoverStyle);
    }

    @FXML
    private void noCategoria(MouseEvent event) {
        if(!boton_AnadirCateg.isSelected()){boton_AnadirCateg.setStyle(normalStyle);}
    }

    @FXML
    private void resaltarCategoria(MouseEvent event) {
        boton_AnadirCateg.setStyle(hoverStyle);
    }

    @FXML
    private void noVisualizar(MouseEvent event) {
       if(!boton_Historial.isSelected()) {boton_Historial.setStyle(normalStyle);}
    }

    @FXML
    private void resaltarVisualizar(MouseEvent event) {
        boton_Historial.setStyle(hoverStyle);
    }

    @FXML
    private void noDatos(MouseEvent event) {
        if(!boton_AjustesDeCuenta.isSelected()){boton_AjustesDeCuenta.setStyle(normalStyle);}
    }

    @FXML
    private void resaltarDatos(MouseEvent event) {
        boton_AjustesDeCuenta.setStyle(hoverStyle);
    }

    @FXML
    private void noCerrar(MouseEvent event) {
        boton_CerrarSesion.setStyle(normalStyle);
    }

    @FXML
    private void resaltarCerrar(MouseEvent event) {
        boton_CerrarSesion.setStyle(hoverStyle);
    }

    @FXML
    private void noVisCat(MouseEvent event) {
        if(!boton_VisualizarCategorias.isSelected()){boton_VisualizarCategorias.setStyle(normalStyle);}
    }

    @FXML
    private void resaltarVisualizarCat(MouseEvent event) {
        boton_VisualizarCategorias.setStyle(hoverStyle);
    }

    private void visualizarCategorias() {
        FXMLLoader historialLoader = new FXMLLoader(getClass().getResource("/javafxmlapplication/VentanaGestionCategorias.fxml"));
        try {
            Parent inicio;
            inicio = historialLoader.load();
            borderPane.setCenter(inicio);
            
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
}
