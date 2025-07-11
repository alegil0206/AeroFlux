package com.aeroflux.geoawareness.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aeroflux.geoawareness.dto.SupportPointDTO;
import com.aeroflux.geoawareness.exception.BadRequestException;
import com.aeroflux.geoawareness.exception.NotFoundException;
import com.aeroflux.geoawareness.service.SupportPointService;

@RestController
@RequestMapping(SupportPointController.SUPPORT_POINT_BASE_URL)
public class SupportPointController {

    public static final String SUPPORT_POINT_BASE_URL = "/support-point";

    private final SupportPointService supportPointService;

    @Autowired
    public SupportPointController(final SupportPointService supportPointService) {
        this.supportPointService = supportPointService;
    }

    @GetMapping
    public ResponseEntity<List<SupportPointDTO>> getAll() {
        return ResponseEntity.ok(supportPointService.getAll());
    }

    @PostMapping
    public ResponseEntity<SupportPointDTO> save(@RequestBody final SupportPointDTO supportPointDTO) throws BadRequestException, NotFoundException {
        SupportPointDTO newSupportPointDTO = supportPointService.save(supportPointDTO);
        URI location = URI.create(String.format(
                "%s/%s",
                SUPPORT_POINT_BASE_URL,
                newSupportPointDTO.getId()
                ));

        return ResponseEntity.created(location).body(newSupportPointDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        supportPointService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportPointDTO> getById(@PathVariable final String id) throws NotFoundException {
        assert id != null;
        return ResponseEntity.ok(supportPointService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupportPointDTO> update(@PathVariable final String id, @RequestBody final SupportPointDTO supportPointDTO) 
            throws NotFoundException, BadRequestException {
        assert id != null;
        supportPointDTO.setId(id);
        return ResponseEntity.ok(supportPointService.update(id, supportPointDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable final String id) throws NotFoundException {
        assert id != null;
        supportPointService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
