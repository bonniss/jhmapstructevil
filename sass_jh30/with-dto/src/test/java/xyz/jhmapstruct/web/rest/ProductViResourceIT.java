package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ProductViAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;
import static xyz.jhmapstruct.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.IntegrationTest;
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.repository.ProductViRepository;
import xyz.jhmapstruct.service.ProductViService;
import xyz.jhmapstruct.service.dto.ProductViDTO;
import xyz.jhmapstruct.service.mapper.ProductViMapper;

/**
 * Integration tests for the {@link ProductViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductViRepository productViRepository;

    @Mock
    private ProductViRepository productViRepositoryMock;

    @Autowired
    private ProductViMapper productViMapper;

    @Mock
    private ProductViService productViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductViMockMvc;

    private ProductVi productVi;

    private ProductVi insertedProductVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVi createEntity() {
        return new ProductVi().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVi createUpdatedEntity() {
        return new ProductVi().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        productVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductVi != null) {
            productViRepository.delete(insertedProductVi);
            insertedProductVi = null;
        }
    }

    @Test
    @Transactional
    void createProductVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductVi
        ProductViDTO productViDTO = productViMapper.toDto(productVi);
        var returnedProductViDTO = om.readValue(
            restProductViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductViDTO.class
        );

        // Validate the ProductVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductVi = productViMapper.toEntity(returnedProductViDTO);
        assertProductViUpdatableFieldsEquals(returnedProductVi, getPersistedProductVi(returnedProductVi));

        insertedProductVi = returnedProductVi;
    }

    @Test
    @Transactional
    void createProductViWithExistingId() throws Exception {
        // Create the ProductVi with an existing ID
        productVi.setId(1L);
        ProductViDTO productViDTO = productViMapper.toDto(productVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productVi.setName(null);

        // Create the ProductVi, which fails.
        ProductViDTO productViDTO = productViMapper.toDto(productVi);

        restProductViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productVi.setPrice(null);

        // Create the ProductVi, which fails.
        ProductViDTO productViDTO = productViMapper.toDto(productVi);

        restProductViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productVi.setStock(null);

        // Create the ProductVi, which fails.
        ProductViDTO productViDTO = productViMapper.toDto(productVi);

        restProductViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductVis() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get all the productViList
        restProductViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(productViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductVi() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        // Get the productVi
        restProductViMockMvc
            .perform(get(ENTITY_API_URL_ID, productVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductVi() throws Exception {
        // Get the productVi
        restProductViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductVi() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productVi
        ProductVi updatedProductVi = productViRepository.findById(productVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductVi are not directly saved in db
        em.detach(updatedProductVi);
        updatedProductVi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
        ProductViDTO productViDTO = productViMapper.toDto(updatedProductVi);

        restProductViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productViDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductViToMatchAllProperties(updatedProductVi);
    }

    @Test
    @Transactional
    void putNonExistingProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productVi.setId(longCount.incrementAndGet());

        // Create the ProductVi
        ProductViDTO productViDTO = productViMapper.toDto(productVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productVi.setId(longCount.incrementAndGet());

        // Create the ProductVi
        ProductViDTO productViDTO = productViMapper.toDto(productVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productVi.setId(longCount.incrementAndGet());

        // Create the ProductVi
        ProductViDTO productViDTO = productViMapper.toDto(productVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductViWithPatch() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productVi using partial update
        ProductVi partialUpdatedProductVi = new ProductVi();
        partialUpdatedProductVi.setId(productVi.getId());

        partialUpdatedProductVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductVi))
            )
            .andExpect(status().isOk());

        // Validate the ProductVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductVi, productVi),
            getPersistedProductVi(productVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductViWithPatch() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productVi using partial update
        ProductVi partialUpdatedProductVi = new ProductVi();
        partialUpdatedProductVi.setId(productVi.getId());

        partialUpdatedProductVi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductVi))
            )
            .andExpect(status().isOk());

        // Validate the ProductVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductViUpdatableFieldsEquals(partialUpdatedProductVi, getPersistedProductVi(partialUpdatedProductVi));
    }

    @Test
    @Transactional
    void patchNonExistingProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productVi.setId(longCount.incrementAndGet());

        // Create the ProductVi
        ProductViDTO productViDTO = productViMapper.toDto(productVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productVi.setId(longCount.incrementAndGet());

        // Create the ProductVi
        ProductViDTO productViDTO = productViMapper.toDto(productVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productVi.setId(longCount.incrementAndGet());

        // Create the ProductVi
        ProductViDTO productViDTO = productViMapper.toDto(productVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductVi() throws Exception {
        // Initialize the database
        insertedProductVi = productViRepository.saveAndFlush(productVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productVi
        restProductViMockMvc
            .perform(delete(ENTITY_API_URL_ID, productVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productViRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ProductVi getPersistedProductVi(ProductVi productVi) {
        return productViRepository.findById(productVi.getId()).orElseThrow();
    }

    protected void assertPersistedProductViToMatchAllProperties(ProductVi expectedProductVi) {
        assertProductViAllPropertiesEquals(expectedProductVi, getPersistedProductVi(expectedProductVi));
    }

    protected void assertPersistedProductViToMatchUpdatableProperties(ProductVi expectedProductVi) {
        assertProductViAllUpdatablePropertiesEquals(expectedProductVi, getPersistedProductVi(expectedProductVi));
    }
}
