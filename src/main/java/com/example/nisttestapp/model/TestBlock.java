package com.example.nisttestapp.model;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class TestBlock {

    private String name;
    private CheckBox checkBox;
    private Label pValueLabel;
    private Label resultLabel;
    private Test test;

    public TestBlock(String name, CheckBox checkBox, Label pValueLabel, Label resultLabel, Test test) {
        this.name = name;
        this.checkBox = checkBox;
        this.pValueLabel = pValueLabel;
        this.resultLabel = resultLabel;
        this.test = test;
    }

    public String getName() {
        return name;
    }

    public Label getResultLabel() {
        return resultLabel;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public Label getpValueLabel() {
        return pValueLabel;
    }

    public Test getTest() {
        return test;
    }
}
