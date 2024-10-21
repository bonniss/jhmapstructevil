package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlPacinoVoucherDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoVoucherDTO.class);
        AlPacinoVoucherDTO alPacinoVoucherDTO1 = new AlPacinoVoucherDTO();
        alPacinoVoucherDTO1.setId(UUID.randomUUID());
        AlPacinoVoucherDTO alPacinoVoucherDTO2 = new AlPacinoVoucherDTO();
        assertThat(alPacinoVoucherDTO1).isNotEqualTo(alPacinoVoucherDTO2);
        alPacinoVoucherDTO2.setId(alPacinoVoucherDTO1.getId());
        assertThat(alPacinoVoucherDTO1).isEqualTo(alPacinoVoucherDTO2);
        alPacinoVoucherDTO2.setId(UUID.randomUUID());
        assertThat(alPacinoVoucherDTO1).isNotEqualTo(alPacinoVoucherDTO2);
        alPacinoVoucherDTO1.setId(null);
        assertThat(alPacinoVoucherDTO1).isNotEqualTo(alPacinoVoucherDTO2);
    }
}
