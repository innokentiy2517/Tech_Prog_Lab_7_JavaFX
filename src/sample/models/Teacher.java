package sample.models;

public class Teacher extends Employee{
    private boolean isOnVacation;

    public Teacher(){}

    public Teacher(String name, int age, double salary, Education education, boolean isOnVacation) {
        super(name, age, salary, education);
        this.isOnVacation = isOnVacation;
    }

    public boolean getIsOnVacation() {
        return isOnVacation;
    }

    public void setIsOnVacation(boolean onVacation) {
        isOnVacation = onVacation;
    }

    public String getStringIsOnVacation(){
        if (isOnVacation){
            return "Yes";
        }
        else {
            return "No";
        }
    }
}
