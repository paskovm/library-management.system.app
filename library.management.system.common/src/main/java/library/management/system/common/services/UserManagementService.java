package library.management.system.common.services;

import library.management.system.common.entities.actors.Clerk;
import library.management.system.common.entities.actors.Librarian;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.entities.actors.Staff;

import java.util.ArrayList;
import java.util.List;

public class UserManagementService {

    private static UserManagementService instance;
    private List<Person> users;

    private UserManagementService() {
        users = new ArrayList<>();
    }

    public static UserManagementService getInstance() {
        if (instance == null)
            instance = new UserManagementService();
        return instance;
    }

    public void setUsers(List<Person> usr) {
        users = usr;
    }

    public List<Person> getUsers() {
        return users;
    }

    public Person getUserById(int userId) {
        for (Person user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }

    public Person getUserByEmail(String email) {
        for (Person user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public Person getUserByFullName(String fullName) {
        boolean validName = fullName.matches("[A-Za-z].*\\s[A-Za-z].*");
        if (validName) {
            String firstName = fullName.substring(0, fullName.indexOf(" ")).trim();
            String lastName = fullName.substring(fullName.indexOf(" ") + 1).trim();
            for (Person user : users) {
                if (user.getFirstName().equals(firstName) && user.getLastName().equals(lastName)) {
                    return user;
                }
            }
        }
        return null;
    }

    public void createUser(Person user) {
        users.add(user);
    }

    private boolean userExists(Person user) {
        if (user instanceof Librarian) {
            Librarian librarian = (Librarian) user;
            if (users.contains(librarian)) {
                System.out.println("Problem detected! The user with name: " + user.getFirstName()
                                   + " " + user.getLastName() + " and email login: " + user.getEmail()
                                   + " already exists in the system. " + System.lineSeparator() + "Please verify the user's info "
                                   + "and try to register user with different first name, last name or email address." + System.lineSeparator());
                return true;
            }
        } else if (user instanceof Clerk) {
            if (!checkClerksDeskOccupied((Clerk) user)) {
                Clerk clerk = (Clerk) user;
                if (users.contains(clerk)) {
                    System.out.println("Problem detected! The user with name: " + user.getFirstName()
                                       + " " + user.getLastName() + " and email login: " + user.getEmail()
                                       + " already exists in the system. " + System.lineSeparator() + "Please verify the user's info "
                                       + "and try to register user with different first name, last name or email address." + System.lineSeparator());
                    return true;
                }
            } else {
                return true;
            }
        } else {
            if (users.contains(user)) {
                System.out.println("Problem detected! The user with name: " + user.getFirstName()
                                   + " " + user.getLastName() + " and email login: " + user.getEmail()
                                   + " already exists in the system. " + System.lineSeparator() + "Please verify the user's info "
                                   + "and try to register user with different first name, last name or email address." + System.lineSeparator());
                return true;
            }
        }
        return false;
    }

    private boolean checkClerksDeskOccupied(Clerk clerk) {
        for (Person person : users) {
            if (person instanceof Clerk && ((Clerk) person).getDeskNumber() == clerk.getDeskNumber()) {
                System.out.println("Provided desk number: " + clerk.getDeskNumber() + " is already in use. " +
                                   "Please, provide new desk number." + System.lineSeparator());
                return true;
            }
        }
        return false;
    }

    public boolean checkClerksDeskOccupied(int deskNum) {
        for (Person person : users) {
            if (person instanceof Clerk && ((Clerk) person).getDeskNumber() == deskNum) {
                System.out.println("Provided desk number: " + deskNum + " is already in use. " +
                                   "Please, provide new desk number." + System.lineSeparator());
                return true;
            }
        }
        return false;
    }

    public void removeUser(Person user) {
        if (!users.contains(user)) {
            System.out.println("Problem detected! User with the name: " + user.getFirstName() + " " + user.getLastName() +
                               ", with email: " + user.getEmail() + " doesn't exist in the system. Please verify the libraryUser's info " +
                               "and try again to delete him/her.");
            return;
        }
        users.remove(user);
    }

    public void updateUser(Person user, String firstName, String lastName, String address, long phoneNumber,
                           String email, String password, double salary, int officeNumber, int deskNumber) {
        if (user != null) {
            if (firstName != null && !firstName.isBlank() && !firstName.isEmpty()) {
                user.setFirstName(firstName);
                System.out.println("The users first name updated successfully.");
            }

            if (lastName != null && !lastName.isBlank() && !lastName.isEmpty()) {
                user.setLastName(lastName);
                System.out.println("The users last name updated successfully.");
            }

            if (address != null && !address.isBlank() && !address.isEmpty()) {
                user.setAddress(address);
                System.out.println("The users address updated successfully.");
            }

            if (phoneNumber > 0) {
                user.setPhoneNumber(phoneNumber);
                System.out.println("The users phone number updated successfully.");
            }

            if (email != null && !email.isBlank() && !email.isEmpty()) {
                user.setEmail(email);
                System.out.println("The users email updated successfully.");
            }

            if (password != null && !password.isBlank() && !password.isEmpty()) {
                user.setPassword(password);
                System.out.println("The users password updated successfully.");
            }

            if (user instanceof Staff) {

                if (salary > 0) {
                    ((Staff) user).setSalary(salary);
                    System.out.println("The users salary updated successfully.");
                }

                if (user instanceof Librarian) {
                    if (officeNumber > 0) {
                        ((Librarian) user).setOfficeNumber(officeNumber);
                        System.out.println("The users office number updated successfully.");
                    }
                } else if (user instanceof Clerk) {
                    if (deskNumber > 0) {
                        if (!checkClerksDeskOccupied(deskNumber)) {
                            ((Clerk) user).setDeskNumber(deskNumber);
                            System.out.println("The users desk number updated successfully.");
                        }
                    }
                }
            }
        }
    }
}
