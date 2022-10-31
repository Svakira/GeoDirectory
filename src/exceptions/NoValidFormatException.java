package exceptions;

public class NoValidFormatException extends RuntimeException{
    public NoValidFormatException(){
        super("The format of the parameter is wrong");
    }
}
