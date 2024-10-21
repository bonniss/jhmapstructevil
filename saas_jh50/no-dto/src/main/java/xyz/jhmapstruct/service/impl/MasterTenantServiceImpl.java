package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.MasterTenantRepository;
import xyz.jhmapstruct.service.MasterTenantService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.MasterTenant}.
 */
@Service
@Transactional
public class MasterTenantServiceImpl implements MasterTenantService {

    private static final Logger LOG = LoggerFactory.getLogger(MasterTenantServiceImpl.class);

    private final MasterTenantRepository masterTenantRepository;

    public MasterTenantServiceImpl(MasterTenantRepository masterTenantRepository) {
        this.masterTenantRepository = masterTenantRepository;
    }

    @Override
    public MasterTenant save(MasterTenant masterTenant) {
        LOG.debug("Request to save MasterTenant : {}", masterTenant);
        return masterTenantRepository.save(masterTenant);
    }

    @Override
    public MasterTenant update(MasterTenant masterTenant) {
        LOG.debug("Request to update MasterTenant : {}", masterTenant);
        return masterTenantRepository.save(masterTenant);
    }

    @Override
    public Optional<MasterTenant> partialUpdate(MasterTenant masterTenant) {
        LOG.debug("Request to partially update MasterTenant : {}", masterTenant);

        return masterTenantRepository
            .findById(masterTenant.getId())
            .map(existingMasterTenant -> {
                if (masterTenant.getCode() != null) {
                    existingMasterTenant.setCode(masterTenant.getCode());
                }

                return existingMasterTenant;
            })
            .map(masterTenantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MasterTenant> findAll() {
        LOG.debug("Request to get all MasterTenants");
        return masterTenantRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MasterTenant> findOne(Long id) {
        LOG.debug("Request to get MasterTenant : {}", id);
        return masterTenantRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MasterTenant : {}", id);
        masterTenantRepository.deleteById(id);
    }
}
