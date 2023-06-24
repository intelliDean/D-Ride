package dean.project.Dride.services.dride_token_services;

import dean.project.Dride.data.models.DrideToken;
import dean.project.Dride.data.repositories.DrideTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class DrideTokenServiceImpl implements DrideTokenService {
    private final DrideTokenRepository drideTokenRepository;

    @Override
    public Optional<DrideToken> findByToken(String accessToken) {
        return drideTokenRepository.findByAccessToken(accessToken);
    }

    @Override
    public void saveToken(DrideToken token) {
        drideTokenRepository.save(token);
    }

    @Override
    public void revokeToken(String accessToken) {
        DrideToken token = findByToken(accessToken)
                .orElse(null);
        if (token != null) {
            token.setExpired(true);
            token.setRevoked(true);
            drideTokenRepository.save(token);
        }
    }



}
