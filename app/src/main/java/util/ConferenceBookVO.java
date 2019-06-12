package util;

public class ConferenceBookVO {
    private int pk, bookPk, time, confirm;
    private String title;

    public ConferenceBookVO(int pk, int bookPk, int time, int confirm, String title) {
        this.pk = pk;
        this.bookPk = bookPk;
        this.time = time;
        this.confirm = confirm;
        this.title = title;
    }

    public int getPk() {
        return pk;
    }

    public int getBookPk() {
        return bookPk;
    }

    public int getTime() {
        return time;
    }

    public int getConfirm() {
        return confirm;
    }

    public String getTitle() {
        return title;
    }
}
