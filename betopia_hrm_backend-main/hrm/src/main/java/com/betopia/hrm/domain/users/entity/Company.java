package com.betopia.hrm.domain.users.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.ZoneId;
import java.util.Currency;
import java.util.Objects;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "companies")
public class Company extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "division_id")
    private Integer divisionId;

    @Column(name = "district_id")
    private Integer districtId;

    @Column(name = "thana")
    private Integer thana;

    @Column(name = "post_office")
    private Integer postOffice;

    @Column(name = "short_name", length = 100)
    private String shortName;

    @Column(name = "code", length = 100)
    private String code;

    @Column(name = "phone", length = 25)
    private String phone;

    @Column(name = "email", length = 55)
    private String email;

    @Column(name = "website_url", length = 55)
    private String websiteUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "image", length = 100)
    private String image;

    @Column(name = "image_url", length = 100)
    private String imageUrl;

    @Column(name = "status", length = 10)
    private Boolean status ;

    @Column(name = "currency", length = 10)
    private Currency currency;

    @Column(name = "time_zone", length = 30)
    private ZoneId timeZone;

    @PrePersist
    @PreUpdate
    public void generateSlug()
    {
        if (this.name != null)
        {
            this.slug = slugify(this.name);
        }
    }

    private String slugify(String input)
    {
        return input.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getThana() {
        return thana;
    }

    public void setThana(Integer thana) {
        this.thana = thana;
    }

    public Integer getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(Integer postOffice) {
        this.postOffice = postOffice;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getStatus() {
        return status;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) && Objects.equals(name, company.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
