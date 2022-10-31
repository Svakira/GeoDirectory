package model;

import exceptions.NoCountryIdException;
import exceptions.NoValidFormatException;
import exceptions.RepeatedCityException;
import exceptions.RepeatedCountryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InsertTest {
    Controler controler;

    //Scenario 1
    public void setUpStage1() throws RepeatedCountryException, NoValidFormatException {
        controler=new Controler();
        controler.insertCountry("'6ec3e8ec-vd-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')");
    }


    @Test
    public void registerCountryTest() throws NoValidFormatException, RepeatedCountryException {
        setUpStage1();
        controler.insertCountry("'6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')");
        boolean flag=false;
        Country country=controler.searchCountry("'6ec3e8ec-3dd0-11ed-b878-0242ac120002'");
        if(country.getId().equals("'6ec3e8ec-3dd0-11ed-b878-0242ac120002'")){
            flag=true;
        }
        assertTrue(flag);
        controler.deleteFromDatabase("DELETE FROM countries WHERE name = 'Colombia'");
    }

    @Test
    public void registerCityTest() throws NoCountryIdException, NoValidFormatException, RepeatedCountryException, RepeatedCityException {
        setUpStage1();
        Controler controler=new Controler();
        controler.insertCountry("'6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')");
        controler.insertCity("'e4aa04f6-3dd0-11ed-b878-0242ac120002', 'Cali', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 2.2)");
        boolean flag=false;
        String city=controler.searchCity("'e4aa04f6-3dd0-11ed-b878-0242ac120002'");
        if(city.equals("'e4aa04f6-3dd0-11ed-b878-0242ac120002'")){
            flag=true;
        }
        assertTrue(flag);
        controler.deleteFromDatabase("DELETE FROM countries WHERE name = 'Colombia'");
        controler.deleteFromDatabase("DELETE FROM cities WHERE name = 'Cali'");
    }

    @Test
    public void repeatedCountryIdTest() throws RepeatedCountryException, NoValidFormatException {
        setUpStage1();
        Controler controler=new Controler();
        boolean flag=false;
        try {
            controler.insertCountry("'6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')");
            controler.insertCountry("'6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Ecuador', 75.2, '+66')");
        }catch (RepeatedCountryException e){
            flag=true;
        }
        assertTrue(flag);

    }

    @Test
    public void noCountryFoundTest() throws RepeatedCountryException, NoValidFormatException {
        setUpStage1();
        Controler controler=new Controler();
        boolean flag=false;
        try {
            controler.insertCountry("'6ec3e8ec-3dd0-11ed-b878-0242ac120', 'Ecuador', 75.2, '+66')");
            controler.insertCity("'e4aa04f6-3dd0-11ed-b878-0242ac120002', 'Cali', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 2.2)");
        }catch (RepeatedCountryException | NoValidFormatException | RepeatedCityException ex){
            flag=true;
        } catch (NoCountryIdException  e) {
            flag=true;
        }
        assertTrue(flag);
        controler.deleteFromDatabase("DELETE FROM countries WHERE name = 'Ecuador'");
        controler.deleteFromDatabase("DELETE FROM cities WHERE name = 'Cali'");
    }


}
