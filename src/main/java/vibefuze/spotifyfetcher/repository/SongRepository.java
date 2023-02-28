package vibefuze.spotifyfetcher.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vibefuze.spotifyfetcher.models.SpotifyTrack;

public interface SongRepository extends MongoRepository<SpotifyTrack, String> {

}
