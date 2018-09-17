package ru.nsu.fit.g16201.Boldyrev;

import java.util.ArrayList;

class CheckConnection extends Thread {
    CheckConnection() {

    }

    void checkConnect(ArrayList<IPInfo> addList) {
        boolean flag = false;

        for (int i = 0; i < addList.size(); i++) {
            if (addList.get(i).getIpStatus() == 0) {
                System.out.println("Program with IP " + addList.get(i).getIpAddress() + " has been shut down.");
                addList.remove(i);
                flag = true;
            }
            else {
                addList.get(i).decreaseStatus();
            }
        }
        if (flag) {
            System.out.println("Edited IP list:");
            this.listAll(addList);
            System.out.println("Total amount of program's copies: " + addList.size() + "\n");
        }
    }

    void listAll(ArrayList<IPInfo> addList) {
        if (addList.size() == 0) {
            System.out.println("List is empty.");
        }
        System.out.println("IP list:");
        for (int i = 0; i < addList.size(); i++) {
            System.out.println(addList.get(i).getIpAddress());
        }
        System.out.println("Total amount of program's copies: " + addList.size() + "\n");
    }

    void updateConnection(ArrayList<IPInfo> addList, IPInfo connection) {
        for (int i = 0; i < addList.size(); i++) {
            if (addList.get(i).equals(connection)) {
                addList.get(i).setIpStatus();
            }
        }
    }
}
