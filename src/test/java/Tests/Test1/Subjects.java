package Tests.Test1;

public enum Subjects {
    Information("Information & Library Science"),
    Education("Education & Public Policy"),
    K_12("K-12 General"),
    Higher("Higher Education General"),
    Vocational("Vocational Technology"),
    Conflict("Conflict Resolution & Mediation (School settings)"),
    Curriculum("Curriculum Tools- General"),
    Special("Special Educational Needs"),
    Theory("Theory of Education"),
    Education_Special("Education Special Topics"),
    Educational_Research("Educational Research & Statistics"),
    Literacy("Literacy & Reading"),
    Classroom("Classroom Management");

    private String title;

    Subjects(String title){
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
