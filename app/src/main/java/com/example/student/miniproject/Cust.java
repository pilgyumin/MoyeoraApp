package com.example.student.miniproject;

public class Cust {

    //회원 가입 시에 사용자가 입력한 정보를 등록
    private String cust_id;
    private String cust_pw;
    private String cust_name;
    private String cust_birthdate;
    private String cust_email;
    private int cust_phone;
    private String cust_address;
    private String cust_sex;

    // 회원가입 때는 자동 설정
    private long cust_regdate;

    //회원 가입 시에 사용자가 입력한 정보를 등록
    private String cust_fav1;
    private String cust_fav2;
    private String cust_fav3;
    private String cust_favcar;

    private String ctmlgt;
    private String ctmtep;
    private String ctmseat;
    private String ctmcar;

    public Cust() {}

    public Cust(String cust_id) {
        this.cust_id = cust_id;
    }



    public Cust(String cust_id, String ctmlgt, String ctmtep, String ctmseat, String ctmcar) {
        super();
        this.cust_id = cust_id;
        this.ctmlgt = ctmlgt;
        this.ctmtep = ctmtep;
        this.ctmseat = ctmseat;
        this.ctmcar = ctmcar;
    }

    public Cust(String cust_id, String cust_pw, String cust_name, String cust_birthdate, String cust_email,
                int cust_phone, String cust_address, String cust_sex, String cust_fav1, String cust_fav2, String cust_fav3,
                String cust_favcar) {
        this.cust_id = cust_id;
        this.cust_pw = cust_pw;
        this.cust_name = cust_name;
        this.cust_birthdate = cust_birthdate;
        this.cust_email = cust_email;
        this.cust_phone = cust_phone;
        this.cust_address = cust_address;
        this.cust_sex = cust_sex;
        this.cust_fav1 = cust_fav1;
        this.cust_fav2 = cust_fav2;
        this.cust_fav3 = cust_fav3;
        this.cust_favcar = cust_favcar;

    }



    public Cust(String cust_id, String cust_pw, String cust_name, String cust_birthdate, String cust_email,
                int cust_phone, String cust_address, String cust_sex, long cust_regdate, String cust_fav1, String cust_fav2,
                String cust_fav3, String cust_favcar) {
        this.cust_id = cust_id;
        this.cust_pw = cust_pw;
        this.cust_name = cust_name;
        this.cust_birthdate = cust_birthdate;
        this.cust_email = cust_email;
        this.cust_phone = cust_phone;
        this.cust_address = cust_address;
        this.cust_sex = cust_sex;
        this.cust_regdate = cust_regdate;
        this.cust_fav1 = cust_fav1;
        this.cust_fav2 = cust_fav2;
        this.cust_fav3 = cust_fav3;
        this.cust_favcar = cust_favcar;
    }


    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCust_pw() {
        return cust_pw;
    }

    public void setCust_pw(String cust_pw) {
        this.cust_pw = cust_pw;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_birthdate() {
        return cust_birthdate;
    }

    public void setCust_birthdate(String cust_birthdate) {
        this.cust_birthdate = cust_birthdate;
    }

    public String getCust_email() {
        return cust_email;
    }

    public void setCust_email(String cust_email) {
        this.cust_email = cust_email;
    }

    public int getCust_phone() {
        return cust_phone;
    }

    public void setCust_phone(int cust_phone) {
        this.cust_phone = cust_phone;
    }

    public String getCust_address() {
        return cust_address;
    }

    public void setCust_address(String cust_address) {
        this.cust_address = cust_address;
    }

    public String getCust_sex() {
        return cust_sex;
    }

    public void setCust_sex(String cust_sex) {
        this.cust_sex = cust_sex;
    }

    public long getCust_regdate() {
        return cust_regdate;
    }

    public void setCust_regdate(long cust_regdate) {
        this.cust_regdate = cust_regdate;
    }

    public String getCust_fav1() {
        return cust_fav1;
    }

    public void setCust_fav1(String cust_fav1) {
        this.cust_fav1 = cust_fav1;
    }

    public String getCust_fav2() {
        return cust_fav2;
    }

    public void setCust_fav2(String cust_fav2) {
        this.cust_fav2 = cust_fav2;
    }

    public String getCust_fav3() {
        return cust_fav3;
    }

    public void setCust_fav3(String cust_fav3) {
        this.cust_fav3 = cust_fav3;
    }

    public String getCust_favcar() {
        return cust_favcar;
    }

    public void setCust_favcar(String cust_favcar) {
        this.cust_favcar = cust_favcar;
    }

    public String getCtmlgt() {
        return ctmlgt;
    }

    public void setCtmlgt(String ctmlgt) {
        this.ctmlgt = ctmlgt;
    }

    public String getCtmtep() {
        return ctmtep;
    }

    public void setCtmtep(String ctmtep) {
        this.ctmtep = ctmtep;
    }

    public String getCtmseat() {
        return ctmseat;
    }

    public void setCtmseat(String ctmseat) {
        this.ctmseat = ctmseat;
    }

    public String getCtmcar() {
        return ctmcar;
    }

    public void setCtmcar(String ctmcar) {
        this.ctmcar = ctmcar;
    }

    @Override
    public String toString() {
        return "Cust [cust_id=" + cust_id + ", cust_pw=" + cust_pw + ", cust_name=" + cust_name + ", cust_birthdate="
                + cust_birthdate + ", cust_email=" + cust_email + ", cust_phone=" + cust_phone + ", cust_address="
                + cust_address + ", cust_sex=" + cust_sex + ", cust_regdate=" + cust_regdate + ", cust_fav1="
                + cust_fav1 + ", cust_fav2=" + cust_fav2 + ", cust_fav3=" + cust_fav3 + ", cust_favcar=" + cust_favcar
                + ", ctmlgt=" + ctmlgt + ", ctmtep=" + ctmtep + ", ctmseat=" + ctmseat + ", ctmcar=" + ctmcar + "]";
    }


}