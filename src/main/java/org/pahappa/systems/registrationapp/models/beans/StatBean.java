package org.pahappa.systems.registrationapp.models.beans;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.models.enums.Gender;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import static org.pahappa.systems.registrationapp.models.beans.DependantBean.*;
import static org.pahappa.systems.registrationapp.models.beans.MainBean.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "statBean")
@SessionScoped
public class StatBean {
    private int numberOfUsers;
    private int usersWithDependants;
    private int numberOfDependants;
    private int maleDependants;

    public int getNumberOfUsers() {
        numberOfUsers = MainBean.getNumberOfUsers();
        return numberOfUsers;
    }
    public int getNumberOfDependants() {
        numberOfDependants = MainBean.getNumberOfDependants();
        return numberOfDependants;
    }
    public int getUsersWithNoDependants() {
        int noDep = 0;
        for (User u: getUserList()) {
            if (u.getDependants().isEmpty()) noDep++;
        }
        usersWithDependants = numberOfUsers - noDep;
        return noDep;
    }
    public int getUsersWithDependants() {return usersWithDependants;}
    public int getFemaleDependants() {
        int fem = 0;
        for (Dependant d: getDependants()) {
            if (d.getGender() == Gender.Female) fem++;
        }
        maleDependants = numberOfDependants - fem;
        return fem;
    }
    public int getMaleDependants() {return maleDependants;}
    public PieChartModel getPieChart() {
        ChartData cd = new ChartData();
        PieChartDataSet ds = new PieChartDataSet();
        PieChartModel pcm = new PieChartModel();

        List<Number> values = List.of(getFemaleDependants(),getMaleDependants());
        ds.setData(values);

        List<String> colors = List.of("#faa","#aaf");
        ds.setBackgroundColor(colors);

        cd.addChartDataSet(ds);
        List<String> labels = List.of("Female Dependants","Male Dependants");
        cd.setLabels(labels);
        pcm.setData(cd);
        return pcm;
    }
}
