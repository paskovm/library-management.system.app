package library.management.system.common.entities.actors;

public abstract class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private long phoneNumber;
    private String email;
    private String password;

    public Person(int id, String firstName, String lastName, String address, long phoneNumber, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj instanceof Person) {
            Person person = (Person) obj;
            return person.getFirstName().equals(this.firstName) &&
                    person.getLastName().equals(this.lastName) &&
                    person.getEmail().equals(this.email);
        }
        return false;
    }

    public void printInfo() {
        System.out.println("\nUser's details are:");
        System.out.println("====================");
        System.out.println("ID: " + this.id);
        System.out.println("First Name: " + this.firstName);
        System.out.println("Last Name: " + this.lastName);
        System.out.println("Address: " + this.address);
        System.out.println("Phone Number: " + this.phoneNumber);
        System.out.println("Email address: " + this.email);
    }

    @Override
    public String toString() {
        return "\nUser's details are:" + System.lineSeparator() +
                "====================" + System.lineSeparator() +
                "ID: " + this.id + System.lineSeparator() +
                "First Name: " + this.firstName + System.lineSeparator() +
                "Last Name: " + this.lastName + System.lineSeparator() +
                "Address: " + this.address + System.lineSeparator() +
                "Phone Number: " + this.phoneNumber + System.lineSeparator() +
                "Email address: " + this.email;
    }
}
