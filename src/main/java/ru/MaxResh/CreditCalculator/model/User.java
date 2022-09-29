package ru.MaxResh.CreditCalculator.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Username cannot be empty")
    private String userName;
    @NotBlank(message = "User Login cannot be empty")
    private String userLogin;
    @NotNull(message = "Wrong size of phone")
    @Size(min=11, max = 11)
    private String phone;
    @NotBlank(message = "Date birthday cannot be empty")
    private String dateBirthday;
    @NotNull(message = "Series passport cannot be empty")
    @Size(min=4, max = 4)
    private String seriesPassport;
    @NotNull(message = "Number passport cannot be empty")
    @Size(min=6, max = 6)
    private String numberPassport;
    private Integer rate;
    @NotNull(message = "Wrong size of phone")
    @Size(min=6)
    private String password;
    @Transient
    private String passwordConfirmation;
    private boolean enabled;

    @OneToMany(fetch = FetchType.EAGER)
    List<MonthlyAmortizationSchedule> monthlyAmortizationSchedules = new ArrayList<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(String dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public String getSeriesPassport() {
        return seriesPassport;
    }

    public void setSeriesPassport(String seriesPassport) {
        this.seriesPassport = seriesPassport;
    }

    public String getNumberPassport() {
        return numberPassport;
    }

    public void setNumberPassport(String numberPassport) {
        this.numberPassport = numberPassport;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public List<MonthlyAmortizationSchedule> getMonthlyAmortizationSchedules() {
        return monthlyAmortizationSchedules;
    }

    public void setMonthlyAmortizationSchedules(List<MonthlyAmortizationSchedule> monthlyAmortizationSchedules) {
        this.monthlyAmortizationSchedules = monthlyAmortizationSchedules;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
