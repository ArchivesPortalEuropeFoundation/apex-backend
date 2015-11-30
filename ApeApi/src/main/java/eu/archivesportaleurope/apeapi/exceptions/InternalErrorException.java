/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.exceptions;

/**
 *
 * @author m.mozadded
 */
public class InternalErrorException extends AppException {

    public InternalErrorException() {
        super(500, 501, "Internal server error", "", "");
    }
    
    public InternalErrorException(String developerMessage) {
        super(500, 501, "Internal server error", developerMessage, "");
    }

    public InternalErrorException(String message, String developerMessage) {
        super(500, 501, message, developerMessage, "");
    }
    
}
