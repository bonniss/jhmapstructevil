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
import xyz.jhmapstruct.domain.SupplierViVi;
import xyz.jhmapstruct.repository.SupplierViViRepository;
import xyz.jhmapstruct.service.SupplierViViService;
import xyz.jhmapstruct.service.dto.SupplierViViDTO;
import xyz.jhmapstruct.service.mapper.SupplierViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierViVi}.
 */
@Service
@Transactional
public class SupplierViViServiceImpl implements SupplierViViService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViViServiceImpl.class);

    private final SupplierViViRepository supplierViViRepository;

    private final SupplierViViMapper supplierViViMapper;

    public SupplierViViServiceImpl(SupplierViViRepository supplierViViRepository, SupplierViViMapper supplierViViMapper) {
        this.supplierViViRepository = supplierViViRepository;
        this.supplierViViMapper = supplierViViMapper;
    }

    @Override
    public SupplierViViDTO save(SupplierViViDTO supplierViViDTO) {
        LOG.debug("Request to save SupplierViVi : {}", supplierViViDTO);
        SupplierViVi supplierViVi = supplierViViMapper.toEntity(supplierViViDTO);
        supplierViVi = supplierViViRepository.save(supplierViVi);
        return supplierViViMapper.toDto(supplierViVi);
    }

    @Override
    public SupplierViViDTO update(SupplierViViDTO supplierViViDTO) {
        LOG.debug("Request to update SupplierViVi : {}", supplierViViDTO);
        SupplierViVi supplierViVi = supplierViViMapper.toEntity(supplierViViDTO);
        supplierViVi = supplierViViRepository.save(supplierViVi);
        return supplierViViMapper.toDto(supplierViVi);
    }

    @Override
    public Optional<SupplierViViDTO> partialUpdate(SupplierViViDTO supplierViViDTO) {
        LOG.debug("Request to partially update SupplierViVi : {}", supplierViViDTO);

        return supplierViViRepository
            .findById(supplierViViDTO.getId())
            .map(existingSupplierViVi -> {
                supplierViViMapper.partialUpdate(existingSupplierViVi, supplierViViDTO);

                return existingSupplierViVi;
            })
            .map(supplierViViRepository::save)
            .map(supplierViViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierViViDTO> findAll() {
        LOG.debug("Request to get all SupplierViVis");
        return supplierViViRepository.findAll().stream().map(supplierViViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<SupplierViViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierViViRepository.findAllWithEagerRelationships(pageable).map(supplierViViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierViViDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierViVi : {}", id);
        return supplierViViRepository.findOneWithEagerRelationships(id).map(supplierViViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierViVi : {}", id);
        supplierViViRepository.deleteById(id);
    }
}
