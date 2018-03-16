package br.com.davidlemos.ezpointmobile.models;

import java.io.Serializable;

public class Ponto implements Serializable {
    private int id;
    private String weekDay;
    private String date;
    private String firstIn;
    private String firstOut;
    private String secondIn;
    private String secondOut;
    private String ch;
    private String ex;
    private String en;

    private String ht;
    private String hn;
    private String at;
    private String fa;
    private String hn_normais;
    private String dsr_pagar;
    private String dsr_extra;
    private String dsr_total;
    private String total_trabalhar;
    private String total_trabalhado_dsr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFirstIn() {
        return firstIn;
    }

    public void setFirstIn(String firstIn) {
        this.firstIn = firstIn;
    }

    public String getFirstOut() {
        return firstOut;
    }

    public void setFirstOut(String firstOut) {
        this.firstOut = firstOut;
    }

    public String getSecondIn() {
        return secondIn;
    }

    public void setSecondIn(String secondIn) {
        this.secondIn = secondIn;
    }

    public String getSecondOut() {
        return secondOut;
    }

    public void setSecondOut(String secondOut) {
        this.secondOut = secondOut;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getEx() {
        return ex;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getHt() {
        return ht;
    }

    public void setHt(String ht) {
        this.ht = ht;
    }

    public String getHn() {
        return hn;
    }

    public void setHn(String hn) {
        this.hn = hn;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    public String getHn_normais() {
        return hn_normais;
    }

    public void setHn_normais(String hn_normais) {
        this.hn_normais = hn_normais;
    }

    public String getDsr_pagar() {
        return dsr_pagar;
    }

    public void setDsr_pagar(String dsr_pagar) {
        this.dsr_pagar = dsr_pagar;
    }

    public String getDsr_extra() {
        return dsr_extra;
    }

    public void setDsr_extra(String dsr_extra) {
        this.dsr_extra = dsr_extra;
    }

    public String getDsr_total() {
        return dsr_total;
    }

    public void setDsr_total(String dsr_total) {
        this.dsr_total = dsr_total;
    }

    public String getTotal_trabalhar() {
        return total_trabalhar;
    }

    public void setTotal_trabalhar(String total_trabalhar) {
        this.total_trabalhar = total_trabalhar;
    }

    public String getTotal_trabalhado_dsr() {
        return total_trabalhado_dsr;
    }

    public void setTotal_trabalhado_dsr(String total_trabalhado_dsr) {
        this.total_trabalhado_dsr = total_trabalhado_dsr;
    }
}