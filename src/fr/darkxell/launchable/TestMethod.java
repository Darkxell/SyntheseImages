package fr.darkxell.launchable;

import java.lang.reflect.Method;

public class TestMethod {

	public String functionToPass(String message) {
        String [] split = message.split(" ");
        for (int i=0; i<split.length; i++)
            System.out.println(split[i]);
        return "return value";
    }
    
    public Object outerFunction(Object object, Method method, String message) throws Exception {
        Object[] parameters = new Object[1];
        parameters[0] = message;
        return method.invoke(object, parameters);
    }

    public static void main(String[] args) throws Exception{
        @SuppressWarnings("rawtypes")
		Class[] parameterTypes = new Class[1];
        parameterTypes[0] = String.class;
        Method functionToPass = TestMethod.class.getMethod("functionToPass", parameterTypes[0]);
        TestMethod main = new TestMethod();
        System.out.println(main.outerFunction(main, functionToPass, "This is the input"));
    }
	
}
