package ru.filippov.neat.config.jwt;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.filippov.neat.service.user.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtAuthEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
    								HttpServletResponse response,
    								FilterChain filterChain)
    										throws ServletException, IOException {
        try {

            String actionToken = tokenProvider.getToken(request);
            if (actionToken !=null) {
                String username = tokenProvider.getUserNameFromJwtToken(actionToken);
                List<GrantedAuthority> authorities = tokenProvider.getAuthoritiesFromJwtToken(actionToken);

                UsernamePasswordAuthenticationToken authentication
                		= new UsernamePasswordAuthenticationToken(userDetailsService.loadUserByUsername(username), null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (IllegalArgumentException e) {
            logger.error("an error occured during getting username from token", e);
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (TokenExpiredException e) {
            response.setStatus(JwtStatus.EXPIRED_TOKEN.value);
            logger.warn("the token is expired and not valid anymore", e);
        } catch(SignatureVerificationException e){
            response.setStatus(JwtStatus.CHANGED_TOKEN.value);
            logger.error("Someone have changed the token!");
        } catch (Exception e) {
            log.error("Can NOT set user authentication -> Message: {}", e);
        }

        filterChain.doFilter(request, response);


    }


}
