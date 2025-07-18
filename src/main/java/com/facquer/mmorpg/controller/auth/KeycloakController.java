package com.facquer.mmorpg.controller.auth;

import com.facquer.mmorpg.dto.UserDTO;
import com.facquer.mmorpg.service.auth.IKeycloakService;
import com.facquer.mmorpg.utils.GenericResponse;
import com.facquer.mmorpg.utils.UtilsApi;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/keycloak")
@PreAuthorize("hasRole('Admin')")
public class KeycloakController {

    @Autowired
    private IKeycloakService keycloakService;

    @GetMapping("/searchUserByUsername/{username}")
    public ResponseEntity<GenericResponse> searchUserByUsername(@PathVariable String username) {
        GenericResponse<List<UserRepresentation>> searchUserByUsernameReturn;
        try {
            searchUserByUsernameReturn = UtilsApi.buildResponse(keycloakService.searchUserByUsername(username), null);
        } catch (Exception e) {
            searchUserByUsernameReturn = UtilsApi.buildResponse(null, e);
        }
        return new ResponseEntity<>(searchUserByUsernameReturn, HttpStatus.OK);
    }

    @GetMapping("/findAllUser")
    public ResponseEntity<GenericResponse> findAllUser() {
        GenericResponse<List<UserRepresentation>> findAllUserReturn;
        try {
            findAllUserReturn = UtilsApi.buildResponse(keycloakService.findAllUsers(), null);
        } catch (Exception e) {
            findAllUserReturn = UtilsApi.buildResponse(null, e);
        }
        return new ResponseEntity<>(findAllUserReturn, HttpStatus.OK);
    }

    @PostMapping("/createUser")
    public ResponseEntity<GenericResponse> createUser(@RequestBody UserDTO userDTO) {
        GenericResponse<String> createUserReturn;
        try {
            createUserReturn = UtilsApi.buildResponse(keycloakService.createUser(userDTO), null);
        } catch (Exception e) {
            createUserReturn = UtilsApi.buildResponse(null, e);
        }
        return new ResponseEntity<>(createUserReturn, HttpStatus.OK);
    }

}
