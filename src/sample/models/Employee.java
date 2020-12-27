package sample.models;

public class Employee  extends Human{
    public double salary;
    public enum Education {University, School, No;};
    public Education education;

    public Employee(){}

    public Employee(String name, int age, double salary, Education education) {
        super(name, age);
        this.salary = salary;
        this.education = education;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    @Override
    public double getMoney() {
        return salary;
    }
}
