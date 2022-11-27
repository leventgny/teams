package teams.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerDTO {

    private String name;
    private int age;

}
