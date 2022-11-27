package teams.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player implements Serializable {

    @Id
    private Long citizenNumber;
    private String name;
    private LocalDate birth;

    @ManyToOne
    private Team team;

}
