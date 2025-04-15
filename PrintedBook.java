public class PrintedBook extends Book {
    private int pageCount;

    public PrintedBook(String title, String author, String genre, String isbn, int pageCount) {
        super(title, author, genre, isbn);
        this.pageCount = pageCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    @Override
    public String getType() {
        return "PrintedBook";
    }

    @Override
    public String toString() {
        return super.toString() + "," + pageCount;
    }
}