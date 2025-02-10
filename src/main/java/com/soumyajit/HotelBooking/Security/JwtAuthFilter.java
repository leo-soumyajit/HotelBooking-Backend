package com.soumyajit.HotelBooking.Security;

import com.soumyajit.HotelBooking.entities.User;
import com.soumyajit.HotelBooking.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import com.soumyajit.HotelBooking.service.UserService;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final com.soumyajit.HotelBooking.service.UserService userService;


    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String requestTokenHeader = request.getHeader("Authorization");
            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = requestTokenHeader.split("Bearer")[1];
            Long userId = jwtService.getUserIdFromToken(token);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userEntities = userService.getUserById(userId);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userEntities, null, userEntities.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            filterChain.doFilter(request, response);
        }catch (JwtException ex){
            handlerExceptionResolver.resolveException(request,response,null,ex);
        }
    }
}
