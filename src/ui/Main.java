package ui;

import exceptions.NoCountryIdException;
import exceptions.NoValidFormatException;
import exceptions.RepeatedCityException;
import exceptions.RepeatedCountryException;

import model.Controler;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static Scanner lect= new Scanner(System.in);
    public static Controler controler=new Controler();
    public static void main(String[] args) throws NoCountryIdException, NoValidFormatException, RepeatedCountryException, RepeatedCityException {
        controler.loadData();
        boolean entry=true;
        int answEntry;
        while(entry){
            menu();
            answEntry= lect.nextInt();
            lect.nextLine();
            switch (answEntry){
                case 1:
                    commands();
                    String comand=lect.nextLine();
                    if(comand.contains("SELECT")){
                        String[] parts=comand.split(" ");
                        if(parts[3].equals("countries")){
                            System.out.println(controler.showList(comand));
                        }else if(parts[3].equals("cities")){
                            System.out.println(controler.showList(comand));
                        }
                    } else if (comand.contains("DELETE")) {
                        System.out.println(controler.deleteFromDatabase(comand));
                    }
                    String[] parts = comand.split( "\\(");
                    if(parts[0].equals("INSERT INTO countries" )){
                        System.out.println(controler.insertCountry(parts[2]));
                    }else if(parts[0].equals("INSERT INTO cities")){
                        System.out.println(controler.insertCity(parts[2]));

                    }

                    break;
                case 2:
                    importDataSQL();
                    break;
                case 3:
                    entry=false;
                    break;
            }
        }
    }

    public static void menu(){
        System.out.println("1. Insert command");
        System.out.println("2. Import data from archive SQL");
        System.out.println("3. Get out");
    }

    public static void commands(){
        System.out.println("----------------");
        System.out.println("LIST OF COMMANDS example");
        System.out.println("INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')\n");
        System.out.println("INSERT INTO cities(id, name, countryID, population) VALUES ('e4aa04f6-3dd0-11ed-b878-0242ac120002', 'Cali', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 2.2)");

    }
    public static void importDataSQL() throws NoCountryIdException {

        JOptionPane.showMessageDialog(null, "Elija su un archivo SQL");
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".sql", "Archivo SQL");
        chooser.addChoosableFileFilter(filter);
        int response = chooser.showOpenDialog(null);

        if(response == JFileChooser.APPROVE_OPTION){
            String name = chooser.getSelectedFile().getName();
            String[] arrayName = name.split("\\.");
            if(arrayName[1].equals("sql")){

                File file = chooser.getSelectedFile();
                JOptionPane.showMessageDialog(null, "Archivo guardado satisfactoriamente");
                controler.importSQL(String.valueOf(file));

            } else {
                JOptionPane.showMessageDialog(null, "Tipo de archivo incorrecto");
                importDataSQL();
            }
        }

    }

}
