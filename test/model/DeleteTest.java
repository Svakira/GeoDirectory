package model;
import exceptions.NoCountryIdException;
import exceptions.NoValidFormatException;
import exceptions.RepeatedCityException;
import exceptions.RepeatedCountryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteTest {

    private Controler controler;
    //Scenario 1
    public void setUpStage1() throws RepeatedCountryException, NoValidFormatException, NoCountryIdException, RepeatedCityException {
        controler=new Controler();
        controler.insertCountry("'6ec3e8ec-vd-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')");
        controler.insertCountry("'6ec3e8ec-vd-11ed-b878-0242ac1202', 'lll', 50.98, '+57')");
        controler.insertCountry("'6ec3e8ec-vd-11ed-b878-0242ac12', 'Ecuador', 0, '+57')");
        controler.insertCity("'e4aa04f6-3dd0-11ed-b878-0242ac120002', 'Cali', '6ec3e8ec-vd-11ed-b878-0242ac120002', 2.2)");
        controler.insertCity("'e4aa04f6-3dd0-11ed-b878-0242ac120003', 'Medellin', '6ec3e8ec-vd-11ed-b878-0242ac120002', 2.3)");
        controler.insertCity("'e4aa04f6-3dd0-11ed-b878-0242ac120004', 'Bogota', '6ec3e8ec-vd-11ed-b878-0242ac120002', 2.4)");
        controler.insertCity("'e4aa04f6-3dd0-11ed-b878-0242ac120005', 'Pasto', '6ec3e8ec-vd-11ed-b878-0242ac120002', 2.5)");
        controler.insertCity("'e4aa04f6-3dd0-11ed-b878-0242ac120006', 'Pereira', '6ec3e8ec-vd-11ed-b878-0242ac120002', 2.6)");
    }


    @Test
    public void deleteByIdTest() throws RepeatedCountryException, NoCountryIdException, NoValidFormatException, RepeatedCityException {
        setUpStage1();
        controler.deleteFromDatabase("DELETE FROM cities WHERE id = 'e4aa04f6-3dd0-11ed-b878-0242ac120002'");
        assertFalse(controler.existCity("'e4aa04f6-3dd0-11ed-b878-0242ac120002'"));
        controler.insertCity("'e4aa04f6-3dd0-11ed-b878-0242ac120002', 'Cali', '6ec3e8ec-vd-11ed-b878-0242ac120002', 2.2)");
        controler.deleteFromDatabase("DELETE FROM countries WHERE name = 'Colombia'");
        controler.deleteFromDatabase("DELETE FROM countries WHERE name = 'lll'");
        controler.deleteFromDatabase("DELETE FROM countries WHERE name = 'Ecuador'");
        controler.deleteFromDatabase("DELETE FROM cities WHERE name = 'Cali'");
        controler.deleteFromDatabase("DELETE FROM cities WHERE name = 'Medellin'");
        controler.deleteFromDatabase("DELETE FROM cities WHERE name = 'Bogota'");
        controler.deleteFromDatabase("DELETE FROM cities WHERE name = 'Pasto'");
        controler.deleteFromDatabase("DELETE FROM cities WHERE name = 'Pereira'");
    }

    @Test
    public void deleteCitiesTest() throws NoCountryIdException, NoValidFormatException, RepeatedCountryException, RepeatedCityException {
        setUpStage1();
        controler.deleteFromDatabase("DELETE FROM cities WHERE country = 'Colombia'");
        assertFalse(controler.existCity("'e4aa04f6-3dd0-11ed-b878-0242ac120002'"));
        assertFalse(controler.existCity("'e4aa04f6-3dd0-11ed-b878-0242ac120003'"));
        assertFalse(controler.existCity("'e4aa04f6-3dd0-11ed-b878-0242ac120004'"));
        assertFalse(controler.existCity("'e4aa04f6-3dd0-11ed-b878-0242ac120005'"));
        assertFalse(controler.existCity("'e4aa04f6-3dd0-11ed-b878-0242ac120006'"));
        controler.deleteFromDatabase("DELETE FROM countries WHERE name = 'Colombia'");
        controler.deleteFromDatabase("DELETE FROM countries WHERE name = 'lll'");
        controler.deleteFromDatabase("DELETE FROM countries WHERE name = 'Ecuador'");
        controler.deleteFromDatabase("DELETE FROM cities WHERE name = 'Cali'");
        controler.deleteFromDatabase("DELETE FROM cities WHERE name = 'Medellin'");
        controler.deleteFromDatabase("DELETE FROM cities WHERE name = 'Bogota'");
        controler.deleteFromDatabase("DELETE FROM cities WHERE name = 'Pasto'");
        controler.deleteFromDatabase("DELETE FROM cities WHERE name = 'Pereira'");


    }


}