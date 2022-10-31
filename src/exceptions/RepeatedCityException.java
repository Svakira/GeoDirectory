package exceptions;

public class RepeatedCityException extends RuntimeException{
    public RepeatedCityException(){
        super("This city already exist in the database");
    }
}
