package com.kaluzny.assistant.app.service.bean;

import com.kaluzny.assistant.api.model.filter.TruckFilter;
import com.kaluzny.assistant.app.domain.Truck;
import com.kaluzny.assistant.app.repository.TruckRepository;
import com.kaluzny.assistant.app.service.TruckService;
import com.kaluzny.assistant.app.service.filter.TruckSpecificationBuilder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TruckServiceImpl
 *
 * @author Oleg Kaluzny
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TruckServiceImpl implements TruckService {

    private final TruckRepository repository;
    private final TruckSpecificationBuilder truckSpecificationBuilder;

    @Override
    public Truck create(Truck requestForSave) {
        log.debug("create() - start: requestForSave = {}", requestForSave);
        Truck entity = repository.save(requestForSave);
        entity.setCreateDate(LocalDateTime.now());
        log.info("create() - end: entity = {}", entity);
        return entity;
    }

    @Override
    public Page<Truck> getPage(Pageable pageable, TruckFilter filter) {
        log.debug("getPage() - start: pageable = {}, filter = {}", pageable, filter);
        Specification<Truck> specification = truckSpecificationBuilder.buildFilterSpec(filter);
        Page<Truck> page = repository.findAll(specification, pageable);
        log.debug("getPage() - end: page = {}", page);
        return page;
    }

    @Override
    public Truck findById(Long id) {
        log.debug("findById() - start: id = {}", id);
        Truck entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("truck not found with id " + id));
        log.info("findById() - end: entity = {}", entity.getId());
        return entity;
    }

    @Override
    public Truck update(Truck truck) {
        log.debug("update() - start: truck = {}", truck);
        Truck updatedEntity = repository.findById(truck.getId())
                .map(entity -> {
                    entity.setCreateDate(truck.getCreateDate());
                    entity.setManufacturer(truck.getManufacturer());
                    entity.setModel(truck.getModel());
                    return repository.save(entity);
                })
                .orElseThrow(() -> new EntityNotFoundException("truck not found with id " + truck.getId()));
        log.info("update() - end: entity = {}", updatedEntity.getId());
        return updatedEntity;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("deleteById() - start: id = {}", id);
        repository.deleteById(id);
        log.debug("deleteById() - end");
    }
}
