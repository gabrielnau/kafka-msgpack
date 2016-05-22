package com.kudosenshi.messaging;

import java.util.ArrayList;
import java.util.List;

@org.msgpack.annotation.Message
public class MyMessage {
    public String name;
    public String version;
    public List<String> sample = new ArrayList<>();
    
    public MyMessage() {
        sample.add("h");
        sample.add("a");
    }
}
