package com.aeroflux.geoauthorization.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aeroflux.geoauthorization.dto.AuthorizationDTO;
import com.aeroflux.geoauthorization.dto.AuthorizationRequestDTO;
import com.aeroflux.geoauthorization.exception.BadRequestException;
import com.aeroflux.geoauthorization.exception.ForbiddenException;
import com.aeroflux.geoauthorization.exception.NotFoundException;
import com.aeroflux.geoauthorization.service.AuthorizationService;

@RestController
@RequestMapping(AuthorizationController.AUTHORIZATION_BASE_URI)
public class AuthorizationController {
	
	public static final String AUTHORIZATION_BASE_URI = "/authorization";
	
	private final AuthorizationService authorizationService;
	
	@Autowired
	public AuthorizationController(
			final AuthorizationService authorizationService
			) {
		this.authorizationService = authorizationService;
	}
	
	@GetMapping
	public ResponseEntity<List<AuthorizationDTO>> getAllAuthorizations() {
		return ResponseEntity.ok().body(authorizationService.getAll());
	}
	
	@PostMapping
	public ResponseEntity<AuthorizationDTO> requestAuthorization(
			@RequestBody final AuthorizationRequestDTO authorizationRequestDTO
			) throws BadRequestException, NotFoundException, ForbiddenException {
		
		AuthorizationDTO authorizationDTO = authorizationService.requestAuthorization(authorizationRequestDTO);
		URI location = URI.create(String.format("%s/%s",
				AUTHORIZATION_BASE_URI,
				authorizationDTO.getId()
                ));
		
		return ResponseEntity.created(location).body(authorizationDTO);
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteAll() {
		authorizationService.deleteAll();
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AuthorizationDTO> getAuthorizationById(@PathVariable("id") final Long id) throws NotFoundException {
		return ResponseEntity.ok().body(authorizationService.getById(id));
	}	
	
	@GetMapping("/drone/{droneId}")
	public ResponseEntity<List<AuthorizationDTO>> getAuthorizationByDroneId(
			@PathVariable("droneId") final String droneId
			) throws NotFoundException {
		return ResponseEntity.ok().body(authorizationService.getByDroneId(droneId));
	}
	
	@GetMapping("/geozone/{geozoneId}")
	public ResponseEntity<List<AuthorizationDTO>> getAuthorizationByGeozoneId(
			@PathVariable("geozoneId") final String geozoneId
			) throws NotFoundException {
		return ResponseEntity.ok().body(authorizationService.getByGeozoneId(geozoneId));
	}
	
	@PostMapping("/revoked/{id}")
	public ResponseEntity<AuthorizationDTO> revokeAuthorization(@PathVariable("id") final Long id) throws NotFoundException, ForbiddenException {
		AuthorizationDTO authorizationDTO = authorizationService.revokeAuthorization(id);
		URI location = URI.create(String.format("%s/%s",
				AUTHORIZATION_BASE_URI,
				authorizationDTO.getId()
                ));
		
		return ResponseEntity.created(location).body(authorizationDTO);
	}
}