package com.tale.domain;

import static com.tale.domain.CompanyTestSamples.*;
import static com.tale.domain.FocusGroupTestSamples.*;
import static com.tale.domain.NormalUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FocusGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FocusGroup.class);
        FocusGroup focusGroup1 = getFocusGroupSample1();
        FocusGroup focusGroup2 = new FocusGroup();
        assertThat(focusGroup1).isNotEqualTo(focusGroup2);

        focusGroup2.setId(focusGroup1.getId());
        assertThat(focusGroup1).isEqualTo(focusGroup2);

        focusGroup2 = getFocusGroupSample2();
        assertThat(focusGroup1).isNotEqualTo(focusGroup2);
    }

    @Test
    void normalUserTest() throws Exception {
        FocusGroup focusGroup = getFocusGroupRandomSampleGenerator();
        NormalUser normalUserBack = getNormalUserRandomSampleGenerator();

        focusGroup.setNormalUser(normalUserBack);
        assertThat(focusGroup.getNormalUser()).isEqualTo(normalUserBack);

        focusGroup.normalUser(null);
        assertThat(focusGroup.getNormalUser()).isNull();
    }

    @Test
    void companyTest() throws Exception {
        FocusGroup focusGroup = getFocusGroupRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        focusGroup.setCompany(companyBack);
        assertThat(focusGroup.getCompany()).isEqualTo(companyBack);

        focusGroup.company(null);
        assertThat(focusGroup.getCompany()).isNull();
    }
}
