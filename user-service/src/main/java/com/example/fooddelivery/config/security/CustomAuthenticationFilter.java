// package com.example.fooddelivery.config.security;

// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import java.io.IOException;

// @Component
// public class CustomAuthenticationFilter extends OncePerRequestFilter {

//     private final RequestLimitService requestLimitService;

//     public CustomAuthenticationFilter(RequestLimitService requestLimitService) {
//         this.requestLimitService = requestLimitService;
//     }

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//             throws ServletException, IOException {
//         String key = request.getRemoteAddr(); // Use the client's IP address as the identifier

//         if ("/api/v1/auth/authenticate".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
//             if (requestLimitService.isBlocked(key)) {
//                 response.sendError(HttpServletResponse.SC_FORBIDDEN, "You have made too many failed attempts. Please try again later.");
//                 return;
//             }
//         }

//         filterChain.doFilter(request, response);
//     }

//     // Call requestLimitService.loginFailed(key) on authentication failure
//     // Call requestLimitService.loginSucceeded(key) on successful authentication
// }
