package main.controllers;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.controllers.common.AbstractController;

@RestController
public class HelpController extends AbstractController {
	
	private final Class<?> [] controllerClasses = new Class<?> [] { CommandController.class, HelpController.class, InfosController.class }; 
	
	
	@GetMapping(value = "/help", produces = {"application/json"})
    public String GetMethodsList(@RequestParam(value = "token") String token, HttpServletResponse response) throws AuthenticationException {
		String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;

    	ArrayList<String> wsMethods = new ArrayList<String>();
    	for (Class<?> controllerClass : this.controllerClasses) {
    		wsMethods.addAll(this.getMethodDescriptionList(controllerClass));
		}
    	    
    	for(int i = 0; i < wsMethods.size(); i++) {
    		wsMethods.set(i, "\"WS_Method n° "+i+"\": " + wsMethods.get(i));
    	}
    	
    	return "{" + String.join(",", wsMethods) + "}";
    }
	
	
	
	private ArrayList<String> getMethodDescriptionList(Class<? extends Object> controllerClass) {
		ArrayList<String> wsMethods = new ArrayList<String>();
		
		Method [] methodsOfControllerClass = controllerClass.getDeclaredMethods();
    	for (Method method : methodsOfControllerClass) {
    		GetMapping getAnnotation = method.getAnnotation(GetMapping.class);
    		PostMapping postAnnotation = method.getAnnotation(PostMapping.class);
    		RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
    		
    		
    		String paramInfos = "";
    		for (Class<?> classParameters : method.getParameterTypes()) {
    			if(paramInfos.isEmpty())
    				paramInfos = classParameters.getSimpleName();
    			else
    				String.join(", ", paramInfos, classParameters.getSimpleName());
			}
    		
    		String methodDetails = null;    		
    		if(getAnnotation != null) {
    			methodDetails = String.format("\"[GET] - %s    -   (paramsTypes: %s)\"", getAnnotation.value()[0], paramInfos);
    		}else if(postAnnotation != null) {
    			methodDetails = String.format("\"[POST] - %s    -   (paramsTypes: %s)\"", postAnnotation.value()[0], paramInfos);
    		}else if(requestMappingAnnotation != null) {
    			String methodTypeName = requestMappingAnnotation.method()[0].name();
    			methodDetails = String.format("\"[%s] - %s    -   (paramsTypes: %s)\"", methodTypeName, requestMappingAnnotation.value()[0], paramInfos);
    		}
    		
    		if(methodDetails != null)
    			wsMethods.add(methodDetails);
		}
    	
    	return wsMethods;
	}
	
}
