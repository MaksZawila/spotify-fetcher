package vibefuze.spotifyfetcher.services;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.DelegatingProgressBarConsumer;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vibefuze.spotifyfetcher.models.*;
import vibefuze.spotifyfetcher.repository.SongRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class VibefuzeService {
    @Value("${spotify.api.user.id}")
    String USER_ID;

    @Autowired
    SpotifyAuthToken TOKEN;

    @Autowired
    SongRepository songRepository;

    @PostConstruct
    public void startTransfer(){
        SpotifyPlaylists playlists = getVibefuzePlaylists();

        playlists.getPlaylists()
                .forEach(playlist -> fetchAllTracks(playlist.getApiURL(), playlist.getName()));
    }

    public void fetchAllTracks(String url, String genre) {
        String OAuthToken = TOKEN.getAccessToken();
        String fields = "next,items(track(id,name,preview_url,external_urls(spotify),album(release_date,images),artists(name)))";
        String apiURL = url + "/tracks?fields=" + fields;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + OAuthToken);

        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<SpotifyPlaylist> res;
        List<SpotifyItem> tracks = new ArrayList<>();
        do {
            res = new RestTemplate().exchange(
                    apiURL,
                    HttpMethod.GET,
                    request,
                    SpotifyPlaylist.class);
            tracks.addAll(Objects.requireNonNull(res.getBody()).getTracks());
            apiURL = res.getBody().getNextURL();
            if(apiURL == null)
                break;

        } while(res.getStatusCode().is2xxSuccessful());

        saveSong(tracks, genre);
    }

    public SpotifyPlaylists getVibefuzePlaylists(){
        String OAuthToken = TOKEN.getAccessToken();
        String url = String.format("https://api.spotify.com/v1/users/%s/playlists", USER_ID);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + OAuthToken);
        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotifyPlaylists> response =
                restTemplate.exchange(url, HttpMethod.GET, request, SpotifyPlaylists.class);

        return response.getBody();
    }

    public void saveSong(List<SpotifyItem> items, String genre) {
        Logger logger = LoggerFactory.getLogger("VibefuzeService");
        logger.info("Started adding tracks to " + genre + " playlist");
        ProgressBar progressBar = new ProgressBarBuilder()
                .setInitialMax(items.size())
                .setTaskName("Adding tracks to " + genre + " playlist")
                .setConsumer(new DelegatingProgressBarConsumer(logger::info))
                .build();
        items.forEach((item) -> {
                    SpotifyTrack track = item.getTrack();
                    track.setGenre(genre);
                    if(track.isAnyFieldEmpty())
                        return;
                    progressBar.step();
                    songRepository.save(track);
                }
        );
        progressBar.close();
    }
}
