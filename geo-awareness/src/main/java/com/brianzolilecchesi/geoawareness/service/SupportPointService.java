package com.brianzolilecchesi.geoawareness.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brianzolilecchesi.geoawareness.dto.SupportPointDTO;
import com.brianzolilecchesi.geoawareness.exception.supportPoint.IllegalSupportPointException;
import com.brianzolilecchesi.geoawareness.exception.supportPoint.SupportPointNotFoundException;
import com.brianzolilecchesi.geoawareness.model.persistency.supportPoint.SupportPoint;
import com.brianzolilecchesi.geoawareness.model.repository.SupportPointRepository;
import com.brianzolilecchesi.geoawareness.validation.SupportPointValidatorSingleton;

@Service
public class SupportPointService {
    
    private final SupportPointRepository repository;

    @Autowired
    public SupportPointService(final SupportPointRepository repository) {
        this.repository = repository;
    }

    public List<SupportPointDTO> getAll() {
        List<SupportPointDTO> supportPointsDTO = new ArrayList<>();
        repository.findAll().forEach(supportPoint -> {
            supportPointsDTO.add(new SupportPointDTO(supportPoint));
        });
        return supportPointsDTO;
    }

    private SupportPoint getSupportPointById(final String id) throws SupportPointNotFoundException {
        return repository.findById(id).orElseThrow(() -> new SupportPointNotFoundException());
    }

    public SupportPointDTO getById(String id) throws SupportPointNotFoundException {
        return new SupportPointDTO(getSupportPointById(id));
    }

    public SupportPointDTO save(SupportPointDTO supportPointDTO) throws IllegalSupportPointException {
        SupportPointValidatorSingleton.getInstance().validate(supportPointDTO);
        SupportPoint supportPoint = new SupportPoint(supportPointDTO.getName(), supportPointDTO.getLongitude(), supportPointDTO.getLatitude());
        supportPoint = repository.save(supportPoint);
        return new SupportPointDTO(supportPoint);
    }

    public SupportPointDTO update(String id, SupportPointDTO supportPointDTO) throws SupportPointNotFoundException, IllegalSupportPointException {
        SupportPointValidatorSingleton.getInstance().validate(supportPointDTO);
        SupportPoint supportPoint = getSupportPointById(id);
        supportPoint.update(supportPointDTO);
        repository.save(supportPoint);
        return new SupportPointDTO(supportPoint);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public SupportPointDTO deleteById(String id) throws SupportPointNotFoundException {
        SupportPointDTO supportPointDTO = getById(id);
        repository.deleteById(id);
        return supportPointDTO;
    }





    

    
}
