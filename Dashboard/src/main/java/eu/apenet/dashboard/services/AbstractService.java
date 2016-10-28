/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services;

import eu.apenet.commons.types.XmlType;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author mahbub
 */
public abstract class AbstractService {
    public abstract void validate(XmlType xmlType, Integer id) throws IOException;
    public abstract void convert(XmlType xmlType, Integer id, Properties properties) throws IOException;
    public abstract void publish(XmlType xmlType, Integer id) throws Exception;
    public abstract void unpublish(XmlType xmlType, Integer id) throws Exception;
    public abstract void delete(XmlType xmlType, Integer id) throws Exception;
    public abstract File download(XmlType xmlType, Integer id);
}
