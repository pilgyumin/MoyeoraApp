package com.example.student.miniproject;

import java.util.Date;

public class Car {
    private int cid;
    private String cnum;
    private Date cregdate;
    private String rpm;
    private String speed;
    private String ptemp;
    private String htemp;
    private String light;
    private String battery;
    private String seat;
    private String Gpsx;
    private String Gpsy;
    private String cstatus;
    // 차량이랑 연결 될 때 설정(carip가 담긴 생성자는 아직 생성 안함. 필요하면 생성 가능)
    private String carip;
    private int carmap;
    private String door;

    public int getCid() {
        return cid;
    }
    public void setCid(int cid) {
        this.cid = cid;
    }
    public String getCnum() {
        return cnum;
    }
    public void setCnum(String cnum) {
        this.cnum = cnum;
    }
    public Date getCregdate() {
        return cregdate;
    }
    public void setCregdate(Date cregdate) {
        this.cregdate = cregdate;
    }
    public String getRpm() {
        return rpm;
    }
    public void setRpm(String rpm) {
        this.rpm = rpm;
    }
    public String getSpeed() {
        return speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }
    public String getPtemp() {
        return ptemp;
    }
    public void setPtemp(String ptemp) {
        this.ptemp = ptemp;
    }
    public String getHtemp() {
        return htemp;
    }
    public void setHtemp(String htemp) {
        this.htemp = htemp;
    }
    public String getLight() {
        return light;
    }
    public void setLight(String light) {
        this.light = light;
    }
    public String getBattery() {
        return battery;
    }
    public void setBattery(String battery) {
        this.battery = battery;
    }
    public String getSeat() {
        return seat;
    }
    public void setSeat(String seat) {
        this.seat = seat;
    }
    public String getGpsx() {
        return Gpsx;
    }
    public void setGpsx(String gpsx) {
        Gpsx = gpsx;
    }
    public String getGpsy() {
        return Gpsy;
    }
    public void setGpsy(String gpsy) {
        Gpsy = gpsy;
    }
    public String getCstatus() {
        return cstatus;
    }
    public void setCstatus(String cstatus) {
        this.cstatus = cstatus;
    }
    public String getCarip() {
        return carip;
    }
    public void setCarip(String carip) {
        this.carip = carip;
    }
    public int getCarmap() {
        return carmap;
    }
    public void setCarmap(int carmap) {
        this.carmap = carmap;
    }
    public String getDoor() {
        return door;
    }
    public void setDoor(String door) {
        this.door = door;
    }

    public Car() {

    }

    public Car(int cid, String cnum, Date cregdate, String rpm, String speed, String ptemp, String htemp, String light,
               String battery, String seat, String gpsx, String gpsy, String cstatus, String carip, int carmap,
               String door) {
        this.cid = cid;
        this.cnum = cnum;
        this.cregdate = cregdate;
        this.rpm = rpm;
        this.speed = speed;
        this.ptemp = ptemp;
        this.htemp = htemp;
        this.light = light;
        this.battery = battery;
        this.seat = seat;
        Gpsx = gpsx;
        Gpsy = gpsy;
        this.cstatus = cstatus;
        this.carip = carip;
        this.carmap = carmap;
        this.door = door;
    }

    @Override
    public String toString() {
        return "Car [cid=" + cid + ", cnum=" + cnum + ", cregdate=" + cregdate + ", rpm=" + rpm + ", speed=" + speed
                + ", ptemp=" + ptemp + ", htemp=" + htemp + ", light=" + light + ", battery=" + battery + ", seat="
                + seat + ", Gpsx=" + Gpsx + ", Gpsy=" + Gpsy + ", cstatus=" + cstatus + ", carip=" + carip + ", carmap="
                + carmap + ", door=" + door + "]";
    }


}