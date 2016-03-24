/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.exceptions;

/**
 *
 * @author kaisar
 */
public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(String message, String developerMessage) {
        super(400, 400, message, developerMessage, "");
    }

}
