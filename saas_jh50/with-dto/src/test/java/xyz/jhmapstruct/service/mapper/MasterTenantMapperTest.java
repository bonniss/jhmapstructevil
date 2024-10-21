package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.MasterTenantAsserts.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MasterTenantMapperTest {

    private MasterTenantMapper masterTenantMapper;

    @BeforeEach
    void setUp() {
        masterTenantMapper = new MasterTenantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMasterTenantSample1();
        var actual = masterTenantMapper.toEntity(masterTenantMapper.toDto(expected));
        assertMasterTenantAllPropertiesEquals(expected, actual);
    }
}
