package test;

import com.accesodatos.Address;
import com.accesodatos.AddressBook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.AccessException;
import java.util.Scanner;

public class AddressBookTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean stopMenuLoop = false;
        AddressBook addressBook = new AddressBook();


        while(!stopMenuLoop){
            //show the option menu
            showMenu();
            //get user option input
            System.out.print("# ");
            String userSelection = sc.nextLine();

            // manage user input.
            switch(userSelection){

                case "1":
                    try {
                        addressBook.open();
                        int id = addressBook.getNumRecords();
                        if (id == 0) id =1;

                        System.out.print("Introduzca el Nombre: ");
                        String addressName = sc.nextLine();

                        System.out.print("Introduzca el Telefono: ");
                        String addressPhone = sc.nextLine();

                        System.out.print("Introduzca el Email: ");
                        String addressEmail = sc.nextLine();

                        System.out.print("Introduzca la Edad: ");
                        int addressAge = Integer.parseInt(sc.nextLine());

                        Address addressToAdd = new Address(id, addressName, addressPhone, addressEmail, addressAge);

                        addressBook.append(addressToAdd);

                        addressBook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case "2":
                    try {
                        addressBook.open();
                        System.out.println("Introduzca el ID :");
                        System.out.print("# ");
                        int userInputId = Integer.parseInt(sc.nextLine());
                        int size = addressBook.getNumRecords();
                        addressBook.close();
                        System.out.println(size);
                        if (userInputId < 0 || userInputId > size){
                            throw new IndexOutOfBoundsException("ID excede del número de registros");
                        }
                        addressBook.open();
                        Address addressSelected = addressBook.select((userInputId));
                        showOneAddress(addressSelected);
                        addressBook.close();

                        System.out.print("Introduzca el Nombre: ");
                        String addressName = sc.nextLine().trim();

                        System.out.print("Introduzca el Telefono: ");
                        String addressPhone = sc.nextLine().trim();

                        System.out.print("Introduzca el Email: ");
                        String addressEmail = sc.nextLine().trim();

                        System.out.print("Introduzca la Edad: ");
                        String addressAgeString = sc.nextLine().trim();

                        if (!addressName.equals(""))  addressSelected.setName(addressName);
                        if (!addressAgeString.equals("")) addressSelected.setAge(Integer.parseInt(addressAgeString));
                        if (!addressPhone.equals("")) addressSelected.setPhone(addressPhone);
                        if (!addressEmail.equals("")) addressSelected.setEmail(addressEmail);

                        addressBook.open();
                        addressBook.update(addressSelected);
                        addressBook.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case  "3":
                    try {
                        addressBook.open();
                        System.out.println("Introduzca el ID :");
                        System.out.print("# ");
                        int id = Integer.parseInt(sc.nextLine());
                        Address addressSelected = addressBook.select((id));
                        showOneAddress(addressSelected);
                        System.out.print("Presione Enter para continuar... ");
                        sc.nextLine();
                        addressBook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case "4":
                    break;

                case "5":
                    break;

                case "6":
                    break;

                case "7":
                    //close the program
                    System.out.println("Programa Finalizado..");
                    stopMenuLoop = true;
                    sc.close();
                    break;
                default:
                    System.out.println("Porfavor selecciona una de las opciones Mencionadas");
            }
        }

    }

    private static void showMenu (){
        String asciiTable[] = {"┌", "┐", "└", "┘", "│", "─"};
        String option[] = {
                "1. Añadir nuevo registro",
                "2. Modificar registro",
                "3. Buscar registro",
                "4. Listar agenda",
                "5. Importar desde XML",
                "6. Exportar a XML",
                "7. Terminar programa"
        };

        System.out.println(asciiTable[0] + asciiTable[5] + " Opciones " + asciiTable[5].repeat(19) + asciiTable[1]);

        for (int i = 0; i < option.length; i++) {
            System.out.println("│ " + padingRight(option[i], 29) + asciiTable[4]);
        }
        System.out.println(asciiTable[2] + asciiTable[5].repeat(30) + asciiTable[3]);
    }

    private static String padingRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private static  void showOneAddress(Address address){
        String asciiTable[] = {"┌", "┐", "└", "┘", "│", "─"};

        System.out.println(asciiTable[0] + asciiTable[5].repeat(43) + asciiTable[1]);
        System.out.println(asciiTable[4] + " ID" + " ".repeat(7) + ": " + address.getId() + " ".repeat(29) + asciiTable[4]);
        System.out.println(asciiTable[4] + " Nombre"+ " ".repeat(3) + ": " + address.getName() + " " .repeat(6)+ asciiTable[4]);
        System.out.println(asciiTable[4] + " Telefono : " + address.getPhone() + " " .repeat(16) +  asciiTable[4]);
        System.out.println(asciiTable[4] + " Email" + " ".repeat(4) + ": " + address.getEmail() + " " .repeat(1) +  asciiTable[4]);
        System.out.println(asciiTable[4] + " Edad" + " ".repeat(5) + ": " + address.getAge() + " " .repeat(29) +  asciiTable[4]);
        System.out.println(asciiTable[2] + asciiTable[5].repeat(43) + asciiTable[3]);
    }

}
