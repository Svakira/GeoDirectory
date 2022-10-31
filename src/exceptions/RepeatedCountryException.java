package exceptions;

public class RepeatedCountryException extends RuntimeException{
    public RepeatedCountryException(){
        super("This country already exist in the database ");
    }
}
