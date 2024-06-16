package com.sondev.identityservice.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sondev.identityservice.dto.request.AuthenticationRequest;
import com.sondev.identityservice.dto.request.IntrospectRequest;
import com.sondev.identityservice.dto.response.AuthenticationResponse;
import com.sondev.identityservice.dto.response.IntrospectResponse;
import com.sondev.identityservice.entity.User;
import com.sondev.identityservice.exception.AppException;
import com.sondev.identityservice.exception.ErrorCode;
import com.sondev.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}") // Đọc biến từ file application.yaml

    protected String SIGNER_KEY;
    public IntrospectResponse introspect (IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

       var verify = signedJWT.verify(verifier);

       return  IntrospectResponse.builder()
               .valid(verify && expirityTime.after(new Date()))
               .build();
    }

   public AuthenticationResponse authenticate (AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated){
            // Trường hợp mật khẩu không khớp với db
            throw  new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(request.getUsername());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }



    private String generateToken(String username){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("sondev.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("customClaim","custom")
                .build();
        Payload payload= new Payload(jwtClaimsSet.toJSONObject());
       JWSObject jwsObject = new JWSObject(header,payload);

       //Sign token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            //log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
}
