package vibefuze.spotifyfetcher.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SpotifyPlaylist {
    @JsonProperty("items")
    private List<SpotifyItem> tracks;

    @JsonProperty("next")
    private String nextURL;

}
