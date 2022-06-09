package com.revature.reimbursement.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimbursement.dtos.requests.NewLoginRequest;
import com.revature.reimbursement.dtos.requests.NewUserRequest;
import com.revature.reimbursement.models.Users;
import com.revature.reimbursement.services.UsersService;
import com.revature.reimbursement.util.annotations.Inject;
import com.revature.reimbursement.util.customException.InvalidRequestException;
import com.revature.reimbursement.util.customException.ResourceConflictException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Inject
    private final ObjectMapper mapper;
    private final UsersService userService;

    @Inject
    public LoginServlet(ObjectMapper mapper, UsersService userService) {
        this.mapper = mapper;
        this.userService = userService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            NewLoginRequest request = mapper.readValue(req.getInputStream(), NewLoginRequest.class);
            Users createdUser = userService.login(request);
            resp.setStatus(201); // CREATED
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(createdUser.getId()));
        } catch (InvalidRequestException e) {
            resp.setStatus(404); // BAD REQUEST
            e.printStackTrace();
        } catch (ResourceConflictException e) {
            resp.setStatus(409); // RESOURCE CONFLICT
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
