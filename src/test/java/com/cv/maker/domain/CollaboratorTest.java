package com.cv.maker.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cv.maker.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CollaboratorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Collaborator.class);
        Collaborator collaborator1 = new Collaborator();
        collaborator1.setId("id1");
        Collaborator collaborator2 = new Collaborator();
        collaborator2.setId(collaborator1.getId());
        assertThat(collaborator1).isEqualTo(collaborator2);
        collaborator2.setId("id2");
        assertThat(collaborator1).isNotEqualTo(collaborator2);
        collaborator1.setId(null);
        assertThat(collaborator1).isNotEqualTo(collaborator2);
    }
}
