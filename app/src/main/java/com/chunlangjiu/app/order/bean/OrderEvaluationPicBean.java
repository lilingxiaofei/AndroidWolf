package com.chunlangjiu.app.order.bean;

public class OrderEvaluationPicBean {
    private boolean isAddButton;
    private boolean isCamera;
    private String picPath;
    private String base64Data;
    private String name;

    public boolean isAddButton() {
        return isAddButton;
    }

    public boolean isCamera() {
        return isCamera;
    }

    public void setCamera(boolean camera) {
        isCamera = camera;
    }

    public void setAddButton(boolean addButton) {
        isAddButton = addButton;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getBase64Data() {
        return base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
