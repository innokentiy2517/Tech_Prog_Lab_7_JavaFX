package sample.models;

public class Student extends Human{
    private double scholarship;
    private int course;

    public Student(String name, int age, double scholarship, int course) {
        super(name, age);
        this.scholarship = scholarship;
        this.course = course;
    }

    @Override
    public double getMoney() {
        return scholarship;
    }

    public double getScholarship() {
        return scholarship;
    }

    public void setScholarship(double scholarship) {
        this.scholarship = scholarship;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }
}
