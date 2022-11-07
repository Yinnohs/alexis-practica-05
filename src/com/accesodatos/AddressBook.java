package com.accesodatos;

import java.io.*;
import java.util.ArrayList;

public class AddressBook {
    private final int ADDRESS_BYTE_SIZE= 148;

    public RandomAccessFile raf;


    public void  open () throws FileNotFoundException {
        File addressBookData = new  File("./addressbook.dat");
        raf = new RandomAccessFile(addressBookData, "rw");
    }

    public  void  close () throws IOException {
        raf.close();
    }

    public int getNumRecords(){
        int position = 0;
        ArrayList<String> ids =  new ArrayList<>();
        try {
            while(raf.getFilePointer() != raf.length()){
                raf.seek(position);
                ids.add("objeto");
                position += ADDRESS_BYTE_SIZE;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  ids.size();
    }

    public  void  append(Address addressToAppend) throws IOException {
        this.raf.seek(raf.length());
        this.raf.writeInt(addressToAppend.getId());
        this.writeBufferToFile(addressToAppend.getName(),"name");
        this.writeBufferToFile(addressToAppend.getPhone(),"phone");
        this.writeBufferToFile(addressToAppend.getEmail(), "email");
        this.raf.writeInt(addressToAppend.getAge());
    }

    public void update(Address addressToUpdate){
        int dataPosition = (addressToUpdate.getId() - 1) * ADDRESS_BYTE_SIZE;
        try {
            raf.seek(dataPosition);
            this.raf.writeInt(addressToUpdate.getId());
            this.writeBufferToFile(addressToUpdate.getName(),"name");
            this.writeBufferToFile(addressToUpdate.getPhone(),"phone");
            this.writeBufferToFile(addressToUpdate.getEmail(), "email");
            this.raf.writeInt(addressToUpdate.getAge());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  Address select (int inputID) throws Exception {
        int position = 0;
        int id;
        ArrayList<Address> addresses  = new ArrayList<>();
        String name;
        String phone;
        String email;
        int age;
        while(raf.getFilePointer() != raf.length()) {
            id = raf.readInt();
            name = this.getStringFromFile("name");
            phone = this.getStringFromFile("phone");
            email = this.getStringFromFile("email");
            age = raf.readInt();

            addresses.add(new Address(id,name,phone,email,age));

            position += ADDRESS_BYTE_SIZE;
        }

        return addresses.get((inputID - 1));
    }

    private void writeBufferToFile(String string, String mode) throws IOException {
        StringBuffer buffer = new StringBuffer(string);
        int bufferLength = 0;
        if (mode.equals("name")) bufferLength = 25;
        if (mode.equals("phone")) bufferLength = 15;
        if (mode.equals("email")) bufferLength = 30;

        buffer.setLength(bufferLength);
        raf.writeChars(buffer.toString());
    }

    private String getStringFromFile (String mode) throws IOException {
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


}
