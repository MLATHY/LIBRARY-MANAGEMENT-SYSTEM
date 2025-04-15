public class Reader extends User {
    public Reader(String username, String password) {
        super(username, password, "Reader");
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== READER MENU ===");
        System.out.println("1. Borrow book");
        System.out.println("2. Return book");
        System.out.println("3. View book list");
        System.out.println("4. Log out");
    }
}