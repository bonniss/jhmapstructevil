package ai.realworld.service;

import ai.realworld.domain.Rihanna;
import ai.realworld.repository.RihannaRepository;
import ai.realworld.service.dto.RihannaDTO;
import ai.realworld.service.mapper.RihannaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.Rihanna}.
 */
@Service
@Transactional
public class RihannaService {

    private static final Logger LOG = LoggerFactory.getLogger(RihannaService.class);

    private final RihannaRepository rihannaRepository;

    private final RihannaMapper rihannaMapper;

    public RihannaService(RihannaRepository rihannaRepository, RihannaMapper rihannaMapper) {
        this.rihannaRepository = rihannaRepository;
        this.rihannaMapper = rihannaMapper;
    }

    /**
     * Save a rihanna.
     *
     * @param rihannaDTO the entity to save.
     * @return the persisted entity.
     */
    public RihannaDTO save(RihannaDTO rihannaDTO) {
        LOG.debug("Request to save Rihanna : {}", rihannaDTO);
        Rihanna rihanna = rihannaMapper.toEntity(rihannaDTO);
        rihanna = rihannaRepository.save(rihanna);
        return rihannaMapper.toDto(rihanna);
    }

    /**
     * Update a rihanna.
     *
     * @param rihannaDTO the entity to save.
     * @return the persisted entity.
     */
    public RihannaDTO update(RihannaDTO rihannaDTO) {
        LOG.debug("Request to update Rihanna : {}", rihannaDTO);
        Rihanna rihanna = rihannaMapper.toEntity(rihannaDTO);
        rihanna = rihannaRepository.save(rihanna);
        return rihannaMapper.toDto(rihanna);
    }

    /**
     * Partially update a rihanna.
     *
     * @param rihannaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RihannaDTO> partialUpdate(RihannaDTO rihannaDTO) {
        LOG.debug("Request to partially update Rihanna : {}", rihannaDTO);

        return rihannaRepository
            .findById(rihannaDTO.getId())
            .map(existingRihanna -> {
                rihannaMapper.partialUpdate(existingRihanna, rihannaDTO);

                return existingRihanna;
            })
            .map(rihannaRepository::save)
            .map(rihannaMapper::toDto);
    }

    /**
     * Get one rihanna by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RihannaDTO> findOne(Long id) {
        LOG.debug("Request to get Rihanna : {}", id);
        return rihannaRepository.findById(id).map(rihannaMapper::toDto);
    }

    /**
     * Delete the rihanna by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Rihanna : {}", id);
        rihannaRepository.deleteById(id);
    }
}
