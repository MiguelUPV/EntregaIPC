package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;

public class VentanaInicioController implements Initializable {

    @FXML
    private PieChart pieChart;
    ObservableList<Charge> obsLista = null;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private Text textoGasto;
    @FXML
    private Text textoCantGastada;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ObservableList<String> options = FXCollections.observableArrayList(
                "Total",
                "Ultimo año",
                "Ultimo mes",
                "Ultima semana"
            );
            choiceBox.setItems(options);
            choiceBox.setValue("Total");

            Acount acount = Acount.getInstance();
            List<Charge> miscargos = acount.getUserCharges();
            
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
            
            colNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
            colCateg.setCellValueFactory(new PropertyValueFactory<>("category"));
            colCant.setCellValueFactory(new PropertyValueFactory<>("cost"));
            colUnidades.setCellValueFactory(new PropertyValueFactory<>("units"));
            colFecha.setCellValueFactory(new PropertyValueFactory<>("date"));

            choiceBox.setOnAction(event -> actualizarVista(miscargos));

            
            actualizarVista(miscargos);
            
        } catch (AcountDAOException | IOException ex) {
            Logger.getLogger(VentanaInicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    private void actualizarVista(List<Charge> miscargos) {
        String seleccion = choiceBox.getValue();
        LocalDate ahora = LocalDate.now();
        
        List<Charge> cargosFiltrados = miscargos;

        switch (seleccion) {
            case "Ultima semana":
                cargosFiltrados = miscargos.stream()
                    .filter(c -> ChronoUnit.DAYS.between(c.getDate(), ahora) <= 7)
                    .collect(Collectors.toList());
                textoGasto.setText("Gasto últimos 7 dias");
                break;
            case "Ultimo mes":
                cargosFiltrados = miscargos.stream()
                    .filter(c -> ChronoUnit.DAYS.between(c.getDate(), ahora) <= 30)
                    .collect(Collectors.toList());
                textoGasto.setText("Gasto último mes");
                break;
            case "Ultimo año":
                cargosFiltrados = miscargos.stream()
                    .filter(c -> ChronoUnit.DAYS.between(c.getDate(), ahora) <= 365)
                    .collect(Collectors.toList());
                textoGasto.setText("Gasto último año");
                break;
            case "Total":
            default:
                
                cargosFiltrados = miscargos;
                textoGasto.setText("Gasto total");
                break;
        }

        
        ObservableList<Charge> cargosTabla = FXCollections.observableArrayList(cargosFiltrados);
        tableView.setItems(cargosTabla);

        
        actualizarPieChart(cargosFiltrados);

        
        sumarGastosYMostrarTotal(cargosFiltrados);
    }

    private void actualizarPieChart(List<Charge> cargos) {
        Map<Category, Double> categoriaTotales = new HashMap<>();
        
        for (Charge cargo : cargos) {
            Category categoria = cargo.getCategory();
            double costo = cargo.getCost();
            
            categoriaTotales.put(categoria, categoriaTotales.getOrDefault(categoria, 0.0) + costo);
        }
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        for (Map.Entry<Category, Double> entry : categoriaTotales.entrySet()) {
            Category categoria = entry.getKey();
            Double total = entry.getValue();
            pieChartData.add(new PieChart.Data(categoria.getName(), total));
        }
        
        
        
        pieChart.setData(pieChartData);
        
    }

    private void sumarGastosYMostrarTotal(List<Charge> miscargos) {
        double totalGastos = 0.0;
        for (Charge cargo : miscargos) {
            totalGastos += cargo.getCost();
        }
        textoCantGastada.setText(String.valueOf(totalGastos)+ " €");
    }
}
