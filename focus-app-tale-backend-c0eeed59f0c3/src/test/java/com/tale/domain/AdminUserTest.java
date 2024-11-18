package com.tale.domain;

import static com.tale.domain.AdminUserTestSamples.*;
import static com.tale.domain.CampaignTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AdminUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminUser.class);
        AdminUser adminUser1 = getAdminUserSample1();
        AdminUser adminUser2 = new AdminUser();
        assertThat(adminUser1).isNotEqualTo(adminUser2);

        adminUser2.setId(adminUser1.getId());
        assertThat(adminUser1).isEqualTo(adminUser2);

        adminUser2 = getAdminUserSample2();
        assertThat(adminUser1).isNotEqualTo(adminUser2);
    }

    @Test
    void campaignTest() throws Exception {
        AdminUser adminUser = getAdminUserRandomSampleGenerator();
        Campaign campaignBack = getCampaignRandomSampleGenerator();

        adminUser.addCampaign(campaignBack);
        assertThat(adminUser.getCampaigns()).containsOnly(campaignBack);
        assertThat(campaignBack.getAdminUser()).isEqualTo(adminUser);

        adminUser.removeCampaign(campaignBack);
        assertThat(adminUser.getCampaigns()).doesNotContain(campaignBack);
        assertThat(campaignBack.getAdminUser()).isNull();

        adminUser.campaigns(new HashSet<>(Set.of(campaignBack)));
        assertThat(adminUser.getCampaigns()).containsOnly(campaignBack);
        assertThat(campaignBack.getAdminUser()).isEqualTo(adminUser);

        adminUser.setCampaigns(new HashSet<>());
        assertThat(adminUser.getCampaigns()).doesNotContain(campaignBack);
        assertThat(campaignBack.getAdminUser()).isNull();
    }
}
