/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.exceptions;

/**
 *
 * @author M.Mozadded
 */
public class UnauthorizeUserException extends AppException {

    public UnauthorizeUserException(String developerMessage) {
        super(401, 400, "You are not authorized to use this service.", developerMessage, "");
    }
    
    public UnauthorizeUserException(String message, String developerMessage) {
        super(401, 400, message, developerMessage, "");
    }
}
