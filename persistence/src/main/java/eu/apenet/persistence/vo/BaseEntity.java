/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.vo;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author m.mozadded
 */
@MappedSuperclass
public abstract class BaseEntity extends AbstractPersistableCustom<Long> {

    public static final String STATUS_CREATED = "created";
    public static final String STATUS_DELETED = "deleted";
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_EXPIRED = "expired";
    public static final String STATUS_ASSIGNED = "doorgeleid";

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    private Date version;

    /**
     * All objects will have a unique UUID which allows for the decoupling from
     * DB generated ids
     *
     */
    @Column(length = 36)
    private String uuid;

    private Date creationTime;

    // @NotNull
    private String status;

    public BaseEntity() {
        this(UUID.randomUUID());
    }

    public BaseEntity(UUID guid) {
        if (guid == null) {
            throw new AssertionError("UUID is required", null);
        }
        setUuid(guid.toString());
        this.creationTime = new Date();
        this.status = STATUS_CREATED;
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BaseEntity other = (BaseEntity) obj;
        return Objects.equals(this.hashCode(), other.hashCode());
    }

    public String getIdentifier() {
        return getUuid().toString();
    }

    public Date getVersion() {
        return version;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
