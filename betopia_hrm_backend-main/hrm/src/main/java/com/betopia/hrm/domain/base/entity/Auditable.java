package com.betopia.hrm.domain.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<ID, VERSION> /*implements Externalizable*/ {

    @CreatedDate @Column(name = "created_at")
    LocalDateTime createdDate;

    @LastModifiedDate  @Column(name = "updated_at")
    LocalDateTime lastModifiedDate;

    @AttributeOverride(name = "username", column = @Column(name = "created_by"))
    @CreatedBy
    Long createdBy;

    @AttributeOverride(name = "username", column = @Column(name = "last_modified_by"))
    @LastModifiedBy
    Long lastModifiedBy;

    @Version
    @JsonIgnore
    private VERSION version;

    public VERSION getVersion() {
        return version;
    }

    public void setVersion(VERSION version) {
        this.version = version;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    //@Override
//    public void writeExternal(ObjectOutput out) throws IOException {
//        out.writeObject(marshallingToMap(true));
//    }
//
//    @Override
//    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//        Map<String, Object> data = (Map<String, Object>) in.readObject();
//        unmarshallingFromMap(data, true);
//    }
//
//    @Override @JsonIgnore
//    public Map<String, Object> get_links() {
//        return super.get_links();
//    }
}
