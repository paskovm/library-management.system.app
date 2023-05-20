package library.management.system.common.menu.help;

import library.management.system.common.entities.actors.*;
import library.management.system.common.services.UserManagementService;
import library.management.system.common.utils.DatabaseInteractions;

import java.util.Scanner;

public abstract class AddUpdateCheckUserMenu extends AddUpdateCheckBookMenu {

    private UserManagementService userService = UserManagementService.getInstance();

    private Scanner scanner = new Scanner(System.in);

    private String firstName;
    private String lastName;
    private String address;
    private long phoneNumber;
    private String email;
    private String password;
    private double salary;
    private int officeNumber;
    private int deskNumber;

    protected Person getUser() {
        Scanner scanner = new Scanner(System.in);
        Person user = null;

        MAIN:
        while (true) {
            try {
                System.out.println("----------------------------------------------------");
                System.out.println("To enter users id press 1," + System.lineSeparator() +
                                   "to enter users full name press 2," + System.lineSeparator() +
                                   "to enter users email press 3");
                System.out.println("Enter your choice: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.println("Enter users id: ");
                        int id = Integer.parseInt(scanner.nextLine());

                        id = DatabaseInteractions.getPerson(id, null, null);
                        if (id > 0) {
                            user = userService.getUserById(id);
                            if (user == null) {
                                System.out.println("There is no user with id \"" + id + "\" registered into the system.");
                            }
                        }
                        break MAIN;
                    case 2:
                        System.out.println("Enter users full name: ");
                        String fullName = scanner.nextLine();

                        id = DatabaseInteractions.getPerson(0, fullName, null);
                        if (id > 0) {
                            user = userService.getUserByFullName(fullName);
                            if (user == null) {
                                System.out.println("There is no user with name \"" + fullName + "\" registered into the system.");
                            }
                        }
                        break MAIN;
                    case 3:
                        System.out.println("Enter users email: ");
                        String email = scanner.nextLine();

                        id = DatabaseInteractions.getPerson(0, null, email);
                        if (id > 0) {
                            user = userService.getUserByEmail(email);
                            if (user == null) {
                                System.out.println("There is no user with email \"" + email + "\" registered into the system.");
                            }
                        }
                        break MAIN;
                    default:
                        System.out.println("Wrong choice selected! You need to enter number between 1-3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong choice provided! Please, try again.");
            }
        }
        return user;
    }

    protected void createUser(String userType) {

        System.out.println("----------------------------------------------------");

        while (true) {
            System.out.println(System.lineSeparator() + "Please, provide first name: ");
            firstName = scanner.nextLine();
            if (firstName.isBlank() || firstName.isEmpty()) {
                System.out.println("You can't provide empty value for first name!");
                break;
            }

            System.out.println("Please, provide last name: ");
            lastName = scanner.nextLine();
            if (lastName.isBlank() || lastName.isEmpty()) {
                System.out.println("You can't provide empty value for last name!");
                break;
            }

            System.out.println("Please, provide address: ");
            address = scanner.nextLine();
            if (address.isBlank() || address.isEmpty()) {
                System.out.println("You can't provide empty value for address!");
                break;
            }

            System.out.println("Please, provide phone number: ");
            String phNum = scanner.nextLine().replace("+", "").replace(" ", "").trim();
            if (phNum.isBlank() || phNum.isEmpty()) {
                System.out.println("You can't provide empty value or null for phone number!");
                break;
            } else {
                try {
                    phoneNumber = Long.parseLong(phNum);
                } catch (NumberFormatException e) {
                    System.out.println("The provided value for phone number must be numeric!");
                    break;
                }
            }

            System.out.println("Please, provide email address: ");
            email = scanner.nextLine();
            if (email.isBlank() || email.isEmpty()) {
                System.out.println("You can't provide empty value or for email!");
                break;
            } else if (!email.contains("@")) {
                System.out.println("You haven't provided valid email!");
                break;
            }

            System.out.println("Please, provide password: ");
            password = scanner.nextLine();
            if (password.isBlank() || password.isEmpty()) {
                System.out.println("You can't provide empty value or for password!");
                break;
            }

            if (userType.equals("Librarian") || userType.equals("Clerk")) {
                System.out.println("Please, provide users salary: ");
                String slry = scanner.nextLine();
                if (slry.isBlank() || slry.isEmpty()) {
                    System.out.println("You can't provide empty value or null for salary!");
                    break;
                } else {
                    try {
                        salary = Double.parseDouble(slry);
                    } catch (NumberFormatException e) {
                        System.out.println("The provided value for salary must be numeric!");
                        break;
                    }
                }
            }

            if (userType.equals("Librarian")) {
                System.out.println("Please, provide users office number: ");
                String offNum = scanner.nextLine();
                if (offNum.isBlank() || offNum.isEmpty()) {
                    System.out.println("You can't provide empty value or null for office number!");
                    break;
                } else {
                    try {
                        officeNumber = Integer.parseInt(offNum);
                    } catch (NumberFormatException e) {
                        System.out.println("The provided value for office must be numeric!");
                        break;
                    }
                }
            }

            if (userType.equals("Clerk")) {
                System.out.println("Please, provide users desk number: ");
                String dskNum = scanner.nextLine();
                if (dskNum.isBlank() || dskNum.isEmpty()) {
                    System.out.println("You can't provide empty value or null for desk number!");
                    break;
                } else {
                    try {
                        deskNumber = Integer.parseInt(dskNum);
                        if (userService.checkClerksDeskOccupied(deskNumber)) {
                            System.out.println("The provided desk number is already in use!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("The provided value for desk must be numeric!");
                        break;
                    }
                }
            }

            int userId = DatabaseInteractions.createPerson(firstName, lastName, address, phoneNumber, email, password,
                    officeNumber, deskNumber, salary);

            if (userId > 0) {
                Person user = null;
                if (userType.equals("Borrower")) {
                    user = new Borrower(userId, firstName, lastName, address, phoneNumber, email, password);
                } else if (userType.equals("Librarian") && salary != 0 && officeNumber != 0) {
                    user = new Librarian(userId, firstName, lastName, address, phoneNumber, email, password, salary, officeNumber);
                } else if (userType.equals("Clerk") && salary != 0 && deskNumber != 0) {
                    user = new Clerk(userId, firstName, lastName, address, phoneNumber, email, password, salary, deskNumber);
                } else {
                    System.out.println("Problem detected! Empty value provided!");
                    System.out.println("The added user can't have empty value!");
                }

                if (user != null) {
                    userService.createUser(user);
                    user.printInfo();
                }
                break;
            }
        }
    }

    protected void updateUser(Person user) {
        String input = "";

        System.out.println("----------------------------------------------------");
        System.out.println("Please, enter users first name or press enter to continue: ");
        this.firstName = scanner.nextLine();

        System.out.println("Please, enter users last name or press enter to continue: ");
        this.lastName = scanner.nextLine();

        System.out.println("Please, enter users new address or press enter to continue: ");
        this.address = scanner.nextLine();

        while (true) {
            try {
                System.out.println("Please, enter users new phone number or press enter to continue: ");
                input = scanner.nextLine();
                if (input.isBlank() || input.isEmpty()) {
                    break;
                }
                this.phoneNumber = Long.parseLong(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Problem detected! Wrong value entered for the phone number: " + input +
                                   System.lineSeparator() + " Please, try again.");
            }
        }

        System.out.println("Please, enter users new email or press enter to continue: ");
        this.email = scanner.nextLine();

        System.out.println("Please, enter users new password or press enter to continue: ");
        this.password = scanner.nextLine();

        if (user instanceof Staff) {
            while (true) {
                try {
                    System.out.println("Please, enter users new salary or press enter to continue: ");
                    input = scanner.nextLine();
                    if (input.isBlank() || input.isEmpty()) {
                        break;
                    }
                    this.salary = Double.parseDouble(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Problem detected! Wrong value entered for the salary: " + input +
                                       System.lineSeparator() + " Please, try again.");
                }
            }
        }

        if (user instanceof Librarian) {
            while (true) {
                try {
                    System.out.println("Please, enter users new office number or press enter to continue: ");
                    input = scanner.nextLine();
                    if (input.isBlank() || input.isEmpty()) {
                        break;
                    }
                    this.officeNumber = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Problem detected! Wrong value entered for the office: " + input +
                                       System.lineSeparator() + " Please, try again.");
                }
            }
        }

        if (user instanceof Clerk) {
            while (true) {
                try {
                    System.out.println("Please, enter users new desk number or press enter to continue: ");
                    input = scanner.nextLine();
                    if (input.isBlank() || input.isEmpty()) {
                        break;
                    }
                    this.deskNumber = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Problem detected! Wrong value entered for the desk: " + input +
                                       System.lineSeparator() + " Please, try again.");
                }
            }
        }

        if ((firstName != null && !firstName.isEmpty() && !firstName.isEmpty()) ||
            (lastName != null && !lastName.isBlank() && !lastName.isEmpty()) ||
            (address != null && !address.isBlank() && !address.isEmpty()) ||
            phoneNumber > 0 ||
            (email != null && !email.isBlank() && !email.isEmpty()) ||
            (password != null && !password.isBlank() && !password.isEmpty()) ||
            salary > 0 ||
            officeNumber > 0 ||
            deskNumber > 0) {

            int result = DatabaseInteractions.updatePerson(user.getId(), firstName, lastName, address, phoneNumber, email, password, officeNumber, deskNumber, salary);
            if (result > 0) {
                userService.updateUser(user, firstName, lastName, address, phoneNumber, email, password, salary, officeNumber, deskNumber);
            }

        } else {
            System.out.println("There is nothing to be updated!");
        }

    }
}
