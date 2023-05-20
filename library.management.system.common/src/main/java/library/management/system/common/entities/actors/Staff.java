package library.management.system.common.entities.actors;

public class Staff extends Person {

    private double salary;

    public Staff(int id, String firstName, String lastName, String address, long phoneNumber, String email, String password, double salary) {
        super(id, firstName, lastName, address, phoneNumber, email, password);
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("Salary: $" + this.salary);
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() + "Salary: $" + this.salary;
    }
}
