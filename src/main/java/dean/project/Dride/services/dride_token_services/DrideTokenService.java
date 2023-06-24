package dean.project.Dride.services.dride_token_services;

import dean.project.Dride.data.models.DrideToken;

import java.util.Optional;


public interface DrideTokenService {
   Optional<DrideToken> findByToken(String accessToken);
   void saveToken(DrideToken token);
   void revokeToken(String accessToken);
   //void generateDrideToken(String accessToken, String refreshToken, User user);
}
