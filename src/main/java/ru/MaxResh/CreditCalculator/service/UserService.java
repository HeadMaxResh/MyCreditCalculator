package ru.MaxResh.CreditCalculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import ru.MaxResh.CreditCalculator.model.Role;
import ru.MaxResh.CreditCalculator.model.User;
import ru.MaxResh.CreditCalculator.repos.UserRepo;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepo userRepo;

   @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        User user = userRepo.findByUserLogin(userLogin);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean addUser(User user) {
        User userFromDB = userRepo.findByUserLogin(user.getUserLogin());

        if (userFromDB != null) {
            return false;
        }

        user.setEnabled(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String userLogin, Map<String, String> form,
                         String userName, String phone, String dateBirthday, String seriesPassport, String numberPassport) {

        user.setUserLogin(userLogin);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        user.setUserName(userName);
        user.setPhone(phone);
        user.setDateBirthday(dateBirthday);
        user.setSeriesPassport(seriesPassport);
        user.setNumberPassport(numberPassport);

        userRepo.save(user);
    }
    public UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }
        return null;
    }

    /*public void updateProfile(User user, String password, String login) {
        String userLogin = user.getUserLogin();

        boolean isLoginChanged = (login != null && !login.equals(userLogin)) ||
                (userLogin != null && !userLogin.equals(login));

        if (isLoginChanged) {
            user.setUserLogin(login);
        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepo.save(user);
    }*/
}
