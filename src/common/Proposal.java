package common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Proposal implements Serializable {
    private static long serialVersionUTD = 1L;
    private long id;
    private String key;
    private String value;

    public enum Status {
        PROMISED,
        ACCEPTED,
        REJECTED
    }

    private Status status;
    private String action;


    public Proposal(String key, String value, String action)
    {
        this.id = generateID();
        this.key = key;
        this.value = value;
        this.status = Status.PROMISED;
        this.action = action;
    }

    public String getAction() {return this.action;}

    public long getProposalNumber()
    {
        return id;
    }

    public String getKey()
    {
        return key;
    }

    public void setProposalNumber(long id)
    {
        this.id = id;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return "Proposal{" +
                "proposalNumber=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public static long generateID() {
        String sid = new SimpleDateFormat("HHmmssSSS").format(new Date());
        int sidNum = Integer.parseInt(sid);

        if (sidNum == serialVersionUTD) {
            sidNum++;
        }
        serialVersionUTD = sidNum;
        System.out.println("generateID() call: " + sidNum);
        return sidNum;
    }


    public Status getStatus() {
        return status;
    }
}
