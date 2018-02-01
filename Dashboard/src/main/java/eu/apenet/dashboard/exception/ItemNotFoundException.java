/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.exception;

/**
 *
 * @author mahbub
 */
public class ItemNotFoundException extends Exception {

    private static final long serialVersionUID = -8579602666806773471L;

    public ItemNotFoundException() {
        super();
    }

    public ItemNotFoundException(String mensaje) {
        super(mensaje);
    }

    public ItemNotFoundException(Throwable cause) {
        super(cause);
    }

    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
