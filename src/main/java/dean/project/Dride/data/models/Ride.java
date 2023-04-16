package dean.project.Dride.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Passenger passenger;
    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private Driver driver;
    private String origin;
    private String destination;
    @Enumerated(EnumType.STRING)
    private Status rideStatus;
    private String startTime;
    private String endTime;
    private String bookTime;
    @Enumerated(EnumType.STRING)
    private Rating driverRating;
    @Enumerated(EnumType.STRING)
    private Rating passengerRating;
    private BigDecimal fare;
}
