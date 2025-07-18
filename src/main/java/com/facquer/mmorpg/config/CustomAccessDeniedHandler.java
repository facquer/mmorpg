package com.facquer.mmorpg.config;

import com.facquer.mmorpg.utils.GenericResponse;
import com.facquer.mmorpg.utils.StateEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        GenericResponse<Object> responseBody = new GenericResponse<>();
        responseBody.setState(StateEnum.UNAUTHORIZED);
        responseBody.setObject(null);
        responseBody.saveMessage(List.of(
                "No tienes permiso para acceder a este recurso.",
                accessDeniedException.getMessage() // mensaje técnico como messageError
        ));

        response.setStatus(HttpServletResponse.SC_OK); // puedes usar SC_UNAUTHORIZED (401) si deseas
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(json);
    }
}