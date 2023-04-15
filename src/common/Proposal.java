package common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Proposal implements Serializable {
    private static final long serialVersionUTD = 1L;
    private long id;
    private KeyValueOperation operation;

    public Proposal(long id, KeyValueOperation operation)
    {
        this.id = id;
        this.operation = operation;
    }

    public long getId()
    {
        return id;
    }

    public KeyValueOperation getOperation()
    {
        return operation;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setOperation(KeyValueOperation operation)
    {
        this.operation = operation;
    }

    public static synchronized Proposal generateProposal(KeyValueOperation operation)
    {
        String sid = new SimpleDateFormat("HHmmssSSS").format(new Date());
        Proposal proposal = new Proposal(Integer.parseInt(sid), operation);
        try
        {
            Thread.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proposal;
    }
}
