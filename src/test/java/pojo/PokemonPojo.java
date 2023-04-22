package pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonPojo {

    private int count;
    private String next;
    private String previous;
    private List<PokemonResult> results;

}
