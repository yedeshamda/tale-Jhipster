package com.tale.domain;

import static com.tale.domain.NormalUserTestSamples.*;
import static com.tale.domain.PointSpendOptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointSpendOptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointSpendOption.class);
        PointSpendOption pointSpendOption1 = getPointSpendOptionSample1();
        PointSpendOption pointSpendOption2 = new PointSpendOption();
        assertThat(pointSpendOption1).isNotEqualTo(pointSpendOption2);

        pointSpendOption2.setId(pointSpendOption1.getId());
        assertThat(pointSpendOption1).isEqualTo(pointSpendOption2);

        pointSpendOption2 = getPointSpendOptionSample2();
        assertThat(pointSpendOption1).isNotEqualTo(pointSpendOption2);
    }

    @Test
    void normalUserTest() throws Exception {
        PointSpendOption pointSpendOption = getPointSpendOptionRandomSampleGenerator();
        NormalUser normalUserBack = getNormalUserRandomSampleGenerator();

        pointSpendOption.setNormalUser(normalUserBack);
        assertThat(pointSpendOption.getNormalUser()).isEqualTo(normalUserBack);

        pointSpendOption.normalUser(null);
        assertThat(pointSpendOption.getNormalUser()).isNull();
    }
}
