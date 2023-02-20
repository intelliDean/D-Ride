package dean.project.Dride.data.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationRequest {
    private  final Sender sender = new Sender("Dride", "noreply@dride_info.net");
    private List<Recipient> to;
    private final String subject="Welcome to Dride";
    private final String htmlContent="<p>Hello, welcome to uber Dride. We take you to your dream</p>";

}
