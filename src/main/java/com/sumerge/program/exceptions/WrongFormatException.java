package com.sumerge.program.exceptions;

import javax.ws.rs.core.Response;
import java.io.Serializable;

public class WrongFormatException  extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public WrongFormatException() {
        super();
    }
    public WrongFormatException(String msg)   {
        super(msg);
    }
    public WrongFormatException(String msg, Exception e , Response.Status s)  {
        super(msg, e);
    }
}
