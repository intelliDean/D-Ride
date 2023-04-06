package dean.project.Dride.data.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Rating {
    BAD(1),
    FAIR(2),
    GOOD(3),
    SATISFACTORY(4),
    EXCELLENT(5);

    private final int rating;
}
