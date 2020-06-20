package io.tyeolrik.tennistring.ui.mypage;

public class StringRecordItem {
    private String stringRecordDate;
    private String stringBrand;
    private String stringName;
    private String stringTension;

    public StringRecordItem() {

    }

    public StringRecordItem(String stringRecordDate, String stringBrand, String stringName, String stringTension) {
        this.stringRecordDate = stringRecordDate;
        this.stringBrand = stringBrand;
        this.stringName = stringName;
        this.stringTension = stringTension;
    }

    public String getStringRecordDate() {
        return stringRecordDate;
    }

    public void setStringRecordDate(String stringRecordDate) {
        this.stringRecordDate = stringRecordDate;
    }

    public String getStringBrand() {
        return stringBrand;
    }

    public void setStringBrand(String stringBrand) {
        this.stringBrand = stringBrand;
    }

    public String getStringName() {
        return stringName;
    }

    public void setStringName(String stringName) {
        this.stringName = stringName;
    }

    public String getStringTension() {
        return stringTension;
    }

    public void setStringTension(String stringTension) {
        this.stringTension = stringTension;
    }
}
