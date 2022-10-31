package model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import com.google.gson.Gson;
import exceptions.NoCountryIdException;
import exceptions.NoValidFormatException;
import exceptions.RepeatedCityException;
import exceptions.RepeatedCountryException;

public class Controler {
    private ArrayList<Country>arrCountry;
    private ArrayList<City>arrCity;

    public Controler() {
        arrCountry=new ArrayList<>();
        arrCity=new ArrayList<>();

    }

    public String insertCountry(String country) throws NoValidFormatException, RepeatedCountryException{
        String mens="";
        try{
            country=country.replace("(","");
            country=country.replace(")","");
            String[] parts = country.split( ", ");
            String id=parts[0];
            id=id.replace(" ","");
            String name=parts[1];
            double population= Double.parseDouble(parts[2]);
            String countryCode=parts[3];
            if(existCountry(id)){
                throw new RepeatedCountryException();
            }else{
                if(correctFormat(id) && correctFormat(name) && correctFormat(countryCode)){
                    mens="Registered";
                    arrCountry.add(new Country(id, name, population, countryCode));
                }else{
                    throw new NoValidFormatException();
                }
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }


        saveData();
        return mens;
    }
    public String insertCity(String cities) throws NoCountryIdException, NoValidFormatException, RepeatedCityException {
        String mens="";
        try{
            cities = cities.replace(")", "");
            cities = cities.replace("(", "");
            String[] parts = cities.split(", ");
            String id = parts[0];
            id=id.replace(" ","");
            String name = parts[1];
            String countryID = parts[2];
            String[] last = parts[3].split("\\)");
            double population = Double.parseDouble(last[0]);
            if(arrCountry.isEmpty()){
                throw new NoCountryIdException();
            }
            if(existCity(id)){
                throw new RepeatedCityException();
            }else{
                if(correctFormat(id) && correctFormat(name) && correctFormat(countryID) ){
                    if(existCountry(countryID)){
                        mens="Registered";
                        arrCity.add(new City(id,name,countryID,population));
                    }else{
                        throw new NoCountryIdException();
                    }
                }else{
                    throw new NoValidFormatException();
                }
            }

        }catch (NumberFormatException es){
            es.printStackTrace();
        }
        saveData();
        return mens;
    }
    public String showList(String comand) throws NoValidFormatException {
        String[] parts=comand.split(" ");
        String message="";
        try{
            if(parts[3].equals("countries")){
                ArrayList<Country> temp=new ArrayList<>(selectCommandCountry(comand));
                for(Country b:temp){
                    message+=b.getName()+"  "+b.getPopulation()+"  "+b.getId()+"  "+b.getCountryCode()+"\n";
                }
                return message;
            }else if(parts[3].equals("cities")){
                ArrayList<City> temp=new ArrayList<>(selectCommandCity(comand));
                for (City a:temp){
                    message+=a.getName()+"  "+a.getPopulation()+"  "+a.getId()+"  "+a.getCountryID()+"\n";
                }
                return message;
            }
            throw new NoValidFormatException();
        }catch (NoValidFormatException e){
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Country> selectCommandCountry(String command) throws NoValidFormatException {
        if(command.contains("ORDER BY")){
            return orderCountry(command);
        }else{
            String[] parts = command.split(" ");
            if(parts[1].equals("*") && parts[2].equals("FROM")){
                if(parts.length==8){
                    boolean minor=parts[6].equals("<");
                    boolean greater=parts[6].equals(">");
                    boolean iqual=parts[6].equals("=");
                    if(parts[4].equals("WHERE") && minor||greater||iqual){
                        //Este compara Strings
                        if(iqual){
                            if(correctFormat(parts[7])){
                                if(parts[3].equals("countries") && (parts[5].equals("id") || parts[5].equals("name") || parts[5].equals("population") || parts[5].equals("countryID") )){
                                    return selectFromCountry(parts[5],parts[7]);
                                }
                                //El else busca el < , >
                            }
                        }else{
                            int p=Integer.parseInt(parts[7]);
                            if(parts[5].equals("population")){
                                if(parts[6].equals(">")){
                                    return selectFromCountryComparator(p,true);
                                }else{
                                    return selectFromCountryComparator(p,false);
                                }
                            }
                        }
                    }
                }else if(parts.length==4){
                    return selectFromCountryAll();
                }
            }
        }
        throw new NoValidFormatException();
    }
    public ArrayList<City> selectCommandCity(String command) throws NoValidFormatException{
        if(command.contains("ORDER BY")){
            return orderCity(command);
        }else{
            String[] parts=command.split(" ");
            if(parts[1].equals("*") && parts[2].equals("FROM")){
                if(parts.length==8){
                    boolean minor=parts[6].equals("<");
                    boolean greater=parts[6].equals(">");
                    boolean iqual=parts[6].equals("=");
                    if(parts[4].equals("WHERE") && minor||greater||iqual){
                        if(iqual){
                            if(correctFormat(parts[7])){
                                if(parts[3].equals("cities") && (parts[5].equals("id") || parts[5].equals("name") || parts[5].equals("population") || parts[5].equals("countryID") || parts[5].equals("country") )){
                                    return selectFromCity(parts[5],parts[7]);
                                }
                            }
                        }else{
                            int p=Integer.parseInt(parts[7]);
                            if(parts[5].equals("population")){
                                if(parts[6].equals(">")){
                                    return selectFromCityComparator(p,true);
                                }else{
                                    return selectFromCityComparator(p,false);
                                }
                            }
                        }
                    }
                }else if(parts.length==4){
                    //selecciona
                    return selectFromCityAll();

                }

            }

        }
        throw new NoValidFormatException();
    }
    public ArrayList<Country> selectFromCountry(String searchVal,String type){
        ArrayList<Country> list=new ArrayList<>();
        if(searchVal.equals("id")){
            for(Country a:arrCountry){
                if(a.getId().equals(type)){
                    list.add(a);
                }
            }
        }
        if(searchVal.equals("name")){
            for (Country a:arrCountry){
                if(a.getName().equals(type)){
                    list.add(a);
                }
            }
        }
        if(searchVal.equals("population")){
            int b=Integer.parseInt(type);
            for (Country a:arrCountry){
                if(a.getPopulation()==b){
                    list.add(a);
                }
            }
        }
        if(searchVal.equals("countryCode")){
            for(Country a:arrCountry){
                if(a.getCountryCode().equals(type)){
                    list.add(a);
                }
            }
        }
        return list;
    }
    public ArrayList<Country> selectFromCountryComparator(int search,boolean greater){
        ArrayList<Country> list=new ArrayList<>();
        if(greater){
            for (Country a:arrCountry){
                if(a.getPopulation()>search){
                    list.add(a);
                }
            }
        }else{
            for(Country a:arrCountry){
                if(a.getPopulation()<search){
                    list.add(a);
                }
            }
        }
        return list;
    }
    public ArrayList<City> selectFromCityComparator(int search,boolean greater){
        ArrayList<City> list=new ArrayList<>();
        if(greater){
            for(City a:arrCity){
                if(a.getPopulation()>search){
                    list.add(a);
                }
            }
        }else{
            for(City a:arrCity){
                if(a.getPopulation()<search){
                    list.add(a);
                }
            }
        }
        return list;
    }
    public ArrayList<City> selectFromCity(String searchVal,String type){
        ArrayList<City> listCity=new ArrayList<>();
        if(searchVal.equals("id")){
            for (City a:arrCity){
                if(a.getId().equals(type)){
                    listCity.add(a);
                }
            }
        }
        if(searchVal.equals("name")){
            for(City a:arrCity){
                if(a.getName().equals(type)){
                    listCity.add(a);
                }
            }
        }
        if(searchVal.equals("population")){
            int c=Integer.parseInt(type);
            for (City a:arrCity){
                if(a.getPopulation()==c){
                    listCity.add(a);
                }
            }
        }
        if(searchVal.equals("countryID")){
            for(City a:arrCity){
                if(a.getCountryID().equals(type)){
                    listCity.add(a);
                }
            }
        }
        if(searchVal.equals("country")){
            for(Country a:arrCountry){
                for (City b:arrCity){
                    if(a.getName().equals(type) && a.getId().equals(b.getCountryID())){
                        listCity.add(b);
                    }
                }
            }
        }
        return listCity;
    }
    public ArrayList<City> selectFromCityAll(){
        ArrayList<City> temp=new ArrayList<>();
        temp.addAll(arrCity);
        return temp;
    }
    public ArrayList<Country> selectFromCountryAll(){
        ArrayList<Country> temp=new ArrayList<>();
        temp.addAll(arrCountry);

        return temp;
    }
    public ArrayList<Country> orderCountry(String comand) throws NoValidFormatException {
        ArrayList<Country> temp=new ArrayList<>();
        String[] parts=comand.split("ORDER BY");
        temp=selectCommandCountry(parts[0]);
        parts[1]=parts[1].replace(" ","");
        if(parts[1].equals("name") || parts[1].equals("id") || parts[1].equals("population") || parts[1].equals("countryCode")){
            if(parts[1].equals("id")){
                temp.sort(Comparator.comparing(Country::getId));
            }else if(parts[1].equals("name")){
                temp.sort(Comparator.comparing(Country::getName));
            }else if(parts[1].equals("population")){
                temp.sort(Comparator.comparing(Country::getPopulation));
            }else {
                temp.sort(Comparator.comparing(Country::getCountryCode));
            }
            return temp;
        }
        throw new NoValidFormatException();
    }
    public ArrayList<City> orderCity(String comand) throws NoValidFormatException {
        ArrayList<City> temp;
        String[] parts=comand.split("ORDER BY");
        temp=selectCommandCity(parts[0]);
        parts[1]=parts[1].replace(" ","");
        if(parts[1].equals("name") || parts[1].equals("id") || parts[1].equals("countryID") || parts[1].equals("population") || parts[1].equals("country")){
            if(parts[1].equals("id")){
                temp.sort(Comparator.comparing(City::getId));
            }else if(parts[1].equals("name")){
                temp.sort(Comparator.comparing(City::getName));
            }else if(parts[1].equals("population")){
                temp.sort(Comparator.comparing(City::getPopulation));
            }else if(parts[1].equals("countryID")){
                temp.sort(Comparator.comparing(City::getCountryID));
            }else if(parts[1].equals("country")){
                temp.sort(Comparator.comparing(City::getCountryID));
            }
            return temp;
        }
        throw new NoValidFormatException();

    }
    public String deleteFromDatabase(String comand) throws NoValidFormatException {
        String delete=comand.replace("DELETE","SELECT *");
        String[] parts=comand.split(" ");
        try{
            if(parts[2].equals("countries")){
                ArrayList<Country> list=new ArrayList<>(selectCommandCountry(delete));
                arrCountry.removeAll(list);
                saveData();
                return "All the countries that meet these conditions were eliminated";
            }if(parts[2].equals("cities")){
                ArrayList<City> list=new ArrayList<>(selectCommandCity(delete));
                arrCity.removeAll(list);
                saveData();
                return "All the cities that meet these conditions where eliminated";
            }
            throw new NoValidFormatException();
        }catch (NoValidFormatException ex){
            ex.printStackTrace();
        }
        return "No values found that meet these requirements";
    }
    public boolean existCountry(String id){
        for (Country a:arrCountry){
            if(a.getId().equals(id)){
                return true;
            }
        }
        return false;
    }
    public boolean existCity(String id){
        for (City a:arrCity){
            if(a.getId().equals(id)){
                return true;
            }
        }
        return false;
    }
    public boolean correctFormat(String a){
        return a.charAt(0)=='\'' && a.charAt(a.length()-1)=='\'';
    }
    public void importSQL(String path) throws NoCountryIdException {
        File file=new File(path);
        try{
            FileInputStream fis=new FileInputStream(file);
            BufferedReader reader= new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line=reader.readLine())!=null){
                String[] part=line.split("VALUES");
                if(part[0].equals("INSERT INTO countries(id, name, population, countryCode) ")){
                    insertCountry(part[1]);
                }else if(part[0].equals("INSERT INTO cities(id, name, countryID, population) ")) {
                    insertCity(part[1]);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        saveData();
    }
    public void loadData(){
        try {
            File file=new File("Countries.json");
            FileInputStream fis=new FileInputStream(file);
            BufferedReader reader= new BufferedReader(new InputStreamReader(fis));
            String json="";
            String line;
            while ((line=reader.readLine())!=null){
                json+=line;
            }
            fis.close();
            Gson gson=new Gson();
            Country[] country=gson.fromJson(json, Country[].class);
            if(country!=null){
                arrCountry.addAll(Arrays.asList(country));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            File file=new File("Cities.json");
            FileInputStream fis=new FileInputStream(file);
            BufferedReader reader= new BufferedReader(new InputStreamReader(fis));
            String json="";
            String line;
            while ((line=reader.readLine())!=null){
                json+=line;
            }
            fis.close();
            Gson gson=new Gson();
            City[] cities=gson.fromJson(json,City[].class);
            if(cities!=null){
                Collections.addAll(arrCity, cities);
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void saveData(){
        Gson gson=new Gson();
        try {
            FileOutputStream fos = new FileOutputStream(new File("Countries.json"));
            String json=gson.toJson(arrCountry);
            fos.write(json.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            FileOutputStream fos = new FileOutputStream(new File("Cities.json"));
            String json=gson.toJson(arrCity);
            fos.write(json.getBytes(StandardCharsets.UTF_8));
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public Country searchCountry(String id){
        for (Country c:arrCountry){
            if(c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }
    public String searchCity(String id){
            for (int i = 0; i < arrCountry.size(); i++) {
                if(arrCity.get(i).getId().equals(id)){
                    String z=arrCity.get(i).getId();
                    return z;
                }
            }
        return null;
    }
    public String listCities(String id){
        String mens="";
        for(City c:arrCity){
            if(c.getCountryID().equals(id)){
                mens +=c.toString()+",";
            }
        }
        return mens;
    }
    public String countryPopulation(){
        String mens="";
        for (int i = 0; i < arrCountry.size(); i++) {
            if(arrCountry.get(i).getPopulation()!=0) {
                mens += arrCountry.get(i).getName()+",";

            }
        }
        return mens;
    }
}
