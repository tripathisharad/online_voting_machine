package com.votingmachine.votingmachine20;

import java.io.Serializable;

public class Participant implements Serializable {

    int serialNo;
    String nameOFP;
    int symbolNumber;


    public Participant() {
    }

    public Participant(int serialNo, String nameOFP, int symbolNumber) {
        this.serialNo = serialNo;
        this.nameOFP = nameOFP;
        this.symbolNumber = symbolNumber;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getNameOFP() {
        return nameOFP;
    }

    public void setNameOFP(String nameOFP) {
        this.nameOFP = nameOFP;
    }

    public int getSymbolNumber() {
        return symbolNumber;
    }

    public void setSymbolNumber(int symbolNumber) {
        this.symbolNumber = symbolNumber;
    }


}
