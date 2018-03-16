package br.com.davidlemos.ezpointmobile.model;

public class Model{

    private int icon;
    private String title;
    private String counter_f;
    private String counter_e;

    private boolean isGroupHeader = false;

    public Model(String title) {
        this(-1,title,null,null);
        isGroupHeader = true;
    }
    public Model(int icon, String title, String counter_e, String counter_f) {
        super();
        this.icon = icon;
        this.title = title;
        this.counter_f = counter_f;
        this.counter_e = counter_e;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCounterF() {
        return counter_f;
    }

    public void setCounterF(String counter_f) {
        this.counter_f = counter_f;
    }

    public String getCounterE() {
        return counter_e;
    }

    public void setCounterE(String counter_e) {
        this.counter_e = counter_e;
    }

    public boolean isGroupHeader() {
        return isGroupHeader;
    }

    public void setGroupHeader(boolean groupHeader) {
        isGroupHeader = groupHeader;
    }
}
