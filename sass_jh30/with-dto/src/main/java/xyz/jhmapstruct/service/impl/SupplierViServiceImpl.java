package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierVi;
import xyz.jhmapstruct.repository.SupplierViRepository;
import xyz.jhmapstruct.service.SupplierViService;
import xyz.jhmapstruct.service.dto.SupplierViDTO;
import xyz.jhmapstruct.service.mapper.SupplierViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierVi}.
 */
@Service
@Transactional
public class SupplierViServiceImpl implements SupplierViService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViServiceImpl.class);

    private final SupplierViRepository supplierViRepository;

    private final SupplierViMapper supplierViMapper;

    public SupplierViServiceImpl(SupplierViRepository supplierViRepository, SupplierViMapper supplierViMapper) {
        this.supplierViRepository = supplierViRepository;
        this.supplierViMapper = supplierViMapper;
    }

    @Override
    public SupplierViDTO save(SupplierViDTO supplierViDTO) {
        LOG.debug("Request to save SupplierVi : {}", supplierViDTO);
        SupplierVi supplierVi = supplierViMapper.toEntity(supplierViDTO);
        supplierVi = supplierViRepository.save(supplierVi);
        return supplierViMapper.toDto(supplierVi);
    }

    @Override
    public SupplierViDTO update(SupplierViDTO supplierViDTO) {
        LOG.debug("Request to update SupplierVi : {}", supplierViDTO);
        SupplierVi supplierVi = supplierViMapper.toEntity(supplierViDTO);
        supplierVi = supplierViRepository.save(supplierVi);
        return supplierViMapper.toDto(supplierVi);
    }

    @Override
    public Optional<SupplierViDTO> partialUpdate(SupplierViDTO supplierViDTO) {
        LOG.debug("Request to partially update SupplierVi : {}", supplierViDTO);

        return supplierViRepository
            .findById(supplierViDTO.getId())
            .map(existingSupplierVi -> {
                supplierViMapper.partialUpdate(existingSupplierVi, supplierViDTO);

                return existingSupplierVi;
            })
            .map(supplierViRepository::save)
            .map(supplierViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierViDTO> findAll() {
        LOG.debug("Request to get all SupplierVis");
        return supplierViRepository.findAll().stream().map(supplierViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<SupplierViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierViRepository.findAllWithEagerRelationships(pageable).map(supplierViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierViDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierVi : {}", id);
        return supplierViRepository.findOneWithEagerRelationships(id).map(supplierViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierVi : {}", id);
        supplierViRepository.deleteById(id);
    }
}
