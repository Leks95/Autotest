package Tests.Test1;

public enum Subheaders {
    Students("Students"),
    Instructors("Instructors"),
    Book("Book Authors"),
    Professionals("Professionals"),
    Researchers("Researchers"),
    Institutions("Institutions"),
    Librarians("Librarians"),
    Corporations("Corporations"),
    Societies("Societies"),
    Journal("Journal Editors"),
    Government("Government");

    private String title;

    Subheaders(String title){
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

}