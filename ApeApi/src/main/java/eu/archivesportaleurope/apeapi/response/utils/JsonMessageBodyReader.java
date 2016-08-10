package eu.archivesportaleurope.apeapi.response.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author M.Mozadded
 */
@Provider
@Produces({ServerConstants.APE_API_V1, "application/json"})
public class JsonMessageBodyReader implements MessageBodyReader {
    @Override
    public boolean isReadable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        Object result;
        result = new ObjectMapper().readValue(entityStream, type);
        return result;
    }
}
