package com.example.yunjeong.project1;

/**
 * Created by YunJeong on 2016-09-16.
 */
public class CardData {
    //cardID,cardNum,cardName, userID,cardExpiration,cardPassword,cardCVCNum,cardType
    int cardID;
    String cardNum;
    String cardName;
    String userID;
    String cardExpiration;
    String cardPassword;
    int cardCVCNum;
    String cardType;

    public CardData(int cardID, String cardNum, String cardName, String userID, String cardExpiration, String cardPassword, int cardCVCNum, String cardType) {
        this.cardID = cardID;
        this.cardNum = cardNum;
        this.cardName = cardName;
        this.userID = userID;
        this.cardExpiration = cardExpiration;
        this.cardPassword = cardPassword;
        this.cardCVCNum = cardCVCNum;
        this.cardType = cardType;
    }
    public CardData(String cardNum, String cardName, String userID, String cardExpiration, String cardPassword, int cardCVCNum, String cardType) {
        this.cardNum = cardNum;
        this.cardName = cardName;
        this.userID = userID;
        this.cardExpiration = cardExpiration;
        this.cardPassword = cardPassword;
        this.cardCVCNum = cardCVCNum;
        this.cardType = cardType;
    }
    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardExpiration() {
        return cardExpiration;
    }

    public void setCardExpiration(String cardExpiration) {
        this.cardExpiration = cardExpiration;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCardPassword() {
        return cardPassword;
    }

    public void setCardPassword(String cardPassword) {
        this.cardPassword = cardPassword;
    }

    public int getCardCVCNum() {
        return cardCVCNum;
    }

    public void setCardCVCNum(int cardCVCNum) {
        this.cardCVCNum = cardCVCNum;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}


