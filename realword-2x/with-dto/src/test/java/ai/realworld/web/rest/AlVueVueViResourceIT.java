package ai.realworld.web.rest;

import static ai.realworld.domain.AlVueVueViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static ai.realworld.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlVueVueVi;
import ai.realworld.domain.AlVueVueViUsage;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.AlcountScopy;
import ai.realworld.domain.enumeration.AlcountTypo;
import ai.realworld.domain.enumeration.PaoloStatus;
import ai.realworld.repository.AlVueVueViRepository;
import ai.realworld.service.dto.AlVueVueViDTO;
import ai.realworld.service.mapper.AlVueVueViMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AlVueVueViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlVueVueViResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_HEITIGA = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_HEITIGA = "BBBBBBBBBB";

    private static final AlcountTypo DEFAULT_DISCOUNT_TYPE = AlcountTypo.BY_FIXED_AMOUNT;
    private static final AlcountTypo UPDATED_DISCOUNT_TYPE = AlcountTypo.BY_PERCENT;

    private static final BigDecimal DEFAULT_DISCOUNT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISCOUNT_RATE = new BigDecimal(1 - 1);

    private static final AlcountScopy DEFAULT_SCOPE = AlcountScopy.ALL_PRODUCTS;
    private static final AlcountScopy UPDATED_SCOPE = AlcountScopy.SPECIFIED_PRODUCTS;

    private static final Boolean DEFAULT_IS_INDIVIDUALLY_USED_ONLY = false;
    private static final Boolean UPDATED_IS_INDIVIDUALLY_USED_ONLY = true;

    private static final Integer DEFAULT_USAGE_LIFE_TIME_LIMIT = 1;
    private static final Integer UPDATED_USAGE_LIFE_TIME_LIMIT = 2;
    private static final Integer SMALLER_USAGE_LIFE_TIME_LIMIT = 1 - 1;

    private static final Integer DEFAULT_USAGE_LIMIT_PER_USER = 1;
    private static final Integer UPDATED_USAGE_LIMIT_PER_USER = 2;
    private static final Integer SMALLER_USAGE_LIMIT_PER_USER = 1 - 1;

    private static final Integer DEFAULT_USAGE_QUANTITY = 1;
    private static final Integer UPDATED_USAGE_QUANTITY = 2;
    private static final Integer SMALLER_USAGE_QUANTITY = 1 - 1;

    private static final BigDecimal DEFAULT_MINIMUM_SPEND = new BigDecimal(1);
    private static final BigDecimal UPDATED_MINIMUM_SPEND = new BigDecimal(2);
    private static final BigDecimal SMALLER_MINIMUM_SPEND = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MAXIMUM_SPEND = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAXIMUM_SPEND = new BigDecimal(2);
    private static final BigDecimal SMALLER_MAXIMUM_SPEND = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_CAN_BE_COLLECTED_BY_USER = false;
    private static final Boolean UPDATED_CAN_BE_COLLECTED_BY_USER = true;

    private static final LocalDate DEFAULT_SALE_PRICE_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SALE_PRICE_FROM_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SALE_PRICE_FROM_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_SALE_PRICE_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SALE_PRICE_TO_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SALE_PRICE_TO_DATE = LocalDate.ofEpochDay(-1L);

    private static final PaoloStatus DEFAULT_PUBLICATION_STATUS = PaoloStatus.DRAFT;
    private static final PaoloStatus UPDATED_PUBLICATION_STATUS = PaoloStatus.PUBLISHED;

    private static final Instant DEFAULT_PUBLISHED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLISHED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/al-vue-vue-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlVueVueViRepository alVueVueViRepository;

    @Autowired
    private AlVueVueViMapper alVueVueViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlVueVueViMockMvc;

    private AlVueVueVi alVueVueVi;

    private AlVueVueVi insertedAlVueVueVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlVueVueVi createEntity() {
        return new AlVueVueVi()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .contentHeitiga(DEFAULT_CONTENT_HEITIGA)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .discountRate(DEFAULT_DISCOUNT_RATE)
            .scope(DEFAULT_SCOPE)
            .isIndividuallyUsedOnly(DEFAULT_IS_INDIVIDUALLY_USED_ONLY)
            .usageLifeTimeLimit(DEFAULT_USAGE_LIFE_TIME_LIMIT)
            .usageLimitPerUser(DEFAULT_USAGE_LIMIT_PER_USER)
            .usageQuantity(DEFAULT_USAGE_QUANTITY)
            .minimumSpend(DEFAULT_MINIMUM_SPEND)
            .maximumSpend(DEFAULT_MAXIMUM_SPEND)
            .canBeCollectedByUser(DEFAULT_CAN_BE_COLLECTED_BY_USER)
            .salePriceFromDate(DEFAULT_SALE_PRICE_FROM_DATE)
            .salePriceToDate(DEFAULT_SALE_PRICE_TO_DATE)
            .publicationStatus(DEFAULT_PUBLICATION_STATUS)
            .publishedDate(DEFAULT_PUBLISHED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlVueVueVi createUpdatedEntity() {
        return new AlVueVueVi()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .scope(UPDATED_SCOPE)
            .isIndividuallyUsedOnly(UPDATED_IS_INDIVIDUALLY_USED_ONLY)
            .usageLifeTimeLimit(UPDATED_USAGE_LIFE_TIME_LIMIT)
            .usageLimitPerUser(UPDATED_USAGE_LIMIT_PER_USER)
            .usageQuantity(UPDATED_USAGE_QUANTITY)
            .minimumSpend(UPDATED_MINIMUM_SPEND)
            .maximumSpend(UPDATED_MAXIMUM_SPEND)
            .canBeCollectedByUser(UPDATED_CAN_BE_COLLECTED_BY_USER)
            .salePriceFromDate(UPDATED_SALE_PRICE_FROM_DATE)
            .salePriceToDate(UPDATED_SALE_PRICE_TO_DATE)
            .publicationStatus(UPDATED_PUBLICATION_STATUS)
            .publishedDate(UPDATED_PUBLISHED_DATE);
    }

    @BeforeEach
    public void initTest() {
        alVueVueVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlVueVueVi != null) {
            alVueVueViRepository.delete(insertedAlVueVueVi);
            insertedAlVueVueVi = null;
        }
    }

    @Test
    @Transactional
    void createAlVueVueVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlVueVueVi
        AlVueVueViDTO alVueVueViDTO = alVueVueViMapper.toDto(alVueVueVi);
        var returnedAlVueVueViDTO = om.readValue(
            restAlVueVueViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlVueVueViDTO.class
        );

        // Validate the AlVueVueVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlVueVueVi = alVueVueViMapper.toEntity(returnedAlVueVueViDTO);
        assertAlVueVueViUpdatableFieldsEquals(returnedAlVueVueVi, getPersistedAlVueVueVi(returnedAlVueVueVi));

        insertedAlVueVueVi = returnedAlVueVueVi;
    }

    @Test
    @Transactional
    void createAlVueVueViWithExistingId() throws Exception {
        // Create the AlVueVueVi with an existing ID
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);
        AlVueVueViDTO alVueVueViDTO = alVueVueViMapper.toDto(alVueVueVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlVueVueViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alVueVueVi.setName(null);

        // Create the AlVueVueVi, which fails.
        AlVueVueViDTO alVueVueViDTO = alVueVueViMapper.toDto(alVueVueVi);

        restAlVueVueViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlVueVueVis() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList
        restAlVueVueViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alVueVueVi.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contentHeitiga").value(hasItem(DEFAULT_CONTENT_HEITIGA)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(sameNumber(DEFAULT_DISCOUNT_RATE))))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].isIndividuallyUsedOnly").value(hasItem(DEFAULT_IS_INDIVIDUALLY_USED_ONLY.booleanValue())))
            .andExpect(jsonPath("$.[*].usageLifeTimeLimit").value(hasItem(DEFAULT_USAGE_LIFE_TIME_LIMIT)))
            .andExpect(jsonPath("$.[*].usageLimitPerUser").value(hasItem(DEFAULT_USAGE_LIMIT_PER_USER)))
            .andExpect(jsonPath("$.[*].usageQuantity").value(hasItem(DEFAULT_USAGE_QUANTITY)))
            .andExpect(jsonPath("$.[*].minimumSpend").value(hasItem(sameNumber(DEFAULT_MINIMUM_SPEND))))
            .andExpect(jsonPath("$.[*].maximumSpend").value(hasItem(sameNumber(DEFAULT_MAXIMUM_SPEND))))
            .andExpect(jsonPath("$.[*].canBeCollectedByUser").value(hasItem(DEFAULT_CAN_BE_COLLECTED_BY_USER.booleanValue())))
            .andExpect(jsonPath("$.[*].salePriceFromDate").value(hasItem(DEFAULT_SALE_PRICE_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].salePriceToDate").value(hasItem(DEFAULT_SALE_PRICE_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].publicationStatus").value(hasItem(DEFAULT_PUBLICATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(DEFAULT_PUBLISHED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAlVueVueVi() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get the alVueVueVi
        restAlVueVueViMockMvc
            .perform(get(ENTITY_API_URL_ID, alVueVueVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alVueVueVi.getId().toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contentHeitiga").value(DEFAULT_CONTENT_HEITIGA))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.discountRate").value(sameNumber(DEFAULT_DISCOUNT_RATE)))
            .andExpect(jsonPath("$.scope").value(DEFAULT_SCOPE.toString()))
            .andExpect(jsonPath("$.isIndividuallyUsedOnly").value(DEFAULT_IS_INDIVIDUALLY_USED_ONLY.booleanValue()))
            .andExpect(jsonPath("$.usageLifeTimeLimit").value(DEFAULT_USAGE_LIFE_TIME_LIMIT))
            .andExpect(jsonPath("$.usageLimitPerUser").value(DEFAULT_USAGE_LIMIT_PER_USER))
            .andExpect(jsonPath("$.usageQuantity").value(DEFAULT_USAGE_QUANTITY))
            .andExpect(jsonPath("$.minimumSpend").value(sameNumber(DEFAULT_MINIMUM_SPEND)))
            .andExpect(jsonPath("$.maximumSpend").value(sameNumber(DEFAULT_MAXIMUM_SPEND)))
            .andExpect(jsonPath("$.canBeCollectedByUser").value(DEFAULT_CAN_BE_COLLECTED_BY_USER.booleanValue()))
            .andExpect(jsonPath("$.salePriceFromDate").value(DEFAULT_SALE_PRICE_FROM_DATE.toString()))
            .andExpect(jsonPath("$.salePriceToDate").value(DEFAULT_SALE_PRICE_TO_DATE.toString()))
            .andExpect(jsonPath("$.publicationStatus").value(DEFAULT_PUBLICATION_STATUS.toString()))
            .andExpect(jsonPath("$.publishedDate").value(DEFAULT_PUBLISHED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAlVueVueVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        UUID id = alVueVueVi.getId();

        defaultAlVueVueViFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where code equals to
        defaultAlVueVueViFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where code in
        defaultAlVueVueViFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where code is not null
        defaultAlVueVueViFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where code contains
        defaultAlVueVueViFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where code does not contain
        defaultAlVueVueViFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where name equals to
        defaultAlVueVueViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where name in
        defaultAlVueVueViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where name is not null
        defaultAlVueVueViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where name contains
        defaultAlVueVueViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where name does not contain
        defaultAlVueVueViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByContentHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where contentHeitiga equals to
        defaultAlVueVueViFiltering("contentHeitiga.equals=" + DEFAULT_CONTENT_HEITIGA, "contentHeitiga.equals=" + UPDATED_CONTENT_HEITIGA);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByContentHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where contentHeitiga in
        defaultAlVueVueViFiltering(
            "contentHeitiga.in=" + DEFAULT_CONTENT_HEITIGA + "," + UPDATED_CONTENT_HEITIGA,
            "contentHeitiga.in=" + UPDATED_CONTENT_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByContentHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where contentHeitiga is not null
        defaultAlVueVueViFiltering("contentHeitiga.specified=true", "contentHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByContentHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where contentHeitiga contains
        defaultAlVueVueViFiltering(
            "contentHeitiga.contains=" + DEFAULT_CONTENT_HEITIGA,
            "contentHeitiga.contains=" + UPDATED_CONTENT_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByContentHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where contentHeitiga does not contain
        defaultAlVueVueViFiltering(
            "contentHeitiga.doesNotContain=" + UPDATED_CONTENT_HEITIGA,
            "contentHeitiga.doesNotContain=" + DEFAULT_CONTENT_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where discountType equals to
        defaultAlVueVueViFiltering("discountType.equals=" + DEFAULT_DISCOUNT_TYPE, "discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where discountType in
        defaultAlVueVueViFiltering(
            "discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE,
            "discountType.in=" + UPDATED_DISCOUNT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where discountType is not null
        defaultAlVueVueViFiltering("discountType.specified=true", "discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where discountRate equals to
        defaultAlVueVueViFiltering("discountRate.equals=" + DEFAULT_DISCOUNT_RATE, "discountRate.equals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where discountRate in
        defaultAlVueVueViFiltering(
            "discountRate.in=" + DEFAULT_DISCOUNT_RATE + "," + UPDATED_DISCOUNT_RATE,
            "discountRate.in=" + UPDATED_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where discountRate is not null
        defaultAlVueVueViFiltering("discountRate.specified=true", "discountRate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByDiscountRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where discountRate is greater than or equal to
        defaultAlVueVueViFiltering(
            "discountRate.greaterThanOrEqual=" + DEFAULT_DISCOUNT_RATE,
            "discountRate.greaterThanOrEqual=" + UPDATED_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByDiscountRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where discountRate is less than or equal to
        defaultAlVueVueViFiltering(
            "discountRate.lessThanOrEqual=" + DEFAULT_DISCOUNT_RATE,
            "discountRate.lessThanOrEqual=" + SMALLER_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByDiscountRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where discountRate is less than
        defaultAlVueVueViFiltering("discountRate.lessThan=" + UPDATED_DISCOUNT_RATE, "discountRate.lessThan=" + DEFAULT_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByDiscountRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where discountRate is greater than
        defaultAlVueVueViFiltering(
            "discountRate.greaterThan=" + SMALLER_DISCOUNT_RATE,
            "discountRate.greaterThan=" + DEFAULT_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByScopeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where scope equals to
        defaultAlVueVueViFiltering("scope.equals=" + DEFAULT_SCOPE, "scope.equals=" + UPDATED_SCOPE);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByScopeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where scope in
        defaultAlVueVueViFiltering("scope.in=" + DEFAULT_SCOPE + "," + UPDATED_SCOPE, "scope.in=" + UPDATED_SCOPE);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByScopeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where scope is not null
        defaultAlVueVueViFiltering("scope.specified=true", "scope.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByIsIndividuallyUsedOnlyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where isIndividuallyUsedOnly equals to
        defaultAlVueVueViFiltering(
            "isIndividuallyUsedOnly.equals=" + DEFAULT_IS_INDIVIDUALLY_USED_ONLY,
            "isIndividuallyUsedOnly.equals=" + UPDATED_IS_INDIVIDUALLY_USED_ONLY
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByIsIndividuallyUsedOnlyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where isIndividuallyUsedOnly in
        defaultAlVueVueViFiltering(
            "isIndividuallyUsedOnly.in=" + DEFAULT_IS_INDIVIDUALLY_USED_ONLY + "," + UPDATED_IS_INDIVIDUALLY_USED_ONLY,
            "isIndividuallyUsedOnly.in=" + UPDATED_IS_INDIVIDUALLY_USED_ONLY
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByIsIndividuallyUsedOnlyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where isIndividuallyUsedOnly is not null
        defaultAlVueVueViFiltering("isIndividuallyUsedOnly.specified=true", "isIndividuallyUsedOnly.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLifeTimeLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLifeTimeLimit equals to
        defaultAlVueVueViFiltering(
            "usageLifeTimeLimit.equals=" + DEFAULT_USAGE_LIFE_TIME_LIMIT,
            "usageLifeTimeLimit.equals=" + UPDATED_USAGE_LIFE_TIME_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLifeTimeLimitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLifeTimeLimit in
        defaultAlVueVueViFiltering(
            "usageLifeTimeLimit.in=" + DEFAULT_USAGE_LIFE_TIME_LIMIT + "," + UPDATED_USAGE_LIFE_TIME_LIMIT,
            "usageLifeTimeLimit.in=" + UPDATED_USAGE_LIFE_TIME_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLifeTimeLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLifeTimeLimit is not null
        defaultAlVueVueViFiltering("usageLifeTimeLimit.specified=true", "usageLifeTimeLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLifeTimeLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLifeTimeLimit is greater than or equal to
        defaultAlVueVueViFiltering(
            "usageLifeTimeLimit.greaterThanOrEqual=" + DEFAULT_USAGE_LIFE_TIME_LIMIT,
            "usageLifeTimeLimit.greaterThanOrEqual=" + UPDATED_USAGE_LIFE_TIME_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLifeTimeLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLifeTimeLimit is less than or equal to
        defaultAlVueVueViFiltering(
            "usageLifeTimeLimit.lessThanOrEqual=" + DEFAULT_USAGE_LIFE_TIME_LIMIT,
            "usageLifeTimeLimit.lessThanOrEqual=" + SMALLER_USAGE_LIFE_TIME_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLifeTimeLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLifeTimeLimit is less than
        defaultAlVueVueViFiltering(
            "usageLifeTimeLimit.lessThan=" + UPDATED_USAGE_LIFE_TIME_LIMIT,
            "usageLifeTimeLimit.lessThan=" + DEFAULT_USAGE_LIFE_TIME_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLifeTimeLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLifeTimeLimit is greater than
        defaultAlVueVueViFiltering(
            "usageLifeTimeLimit.greaterThan=" + SMALLER_USAGE_LIFE_TIME_LIMIT,
            "usageLifeTimeLimit.greaterThan=" + DEFAULT_USAGE_LIFE_TIME_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLimitPerUserIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLimitPerUser equals to
        defaultAlVueVueViFiltering(
            "usageLimitPerUser.equals=" + DEFAULT_USAGE_LIMIT_PER_USER,
            "usageLimitPerUser.equals=" + UPDATED_USAGE_LIMIT_PER_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLimitPerUserIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLimitPerUser in
        defaultAlVueVueViFiltering(
            "usageLimitPerUser.in=" + DEFAULT_USAGE_LIMIT_PER_USER + "," + UPDATED_USAGE_LIMIT_PER_USER,
            "usageLimitPerUser.in=" + UPDATED_USAGE_LIMIT_PER_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLimitPerUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLimitPerUser is not null
        defaultAlVueVueViFiltering("usageLimitPerUser.specified=true", "usageLimitPerUser.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLimitPerUserIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLimitPerUser is greater than or equal to
        defaultAlVueVueViFiltering(
            "usageLimitPerUser.greaterThanOrEqual=" + DEFAULT_USAGE_LIMIT_PER_USER,
            "usageLimitPerUser.greaterThanOrEqual=" + UPDATED_USAGE_LIMIT_PER_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLimitPerUserIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLimitPerUser is less than or equal to
        defaultAlVueVueViFiltering(
            "usageLimitPerUser.lessThanOrEqual=" + DEFAULT_USAGE_LIMIT_PER_USER,
            "usageLimitPerUser.lessThanOrEqual=" + SMALLER_USAGE_LIMIT_PER_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLimitPerUserIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLimitPerUser is less than
        defaultAlVueVueViFiltering(
            "usageLimitPerUser.lessThan=" + UPDATED_USAGE_LIMIT_PER_USER,
            "usageLimitPerUser.lessThan=" + DEFAULT_USAGE_LIMIT_PER_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageLimitPerUserIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageLimitPerUser is greater than
        defaultAlVueVueViFiltering(
            "usageLimitPerUser.greaterThan=" + SMALLER_USAGE_LIMIT_PER_USER,
            "usageLimitPerUser.greaterThan=" + DEFAULT_USAGE_LIMIT_PER_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageQuantity equals to
        defaultAlVueVueViFiltering("usageQuantity.equals=" + DEFAULT_USAGE_QUANTITY, "usageQuantity.equals=" + UPDATED_USAGE_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageQuantity in
        defaultAlVueVueViFiltering(
            "usageQuantity.in=" + DEFAULT_USAGE_QUANTITY + "," + UPDATED_USAGE_QUANTITY,
            "usageQuantity.in=" + UPDATED_USAGE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageQuantity is not null
        defaultAlVueVueViFiltering("usageQuantity.specified=true", "usageQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageQuantity is greater than or equal to
        defaultAlVueVueViFiltering(
            "usageQuantity.greaterThanOrEqual=" + DEFAULT_USAGE_QUANTITY,
            "usageQuantity.greaterThanOrEqual=" + UPDATED_USAGE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageQuantity is less than or equal to
        defaultAlVueVueViFiltering(
            "usageQuantity.lessThanOrEqual=" + DEFAULT_USAGE_QUANTITY,
            "usageQuantity.lessThanOrEqual=" + SMALLER_USAGE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageQuantity is less than
        defaultAlVueVueViFiltering("usageQuantity.lessThan=" + UPDATED_USAGE_QUANTITY, "usageQuantity.lessThan=" + DEFAULT_USAGE_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByUsageQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where usageQuantity is greater than
        defaultAlVueVueViFiltering(
            "usageQuantity.greaterThan=" + SMALLER_USAGE_QUANTITY,
            "usageQuantity.greaterThan=" + DEFAULT_USAGE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMinimumSpendIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where minimumSpend equals to
        defaultAlVueVueViFiltering("minimumSpend.equals=" + DEFAULT_MINIMUM_SPEND, "minimumSpend.equals=" + UPDATED_MINIMUM_SPEND);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMinimumSpendIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where minimumSpend in
        defaultAlVueVueViFiltering(
            "minimumSpend.in=" + DEFAULT_MINIMUM_SPEND + "," + UPDATED_MINIMUM_SPEND,
            "minimumSpend.in=" + UPDATED_MINIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMinimumSpendIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where minimumSpend is not null
        defaultAlVueVueViFiltering("minimumSpend.specified=true", "minimumSpend.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMinimumSpendIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where minimumSpend is greater than or equal to
        defaultAlVueVueViFiltering(
            "minimumSpend.greaterThanOrEqual=" + DEFAULT_MINIMUM_SPEND,
            "minimumSpend.greaterThanOrEqual=" + UPDATED_MINIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMinimumSpendIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where minimumSpend is less than or equal to
        defaultAlVueVueViFiltering(
            "minimumSpend.lessThanOrEqual=" + DEFAULT_MINIMUM_SPEND,
            "minimumSpend.lessThanOrEqual=" + SMALLER_MINIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMinimumSpendIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where minimumSpend is less than
        defaultAlVueVueViFiltering("minimumSpend.lessThan=" + UPDATED_MINIMUM_SPEND, "minimumSpend.lessThan=" + DEFAULT_MINIMUM_SPEND);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMinimumSpendIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where minimumSpend is greater than
        defaultAlVueVueViFiltering(
            "minimumSpend.greaterThan=" + SMALLER_MINIMUM_SPEND,
            "minimumSpend.greaterThan=" + DEFAULT_MINIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMaximumSpendIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where maximumSpend equals to
        defaultAlVueVueViFiltering("maximumSpend.equals=" + DEFAULT_MAXIMUM_SPEND, "maximumSpend.equals=" + UPDATED_MAXIMUM_SPEND);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMaximumSpendIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where maximumSpend in
        defaultAlVueVueViFiltering(
            "maximumSpend.in=" + DEFAULT_MAXIMUM_SPEND + "," + UPDATED_MAXIMUM_SPEND,
            "maximumSpend.in=" + UPDATED_MAXIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMaximumSpendIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where maximumSpend is not null
        defaultAlVueVueViFiltering("maximumSpend.specified=true", "maximumSpend.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMaximumSpendIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where maximumSpend is greater than or equal to
        defaultAlVueVueViFiltering(
            "maximumSpend.greaterThanOrEqual=" + DEFAULT_MAXIMUM_SPEND,
            "maximumSpend.greaterThanOrEqual=" + UPDATED_MAXIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMaximumSpendIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where maximumSpend is less than or equal to
        defaultAlVueVueViFiltering(
            "maximumSpend.lessThanOrEqual=" + DEFAULT_MAXIMUM_SPEND,
            "maximumSpend.lessThanOrEqual=" + SMALLER_MAXIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMaximumSpendIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where maximumSpend is less than
        defaultAlVueVueViFiltering("maximumSpend.lessThan=" + UPDATED_MAXIMUM_SPEND, "maximumSpend.lessThan=" + DEFAULT_MAXIMUM_SPEND);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByMaximumSpendIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where maximumSpend is greater than
        defaultAlVueVueViFiltering(
            "maximumSpend.greaterThan=" + SMALLER_MAXIMUM_SPEND,
            "maximumSpend.greaterThan=" + DEFAULT_MAXIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByCanBeCollectedByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where canBeCollectedByUser equals to
        defaultAlVueVueViFiltering(
            "canBeCollectedByUser.equals=" + DEFAULT_CAN_BE_COLLECTED_BY_USER,
            "canBeCollectedByUser.equals=" + UPDATED_CAN_BE_COLLECTED_BY_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByCanBeCollectedByUserIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where canBeCollectedByUser in
        defaultAlVueVueViFiltering(
            "canBeCollectedByUser.in=" + DEFAULT_CAN_BE_COLLECTED_BY_USER + "," + UPDATED_CAN_BE_COLLECTED_BY_USER,
            "canBeCollectedByUser.in=" + UPDATED_CAN_BE_COLLECTED_BY_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByCanBeCollectedByUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where canBeCollectedByUser is not null
        defaultAlVueVueViFiltering("canBeCollectedByUser.specified=true", "canBeCollectedByUser.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceFromDate equals to
        defaultAlVueVueViFiltering(
            "salePriceFromDate.equals=" + DEFAULT_SALE_PRICE_FROM_DATE,
            "salePriceFromDate.equals=" + UPDATED_SALE_PRICE_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceFromDate in
        defaultAlVueVueViFiltering(
            "salePriceFromDate.in=" + DEFAULT_SALE_PRICE_FROM_DATE + "," + UPDATED_SALE_PRICE_FROM_DATE,
            "salePriceFromDate.in=" + UPDATED_SALE_PRICE_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceFromDate is not null
        defaultAlVueVueViFiltering("salePriceFromDate.specified=true", "salePriceFromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceFromDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceFromDate is greater than or equal to
        defaultAlVueVueViFiltering(
            "salePriceFromDate.greaterThanOrEqual=" + DEFAULT_SALE_PRICE_FROM_DATE,
            "salePriceFromDate.greaterThanOrEqual=" + UPDATED_SALE_PRICE_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceFromDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceFromDate is less than or equal to
        defaultAlVueVueViFiltering(
            "salePriceFromDate.lessThanOrEqual=" + DEFAULT_SALE_PRICE_FROM_DATE,
            "salePriceFromDate.lessThanOrEqual=" + SMALLER_SALE_PRICE_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceFromDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceFromDate is less than
        defaultAlVueVueViFiltering(
            "salePriceFromDate.lessThan=" + UPDATED_SALE_PRICE_FROM_DATE,
            "salePriceFromDate.lessThan=" + DEFAULT_SALE_PRICE_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceFromDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceFromDate is greater than
        defaultAlVueVueViFiltering(
            "salePriceFromDate.greaterThan=" + SMALLER_SALE_PRICE_FROM_DATE,
            "salePriceFromDate.greaterThan=" + DEFAULT_SALE_PRICE_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceToDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceToDate equals to
        defaultAlVueVueViFiltering(
            "salePriceToDate.equals=" + DEFAULT_SALE_PRICE_TO_DATE,
            "salePriceToDate.equals=" + UPDATED_SALE_PRICE_TO_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceToDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceToDate in
        defaultAlVueVueViFiltering(
            "salePriceToDate.in=" + DEFAULT_SALE_PRICE_TO_DATE + "," + UPDATED_SALE_PRICE_TO_DATE,
            "salePriceToDate.in=" + UPDATED_SALE_PRICE_TO_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceToDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceToDate is not null
        defaultAlVueVueViFiltering("salePriceToDate.specified=true", "salePriceToDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceToDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceToDate is greater than or equal to
        defaultAlVueVueViFiltering(
            "salePriceToDate.greaterThanOrEqual=" + DEFAULT_SALE_PRICE_TO_DATE,
            "salePriceToDate.greaterThanOrEqual=" + UPDATED_SALE_PRICE_TO_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceToDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceToDate is less than or equal to
        defaultAlVueVueViFiltering(
            "salePriceToDate.lessThanOrEqual=" + DEFAULT_SALE_PRICE_TO_DATE,
            "salePriceToDate.lessThanOrEqual=" + SMALLER_SALE_PRICE_TO_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceToDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceToDate is less than
        defaultAlVueVueViFiltering(
            "salePriceToDate.lessThan=" + UPDATED_SALE_PRICE_TO_DATE,
            "salePriceToDate.lessThan=" + DEFAULT_SALE_PRICE_TO_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisBySalePriceToDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where salePriceToDate is greater than
        defaultAlVueVueViFiltering(
            "salePriceToDate.greaterThan=" + SMALLER_SALE_PRICE_TO_DATE,
            "salePriceToDate.greaterThan=" + DEFAULT_SALE_PRICE_TO_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByPublicationStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where publicationStatus equals to
        defaultAlVueVueViFiltering(
            "publicationStatus.equals=" + DEFAULT_PUBLICATION_STATUS,
            "publicationStatus.equals=" + UPDATED_PUBLICATION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByPublicationStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where publicationStatus in
        defaultAlVueVueViFiltering(
            "publicationStatus.in=" + DEFAULT_PUBLICATION_STATUS + "," + UPDATED_PUBLICATION_STATUS,
            "publicationStatus.in=" + UPDATED_PUBLICATION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByPublicationStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where publicationStatus is not null
        defaultAlVueVueViFiltering("publicationStatus.specified=true", "publicationStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByPublishedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where publishedDate equals to
        defaultAlVueVueViFiltering("publishedDate.equals=" + DEFAULT_PUBLISHED_DATE, "publishedDate.equals=" + UPDATED_PUBLISHED_DATE);
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByPublishedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where publishedDate in
        defaultAlVueVueViFiltering(
            "publishedDate.in=" + DEFAULT_PUBLISHED_DATE + "," + UPDATED_PUBLISHED_DATE,
            "publishedDate.in=" + UPDATED_PUBLISHED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByPublishedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        // Get all the alVueVueViList where publishedDate is not null
        defaultAlVueVueViFiltering("publishedDate.specified=true", "publishedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByImageIsEqualToSomething() throws Exception {
        Metaverse image;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alVueVueViRepository.saveAndFlush(alVueVueVi);
            image = MetaverseResourceIT.createEntity();
        } else {
            image = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(image);
        em.flush();
        alVueVueVi.setImage(image);
        alVueVueViRepository.saveAndFlush(alVueVueVi);
        Long imageId = image.getId();
        // Get all the alVueVueViList where image equals to imageId
        defaultAlVueVueViShouldBeFound("imageId.equals=" + imageId);

        // Get all the alVueVueViList where image equals to (imageId + 1)
        defaultAlVueVueViShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByAlVueVueViUsageIsEqualToSomething() throws Exception {
        AlVueVueViUsage alVueVueViUsage;
        if (TestUtil.findAll(em, AlVueVueViUsage.class).isEmpty()) {
            alVueVueViRepository.saveAndFlush(alVueVueVi);
            alVueVueViUsage = AlVueVueViUsageResourceIT.createEntity();
        } else {
            alVueVueViUsage = TestUtil.findAll(em, AlVueVueViUsage.class).get(0);
        }
        em.persist(alVueVueViUsage);
        em.flush();
        alVueVueVi.setAlVueVueViUsage(alVueVueViUsage);
        alVueVueViRepository.saveAndFlush(alVueVueVi);
        UUID alVueVueViUsageId = alVueVueViUsage.getId();
        // Get all the alVueVueViList where alVueVueViUsage equals to alVueVueViUsageId
        defaultAlVueVueViShouldBeFound("alVueVueViUsageId.equals=" + alVueVueViUsageId);

        // Get all the alVueVueViList where alVueVueViUsage equals to UUID.randomUUID()
        defaultAlVueVueViShouldNotBeFound("alVueVueViUsageId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlVueVueVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alVueVueViRepository.saveAndFlush(alVueVueVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alVueVueVi.setApplication(application);
        alVueVueViRepository.saveAndFlush(alVueVueVi);
        UUID applicationId = application.getId();
        // Get all the alVueVueViList where application equals to applicationId
        defaultAlVueVueViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alVueVueViList where application equals to UUID.randomUUID()
        defaultAlVueVueViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlVueVueViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlVueVueViShouldBeFound(shouldBeFound);
        defaultAlVueVueViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlVueVueViShouldBeFound(String filter) throws Exception {
        restAlVueVueViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alVueVueVi.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contentHeitiga").value(hasItem(DEFAULT_CONTENT_HEITIGA)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(sameNumber(DEFAULT_DISCOUNT_RATE))))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].isIndividuallyUsedOnly").value(hasItem(DEFAULT_IS_INDIVIDUALLY_USED_ONLY.booleanValue())))
            .andExpect(jsonPath("$.[*].usageLifeTimeLimit").value(hasItem(DEFAULT_USAGE_LIFE_TIME_LIMIT)))
            .andExpect(jsonPath("$.[*].usageLimitPerUser").value(hasItem(DEFAULT_USAGE_LIMIT_PER_USER)))
            .andExpect(jsonPath("$.[*].usageQuantity").value(hasItem(DEFAULT_USAGE_QUANTITY)))
            .andExpect(jsonPath("$.[*].minimumSpend").value(hasItem(sameNumber(DEFAULT_MINIMUM_SPEND))))
            .andExpect(jsonPath("$.[*].maximumSpend").value(hasItem(sameNumber(DEFAULT_MAXIMUM_SPEND))))
            .andExpect(jsonPath("$.[*].canBeCollectedByUser").value(hasItem(DEFAULT_CAN_BE_COLLECTED_BY_USER.booleanValue())))
            .andExpect(jsonPath("$.[*].salePriceFromDate").value(hasItem(DEFAULT_SALE_PRICE_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].salePriceToDate").value(hasItem(DEFAULT_SALE_PRICE_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].publicationStatus").value(hasItem(DEFAULT_PUBLICATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(DEFAULT_PUBLISHED_DATE.toString())));

        // Check, that the count call also returns 1
        restAlVueVueViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlVueVueViShouldNotBeFound(String filter) throws Exception {
        restAlVueVueViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlVueVueViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlVueVueVi() throws Exception {
        // Get the alVueVueVi
        restAlVueVueViMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlVueVueVi() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueVi
        AlVueVueVi updatedAlVueVueVi = alVueVueViRepository.findById(alVueVueVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlVueVueVi are not directly saved in db
        em.detach(updatedAlVueVueVi);
        updatedAlVueVueVi
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .scope(UPDATED_SCOPE)
            .isIndividuallyUsedOnly(UPDATED_IS_INDIVIDUALLY_USED_ONLY)
            .usageLifeTimeLimit(UPDATED_USAGE_LIFE_TIME_LIMIT)
            .usageLimitPerUser(UPDATED_USAGE_LIMIT_PER_USER)
            .usageQuantity(UPDATED_USAGE_QUANTITY)
            .minimumSpend(UPDATED_MINIMUM_SPEND)
            .maximumSpend(UPDATED_MAXIMUM_SPEND)
            .canBeCollectedByUser(UPDATED_CAN_BE_COLLECTED_BY_USER)
            .salePriceFromDate(UPDATED_SALE_PRICE_FROM_DATE)
            .salePriceToDate(UPDATED_SALE_PRICE_TO_DATE)
            .publicationStatus(UPDATED_PUBLICATION_STATUS)
            .publishedDate(UPDATED_PUBLISHED_DATE);
        AlVueVueViDTO alVueVueViDTO = alVueVueViMapper.toDto(updatedAlVueVueVi);

        restAlVueVueViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alVueVueViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlVueVueViToMatchAllProperties(updatedAlVueVueVi);
    }

    @Test
    @Transactional
    void putNonExistingAlVueVueVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueVi.setId(UUID.randomUUID());

        // Create the AlVueVueVi
        AlVueVueViDTO alVueVueViDTO = alVueVueViMapper.toDto(alVueVueVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlVueVueViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alVueVueViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlVueVueVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueVi.setId(UUID.randomUUID());

        // Create the AlVueVueVi
        AlVueVueViDTO alVueVueViDTO = alVueVueViMapper.toDto(alVueVueVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlVueVueVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueVi.setId(UUID.randomUUID());

        // Create the AlVueVueVi
        AlVueVueViDTO alVueVueViDTO = alVueVueViMapper.toDto(alVueVueVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlVueVueVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlVueVueViWithPatch() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueVi using partial update
        AlVueVueVi partialUpdatedAlVueVueVi = new AlVueVueVi();
        partialUpdatedAlVueVueVi.setId(alVueVueVi.getId());

        partialUpdatedAlVueVueVi
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .scope(UPDATED_SCOPE)
            .usageQuantity(UPDATED_USAGE_QUANTITY)
            .maximumSpend(UPDATED_MAXIMUM_SPEND)
            .salePriceFromDate(UPDATED_SALE_PRICE_FROM_DATE)
            .salePriceToDate(UPDATED_SALE_PRICE_TO_DATE)
            .publishedDate(UPDATED_PUBLISHED_DATE);

        restAlVueVueViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlVueVueVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlVueVueVi))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlVueVueViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlVueVueVi, alVueVueVi),
            getPersistedAlVueVueVi(alVueVueVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlVueVueViWithPatch() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVueVi using partial update
        AlVueVueVi partialUpdatedAlVueVueVi = new AlVueVueVi();
        partialUpdatedAlVueVueVi.setId(alVueVueVi.getId());

        partialUpdatedAlVueVueVi
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .scope(UPDATED_SCOPE)
            .isIndividuallyUsedOnly(UPDATED_IS_INDIVIDUALLY_USED_ONLY)
            .usageLifeTimeLimit(UPDATED_USAGE_LIFE_TIME_LIMIT)
            .usageLimitPerUser(UPDATED_USAGE_LIMIT_PER_USER)
            .usageQuantity(UPDATED_USAGE_QUANTITY)
            .minimumSpend(UPDATED_MINIMUM_SPEND)
            .maximumSpend(UPDATED_MAXIMUM_SPEND)
            .canBeCollectedByUser(UPDATED_CAN_BE_COLLECTED_BY_USER)
            .salePriceFromDate(UPDATED_SALE_PRICE_FROM_DATE)
            .salePriceToDate(UPDATED_SALE_PRICE_TO_DATE)
            .publicationStatus(UPDATED_PUBLICATION_STATUS)
            .publishedDate(UPDATED_PUBLISHED_DATE);

        restAlVueVueViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlVueVueVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlVueVueVi))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVueVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlVueVueViUpdatableFieldsEquals(partialUpdatedAlVueVueVi, getPersistedAlVueVueVi(partialUpdatedAlVueVueVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlVueVueVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueVi.setId(UUID.randomUUID());

        // Create the AlVueVueVi
        AlVueVueViDTO alVueVueViDTO = alVueVueViMapper.toDto(alVueVueVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlVueVueViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alVueVueViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alVueVueViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlVueVueVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueVi.setId(UUID.randomUUID());

        // Create the AlVueVueVi
        AlVueVueViDTO alVueVueViDTO = alVueVueViMapper.toDto(alVueVueVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alVueVueViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVueVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlVueVueVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVueVi.setId(UUID.randomUUID());

        // Create the AlVueVueVi
        AlVueVueViDTO alVueVueViDTO = alVueVueViMapper.toDto(alVueVueVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alVueVueViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlVueVueVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlVueVueVi() throws Exception {
        // Initialize the database
        insertedAlVueVueVi = alVueVueViRepository.saveAndFlush(alVueVueVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alVueVueVi
        restAlVueVueViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alVueVueVi.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alVueVueViRepository.count();
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

    protected AlVueVueVi getPersistedAlVueVueVi(AlVueVueVi alVueVueVi) {
        return alVueVueViRepository.findById(alVueVueVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlVueVueViToMatchAllProperties(AlVueVueVi expectedAlVueVueVi) {
        assertAlVueVueViAllPropertiesEquals(expectedAlVueVueVi, getPersistedAlVueVueVi(expectedAlVueVueVi));
    }

    protected void assertPersistedAlVueVueViToMatchUpdatableProperties(AlVueVueVi expectedAlVueVueVi) {
        assertAlVueVueViAllUpdatablePropertiesEquals(expectedAlVueVueVi, getPersistedAlVueVueVi(expectedAlVueVueVi));
    }
}
