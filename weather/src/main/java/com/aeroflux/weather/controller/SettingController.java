package com.aeroflux.weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aeroflux.weather.service.GridService;
import com.aeroflux.weather.dto.CoordinatesDTO;

@RestController
@RequestMapping(SettingController.SETTING_BASE_URL)
public class SettingController {
	
	public static final String SETTING_BASE_URL = "/setting";

    private final GridService gridService;

    public SettingController(GridService gridService) {
        this.gridService = gridService;
    }

    @GetMapping("/coordinates")
    public CoordinatesDTO getCenterCoordinates() {
        return gridService.getCenterCoordinatesDTO();
    }

    @PutMapping("/coordinates")
    public CoordinatesDTO updateCenterCoordinates(@RequestBody CoordinatesDTO coordinates) {
        return gridService.setCenterCoordinates(coordinates);
    }
}
