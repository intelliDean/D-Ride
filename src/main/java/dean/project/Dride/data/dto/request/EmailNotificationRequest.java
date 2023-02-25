package dean.project.Dride.data.dto.request;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailNotificationRequest {
    private  final Sender sender = new Sender("Dride", "noreply@dride_info.net");
    private List<Recipient> to = new ArrayList<>();
    private final String subject="Welcome to Dride";
    private  String htmlContent; //="<p>Hello, welcome to uber Dride. We take you to your dream</p>";

}
