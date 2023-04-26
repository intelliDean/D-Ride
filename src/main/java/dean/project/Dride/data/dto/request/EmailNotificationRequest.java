package dean.project.Dride.data.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static dean.project.Dride.utilities.Constants.DRIDE_EMAIL;
import static dean.project.Dride.utilities.Constants.ISSUER;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationRequest {
    private  final Sender sender = new Sender(ISSUER, DRIDE_EMAIL);
    private List<Recipient> to = new ArrayList<>();
    private String subject;
    private String htmlContent;
}
