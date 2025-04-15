public class Ebook extends Book {
    private String fileFormat;

    public Ebook(String title, String author, String genre, String isbn, String fileFormat) {
        super(title, author, genre, isbn);
        this.fileFormat = fileFormat;
        super.setAvailable(false);
    }

    public String getFileFormat() {
        return fileFormat;
    }

    @Override
    public String getType() {
        return "Ebook";
    }

    @Override
    public void setAvailable(boolean available) {
        // Ebooks can never be available for borrowing
        super.setAvailable(false);
    }

    @Override
    public String toString() {
        return super.toString() + "," + fileFormat;
    }
}