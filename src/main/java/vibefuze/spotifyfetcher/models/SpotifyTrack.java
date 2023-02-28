package vibefuze.spotifyfetcher.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Document("songs")
public class SpotifyTrack {
    @Id
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String title;

    @JsonProperty("preview_url")
    private String previewUrl;
    private List<String> artists;
    private String genre;
    private String imageUrl;
    private String url;
    private String releaseDate;

    @JsonProperty("artists")
    public void setArtists(List<Map<String, String>> artists) {
        this.artists = new ArrayList<>();
        for (Map<String, String> artist : artists) {
            this.artists.add(artist.get("name"));
        }
    }

    @JsonProperty("external_urls")
    public void setUrl(Map<String, String> url) {
        this.url = url.get("spotify");
    }

    @JsonProperty("album")
    public void setAlbumProperties(SpotifyAlbum album) {
        this.releaseDate = album.getRelease_date();
        this.imageUrl = album.getImages().get(0).get("url");
    }

    @SneakyThrows
    public boolean isAnyFieldEmpty() {
        for (Field field : getClass().getDeclaredFields()) {
            if (field.get(this) == null) {
                return true;
            }
        }
        return false;
    }
}
