import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LibraryManagementSystem {
    private List<User> users;
    private List<Book> books;
    private Map<String, List<Book>> borrowedBooks;
    private User currentUser;
    private Scanner scanner;
    private DateTimeFormatter dateFormatter;

    public LibraryManagementSystem() {
        users = new ArrayList<>();
        books = new ArrayList<>();
        borrowedBooks = new HashMap<>();
        scanner = new Scanner(System.in);
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        loadData();
    }

    private void loadData() {
        loadUsers();
        loadBooks();
    }

    private void loadUsers() {
        File file = new File("users.txt");
        if (!file.exists()) {
            // Create default users if file doesn't exist
            users.add(new Admin("admin", "admin123"));
            users.add(new Librarian("librarian", "lib123"));
            users.add(new Reader("reader", "reader123"));
            saveUsers();
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(",");
                if (parts.length == 3) {
                    switch (parts[2]) {
                        case "Admin":
                            users.add(new Admin(parts[0], parts[1]));
                            break;
                        case "Librarian":
                            users.add(new Librarian(parts[0], parts[1]));
                            break;
                        case "Reader":
                            users.add(new Reader(parts[0], parts[1]));
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
    }

    private void loadBooks() {
        File file = new File("books.txt");
        if (!file.exists()) {
            // Add sample books if file doesn't exist
            books.add(new PrintedBook("Java Programming", "John Doe", "Programming", "1234567890", 500));
            books.add(new Ebook("Python Basics", "Jane Smith", "Programming", "0987654321", "PDF"));
            saveBooks();
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(",");
                if (parts.length >= 7) {
                    String type = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    String genre = parts[3];
                    String isbn = parts[4];
                    boolean available = Boolean.parseBoolean(parts[5]);
                    LocalDate dueDate = parts[6].equals("null") ? null : LocalDate.parse(parts[6], dateFormatter);

                    Book book;
                    if (type.equals("PrintedBook") && parts.length == 8) {
                        book = new PrintedBook(title, author, genre, isbn, Integer.parseInt(parts[7]));
                    } else if (type.equals("Ebook") && parts.length == 8) {
                        book = new Ebook(title, author, genre, isbn, parts[7]);
                    } else {
                        continue;
                    }

                    book.setAvailable(available);
                    book.setDueDate(dueDate);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading books: " + e.getMessage());
        }
    }

    public void login() {
        System.out.println("=== Log in ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Logged in successfully as: " + user.getRole());
                showMenu();
                return;
            }
        }
        System.out.println("Incorrect username or password!");
    }

    private void showMenu() {
        currentUser.displayMenu();
        System.out.print("Select an option: ");
        
        String input = scanner.nextLine();
        if (!isNumeric(input)) {
            System.out.println("Invalid input! Please enter a number.");
            showMenu();
            return;
        }
        
        int choice = Integer.parseInt(input);

        switch (currentUser.getRole()) {
            case "Admin":
                handleAdminMenu(choice);
                break;
            case "Librarian":
                handleLibrarianMenu(choice);
                break;
            case "Reader":
                handleReaderMenu(choice);
                break;
        }
    }

    private void handleAdminMenu(int choice) {
        switch (choice) {
            case 1:
                addUser();
                break;
            case 2:
                deleteUser();
                break;
            case 3:
                listUsers();
                break;
            case 4:
                logout();
                break;
            default:
                System.out.println("Invalid selection!");
        }
        if (choice != 4) showMenu();
    }

    private void handleLibrarianMenu(int choice) {
        switch (choice) {
            case 1:
                addBook();
                break;
            case 2:
                deleteBook();
                break;
            case 3:
                listAvailableBooks();
                break;
            case 4:
                logout();
                break;
            default:
                System.out.println("Invalid selection!");
        }
        if (choice != 4) showMenu();
    }

    private void handleReaderMenu(int choice) {
        switch (choice) {
            case 1:
                borrowBook();
                break;
            case 2:
                returnBook();
                break;
            case 3:
                listAllBooks();
                break;
            case 4:
                logout();
                break;
            default:
                System.out.println("Invalid selection!");
        }
        if (choice != 4) showMenu();
    }

    private void addUser() {
        System.out.println("\n=== ADD USER ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        boolean exists = users.stream()
                             .anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
        if (exists) {
            System.out.println("Error: Username already exists!");
            return;
        }
    
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Role (Admin/Librarian/Reader): ");
        String role = scanner.nextLine().trim();
    
        if (!role.equalsIgnoreCase("Admin") && 
            !role.equalsIgnoreCase("Librarian") && 
            !role.equalsIgnoreCase("Reader")) {
            System.out.println("Invalid role!");
            return;
        }
    
        switch (role.toLowerCase()) {
            case "admin":
                users.add(new Admin(username, password));
                break;
            case "librarian":
                users.add(new Librarian(username, password));
                break;
            case "reader":
                users.add(new Reader(username, password));
                break;
        }
        
        saveUsers();
        System.out.println("User added successfully!");
    }

    private void deleteUser() {
        System.out.println("\n=== DELETE USER ===");
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();

        User userToRemove = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                userToRemove = user;
                break;
            }
        }

        if (userToRemove != null) {
            users.remove(userToRemove);
            saveUsers();
            System.out.println("User deleted successfully!");
        } else {
            System.out.println("User not found!");
        }
    }

    private void listUsers() {
        System.out.println("\n=== USER LIST ===");
        for (User user : users) {
            System.out.println("Username: " + user.getUsername() + ", Role: " + user.getRole());
        }
    }

    private void addBook() {
        System.out.println("\n=== ADD BOOK ===");
        System.out.print("Book type (PrintedBook/Ebook): ");
        String type = scanner.nextLine();
    
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
    
        if (type.equals("PrintedBook")) {
            System.out.print("Page number: ");
            int pageCount = Integer.parseInt(scanner.nextLine());
            books.add(new PrintedBook(title, author, genre, isbn, pageCount));
        } else if (type.equals("Ebook")) {
            System.out.print("File format (PDF/EPUB/MOBI): ");
            String fileFormat = scanner.nextLine().toUpperCase();
    
            Set<String> validFormats = new HashSet<>(Arrays.asList("PDF", "EPUB", "MOBI"));
            if (!validFormats.contains(fileFormat)) {
                System.out.println("Invalid file format!! Must be PDF, EPUB, or MOBI.");
                return;
            }
    
            books.add(new Ebook(title, author, genre, isbn, fileFormat));
        } else {
            System.out.println("Invalid book type!!");
            return;
        }
    
        saveBooks();
        System.out.println("Book added successfully!");
    }
    

    private void deleteBook() {
        System.out.println("\n=== DELETE BOOK ===");
        System.out.print("Enter ISBN of the book to delete: ");
        String isbn = scanner.nextLine();

        Book bookToRemove = null;
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                bookToRemove = book;
                break;
            }
        }

        if (bookToRemove != null) {
            books.remove(bookToRemove);
            saveBooks();
            System.out.println("Book deleted successfully!");
        } else {
            System.out.println("Book not found!");
        }
    }

    private void listAvailableBooks() {
        System.out.println("\n=== AVAILABLE BOOKS ===");
        for (Book book : books) {
            if (book.isAvailable()) {
                System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor() + 
                                 ", ISBN: " + book.getIsbn());
            }
        }
    }

    private void borrowBook() {
        System.out.println("\n=== BORROW BOOK ===");
        System.out.print("Enter ISBN of the book to borrow: ");
        String isbn = scanner.nextLine();

        Book bookToBorrow = null;
        for (Book book : books) {
            if (book.getIsbn().equals(isbn) && book.isAvailable() && !(book instanceof Ebook)) {
                bookToBorrow = book;
                break;
            }
        }

        if (bookToBorrow != null) {
            LocalDate dueDate = LocalDate.now().plusDays(14);
            bookToBorrow.setAvailable(false);
            bookToBorrow.setDueDate(dueDate);
            
            borrowedBooks.computeIfAbsent(currentUser.getUsername(), k -> new ArrayList<>()).add(bookToBorrow);
            
            saveBooks();
            System.out.println("Book borrowed successfully! Due date: " + dueDate.format(dateFormatter));
        } else {
            System.out.println("Cannot borrow this book (not available or is an ebook)!");
        }
    }

    private void returnBook() {
        System.out.println("\n=== RETURN BOOK ===");
        List<Book> userBooks = borrowedBooks.getOrDefault(currentUser.getUsername(), new ArrayList<>());
        
        if (userBooks.isEmpty()) {
            System.out.println("You have no books to return!");
            return;
        }

        System.out.println("Books you have borrowed:");
        for (int i = 0; i < userBooks.size(); i++) {
            Book book = userBooks.get(i);
            System.out.println((i+1) + ". " + book.getTitle() + " (ISBN: " + book.getIsbn() + 
                             "), Due date: " + book.getDueDate().format(dateFormatter));
        }

        System.out.print("Choose the number of the book to return: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;

        if (choice >= 0 && choice < userBooks.size()) {
            Book bookToReturn = userBooks.get(choice);
            bookToReturn.setAvailable(true);
            bookToReturn.setDueDate(null);
            userBooks.remove(choice);
            
            saveBooks();
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Invalid selection!");
        }
    }

    private void listAllBooks() {
        System.out.println("\n=== ALL BOOKS ===");
        for (Book book : books) {
            String status = book.isAvailable() ? "Available" : "Borrowed";
            if (book instanceof Ebook) {
                status = "Ebook (not borrowable)";
            }
            System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor() + 
                             ", ISBN: " + book.getIsbn() + ", Status: " + status);
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("Logged out successfully!");
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.txt"))) {
            for (User user : users) {
                writer.println(user.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private void saveBooks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("books.txt"))) {
            for (Book book : books) {
                writer.println(book.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public void run() {
        while (true) {
            System.out.println("\n=== LIBRARY MANAGEMENT SYSTEM ===");
            System.out.println("1. Log in");
            System.out.println("2. Exit");
            System.out.print("Select an option: ");
            
            String input = scanner.nextLine();
            if (!isNumeric(input)) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            
            int choice = Integer.parseInt(input);
            
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    System.out.println("Exiting the program...");
                    return;
                default:
                    System.out.println("Invalid selection!");
            }
        }
    }


    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  // Match a number with optional '-' and decimal
    }
}