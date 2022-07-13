package com.pac.project.presenter.GestioneUtenze.exceptions;

public class AlreadyUsedTokenException extends Exception {
    public AlreadyUsedTokenException() {
        super("This link has already been used, please ask for another mail");
    }
}
