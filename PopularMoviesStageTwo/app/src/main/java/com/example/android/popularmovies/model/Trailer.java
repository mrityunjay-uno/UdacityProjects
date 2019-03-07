package com.example.android.popularmovies.model;


public class Trailer {

    private String name;
    private String id;
    private String key;
    private String type;



    public  void setID(String id)
    {
        this.id = id;
    }
    public String getID()
    {
        return this.id;
    }

    public  void setKey(String key)
    {
        this.key = key;
    }
    public String getKey()
    {
        return this.key;
    }

    public  void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return this.name;
    }

    public  void setType(String type)
    {
        this.type = type;
    }
    public String getType()
    {
        return this.type;
    }
}
