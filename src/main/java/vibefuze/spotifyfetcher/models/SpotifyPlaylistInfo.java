package vibefuze.spotifyfetcher.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotifyPlaylistInfo {
    @JsonProperty("href")
    private String apiURL;

    @JsonProperty("name")
    private String name;

    public String getName() {
        return name.replaceAll("Vibefuze:", "");
    }
}
