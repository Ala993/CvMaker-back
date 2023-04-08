package com.cv.maker.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cv.maker.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HumanResourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HumanResource.class);
        HumanResource humanResource1 = new HumanResource();
        humanResource1.setId("id1");
        HumanResource humanResource2 = new HumanResource();
        humanResource2.setId(humanResource1.getId());
        assertThat(humanResource1).isEqualTo(humanResource2);
        humanResource2.setId("id2");
        assertThat(humanResource1).isNotEqualTo(humanResource2);
        humanResource1.setId(null);
        assertThat(humanResource1).isNotEqualTo(humanResource2);
    }
}
