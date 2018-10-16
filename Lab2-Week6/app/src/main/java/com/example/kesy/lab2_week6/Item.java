package com.example.kesy.lab2_week6;

import java.io.Serializable;

public class Item implements Serializable{
    private String btnText;
    private String textViewContent;
    private String category;
    private String nutrient;
    private int bgColor;
    private boolean isCollected;

    public Item(String btnText,String textViewContent,String category,String nutrient,int bgColor){
        this.btnText = btnText;
        this.textViewContent = textViewContent;
        this.category = category;
        this.nutrient = nutrient;
        this.bgColor = bgColor;
        isCollected = false;
    }

    public String getBtnText(){
        return btnText;
    }
    public String getTextViewContent(){
        return textViewContent;
    }
    public String getCategory(){
        return category;
    }
    public String getNutrient(){
        return nutrient;
    }
    public int getBgColor(){
        return bgColor;
    }
    public boolean getIsCollected(){
        return isCollected;
    }
    public void setCollected(boolean collected){
        isCollected = collected;
    }
}
