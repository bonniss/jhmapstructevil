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
import xyz.jhmapstruct.domain.SupplierMiMi;
import xyz.jhmapstruct.repository.SupplierMiMiRepository;
import xyz.jhmapstruct.service.SupplierMiMiService;
import xyz.jhmapstruct.service.dto.SupplierMiMiDTO;
import xyz.jhmapstruct.service.mapper.SupplierMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierMiMi}.
 */
@Service
@Transactional
public class SupplierMiMiServiceImpl implements SupplierMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierMiMiServiceImpl.class);

    private final SupplierMiMiRepository supplierMiMiRepository;

    private final SupplierMiMiMapper supplierMiMiMapper;

    public SupplierMiMiServiceImpl(SupplierMiMiRepository supplierMiMiRepository, SupplierMiMiMapper supplierMiMiMapper) {
        this.supplierMiMiRepository = supplierMiMiRepository;
        this.supplierMiMiMapper = supplierMiMiMapper;
    }

    @Override
    public SupplierMiMiDTO save(SupplierMiMiDTO supplierMiMiDTO) {
        LOG.debug("Request to save SupplierMiMi : {}", supplierMiMiDTO);
        SupplierMiMi supplierMiMi = supplierMiMiMapper.toEntity(supplierMiMiDTO);
        supplierMiMi = supplierMiMiRepository.save(supplierMiMi);
        return supplierMiMiMapper.toDto(supplierMiMi);
    }

    @Override
    public SupplierMiMiDTO update(SupplierMiMiDTO supplierMiMiDTO) {
        LOG.debug("Request to update SupplierMiMi : {}", supplierMiMiDTO);
        SupplierMiMi supplierMiMi = supplierMiMiMapper.toEntity(supplierMiMiDTO);
        supplierMiMi = supplierMiMiRepository.save(supplierMiMi);
        return supplierMiMiMapper.toDto(supplierMiMi);
    }

    @Override
    public Optional<SupplierMiMiDTO> partialUpdate(SupplierMiMiDTO supplierMiMiDTO) {
        LOG.debug("Request to partially update SupplierMiMi : {}", supplierMiMiDTO);

        return supplierMiMiRepository
            .findById(supplierMiMiDTO.getId())
            .map(existingSupplierMiMi -> {
                supplierMiMiMapper.partialUpdate(existingSupplierMiMi, supplierMiMiDTO);

                return existingSupplierMiMi;
            })
            .map(supplierMiMiRepository::save)
            .map(supplierMiMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierMiMiDTO> findAll() {
        LOG.debug("Request to get all SupplierMiMis");
        return supplierMiMiRepository.findAll().stream().map(supplierMiMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<SupplierMiMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierMiMiRepository.findAllWithEagerRelationships(pageable).map(supplierMiMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierMiMi : {}", id);
        return supplierMiMiRepository.findOneWithEagerRelationships(id).map(supplierMiMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierMiMi : {}", id);
        supplierMiMiRepository.deleteById(id);
    }
}
