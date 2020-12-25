package sample.gui;

import com.sun.scenario.effect.Flood;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.models.Employee;
import sample.models.Human;
import sample.models.Student;

import java.net.URL;
import java.util.ResourceBundle;

public class HumanFormController implements Initializable {
    public Label moneyLabel;
    public TextField nameTxtField;
    public TextField ageTxtField;
    public TextField moneyTxtField;
    public Label courseLabel;
    public ChoiceBox courseCmb;
    public ChoiceBox humanTypeCmb;

    final String HUMAN_STUDENT = "Student";
    final String HUMAN_EMPLOYEE = "Employee";

    private Boolean modalResult = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        humanTypeCmb.setItems(FXCollections.observableArrayList(
                HUMAN_STUDENT,
                HUMAN_EMPLOYEE
        ));

        courseCmb.setItems(FXCollections.observableArrayList(
                1,
                2,
                3,
                4
        ));
        courseLabel.setVisible(false);
        courseCmb.setVisible(false);
        humanTypeCmb.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(HUMAN_STUDENT)) {
                moneyLabel.setText("Scholarship");
                courseLabel.setVisible(true);
                courseCmb.setVisible(true);
            }
            if (newValue.equals(HUMAN_EMPLOYEE)) {
                moneyLabel.setText("Salary");
                courseLabel.setVisible(false);
                courseCmb.setVisible(false);
            }
        }));
    }

    public void onSaveClick(ActionEvent actionEvent) {
        if (isInputValid()){
            this.modalResult = true;
            ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
        }
    }

    private boolean isInputValid(){
        String errorMessage = "";

        if (nameTxtField.getText() == null || nameTxtField.getText().length() == 0){
            errorMessage += "No valid name!\n";
        }
        if (ageTxtField.getText() == null || ageTxtField.getText().length() == 0){
            errorMessage += "No valid age!\n";
        }
        else{
            try {
                Integer.parseInt(ageTxtField.getText());
            }catch (NumberFormatException e){
                errorMessage += "Age must be an Integer!\n";
            }
        }
        if ((String)this.humanTypeCmb.getValue() == HUMAN_STUDENT){
            if (moneyTxtField.getText() == null || moneyTxtField.getText().length() == 0){
                errorMessage += "No valid Scholarship!\n";
            }
            else {
                try {
                    Double.parseDouble(moneyTxtField.getText());
                }catch (NumberFormatException e){
                    errorMessage += "Scholarship must be a Double or Integer!\n";
                }
            }
            if (courseCmb.getValue() == null){
                errorMessage += "No valid course!\n";
            }
        }
        if ((String)this.humanTypeCmb.getValue() == HUMAN_EMPLOYEE){
            if (moneyTxtField.getText() == null || moneyTxtField.getText().length() == 0){
                errorMessage += "No valid Salary!\n";
            }
            else {
                try {
                    Double.parseDouble(moneyTxtField.getText());
                }catch (NumberFormatException e){
                    errorMessage += "Salary must be a Double or Integer!\n";
                }
            }
        }
        if (errorMessage.length() == 0){
            return true;
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid input");
            alert.setHeaderText("Please corect invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    public void onCancelClick(ActionEvent actionEvent) {
        this.modalResult = false;
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }

    public Boolean getModalResult() {
        return modalResult;
    }

    public Human getHuman(){
        Human result = null;
        String name = this.nameTxtField.getText();
        int age = Integer.parseInt(this.ageTxtField.getText());
        double money = Double.parseDouble(this.moneyTxtField.getText());

        switch ((String)this.humanTypeCmb.getValue()){
            case HUMAN_STUDENT:
                int course = (Integer) courseCmb.getValue();
                result = new Student(name,age,money,course);
                break;
            case HUMAN_EMPLOYEE:
                result = new Employee(name,age,money);
                break;
        }
        return result;
    }

    public void setHuman(Human human){
        this.humanTypeCmb.setDisable(human != null);
        if (human != null){
            this.nameTxtField.setText(human.getName());
            this.ageTxtField.setText(String.valueOf(human.getAge()));
            this.moneyTxtField.setText(String.valueOf(human.getMoney()));
            if (human instanceof Student){
                this.courseCmb.setValue(((Student) human).getCourse());
                this.humanTypeCmb.setValue(HUMAN_STUDENT);
            }
            if (human instanceof Employee){
                this.humanTypeCmb.setValue(HUMAN_EMPLOYEE);
            }
        }
    }
}
