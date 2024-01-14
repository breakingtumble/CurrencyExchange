package com.breakingtumble.exchanger.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonResponseSender {
    public static void sendJsonResponse(HttpServletResponse resp, Object object) throws IOException {
        String json = new ObjectMapper().writeValueAsString(object);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
        resp.getWriter().flush();
    }
}
