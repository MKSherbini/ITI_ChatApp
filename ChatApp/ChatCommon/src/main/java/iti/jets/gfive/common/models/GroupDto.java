package iti.jets.gfive.common.models;

import java.io.Serializable;

public class GroupDto implements Serializable {
    private String groupname;
    private int id;

   public GroupDto() {

    }

   public GroupDto(String name, int id) {
        this.groupname = name;
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
