package io.goolean.tech.hawker.sales.Model;

public class QuestionCheckModel {
    String sQuestionID,sQuestion,sQuestionSatatus;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setsQuestionID(String sQuestionID) {
        this.sQuestionID = sQuestionID;
    }

    public void setsQuestion(String sQuestion) {
        this.sQuestion = sQuestion;
    }

    public void setsQuestionSatatus(String sQuestionSatatus) {
        this.sQuestionSatatus = sQuestionSatatus;
    }

    public String getsQuestionID() {
        return sQuestionID;
    }

    public String getsQuestion() {
        return sQuestion;
    }

    public String getsQuestionSatatus() {
        return sQuestionSatatus;
    }
}
