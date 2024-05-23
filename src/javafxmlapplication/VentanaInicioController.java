/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;


/**
 * FXML Controller class
 *
 * @author 34744
 */
public class VentanaInicioController implements Initializable {

    @FXML
    private PieChart pieChart;
     ObservableList<Charge> obsLista = null;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Obtén la instancia de Acount
            Acount acount = Acount.getInstance();
            
            // Obtén la lista de cargos del usuario
            List<Charge> miscargos = acount.getUserCharges();
            
            // Mapa para almacenar el total de cada categoría
            Map<Category, Double> categoriaTotales = new HashMap<>();
            
            // Recorre la lista de cargos y acumula los totales por categoría
            for (Charge cargo : miscargos) {
                Category categoria = cargo.getCategory();
                double costo = cargo.getCost();
                
                // Suma el costo al total de la categoría
                categoriaTotales.put(categoria, categoriaTotales.getOrDefault(categoria, 0.0) + costo);
            }
            
            // Crea la lista de datos para el PieChart
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            
            // Recorre el mapa de totales y crea los datos del PieChart
            for (Map.Entry<Category, Double> entry : categoriaTotales.entrySet()) {
                Category categoria = entry.getKey();
                Double total = entry.getValue();
                
                // Agrega el dato al PieChart
                pieChartData.add(new PieChart.Data(categoria.getName(), total));
            }
            
            // Asigna los datos al PieChart
            pieChart.setData(pieChartData);
            
        } catch (AcountDAOException | IOException ex) {
            Logger.getLogger(VentanaInicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
}
