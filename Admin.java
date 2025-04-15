public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password, "Admin");
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== ADMIN MENU ===");
        System.out.println("1. Add user");
        System.out.println("2. Delete user");
        System.out.println("3. List users");
        System.out.println("4. Log out");
    }
}