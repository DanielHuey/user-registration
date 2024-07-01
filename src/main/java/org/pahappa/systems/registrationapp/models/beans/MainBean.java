package org.pahappa.systems.registrationapp.models.beans;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.models.enums.*;
import org.pahappa.systems.registrationapp.services.DependantService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.pahappa.systems.registrationapp.models.beans.DependantBean.getDependants;
import static org.pahappa.systems.registrationapp.models.beans.DependantBean.loadDependants;
import static org.pahappa.systems.registrationapp.models.beans.UserBean.getUsers;

@ManagedBean(name = "indexBean")
@SessionScoped
public class MainBean implements Serializable {
    private static List<User> userList;
    private static List<User> searchUserList;
    private static List<User> paginatedUserList;
    private static List<Dependant> dependantList;
    private static List<Dependant> filterDependantList;
    private static List<Dependant> searchDependantList;
    private static List<Dependant> paginatedDependantList;
    private int resultsPerPage = 5;
    private int pageNumber = 1;
    private List<Integer> pageRange;
    private String search;
    private char confirmAction;

    public MainBean() {
        refreshUsersAndDependants();
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }
    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = Integer.max(resultsPerPage,3);
    }
    public void setUserList(List<User> userList) {
        MainBean.userList = userList;
    }
    public List<User> getSearchUserList() {
        return searchUserList;
    }
    public void setSearchUserList(List<User> searchUserList) {
        MainBean.searchUserList = searchUserList;
    }
    public List<User> getPaginatedUserList() {
        searchU();
        paginate(resultsPerPage,pageNumber);
        return paginatedUserList;
    }
    public void setPaginatedUserList(List<User> paginatedUserList) {
        MainBean.paginatedUserList = paginatedUserList;
    }
    public List<Dependant> getDependantList() {
        return dependantList;
    }
    public void setDependantList(List<Dependant> dependantlist) {
        dependantList = dependantlist;
    }
    public List<Dependant> getFilterDependantList() {
        return filterDependantList;
    }
    public void setFilterDependantList(List<Dependant> filterDependantList) {
        MainBean.filterDependantList = filterDependantList;
    }
    public List<Dependant> getSearchDependantList() {
        return searchDependantList;
    }
    public void setSearchDependantList(List<Dependant> searchDependantList) {
        MainBean.searchDependantList = searchDependantList;
    }
    public List<Dependant> getPaginatedDependantList() {
        searchD();
        paginateD(resultsPerPage,pageNumber);
        return paginatedDependantList;
    }
    public void setPaginatedDependantList(List<Dependant> paginatedDependantList) {
        MainBean.paginatedDependantList = paginatedDependantList;
    }
    public int getPageNumber() {
        return pageNumber;
    }
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    public List<Integer> getPageRange() {
        return pageRange;
    }
    public void setPageRange(char c) {
        List<Integer> range = new ArrayList<> ();
        for (int i = 1; i <= (((c=='u'?getNumberOfUsers():getNumberOfDependants()) / resultsPerPage) + 1); i++)
            range.add(i);
        pageRange = range;
    }
    public static int getNumberOfUsers() {return userList.size();}
    public static int getNumberOfDependants() {return dependantList.size();}
    public String getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search = search;
    }

    public void firstLaunch() {
        log("Launch Bootstrap");
        container(() -> {
            // is it the first launch?
            if (getUsers().isEmpty())
                redirect("/pages/admin/admin_setup");
                // else go to log in
            else redirect("/pages/login");
        });
    }

    public static List<User> getUserList() {
        return userList;
    }

    public static void refreshUsersAndDependants() {
        log("refreshing users");
        refreshUsers();
        refreshDependants();
    }
    public static void refreshUsers() {
        userList = getUsers();
        paginatedUserList = searchUserList = userList;
    }
    public static void refreshDependants() {
        dependantList = getDependants();
        filterDependantList = dependantList;
    }

    public static void log(String s) {System.out.printf("[log]: %s%n",s);}

    public static void redirect(String location) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/registration-app"+location+".xhtml");
    }

    public void deleteAll(char group) {
        confirmAction = group;
        container(() -> redirect("/pages/admin/confirm"));
    }
    public void confirm() {
        if (confirmAction == 'd') {
            DependantBean.deleteAll();
        } else if (confirmAction == 'u'){
            UserBean.deleteAll();
        }
        confirmAction = 'x';
    }

    @FunctionalInterface
    interface Ex {void run() throws Exception;}
    public static boolean container(Ex ex) {
        try {
            ex.run();
            return true;
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null,msg);
            return false;
        }
    }
    public void filterDependants(User user, Gender gender) {
        List<Dependant> filterList = new ArrayList<>();
        for (Dependant d: dependantList) {
            if (d.getOwner() == user && d.getGender() == gender) {
                filterList.add(d);
            }
        }
        filterDependantList = filterList;
    }
    public void searchU() {
        List<User> searchList = new ArrayList<>();
        if (!(search==null||search.isEmpty())) {
            search = search.toLowerCase();
            for (User u: userList) {
                if (u.getUsername().toLowerCase().contains(search)
                        || u.getFirstname().toLowerCase().contains(search)
                        || (u.getLastname()!=null?u.getLastname():"").toLowerCase().contains(search))
                    searchList.add(u);
            }
        } else searchList = userList;
        searchUserList = searchList;
    }
    public void searchD() {
        List<Dependant> searchList = new ArrayList<>();
        if (!(search==null||search.isEmpty())) {
            search = search.toLowerCase();
            for (Dependant d: filterDependantList) {
                if (d.getUsername().toLowerCase().contains(search)
                        || d.getFirstname().toLowerCase().contains(search)
                        || d.getLastname().toLowerCase().contains(search))
                    searchList.add(d);
            }
        } else searchList = filterDependantList;
        searchDependantList = searchList;
    }
    public void paginate(int resultsPerPage, int pageNumber) {
        List<User> paginatedList = new ArrayList<>();
        setPageRange('u');
        for (int result = 0; result < Integer.min(resultsPerPage,searchUserList.size()); result++) {
            paginatedList.add(searchUserList.get(result + ((pageNumber - 1) * resultsPerPage)));
        }
        paginatedUserList = paginatedList;
    }
    public void paginateD(int resultsPerPage, int pageNumber) {
        List<Dependant> paginatedListD = new ArrayList<>();
        setPageRange('d');
        for (int result = 0; result < Integer.min(resultsPerPage,searchDependantList.size()); result++) {
            paginatedListD.add(searchDependantList.get(result + ((pageNumber - 1) * resultsPerPage)));
        }
        paginatedDependantList = paginatedListD;
    }
    public void paginationValueChanged(ValueChangeEvent e) {
        pageNumber = (int) e.getNewValue();
        paginate(resultsPerPage,pageNumber);
        paginateD(resultsPerPage,pageNumber);
    }

    private static String route = "";
    public static void router(String s) {route=s;}
    public String routeIs(String s) {
        return route.equals(s)?"route":"";
    }
}
