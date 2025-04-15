public class Librarian extends User {
    public Librarian(String username, String password) {
        super(username, password, "Librarian");
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== LIBRARIAN MENU ===");
        System.out.println("1. Add book");
        System.out.println("2. Delete book");
        System.out.println("3. List available books");
        System.out.println("4. Log out");
    }
}