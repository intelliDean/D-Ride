package dean.project.Dride.data.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Rating {
    POOR(1),
    BAD(2),
    SATISFACTORY(3),
    GOOD(4),
    EXCELLENT(5);

    private final int rating;

}
