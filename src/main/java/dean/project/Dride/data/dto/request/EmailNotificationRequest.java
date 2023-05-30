package dean.project.Dride.data.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static dean.project.Dride.utilities.Constants.APP_NAME;
import static dean.project.Dride.utilities.Constants.DRIDE_EMAIL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationRequest {
    private  final Sender sender = new Sender(APP_NAME, DRIDE_EMAIL);
    private List<Recipient> to = new ArrayList<>();
    private String subject;
    private String htmlContent;
}
