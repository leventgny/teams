package teams.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(
        mappedBy = "team",
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        targetEntity = Player.class
    )
    private final Set<Player> players = new HashSet<>();

    public void addPlayer(Player player) {
        this.getPlayers().add(player);
    }

    public void removePlayer(Player player) {
        this.getPlayers().removeIf(p ->
            p.getCitizenNumber().longValue() == player.getCitizenNumber().longValue()
        );
        player.setTeam(null);
    }
}
