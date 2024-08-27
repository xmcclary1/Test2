//package com.example.talent_api.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//// Execute Before Executing Spring Security Filters
//// Validate the JWT Token and Provides user details to Spring Security for Authentication
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private JwtTokenProvider jwtTokenProvider;
//
//    private UserDetailsService userDetailsService;
//
//    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.userDetailsService = userDetailsService;
//    }
//
////    @Override
////    protected void doFilterInternal(HttpServletRequest request,
////                                    HttpServletResponse response,
////                                    FilterChain filterChain) throws ServletException, IOException {
////
////        // Get JWT token from HTTP request
////        String token = getTokenFromRequest(request);
////
////        // Validate Token
////        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){
////            // get username from token
////            String username = jwtTokenProvider.getUsername(token);
////
////            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
////
////            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
////                    userDetails,
////                    null,
////                    userDetails.getAuthorities()
////            );
////
////            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////
////            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
////        }
////
////        filterChain.doFilter(request, response);
////    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String token = getTokenFromRequest(request);
//
//        if (StringUtils.hasText(token)) {
//            System.out.println("Bearer Token: " + token); // Log the token
//        }
//
//        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
//            String username = jwtTokenProvider.getUsername(token);
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                    userDetails,
//                    null,
//                    userDetails.getAuthorities()
//            );
//
//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        } else {
//            System.out.println("Token validation failed");
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//
//    private String getTokenFromRequest(HttpServletRequest request){
//        String bearerToken = request.getHeader("Authorization");
//
//        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
//            return bearerToken.substring(7, bearerToken.length());
//        }
//
//        return null;
//    }
//}

package com.example.talent_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);
        logger.info("JWT Token received: " + token);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);
            logger.info("Token validated successfully, username: " + username);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            logger.warning("Token validation failed.");
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.info("Authorization header: " + bearerToken);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
