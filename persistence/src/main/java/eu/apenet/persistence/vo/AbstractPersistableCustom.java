/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.vo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import org.springframework.data.domain.Persistable;

/**
 * Abstract base class for entities. Which will generate Table Id independent 
 * from each other.
 * 
 * Inspired by Spring Data JPA's AbstractPersistable implementation.
 *
 * @author Mozadded
 * @param <PK>
 */
@MappedSuperclass
public abstract class AbstractPersistableCustom<PK extends Serializable> implements Persistable<PK> {

    private static final long serialVersionUID = -5554308939380869754L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "SequenceIdGenerator", sequenceName = "pk_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SequenceIdGenerator")
    private PK id;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Persistable#getId()
     */
    @Override
    public PK getId() {
        return id;
    }

    /**
     * Sets the id of the entity.
     *
     * @param id the id to set
     */
    public void setId(final PK id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.domain.Persistable#isNew()
     */
    @Override
    public boolean isNew() {

        return getId()==null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s", getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        AbstractPersistableCustom that = (AbstractPersistableCustom) obj;

        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += getId()==null ? 0 : getId().hashCode() * 31;

        return hashCode;
    }
}
