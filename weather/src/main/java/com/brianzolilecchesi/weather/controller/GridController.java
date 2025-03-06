package com.brianzolilecchesi.weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brianzolilecchesi.weather.service.GridService;
import com.brianzolilecchesi.weather.dto.CoordinatesDTO;

@RestController
@RequestMapping(GridController.GRID_BASE_URL)
public class GridController {
	
	public static final String GRID_BASE_URL = "/grid";

    private final GridService gridService;

    public GridController(GridService gridService) {
        this.gridService = gridService;
    }

    @GetMapping()
    public CoordinatesDTO getCenterCoordinates() {
        return gridService.getCenterCoordinatesDTO();
    }

    @PutMapping()
    public CoordinatesDTO updateCenterCoordinates(@RequestBody CoordinatesDTO coordinates) {
        return gridService.setCenterCoordinates(coordinates);
    }
}
