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
import javafx.util.StringConverter;
import sample.models.*;

import java.awt.datatransfer.StringSelection;
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
    public Label educationLabel;
    public Label isOnVacationLabel;
    public Label educationLabelData;
    public Label isOnVacationLabelData;
    public ChoiceBox cmbHumanType;

    ObservableList<Class<? extends Human>> humanTypes = FXCollections.observableArrayList(
            Human.class,
            Employee.class,
            Student.class,
            Teacher.class
    );

    @FXML
    private TableView<Human> mainTable;
    @FXML
    private TableColumn<Human,String> nameColumn;
    @FXML
    private TableColumn<Human,Integer> ageColumn;

    HumanModel humanModel = new HumanModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbHumanType.setItems(humanTypes);
        cmbHumanType.getSelectionModel().select(0);
        cmbHumanType.setConverter(new StringConverter<Class>(){
            @Override
            public String toString(Class object){
                if(Human.class.equals(object)){
                    return "Все";
                } else if (Employee.class.equals(object)){
                    return "Employee";
                } else if (Student.class.equals(object)){
                    return "Student";
                }else if (Teacher.class.equals(object)){
                    return "Teacher";
                }
                return null;
            }
            @Override
            public Class fromString(String string){
                return null;
            }
        });
        cmbHumanType.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {this.humanModel.setHumanFilter((Class<? extends Human>) newValue);}));
        humanModel.addDataChangedListener(humans -> {
            mainTable.setItems(FXCollections.observableArrayList(humans));
        });

        humanModel.load();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

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
                educationLabelData.setVisible(false);
                educationLabel.setVisible(false);
                isOnVacationLabelData.setVisible(false);
                isOnVacationLabel.setVisible(false);
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
               educationLabel.setVisible(true);
               educationLabelData.setVisible(true);
               educationLabelData.setText(employee.getEducation().toString());
               isOnVacationLabelData.setVisible(false);
               isOnVacationLabel.setVisible(false);
            }
            if (human instanceof Teacher){
                Teacher teacher = (Teacher) human;
                nameLabelData.setText(teacher.getName());
                ageLabelData.setText(Integer.toString(teacher.getAge()));
                moneyLabel.setVisible(true);
                moneyLabel.setText("Salary");
                moneyLabelData.setVisible(true);
                moneyLabelData.setText(Double.toString(teacher.getMoney()));
                courseLabel.setVisible(false);
                courseLabelData.setVisible(false);
                educationLabel.setVisible(true);
                educationLabelData.setVisible(true);
                educationLabelData.setText(teacher.getEducation().toString());
                isOnVacationLabel.setVisible(true);
                isOnVacationLabelData.setVisible(true);
                isOnVacationLabelData.setText(String.valueOf(teacher.getIsOnVacation()));
            }
        }
        else {
            nameLabelData.setText("");
            ageLabelData.setText("");
            moneyLabel.setVisible(false);
            moneyLabelData.setVisible(false);
            courseLabel.setVisible(false);
            courseLabelData.setVisible(false);
            educationLabel.setVisible(false);
            educationLabelData.setVisible(false);
            isOnVacationLabel.setVisible(false);
            isOnVacationLabelData.setVisible(false);
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

        HumanFormController controller = loader.getController();
        controller.humanModel = humanModel;
        stage.showAndWait();
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
        controller.humanModel = humanModel;
        stage.showAndWait();
    }

    public void onDeleteClick(ActionEvent actionEvent) {
        Human human = (Human)  this.mainTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(String.format("Are you sure you want to delete %s?", human.getName()));
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK){
            humanModel.delete(human.id);
        }
    }

    public void onSaveToFileClick(ActionEvent actionEvent) {
        humanModel.saveToFile("data.json");
    }

    public void onLoadFromFileClick(ActionEvent actionEvent) {
        humanModel.loadFromFile("data.json");
    }
}
