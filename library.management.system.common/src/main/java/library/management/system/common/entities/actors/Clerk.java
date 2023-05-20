package library.management.system.common.entities.actors;

public class Clerk extends Staff {

    private int deskNumber;

    public Clerk(int id, String firstName, String lastName, String address, long phoneNumber, String email, String password, double salary, int deskNumber) {
        super(id, firstName, lastName, address, phoneNumber, email, password, salary);
        this.deskNumber = deskNumber;
    }

    public int getDeskNumber() {
        return deskNumber;
    }

    public void setDeskNumber(int deskNumber) {
        this.deskNumber = deskNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Clerk) {
            Clerk clerk = (Clerk) obj;
            return super.equals(clerk) && clerk.getDeskNumber() == this.deskNumber;
        }
        return false;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("Desk number: " + this.deskNumber);
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() + "Desk number: " + this.deskNumber;
    }
}
