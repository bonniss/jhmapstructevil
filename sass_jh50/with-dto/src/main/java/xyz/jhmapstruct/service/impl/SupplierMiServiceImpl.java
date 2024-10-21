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
import xyz.jhmapstruct.domain.SupplierMi;
import xyz.jhmapstruct.repository.SupplierMiRepository;
import xyz.jhmapstruct.service.SupplierMiService;
import xyz.jhmapstruct.service.dto.SupplierMiDTO;
import xyz.jhmapstruct.service.mapper.SupplierMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierMi}.
 */
@Service
@Transactional
public class SupplierMiServiceImpl implements SupplierMiService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierMiServiceImpl.class);

    private final SupplierMiRepository supplierMiRepository;

    private final SupplierMiMapper supplierMiMapper;

    public SupplierMiServiceImpl(SupplierMiRepository supplierMiRepository, SupplierMiMapper supplierMiMapper) {
        this.supplierMiRepository = supplierMiRepository;
        this.supplierMiMapper = supplierMiMapper;
    }

    @Override
    public SupplierMiDTO save(SupplierMiDTO supplierMiDTO) {
        LOG.debug("Request to save SupplierMi : {}", supplierMiDTO);
        SupplierMi supplierMi = supplierMiMapper.toEntity(supplierMiDTO);
        supplierMi = supplierMiRepository.save(supplierMi);
        return supplierMiMapper.toDto(supplierMi);
    }

    @Override
    public SupplierMiDTO update(SupplierMiDTO supplierMiDTO) {
        LOG.debug("Request to update SupplierMi : {}", supplierMiDTO);
        SupplierMi supplierMi = supplierMiMapper.toEntity(supplierMiDTO);
        supplierMi = supplierMiRepository.save(supplierMi);
        return supplierMiMapper.toDto(supplierMi);
    }

    @Override
    public Optional<SupplierMiDTO> partialUpdate(SupplierMiDTO supplierMiDTO) {
        LOG.debug("Request to partially update SupplierMi : {}", supplierMiDTO);

        return supplierMiRepository
            .findById(supplierMiDTO.getId())
            .map(existingSupplierMi -> {
                supplierMiMapper.partialUpdate(existingSupplierMi, supplierMiDTO);

                return existingSupplierMi;
            })
            .map(supplierMiRepository::save)
            .map(supplierMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierMiDTO> findAll() {
        LOG.debug("Request to get all SupplierMis");
        return supplierMiRepository.findAll().stream().map(supplierMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<SupplierMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierMiRepository.findAllWithEagerRelationships(pageable).map(supplierMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierMiDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierMi : {}", id);
        return supplierMiRepository.findOneWithEagerRelationships(id).map(supplierMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierMi : {}", id);
        supplierMiRepository.deleteById(id);
    }
}
