package com.cv.maker.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Cv.
 */
@Document(collection = "cv")
public class Cv implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field
    private String title;


    @Field("address")
    private String address;

    @Field("phone_number")
    private Long phoneNumber;

    @Field("postal_code")
    private Integer postalCode;

    @Field("email")
    private String email;

    @Field("skills")
    @DBRef
    private List<Skill> skills;

    @Field("experiences")
    @DBRef
    private List<Experience> experiences;

    @DBRef
    @JsonIgnoreProperties(value = {"cv"})
    private Collaborator collaborator;

    @Field
    @DBRef
    private List<Study> studies;

    @Field
    @DBRef
    private FileEntry image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
// jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Cv id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public Cv address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getPhoneNumber() {
        return this.phoneNumber;
    }

    public Cv phoneNumber(Long phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getPostalCode() {
        return this.postalCode;
    }

    public Cv postalCode(Integer postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return this.email;
    }

    public Cv email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    public List<Study> getStudies() {
        return studies;
    }

    public void setStudies(List<Study> studies) {
        this.studies = studies;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public FileEntry getImage() {
        return image;
    }

    public void setImage(FileEntry image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cv)) {
            return false;
        }
        return id != null && id.equals(((Cv) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cv{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            ", postalCode=" + getPostalCode() +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
