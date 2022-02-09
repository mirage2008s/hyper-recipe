package recipes.businesslayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Data
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Pattern(regexp = ".+@.+\\..+")
    @NotNull
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;
}
