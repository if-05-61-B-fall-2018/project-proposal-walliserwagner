package smartshoppinglist.at.smartshoppinglist.objects;

import java.util.ArrayList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.localsave.Save;
import smartshoppinglist.at.smartshoppinglist.server.Server;

public class GroupList {
    private List<Group> groups;

    public GroupList() {
        groups = new ArrayList<>();
    }

    public void populateGroups(){
        for (Group g:groups) {
            g.populateShoppinglist();
        }
    }

    public boolean addGroup(Group group){
        if(findGroupById(group.getId())!= null){
            return false;
        }
        groups.add(group);
        setChanges();
        return true;
    }
    public void removeGroups(Group group){
        groups.remove(group);
        Server.getInstance().deleteRequest(String.format("/group?userid=%d&groupid=%d", MainActivity.getInstance().getCurrentUser().getId(), group.getId()));
        setChanges();
    }
    public Group[] getGroups(){
        return groups.toArray(new Group[0]);
    }

    public String[] getGroupNames(){
        List<String> result = new ArrayList<>();
        for (Group group:groups) {
                result.add(group.getName());
        }
        return result.toArray(new String[0]);
    }
    public Group findGroupById(int id){
        for (Group group:groups) {
            if(group.getId() == id) {
                return group;
            }
        }
        return null;
    }
    public Group getDefault(){
        for (Group group:groups) {
            if(group.isDefault()) return group;
        }
        return null;
    }

    protected void setChanges(){
        Save.save(this);
    }
}
