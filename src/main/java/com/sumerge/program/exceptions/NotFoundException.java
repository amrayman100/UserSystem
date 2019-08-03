package com.sumerge.program.exceptions;

import javax.ws.rs.core.Response;
import java.io.Serializable;

public class NotFoundException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super();
    }
    public NotFoundException(String msg)   {
        super(msg);
    }
    public NotFoundException(String msg, Exception e , Response.Status s)  {
        super(msg, e);
    }
}
