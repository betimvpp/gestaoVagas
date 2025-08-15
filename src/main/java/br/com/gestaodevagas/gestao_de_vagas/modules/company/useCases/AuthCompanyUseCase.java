package br.com.gestaodevagas.gestao_de_vagas.modules.company.useCases;

import java.time.Duration;
import java.time.Instant;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.gestaodevagas.gestao_de_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.gestaodevagas.gestao_de_vagas.modules.company.repositories.CompanyRepository;

@Service
public class AuthCompanyUseCase {

    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder PasswordEncoder;

    public String execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
        var company = this.companyRepository.findByUsername((authCompanyDTO.getUsername())).orElseThrow(
                () -> {
                    throw new UsernameNotFoundException("Company/Password incorrect.");
                });
        var passwordMatches = this.PasswordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        if (!passwordMatches) {
            throw new AuthenticationException();
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var token = JWT.create().withIssuer("devjobs")
                .withExpiresAt(Instant.now().plus(Duration.ofHours(168)))
                .withSubject(company.getId().toString())
                .sign(algorithm);
        return token;
    }
}
