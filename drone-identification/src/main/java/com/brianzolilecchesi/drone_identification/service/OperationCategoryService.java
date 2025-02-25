package com.brianzolilecchesi.drone_identification.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brianzolilecchesi.drone_identification.dto.OperationCategoryDTO;
import com.brianzolilecchesi.drone_identification.exception.operationcategory.OperationCategoryNotFoundException;
import com.brianzolilecchesi.drone_identification.model.persistency.operationcategory.OperationCategory;
import com.brianzolilecchesi.drone_identification.model.persistency.operationcategory.OperationCategoryFacadeSingleton;
import com.brianzolilecchesi.drone_identification.model.repository.OperationCategoryRepository;

@Service
public class OperationCategoryService {
	
	private final OperationCategoryRepository operationCategoryRepository;
	
	@Autowired
	public OperationCategoryService(
			final OperationCategoryRepository operationCategoryRepository
			) {
		this.operationCategoryRepository = operationCategoryRepository;
	}
		
	OperationCategory getOperationCategoryByName(final String name) throws OperationCategoryNotFoundException {
		return operationCategoryRepository.findByName(name.toUpperCase()).orElseThrow(() -> new OperationCategoryNotFoundException());
	}
	
	public List<OperationCategoryDTO> getAll() {
		List<OperationCategoryDTO> operationCategories = new ArrayList<>();
		
		operationCategoryRepository.findAll().forEach(operationCategory -> {
			operationCategories.add(OperationCategoryFacadeSingleton
                .getInstance()
                .getOperationCategoryFactory()
                .createOperationCategoryDTO(operationCategory));
            });
		return operationCategories;
	}
	
	public OperationCategoryDTO getByName(final String name) throws OperationCategoryNotFoundException {
		return OperationCategoryFacadeSingleton
				.getInstance()
				.getOperationCategoryFactory()
				.createOperationCategoryDTO(getOperationCategoryByName(name));
	}
}