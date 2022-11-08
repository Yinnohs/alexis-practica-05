package test;

import com.accesodatos.Address;
import com.accesodatos.AddressBook;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
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
                        if (id == 0) id = 1;

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

                        if(userInputId <= 0){
                            throw new IndexOutOfBoundsException("ID Inválido");
                        }

                        if( userInputId > size){
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
                    // Show all Data points.
                    try {
                        addressBook.open();
                        int id;
                        RandomAccessFile raf = addressBook.raf;
                        ArrayList<Address> addresses  = new ArrayList<>();
                        String name;
                        String phone;
                        String email;
                        int age;
                        while(raf.getFilePointer() != raf.length()) {
                            id = raf.readInt();
                            name = getStringDataFromFile("name", raf);
                            phone = getStringDataFromFile("phone",raf);
                            email = getStringDataFromFile("email",raf);
                            age = raf.readInt();

                            addresses.add(new Address(id,name,phone,email,age));
                        }
                        addressBook.close();
                        showAddressBook(addresses);

                        System.out.print("Presione Enter para continuar... ");
                        sc.nextLine();

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "5":
                    System.out.println("Inicio de la importacion");
                    List<Address> addresses = new ArrayList<>();
                    DocumentBuilderFactory xmlParserBuilderFactory =  DocumentBuilderFactory.newInstance();
                    try {
                        DocumentBuilder xmlParserBuilder = xmlParserBuilderFactory.newDocumentBuilder();
                        Document xml = xmlParserBuilder.parse(new File("address.xml"));
                        xml.getDocumentElement().normalize();
                        NodeList nodes = xml.getElementsByTagName("address");


                        for (int i = 0; i <nodes.getLength(); i++) {
                            Node node = nodes.item(i);
                            if (node.getNodeType() == Node.ELEMENT_NODE){
                                addressBook.open();
                                int id = addressBook.getNumRecords();
                                Element nodeElement = (Element) node;
                                String name = nodeElement.getElementsByTagName("name").item(0).getTextContent();
                                String email = nodeElement.getElementsByTagName("email").item(0).getTextContent();
                                String phone = nodeElement.getElementsByTagName("phone").item(0).getTextContent();
                                int age = Integer.parseInt(nodeElement.getElementsByTagName("age").item(0).getTextContent().trim());
                                Address addressToAdd = new Address(id,name,phone,email,age);
                                addressBook.append(addressToAdd);
                                addresses.add(addressToAdd);
                                addressBook.close();
                            }
                        }
                        System.out.println("Importación finalizada");
                        System.out.println("Datos importado... ");
                        addresses.stream().forEach(address -> System.out.println(address.toString()));
                        System.out.print("Presione Enter para continuar... ");
                        sc.nextLine();

                    } catch (ParserConfigurationException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SAXException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "6":
                    //exportar a XML
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    List<Address> addressesToExport = new ArrayList<>();
                    try {
                        int id;
                        String name;
                        String phone;
                        String email;
                        int age;
                        addressBook.open();
                        RandomAccessFile  raf = addressBook.raf;
                        while(raf.getFilePointer() != raf.length()) {
                            id = raf.readInt();
                            name = getStringDataFromFile("name", raf);
                            phone = getStringDataFromFile("phone",raf);
                            email = getStringDataFromFile("email",raf);
                            age = raf.readInt();

                            addressesToExport.add(new Address(id,name,phone,email,age));
                        }
                        addressBook.close();


                        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
                        DOMImplementation dom = builder.getDOMImplementation();
                        Document document = dom.createDocument(null,"addressbook",null);
                        document.setXmlVersion("1.0");

                        for (Address address : addressesToExport){
                            Element addressElement = document.createElement("address");
                            addressElement.setAttribute("id", "" + address.getId());
                            createAddressXmlNode("name",address.getName().trim(),document,addressElement);
                            createAddressXmlNode("email", address.getEmail().trim(),document,addressElement);
                            createAddressXmlNode("phone",address.getPhone().trim(),document,addressElement);
                            createAddressXmlNode("age", ""+ address.getAge(),document,addressElement);
                            document.getDocumentElement().appendChild(addressElement);
                        };
                        Source source = new DOMSource(document);
                        Result result = new StreamResult(new File("addressExport.xml"));
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        transformer.transform(source,result);

                        System.out.println("Archivos exportados.... " + addressesToExport.size());
                        System.out.print("Presione Enter para continuar...");
                        sc.nextLine();

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
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

    private static void showAddressBook(List<Address> addresses){
        String asciiTable[] = {"┌", "┐", "└", "┘", "│", "─", "┼" , "┤", "├", "┬", "┴"};
        String tableTopSide =    asciiTable[0] + asciiTable[5].repeat(4 ) + asciiTable[9] + asciiTable[5].repeat(26)
                + asciiTable[9] + asciiTable[5].repeat(31) + asciiTable[9] + asciiTable[5].repeat(16)
                + asciiTable[9] + asciiTable[5].repeat(6) + asciiTable[1];

        String tableBottomSide = asciiTable[2] + asciiTable[5].repeat(4 ) + asciiTable[10] + asciiTable[5].repeat(26)
                + asciiTable[10] + asciiTable[5].repeat(31) + asciiTable[10] + asciiTable[5].repeat(16)
                + asciiTable[10] + asciiTable[5].repeat(6) + asciiTable[3];

        System.out.println(tableTopSide);
        System.out.println(
                asciiTable[4] + " Id " + asciiTable[4] + " Nombre" + " ".repeat(19)
                + asciiTable[4] + " Email" + " ".repeat(25) + asciiTable[4] + " Telefono"
                + " ".repeat(7) + asciiTable[4] + " Edad " + asciiTable[4]
                );
        System.out.println(tableBottomSide);

        // Filing the table
        System.out.println(tableTopSide);
        for (Address address : addresses){
            System.out.println(
                    asciiTable[4] + " " + address.getId() + "  " +  asciiTable[4] +
                    " " + address.getName() + asciiTable[4] +
                    " " + address.getEmail() + asciiTable[4] +
                    " " + address.getPhone() + asciiTable[4] +
                    "  " + address.getAge() +  "  " +  asciiTable[4]
                    );
        }
        System.out.println(tableBottomSide);

        String dataSize = ""+ addresses.size();
        int maxlength = 2;
        String middleLine = asciiTable[4] + " Nº de registros : " + dataSize + "".repeat((maxlength - dataSize.length()));
        // data array size presentation
        System.out.println(asciiTable[0] + asciiTable[5].repeat(tableTopSide.length() - 2) + asciiTable[1]);
        System.out.println(middleLine + " ".repeat(tableTopSide.length() - (middleLine.length() + 1)) + asciiTable[4]);
        System.out.println(asciiTable[2] + asciiTable[5].repeat(tableBottomSide.length() - 2) + asciiTable[3]);
    }

    private static String getStringDataFromFile (String mode, RandomAccessFile raf) throws IOException {
        int charSize = 0;
        if (mode.equals("name")) charSize = 25;
        if (mode.equals("phone")) charSize = 15;
        if (mode.equals("email")) charSize = 30;
        char aux;
        char[] charToString = new char[charSize];

        for (int i = 0; i < charToString.length ; i++) {
            aux = raf.readChar();
            charToString[i] = aux;
        }

        String stringToReturn =  new String(charToString).trim();
        stringToReturn += " ".repeat(charSize - stringToReturn.length());

        return  stringToReturn;
    }

    private static void createAddressXmlNode(String tagName, String tagValue, Document xmlDocument, Element parentNode){
        Element xmlElement = xmlDocument.createElement(tagName);
        Text nodeValue = xmlDocument.createTextNode(tagValue);
        xmlElement.appendChild(nodeValue);
        parentNode.appendChild(xmlElement);
    }
}
