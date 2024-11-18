package com.tale.domain;

import static com.tale.domain.NormalUserTestSamples.*;
import static com.tale.domain.PointTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointTransaction.class);
        PointTransaction pointTransaction1 = getPointTransactionSample1();
        PointTransaction pointTransaction2 = new PointTransaction();
        assertThat(pointTransaction1).isNotEqualTo(pointTransaction2);

        pointTransaction2.setId(pointTransaction1.getId());
        assertThat(pointTransaction1).isEqualTo(pointTransaction2);

        pointTransaction2 = getPointTransactionSample2();
        assertThat(pointTransaction1).isNotEqualTo(pointTransaction2);
    }

    @Test
    void normalUserTest() throws Exception {
        PointTransaction pointTransaction = getPointTransactionRandomSampleGenerator();
        NormalUser normalUserBack = getNormalUserRandomSampleGenerator();

        pointTransaction.setNormalUser(normalUserBack);
        assertThat(pointTransaction.getNormalUser()).isEqualTo(normalUserBack);

        pointTransaction.normalUser(null);
        assertThat(pointTransaction.getNormalUser()).isNull();
    }
}
