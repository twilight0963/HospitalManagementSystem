package org.HospitalSystem.Classes.Exceptions;

public class AuthError extends Exception{
    public AuthError(){
        super("Failed to authenticate! Please try again.");
    }
}
