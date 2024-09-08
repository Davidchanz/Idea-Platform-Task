package org.gn.exception;

public class TicketsReadException extends RuntimeException{
    public TicketsReadException(String message){
        super("Error: Read Tickets Exception!\n Message: " + message);
    }
}
