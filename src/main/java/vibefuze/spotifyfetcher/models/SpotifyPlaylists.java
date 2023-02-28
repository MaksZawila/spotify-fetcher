package vibefuze.spotifyfetcher.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SpotifyPlaylists {
    @JsonProperty("items")
    private List<SpotifyPlaylistInfo> playlists;
}
