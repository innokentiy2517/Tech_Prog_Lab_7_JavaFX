package sample.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.models.Employee;
import sample.models.Human;
import sample.models.Student;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    public Label nameLabelData;
    public Label ageLabelData;
    public Label moneyLabel;
    public Label moneyLabelData;
    public Label courseLabel;
    public Label courseLabelData;
    @FXML
    private TableView<Human> mainTable;
    @FXML
    private TableColumn<Human,String> nameColumn;
    @FXML
    private TableColumn<Human,Integer> ageColumn;

    ObservableList<Human> humanList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        humanList.add(new Student("Ken", 21, 16000, 3));
        humanList.add(new Student("Alex", 20, 0, 4));
        humanList.add(new Student("Viva", 19, 3700, 2));
        humanList.add(new Employee("Eugene",21,15000));

        mainTable.setItems(humanList);

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());

        showHumanDetails(null);

        mainTable.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->showHumanDetails(newValue));
    }

    public void showHumanDetails(Human human){
        if(human != null){
            if (human instanceof Student){
                Student stud  = (Student) human;
                nameLabelData.setText(stud.getName());
                ageLabelData.setText(Integer.toString(stud.getAge()));
                moneyLabel.setVisible(true);
                moneyLabel.setText("Scholarship");
                moneyLabelData.setVisible(true);
                moneyLabelData.setText(Double.toString(stud.getMoney()));
                courseLabel.setVisible(true);
                courseLabelData.setVisible(true);
                courseLabelData.setText(Integer.toString(stud.getCourse()));
            }
            if (human instanceof Employee){
               Employee employee = (Employee) human;
               nameLabelData.setText(employee.getName());
               ageLabelData.setText(Integer.toString(employee.getAge()));
               moneyLabel.setVisible(true);
               moneyLabel.setText("Salary");
               moneyLabelData.setVisible(true);
               moneyLabelData.setText(Double.toString(employee.getMoney()));
               courseLabel.setVisible(false);
               courseLabelData.setVisible(false);
            }
        }
        else {
            nameLabelData.setText("");
            ageLabelData.setText("");
            moneyLabel.setVisible(false);
            moneyLabelData.setVisible(false);
            courseLabel.setVisible(false);
            courseLabelData.setVisible(false);
        }
    }

    public void onAddClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("HumanForm.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.mainTable.getScene().getWindow());
        stage.showAndWait();

        HumanFormController controller = loader.getController();
        if (controller.getModalResult()){
            Human newHuman = controller.getHuman();
            this.humanList.add(newHuman);
        }
    }

    public void onEditClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("HumanForm.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.mainTable.getScene().getWindow());

        HumanFormController controller = loader.getController();
        controller.setHuman((Human) this.mainTable.getSelectionModel().getSelectedItem());

        stage.showAndWait();

        if (controller.getModalResult()){
            int index = this.mainTable.getSelectionModel().getSelectedIndex();
            this.mainTable.getItems().set(index, controller.getHuman());
        }
    }

    public void onDeleteClick(ActionEvent actionEvent) {
        int index = mainTable.getSelectionModel().getSelectedIndex();

        if (index >= 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(String.format("Are you sure you want to delete %s?", humanList.get(index).getName()));
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK){
                this.mainTable.getItems().remove(index);
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection error");
            alert.setHeaderText("No human selected");
            alert.setContentText("Please choose a human to delete");
            alert.showAndWait();
        }
    }
}
