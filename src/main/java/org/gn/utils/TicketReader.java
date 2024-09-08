package org.gn.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gn.exception.TicketsReadException;
import org.gn.task.Tickets;

import java.io.File;
import java.io.IOException;

public class TicketReader {

    private ObjectMapper objectMapper;

    public TicketReader(){
        this.objectMapper = new ObjectMapper();
    }
    public Tickets readTickets(String path){
        File jsonFile = new File(path);
        if (!jsonFile.isFile()){
            throw new TicketsReadException("Tickets path '"+ path +"' is invalid!");
        }

        try {
            return this.objectMapper.readValue(jsonFile, Tickets.class);
        } catch (IOException e) {
            throw new TicketsReadException("Tickets File is invalid!");
        }
    }

}
