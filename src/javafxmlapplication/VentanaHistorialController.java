/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;



import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Cell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
    private TextField buscador;
    @FXML
    private Button Imprimir_PDF;
    
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        refreshTable();
        
        buscador.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterTable(newValue);
            }
        });


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
            Charge cargoEditar = tableView.getSelectionModel().getSelectedItem();
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaModificarGasto.fxml"));
            Parent root = fxmlLoader.load();
        
            VentanaModificarGastoController modContr = fxmlLoader.getController();
            modContr.setCargo(cargoEditar);
            // Crear una nueva ventana (stage) para la ventana emergente
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal mientras la ventana emergente está abierta
        stage.setTitle("Modificar Gasto");
        stage.setScene(new Scene(root));
        
        stage.setResizable(false);
        stage.setOnHidden(e -> refreshTable());
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
                         refreshTable();
                     }
                } catch (IOException ex) {
                    Logger.getLogger(VentanaHistorialController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
       
        

    }
    @FXML
    private void imprimir_PDF(ActionEvent event) throws DocumentException, FileNotFoundException {
        FileChooser fileC = new FileChooser();
        fileC.setTitle("Guardar como");
        fileC.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        File selectedFile = fileC.showSaveDialog(Imprimir_PDF.getScene().getWindow());
        if(selectedFile != null){
          String filePath = selectedFile.getAbsolutePath();
          try{
              Document documento = new Document(PageSize.A4);
              PdfWriter.getInstance(documento, new FileOutputStream(filePath));
              documento.open();

              int numColumnas = tableView.getColumns().size();
              PdfPTable pdfTabla = new PdfPTable(numColumnas);
              
              PdfPCell headerCellNombre = new PdfPCell(new Phrase(colNombre.getText(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
              pdfTabla.addCell(headerCellNombre);
              PdfPCell headerCellCategory = new PdfPCell(new Phrase(colCateg.getText(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
              pdfTabla.addCell(headerCellCategory);
              PdfPCell headerCellCant = new PdfPCell(new Phrase(colCant.getText(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
              pdfTabla.addCell(headerCellCant);
              PdfPCell headerCellUnid = new PdfPCell(new Phrase(colUnidades.getText(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
              pdfTabla.addCell(headerCellUnid);
              PdfPCell headerCellFecha = new PdfPCell(new Phrase(colFecha.getText(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
              pdfTabla.addCell(headerCellFecha);
              
              ObservableList<Charge> items = tableView.getItems();
                for (Charge item : items){
                Object cellDataNombre = colNombre.getCellData(item);
                PdfPCell celdaNombre = new PdfPCell(new Phrase((String) cellDataNombre.toString()));
                celdaNombre.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTabla.addCell(celdaNombre);
                
                Category cellDataCategory = colCateg.getCellData(item);
                PdfPCell celdaCat = new PdfPCell(new Phrase((String)cellDataCategory.getName()));
                celdaCat.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTabla.addCell(celdaCat);
                
                Object cellDataCant = colCant.getCellData(item);
                PdfPCell celdaCant = new PdfPCell(new Phrase((String) cellDataCant.toString()));
                celdaCant.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTabla.addCell(celdaCant);
                
                Object cellDataUnid = colUnidades.getCellData(item);
                PdfPCell celdaUnid = new PdfPCell(new Phrase((String) cellDataUnid.toString()));
                celdaUnid.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTabla.addCell(celdaUnid);
                
                Object cellDataFecha = colFecha.getCellData(item);
                PdfPCell celdaFecha = new PdfPCell(new Phrase((String) cellDataFecha.toString()));
                celdaFecha.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTabla.addCell(celdaFecha);
              }
              documento.add(pdfTabla);
              documento.close();
              Alert alert = new Alert(Alert.AlertType.INFORMATION);
              alert.setTitle("PDF guardado");
              alert.setHeaderText("PDF generado");
              alert.setContentText("El PDF se ha generado correctamente");
              alert.showAndWait();
    
            
          }catch (DocumentException | FileNotFoundException e){e.printStackTrace();}
        }
    }
    

        
        
        
        
    
                

                

          
         
    
    
    
    
            
        
    
    
        
    
    private void filterTable(String searchText) {
    if (searchText == null || searchText.trim().isEmpty()) {
        
        tableView.setItems(obsLista);
    } else {
        
        ObservableList<Charge> filteredList = FXCollections.observableArrayList();
        String searchTextLower = searchText.toLowerCase();
        for (Charge charge : obsLista) {
            if (charge.getName().toLowerCase().contains(searchTextLower)) {
                filteredList.add(charge);
            }
        }
        tableView.setItems(filteredList);
    }
    
    }
}
