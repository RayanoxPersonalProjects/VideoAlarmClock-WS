package main.controllers.common;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;

import main.authentication.ServerTokenProvider;
import main.model.ErrorBuilder;
import main.providers.DataStorage;
import main.utils.Converter;

public class AbstractController {

	@Autowired
	ServerTokenProvider serverTokenProvider;

	@Autowired
	protected DataStorage dataStorage;
	
	protected String formatStringException(Exception e) {
    	String stringStackTrace;
    	if(e.getCause() != null)
    		stringStackTrace = Converter.convertStacktraceToString(e.getCause().getStackTrace());
    	else
    		stringStackTrace = Converter.convertStacktraceToString(e.getStackTrace());
    	
    	return e.getMessage() + "       -----||-----     " + e.getClass().getName() + " ----- " + stringStackTrace;
    }
    
    protected String processAuthorization(String token) throws AuthenticationException {
    	if(!serverTokenProvider.AuthorizeRequestToken(token)) {
    		return ErrorBuilder.buildError("The token does not equal the server-side token.");
    	}else {
    		return null;
    	}
    }
	
}
