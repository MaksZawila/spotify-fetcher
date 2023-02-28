package vibefuze.spotifyfetcher.models;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SpotifyAlbum {
    private List<Map<String,String>> images;
    private String release_date;

}
