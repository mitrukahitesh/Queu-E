package com.example.queu_e.model;

import java.util.Objects;

public class TokenDetails {
    public String key;
    public String orgId;
    public String org;
    public String task;
    public Long myNum;
    public Long current;
    public Long availed;

    @Override
    public boolean equals(Object o) {
        TokenDetails t = (TokenDetails) o;
        return org.equals(t.org) && task.equals(t.task);
    }
}
