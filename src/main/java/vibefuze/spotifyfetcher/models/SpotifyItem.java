package vibefuze.spotifyfetcher.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotifyItem {
    @JsonProperty("track")
    private SpotifyTrack track;

}
