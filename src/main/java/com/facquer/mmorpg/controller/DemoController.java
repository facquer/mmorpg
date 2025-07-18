package com.facquer.mmorpg.controller;

import com.facquer.mmorpg.utils.GenericResponse;
import com.facquer.mmorpg.utils.UtilsApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/helloAdmin")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<GenericResponse> helloAdmin() {
        GenericResponse<String> adminReturn;
        try {
            //throw new IllegalArgumentException("Parámetro inválido proporcionado");
            //throw new RuntimeException("Fallo simulado para probar el manejo de errores");
            adminReturn = UtilsApi.buildResponse("Hello Admin!!", null);
        } catch (Exception e) {
            adminReturn = UtilsApi.buildResponse(null, e);
        }
        return new ResponseEntity<>(adminReturn, HttpStatus.OK);
    }

    @GetMapping("/helloPlayer")
    @PreAuthorize("hasRole('Player')")
    public ResponseEntity<GenericResponse> helloPlayer() {
        GenericResponse<String> playerReturn;
        try {
            playerReturn = UtilsApi.buildResponse("Hello Player!!", null);
        } catch (Exception e) {
            playerReturn = UtilsApi.buildResponse(null, e);
        }
        return new ResponseEntity<>(playerReturn, HttpStatus.OK);
    }
}
