package library.management.system.common.utils;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.HoldRequest;
import library.management.system.common.entities.Loan;
import library.management.system.common.entities.actors.*;
import library.management.system.common.services.BookManagementService;
import library.management.system.common.services.HoldRequestManagementService;
import library.management.system.common.services.LoanManagementService;
import library.management.system.common.services.UserManagementService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PopulateLibrary {

    private static final String HOST = "jdbc:mysql://127.0.0.1:3306/lms";
    private static final String USERNAME = "devuser";
    private static final String PASSWORD = "12345";
    private static List<Book> books = new ArrayList<>();
    private static List<Person> users = new ArrayList<>();
    private static List<Loan> loans = new ArrayList<>();
    private static List<HoldRequest> holdRequests = new ArrayList<>();
    private static TempLibrarian tempLibrarian = null;
    private static List<TempClerk> tempClerks = new ArrayList<>();
    private static List<TempStaff> tempStaffs = new ArrayList<>();
    private static UserManagementService userService = UserManagementService.getInstance();
    private static BookManagementService bookService = BookManagementService.getInstance();
    private static LoanManagementService loanService = LoanManagementService.getInstance();
    private static HoldRequestManagementService holdReqService = HoldRequestManagementService.getInstance();

    public static void populate() {
        try (Connection connection = DriverManager.getConnection(HOST, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            collectAllUsers(statement);
            collectAllBooks(statement);
            collectAllLoans(statement);
            collectHoldRequests(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void collectHoldRequests(Statement statement) throws SQLException {
        String sql = "SELECT * FROM LMS.HOLDREQUEST";
        ResultSet result = statement.executeQuery(sql);

        while (result.next()) {
            int borrowerId = result.getInt("Borrower");
            int bookId = result.getInt("Book");
            String requestDateStr = result.getString("RequestDate");
            LocalDate requestDate = null;
            if (requestDateStr != null) {
                requestDate = LocalDate.parse(requestDateStr);
            }

            if (borrowerId > 0 && bookId > 0 && requestDate != null) {
                Borrower borrower = (Borrower) getUser(borrowerId);
                Book book = getBook(bookId);

                if (borrower != null && book != null) {
                    HoldRequest holdRequest = new HoldRequest(borrower, book);
                    holdRequest.setRequestDate(requestDate);

                    holdRequests.add(holdRequest);
                }
            }
        }

        holdReqService.setHoldRequests(holdRequests);
    }

    private static void collectAllLoans(Statement statement) throws SQLException {
        String sql = "SELECT * FROM LMS.LOAN";
        ResultSet result = statement.executeQuery(sql);

        while (result.next()) {
            int borrowerId = result.getInt("Borrower");
            int bookId = result.getInt("Book");
            int issuerId = result.getInt("Issuer");
            LocalDate issueDate = LocalDate.parse(result.getString("IssueDate"));
            int receiverId = result.getInt("Receiver");
            boolean finePaid = result.getBoolean("FinePaid");

            LocalDate returnDate = null;
            String returnDateStr = result.getString("ReturnDate");
            if (returnDateStr != null) {
                returnDate = LocalDate.parse(result.getString("ReturnDate"));
            }

            if (!books.isEmpty() && !users.isEmpty()) {
                Borrower borrower = (Borrower) getUser(borrowerId);
                Staff issuer = (Staff) getUser(issuerId);
                Staff receiver = null;
                if (receiverId > 0) {
                    receiver = (Staff) getUser(receiverId);
                }
                Book book = getBook(bookId);

                if (borrower != null && issuer != null && book != null && issueDate != null) {
                    Loan loan = new Loan(borrower, book, issuer);
                    loan.setIssueDate(issueDate);
                    loan.setReceiver(receiver);
                    loan.setReturnDate(returnDate);
                    loan.setFinePaid(finePaid);

                    loans.add(loan);
                }
            }
        }

        loanService.setLoans(loans);
    }

    private static Book getBook(int id) {
        for (Book book : books) {
            if (book.getBookId() == id) {
                return book;
            }
        }
        return null;
    }

    private static Person getUser(int id) {
        for (Person user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    private static void collectAllBooks(Statement statement) throws SQLException {
        String sql = "SELECT * FROM LMS.BOOK";
        ResultSet result = statement.executeQuery(sql);

        while (result.next()) {
            int id = result.getInt("ID");
            String title = result.getString("Title");
            String author = result.getString("Author");
            String genre = result.getString("Genre");
            boolean isIssued = result.getBoolean("IsIssued");

            Book book = new Book(id, title, author, genre);
            book.setIssued(isIssued);
            books.add(book);
        }

        bookService.setBooks(books);
    }

    private static void collectAllUsers(Statement statement) throws SQLException {
        collectLibrarian(statement);
        collectClerks(statement);
        collectStaff(statement);

        String sql = "SELECT * FROM LMS.PERSON";
        ResultSet result = statement.executeQuery(sql);

        while (result.next()) {
            int id = result.getInt("ID");
            String fName = result.getString("FirstName");
            String lName = result.getString("LastName");
            String address = result.getString("Address");
            long phone = Long.parseLong(result.getString("PhoneNumber").replaceAll("\\s", "").replace("+", ""));
            String email = result.getString("Email");
            String password = result.getString("Password");

            if (isLibrarian(id)) {
                int office = tempLibrarian.officeNumber;

                if (tempStaffs.size() > 0) {
                    double salary = 0;

                    for (TempStaff staff : tempStaffs) {
                        if (staff.id == id) {
                            salary = staff.salary;
                            break;
                        }
                    }

                    if (salary > 0) {
                        Librarian librarian = new Librarian(id, fName, lName, address, phone, email, password, salary, office);
                        users.add(librarian);
                    } else {
                        System.out.println("There is inconsistency in the db about the librarian!");
                    }
                } else {
                    System.out.println("There is inconsistency in the db about the librarian!");
                }

            } else if (isClerk(id)) {

                TempClerk tempClerk = null;
                for (TempClerk tmpC : tempClerks) {
                    if (tmpC.id == id) {
                        tempClerk = tmpC;
                        break;
                    }
                }

                if (tempClerk != null) {
                    int deskNumber = tempClerk.deskNumber;
                    double salary = 0;

                    for (TempStaff tempStaff :
                            tempStaffs) {
                        if (tempStaff.id == id) {
                            salary = tempStaff.salary;
                            break;
                        }
                    }

                    if (salary > 0) {
                        Clerk clerk = new Clerk(id, fName, lName, address, phone, email, password, salary, deskNumber);
                        users.add(clerk);
                    } else {
                        System.out.println("There is inconsistency in the db about the clerk! ID: " + id);
                    }
                } else {
                    System.out.println("There is inconsistency in the db about the clerk! ID: " + id);
                }

            } else { // This must be borrower
                Borrower borrower = new Borrower(id, fName, lName, address, phone, email, password);
                users.add(borrower);
            }

        }

        userService.setUsers(users);
    }

    private static boolean isLibrarian(int id) {
        if (tempLibrarian != null) {
            if (tempLibrarian.id == id) {
                return true;
            }
        }
        return false;
    }

    private static boolean isClerk(int id) {
        if (tempClerks.size() > 0) {
            for (TempClerk tempClerk : tempClerks) {
                if (tempClerk.id == id) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void collectLibrarian(Statement statement) throws SQLException {
        String sql = "SELECT * FROM LMS.LIBRARIAN";

        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            int id = result.getInt("ID");
            int officeNumber = result.getInt("OfficeNumber");

            tempLibrarian = new TempLibrarian(id, officeNumber);
        }
    }

    private static void collectClerks(Statement statement) throws SQLException {
        String sql = "SELECT * FROM LMS.CLERK";

        ResultSet result = statement.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("ID");
            int deskNumber = result.getInt("DeskNumber");

            tempClerks.add(new TempClerk(id, deskNumber));
        }
    }

    private static void collectStaff(Statement statement) throws SQLException {
        String sql = "SELECT * FROM LMS.STAFF";

        ResultSet result = statement.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("ID");
            int salary = result.getInt("Salary");

            tempStaffs.add(new TempStaff(id, salary));
        }
    }

    private static void printBooks() {
        for (Book book : books) {
            book.printInfo();
        }
    }

    private static void printUsers() {
        for (Person user : users) {
            user.printInfo();
        }
    }

    private static void printLoans() {
        for (Loan loan : loans) {
            loan.printInfo();
        }
    }

    private static void printHoldRequests() {
        for (HoldRequest holdRequest : holdRequests) {
            holdRequest.printInfo();
        }
    }

    private static class TempLibrarian {
        int id;
        int officeNumber;

        public TempLibrarian(int id, int officeNumber) {
            this.id = id;
            this.officeNumber = officeNumber;
        }
    }

    private static class TempClerk {
        int id;
        int deskNumber;

        public TempClerk(int id, int deskNumber) {
            this.id = id;
            this.deskNumber = deskNumber;
        }
    }

    private static class TempStaff {
        int id;
        double salary;

        public TempStaff(int id, double salary) {
            this.id = id;
            this.salary = salary;
        }
    }

}
