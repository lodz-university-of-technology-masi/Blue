package com.masiblue.backend.service;

import com.masiblue.backend.exception.PositionAlreadyExistsException;
import com.masiblue.backend.exception.PositionNotFoundException;
import com.masiblue.backend.exception.PositionNotValidException;
import com.masiblue.backend.model.Position;
import com.masiblue.backend.repository.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {
    private PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public Position findByName(String name) throws PositionNotFoundException {
        return positionRepository.findByName(name).orElseThrow(PositionNotFoundException::new);
    }

    public Position findById(long id) throws PositionNotFoundException {
        return positionRepository.findById(id).orElseThrow(PositionNotFoundException::new);
    }

    public List<Position> findAll() {
        return positionRepository.findAll();
    }

    public boolean add(Position position) throws PositionAlreadyExistsException {
        if(positionRepository.findByName(position.getName()).isPresent()) {
            throw new PositionAlreadyExistsException();
        }

        Position savedPosition = positionRepository.save(position);
        return savedPosition != null;

    }

    public boolean add(String positionName) throws PositionAlreadyExistsException {
        return add(new Position(positionName));
    }

    public boolean update(long id, Position newPositionData) throws PositionNotFoundException, PositionAlreadyExistsException, PositionNotValidException {
        Position oldPosition = findById(id);
        if(oldPosition == null) {
            throw new PositionNotFoundException();
        }

        if(!validateUpdateData(newPositionData)) {
            throw new PositionNotValidException();
        }

        newPositionData.setId(id);
        positionRepository.save(newPositionData);

        return positionRepository.findByName(newPositionData.getName()).isPresent();
    }

    public boolean delete(long id) throws PositionNotFoundException {
        if(!positionRepository.existsById(id)) {
            throw new PositionNotFoundException();
        }

        positionRepository.deleteById(id);

        return !positionRepository.findById(id).isPresent();
    }

    private boolean validateUpdateData(Position newPositionData){
        return newPositionData.getName() != null
                && !newPositionData.getName().equals("");
    }

    public boolean exists(Position position) throws PositionNotFoundException {
        return positionRepository.findById(position.getId()).orElseThrow(PositionNotFoundException::new).equals(position);
    }
}
