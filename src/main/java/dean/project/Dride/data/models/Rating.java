package dean.project.Dride.data.models;


public enum Rating {
    BAD(1),
    FAIR(2),
    GOOD(3),
    SATISFACTORY(4),
    EXCELLENT(5);

    private final int rating;

    Rating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

}
