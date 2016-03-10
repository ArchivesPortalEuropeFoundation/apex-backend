/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.transaction.entity.nosql;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Id;

/**
 *
 * @author mahbub
 */
public class BaseEntityNoSql {

    private Date date;

    @Id
    private String id;

    public BaseEntityNoSql() {
        this(UUID.randomUUID().toString());
    }

    public BaseEntityNoSql(String id) {
        if (id == null || id.isEmpty()) {
            throw new AssertionError("Id can not be empty");
        }
        this.id = id;
        this.date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
