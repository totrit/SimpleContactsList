package com.example.alvin.simplecontactlist.control;

public interface ITopLevelDelegate {
    public void displayContact(int itemIndex);

    public void changeTitle(String newTitle);

    public void showBackButton(boolean show);
}