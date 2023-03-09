package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TenmoService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public TenmoService(String url) {
        this.baseUrl= url;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal getBalance() {
        BigDecimal balance;
        Account account = new Account();
        ResponseEntity<Account> response = restTemplate.exchange(baseUrl + "tenmo/accounts", HttpMethod.GET, makeAccountEntity(account), Account.class);
        account = response.getBody();
        balance = account.getBalance();
        return balance;
    }

    public List<User> filteredUserList(User user) {
        User[] users = null;
        //User currentUser = new User();
        List<User> usersList = new ArrayList<>();

        ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "tenmo/users", HttpMethod.GET, makeAuthEntity(), User[].class);
        users = response.getBody();
        for(User newUser: users){
            if (!newUser.equals(user)){
                usersList.add(newUser);
            }
        }

            return usersList;
        }



    private HttpEntity<User> makeUserEntity(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(user, headers);
    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }


}
