package exceptions;

public class NoCountryIdException extends RuntimeException{
    public NoCountryIdException(){
        super("There is no country with that id");
    }
}
