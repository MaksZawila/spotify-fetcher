package vibefuze.spotifyfetcher.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vibefuze.spotifyfetcher.models.SpotifyAuthToken;

import java.util.Base64;
import java.util.Collections;
import java.util.Objects;

@Service
public class SpotifyAuthTokenService {
    @Value("${spotify.api.client.id}")
    String ID;

    @Value("${spotify.api.client.secret}")
    String SECRET;

    @Value("${spotify.api.token-url}")
    String TOKEN_URL;

    @Bean
    public SpotifyAuthToken getSpotifyAuthToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", getEncodedAuthorization());

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotifyAuthToken> response =
                restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, SpotifyAuthToken.class);
        return Objects.requireNonNull(response.getBody());
    }

    private String getEncodedAuthorization() {
        String authString = ID + ":" + SECRET;
        byte[] authBytes = authString.getBytes();
        String encodedAuth = Base64.getEncoder().encodeToString(authBytes);
        return "Basic " + encodedAuth;
    }
}
