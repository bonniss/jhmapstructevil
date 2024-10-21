package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.MasterTenantRepository;
import xyz.jhmapstruct.service.MasterTenantService;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.mapper.MasterTenantMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.MasterTenant}.
 */
@Service
@Transactional
public class MasterTenantServiceImpl implements MasterTenantService {

    private static final Logger LOG = LoggerFactory.getLogger(MasterTenantServiceImpl.class);

    private final MasterTenantRepository masterTenantRepository;

    private final MasterTenantMapper masterTenantMapper;

    public MasterTenantServiceImpl(MasterTenantRepository masterTenantRepository, MasterTenantMapper masterTenantMapper) {
        this.masterTenantRepository = masterTenantRepository;
        this.masterTenantMapper = masterTenantMapper;
    }

    @Override
    public MasterTenantDTO save(MasterTenantDTO masterTenantDTO) {
        LOG.debug("Request to save MasterTenant : {}", masterTenantDTO);
        MasterTenant masterTenant = masterTenantMapper.toEntity(masterTenantDTO);
        masterTenant = masterTenantRepository.save(masterTenant);
        return masterTenantMapper.toDto(masterTenant);
    }

    @Override
    public MasterTenantDTO update(MasterTenantDTO masterTenantDTO) {
        LOG.debug("Request to update MasterTenant : {}", masterTenantDTO);
        MasterTenant masterTenant = masterTenantMapper.toEntity(masterTenantDTO);
        masterTenant = masterTenantRepository.save(masterTenant);
        return masterTenantMapper.toDto(masterTenant);
    }

    @Override
    public Optional<MasterTenantDTO> partialUpdate(MasterTenantDTO masterTenantDTO) {
        LOG.debug("Request to partially update MasterTenant : {}", masterTenantDTO);

        return masterTenantRepository
            .findById(masterTenantDTO.getId())
            .map(existingMasterTenant -> {
                masterTenantMapper.partialUpdate(existingMasterTenant, masterTenantDTO);

                return existingMasterTenant;
            })
            .map(masterTenantRepository::save)
            .map(masterTenantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MasterTenantDTO> findAll() {
        LOG.debug("Request to get all MasterTenants");
        return masterTenantRepository.findAll().stream().map(masterTenantMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MasterTenantDTO> findOne(Long id) {
        LOG.debug("Request to get MasterTenant : {}", id);
        return masterTenantRepository.findById(id).map(masterTenantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MasterTenant : {}", id);
        masterTenantRepository.deleteById(id);
    }
}
