package com.aeroflux.drone_identification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aeroflux.drone_identification.dto.OperationCategoryDTO;
import com.aeroflux.drone_identification.exception.operationcategory.OperationCategoryNotFoundException;
import com.aeroflux.drone_identification.service.OperationCategoryService;

@RestController
@RequestMapping(OperationCategoryController.OPERATION_CATEGORY_BASE_URI)
public class OperationCategoryController {
	
	public final static String OPERATION_CATEGORY_BASE_URI = DroneController.DRONE_BASE_URI + "/operation";
	
	private final  OperationCategoryService operationCategoryService;
	
	@Autowired
	public OperationCategoryController(final OperationCategoryService operationCategoryService) {
		this.operationCategoryService = operationCategoryService;
	}
	
	@GetMapping()
	public ResponseEntity<List<OperationCategoryDTO>> getOperationCategories() {
		return ResponseEntity.ok().body(operationCategoryService.getAll());
	}
	
	
	@GetMapping("/{name}")
	public ResponseEntity<OperationCategoryDTO> getOperationCategoryByName(@PathVariable String name) throws OperationCategoryNotFoundException {
		assert name != null;
		return ResponseEntity.ok().body(operationCategoryService.getByName(name));
	}
}