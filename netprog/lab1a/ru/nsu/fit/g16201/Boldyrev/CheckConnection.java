package ru.nsu.fit.g16201.Boldyrev;

import java.util.ArrayList;

class CheckConnection extends Thread {
    CheckConnection() {

    }

    void checkConnect(ArrayList<IPInfo> addList) {
        boolean flag = false;

        for (int i = 0; i < addList.size(); i++) {
            if (addList.get(i).getIpStatus() == 0) {
                System.out.println("Program with IP " + addList.get(i).getIpAddress() + "has been shut down.");
                addList.remove(i);
                flag = true;
            }
            else {
                IPInfo editInfo = addList.get(i);
                editInfo.decreaseStatus();
                addList.add(i, editInfo);
            }
        }
        if (flag) {
            System.out.println("Edited IP list:");
            this.listAll(addList);
        }
    }

    void listAll(ArrayList<IPInfo> addList) {
        System.out.println("IP list:");
        for (int i = 0; i < addList.size(); i++) {
            System.out.println(addList.get(i).getIpAddress());
        }
    }
}