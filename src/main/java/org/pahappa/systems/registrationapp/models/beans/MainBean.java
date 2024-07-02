package org.pahappa.systems.registrationapp.models.beans;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.models.enums.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.pahappa.systems.registrationapp.models.beans.DependantBean.getDependants;
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
    private String filteredUser = "";
    private Gender filteredGender = null;
    private final List<Gender> filterOptions = List.of(Gender.Male,Gender.Female);
    private String search;
    private String confirmAction;

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
        paginateU(resultsPerPage,pageNumber);
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
        filterDependants(filteredUser,filteredGender);
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
        for (int i = 1; i <= Math.ceil(((double) (c == 'u' ? getNumberOfUsers() : getNumberOfDependants()) / resultsPerPage)); i++)
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
    public Gender getFilteredGender() {
        return filteredGender;
    }
    public void setFilteredGender(Gender filteredGender) {
        this.filteredGender = filteredGender;
    }
    public List<Gender> getFilterOptions() {
        return filterOptions;
    }
    public String getFilteredUser() {
        return filteredUser;
    }
    public void setFilteredUser(String filteredUser) {
        this.filteredUser = filteredUser;
    }

    public void resetFilters() {setFilteredUser("");setFilteredGender(null);}

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

    public void deleteAll(String group) {
        confirmAction = group;
        container(() -> redirect("/pages/admin/confirm"));
    }
    public void confirm() {
        log(confirmAction);
        container(() -> {
            if (confirmAction.equals("d")) {
                DependantBean.deleteAll();
                refreshDependants();
                redirect("/pages/dependant/list");
            } else if (confirmAction.equals("u")){
                UserBean.deleteAll();
                refreshUsers();
                redirect("/pages/user/list");
            }
            confirmAction = "x";
        });
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
    public void filterDependants(String uName, Gender gender) {
        List<Dependant> filterList = new ArrayList<>();
        for (Dependant d: dependantList) {
            if ((uName==null||d.getOwner().getUsername().toLowerCase().contains(uName.toLowerCase())) && (gender == null || d.getGender() == gender)) {
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
    public void paginateU(int resultsPerPage, int pageNumber) {
        List<User> paginatedList = new ArrayList<>();
        setPageRange('u');
        for (int result = 0; result < Integer.min(resultsPerPage,searchUserList.size()); result++) {
            User u = searchUserList.get(Integer.min(result + ((pageNumber - 1) * resultsPerPage),searchUserList.size()-1));
            if (!paginatedList.contains(u)) paginatedList.add(u);
        }
        paginatedUserList = paginatedList;
    }
    public void paginateD(int resultsPerPage, int pageNumber) {
        List<Dependant> paginatedListD = new ArrayList<>();
        setPageRange('d');
        for (int result = 0; result < Integer.min(resultsPerPage,searchDependantList.size()); result++) {
            Dependant d = searchDependantList.get(Integer.min(result + ((pageNumber - 1) * resultsPerPage),searchDependantList.size()-1));
            if (!paginatedListD.contains(d)) paginatedListD.add(d);
        }
        paginatedDependantList = paginatedListD;
    }

    private static String route = "";
    public static void router(String s) {route=s;}
    public String routeIs(String s) {
        return route.equals(s)?"route":"";
    }

    public void clearThenGo(String location) {
        setSearch("");
        container(() -> redirect(location));
    }
    public void nextPage(int i) {
        pageNumber += i;
        pageNumber = Integer.min(pageRange.size(),pageNumber);
        pageNumber = Integer.max(1,pageNumber);
    }
}
