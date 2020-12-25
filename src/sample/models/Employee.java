package sample.models;

public class Employee  extends Human{
    private double salary;

    public Employee(String name, int age, double salary) {
        super(name, age);
        this.salary = salary;
    }

    @Override
    public double getMoney() {
        return salary;
    }
}
