package com.java.centralizedNotificationBackend.config;

import com.google.gson.Gson;
import com.java.centralizedNotificationBackend.services.Impl.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        System.out.println("requestTokenHeader++"+requestTokenHeader);
        String username=null;
        String jwtToken=null;
        Map<String,Object> map = new HashMap<String,Object>();
        Gson getJson = new Gson();

        if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer "))
        {
            try{

                jwtToken=requestTokenHeader.substring(7);
                username=this.jwtUtil.extractUsername(jwtToken);

                System.out.println(username);

            }catch (ExpiredJwtException e){
                httpServletResponse.setStatus(401);
                map.put("status", HttpStatus.UNAUTHORIZED.value());
                map.put("message","jwt token has expired");
                map.put("error",true);
                String jsonString  = getJson.toJson(map);
                PrintWriter out  = httpServletResponse.getWriter();
                httpServletResponse.setContentType("application/json");
                httpServletRequest.setCharacterEncoding("UTF-8");
                out.print(jsonString);
                out.flush();
                return;
            } catch (Exception e){
                httpServletResponse.setStatus(400);
                map.put("status", HttpStatus.BAD_REQUEST.value());
                map.put("message",e.getMessage());
                map.put("error",true);
                String jsonString  = getJson.toJson(map);
                PrintWriter out  = httpServletResponse.getWriter();
                httpServletResponse.setContentType("application/json");
                httpServletRequest.setCharacterEncoding("UTF-8");
                out.print(jsonString);
                out.flush();
                return;
            }
        }else{
            System.out.println("Invalid token , not start with bearer string");
        }

        //validate the token
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(this.jwtUtil.validateToken(jwtToken,userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthentication=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthentication);
            }
        }else{
            System.out.println("Token is not valid");
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }
}

