import java.time.LocalDate;

public abstract class Book {
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private boolean available;
    private LocalDate dueDate;

    public Book(String title, String author, String genre, String isbn) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.available = true;
        this.dueDate = null;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return available; }
    public LocalDate getDueDate() { return dueDate; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public abstract String getType();

    @Override
    public String toString() {
        String dueDateStr = (dueDate != null) ? dueDate.toString() : "null";
        return getType() + "," + title + "," + author + "," + genre + "," + isbn + "," 
               + available + "," + dueDateStr;
    }
}