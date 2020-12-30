package com.lib;

import com.thoughtworks.selenium.SeleniumException;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.ErrorHandler;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.remote.UnreachableBrowserException;
import java.sql.SQLException;

/**
 * A generic exception handling class used across automation framework.
 *
 * @version 0.1  -
 *
 *  <font size="-2"> <br><br>
 *
 * </br> </br> </font>
 */

public class ExceptionHandler extends Exception {

    // Define logger
    protected Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * Returns the character at the specified index. An index
     * ranges from <code>0</code> to <code>length() - 1</code>.
     *
     * @param     e the exception
     * @param     sMethodName Name of the method in which exception caught.
     * @return    Nothing.
     *
     */
    public ExceptionHandler(Exception e,String sMethodName)  {
       try{
        log.error("Exception caught in Method: " + sMethodName );
        if ( e instanceof SQLException){
            //log.error("SQL Exception " + e.getMessage());
        }
        else if(e instanceof SeleniumException)
        {
            log.error("Selenium Exception " + e.getMessage());
        }
        else if (e instanceof NoSuchMethodException)
        {
            log.error("No Such Method Exception " + e.getMessage());
        }
        else  if (e instanceof ErrorHandler.UnknownServerException)
        {
            log.error("Unknown server Exception " + e.getMessage());
        }
        else if (e instanceof UnreachableBrowserException)
        {
            log.error("Unreachable Browser Exception "+ e.getMessage());
        }
        else if ( e instanceof org.openqa.selenium.NoSuchElementException)
        {
            log.error("No Such Element Exception "+ e.getMessage());
        }

       }
       catch (Exception eH){

       }
    }


}
