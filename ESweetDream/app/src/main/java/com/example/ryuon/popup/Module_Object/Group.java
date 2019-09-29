package com.example.ryuon.popup.Module_Object;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private String groupName;
    private ArrayList<module> module_List = new ArrayList<>();

    public void setName(String name) {
        groupName = name;
    }
    public String getName() {
        return groupName;
    }


    public void addModule_List(module Module) {
        module_List.add(Module);
    }

    public ArrayList<module> getModule_List() {
        return module_List;
    }
}
