package com.pac.project.presenter.GestioneUtenze.exceptions;

public class ExpiredTokenException extends Exception {
    public ExpiredTokenException() {
        super("This link has expired, please ask for another mail");
    }
}
