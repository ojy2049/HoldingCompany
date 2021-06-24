package com.example.app;

import java.util.HashMap;
import java.util.Map;

public class Chatmodel {
    public Map<String,Boolean> users = new HashMap<>(); //유저
    public Map<String,Comment> comments = new HashMap<>(); // 메시지

    public static class Comment
    {
        public String uid;
        public String message;
        public Object timestamp;
    }


}
