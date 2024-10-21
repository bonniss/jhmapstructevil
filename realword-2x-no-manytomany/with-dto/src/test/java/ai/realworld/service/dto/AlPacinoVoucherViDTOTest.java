package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlPacinoVoucherViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoVoucherViDTO.class);
        AlPacinoVoucherViDTO alPacinoVoucherViDTO1 = new AlPacinoVoucherViDTO();
        alPacinoVoucherViDTO1.setId(UUID.randomUUID());
        AlPacinoVoucherViDTO alPacinoVoucherViDTO2 = new AlPacinoVoucherViDTO();
        assertThat(alPacinoVoucherViDTO1).isNotEqualTo(alPacinoVoucherViDTO2);
        alPacinoVoucherViDTO2.setId(alPacinoVoucherViDTO1.getId());
        assertThat(alPacinoVoucherViDTO1).isEqualTo(alPacinoVoucherViDTO2);
        alPacinoVoucherViDTO2.setId(UUID.randomUUID());
        assertThat(alPacinoVoucherViDTO1).isNotEqualTo(alPacinoVoucherViDTO2);
        alPacinoVoucherViDTO1.setId(null);
        assertThat(alPacinoVoucherViDTO1).isNotEqualTo(alPacinoVoucherViDTO2);
    }
}
