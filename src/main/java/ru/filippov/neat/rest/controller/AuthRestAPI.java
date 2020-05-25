package ru.filippov.neat.rest.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.filippov.neat.config.jwt.TokenProvider;
import ru.filippov.neat.domain.Auth;
import ru.filippov.neat.domain.User;
import ru.filippov.neat.dto.UserAuthDetailsResponse;
import ru.filippov.neat.dto.SignInDto;
import ru.filippov.neat.dto.SignUpDto;
import ru.filippov.neat.dto.TokenDto;
import ru.filippov.neat.exceptions.RefreshTokenNotExists;
import ru.filippov.neat.service.auth.AuthServiceImpl;
import ru.filippov.neat.service.user.UserDetailsServiceImpl;
import ru.filippov.neat.service.user.UserPrinciple;

import javax.validation.Valid;
import java.util.Date;

@RestController
@Slf4j
public class AuthRestAPI {

    final private AuthenticationManager authenticationManager;

    final private UserDetailsServiceImpl userService;

    final private AuthServiceImpl authService;

    final private TokenProvider jwtProvider;

    public AuthRestAPI(AuthenticationManager authenticationManager, UserDetailsServiceImpl userService, AuthServiceImpl authService, TokenProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.authService = authService;
        this.jwtProvider = jwtProvider;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInDto loginRequest) {

        boolean userExists = false;

        if(loginRequest.getUsername().indexOf('@')!= -1) {
            userExists = userService.existsByEmail(loginRequest.getUsername());
        } else {
            userExists = userService.existsByUsername(loginRequest.getUsername());
        }

        if(!userExists){
            return new ResponseEntity<String>("Пользователя с таким Логином / Email не существует",
                    HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        User user = ((UserPrinciple) authentication.getPrincipal()).toUser();


        String[] roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);

        String refreshToken = jwtProvider.generateRefreshToken(user.getUsername());



        authService.addToken(user,
                refreshToken, new Date(new Date().getTime() + jwtProvider.getRefreshTokenExpiration()));

        String accessToken = jwtProvider.generateAccessToken(user.getUsername(), roles);

        UserAuthDetailsResponse profile = UserAuthDetailsResponse.build(new TokenDto(
                accessToken, refreshToken, jwtProvider.getAccessTokenExpiration(), jwtProvider.getRefreshTokenExpiration()
        ),(UserPrinciple) authentication.getPrincipal());



        return ResponseEntity.ok(profile);
    }

    @GetMapping("/email-exist")
    public ResponseEntity<Boolean> isEmailExist(@RequestParam String email){
        Boolean existsByEmail = userService.existsByEmail(email);

        return new ResponseEntity<Boolean>(existsByEmail,
                HttpStatus.OK);

    }

    @GetMapping("/username-exist")
    public ResponseEntity<Boolean> isUsernameExist(@RequestParam String username){
        Boolean existsByUsername = userService.existsByUsername(username);

        return new ResponseEntity<Boolean>(existsByUsername,
                HttpStatus.OK);

    }


    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpDto signUpRequest) {
        if(userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("Ошибка. Такой e-mail уже зарегистрирован в системе",
                    HttpStatus.BAD_REQUEST);
        }

        try {
            userService.registrate(signUpRequest);
        } catch (PSQLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().body("Ползователь зарегистрирован успешно. На ваш e-mail отправлено письмо с подтверждением регистрации.");
    }

    @GetMapping("/refresh-tokens")
    public ResponseEntity<Object> refreshTokens(@Valid @RequestParam String refreshToken) {
        try {




            boolean shouldDeleteTokenFromBase = false;
            try{
                jwtProvider.getUserNameFromJwtToken(refreshToken);
            } catch (TokenExpiredException e) {
                shouldDeleteTokenFromBase = true;
            }


            Auth previousRefreshToken = authService.findPreviousRefreshToken(refreshToken);
            if(previousRefreshToken != null){
                authService.deleteToken(previousRefreshToken);
                log.error("Old refresh token was used by user", previousRefreshToken.getUser().getUsername());
                return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Кто-то воспользовался вашими старыми данными аутентификации");
            }


            Auth refreshTokenFromBase = authService.findRefreshToken(refreshToken);

            if( shouldDeleteTokenFromBase ) {
                log.error("Refresh token expired for user: ", refreshTokenFromBase.getUser().getUsername());
                authService.deleteToken(refreshTokenFromBase);
                return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Время жизни токена истекло");
            }

            User user = refreshTokenFromBase.getUser();
            String newRefreshToken = jwtProvider.generateRefreshToken(user.getUsername());

            refreshTokenFromBase.setPreviousToken(refreshToken);
            refreshTokenFromBase.setRefreshToken(newRefreshToken);
            refreshTokenFromBase.setExpirationDate(new Date(new Date().getTime()+ jwtProvider.getRefreshTokenExpiration()));

            authService.updateAuth(refreshTokenFromBase);

            String[] roles = user.getRoles().stream().map(role -> role.name()).toArray(String[]::new);
            String accessToken = jwtProvider.generateAccessToken(user.getUsername(), roles);

            TokenDto tokens = new TokenDto(
                    accessToken, newRefreshToken, jwtProvider.getAccessTokenExpiration(), jwtProvider.getRefreshTokenExpiration()
            );
            return ResponseEntity.ok(tokens);
        } catch (RefreshTokenNotExists e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body("Токена не существует в базе");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Невозможно обновить токен");
        }


    }


}