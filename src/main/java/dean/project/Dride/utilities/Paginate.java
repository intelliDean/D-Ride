package dean.project.Dride.utilities;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Paginate<T> {
    private Long totalElements;
    private Long totalPages;
    private Long pageNumber;
    private Long pageSize;
    private List<T> content;
}
