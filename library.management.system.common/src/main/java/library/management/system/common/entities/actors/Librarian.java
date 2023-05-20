package library.management.system.common.entities.actors;

public class Librarian extends Staff {

    private int officeNumber;

    public Librarian(int id, String firstName, String lastName, String address, long phoneNumber, String email, String password, double salary, int officeNumber) {
        super(id, firstName, lastName, address, phoneNumber, email, password, salary);
        this.officeNumber = officeNumber;
    }

    public int getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(int officeNumber) {
        this.officeNumber = officeNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Librarian) {
            Librarian librarian = (Librarian) obj;
            return super.equals(librarian) && librarian.getOfficeNumber() == this.officeNumber;
        }
        return false;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("Office number: " + this.officeNumber);
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() + "Office number: " + this.officeNumber;
    }
}
