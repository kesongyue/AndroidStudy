package com.example.ksy.webapi;

import java.util.ArrayList;

public class PriviewInfo {
    private String pvdata;
    private int img_x_len;
    private int img_y_len;
    private int img_x_size;
    private int img_y_size;
    private ArrayList<String> image;
    private ArrayList<Integer> index;

    public int GetImageXLen(){return img_x_len;}
    public int GetImageYLen(){return img_y_len;}
    public int GetImageXSize(){return img_x_size;}
    public int GetImageYSize(){return img_y_size;}

    public ArrayList<Integer> GetIndex() {
        return index;
    }

    public ArrayList<String> GetImage() {
        return image;
    }
}
