package com.revature.reimbursement.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.revature.reimbursement.dtos.requests.NewUserRequest;
import com.revature.reimbursement.dtos.responses.Principal;
import com.revature.reimbursement.models.Users;
import com.revature.reimbursement.services.TokenService;
import com.revature.reimbursement.services.UserRolesService;
import com.revature.reimbursement.services.UsersService;
import com.revature.reimbursement.util.annotations.Inject;
import com.revature.reimbursement.util.customException.InvalidRequestException;
import com.revature.reimbursement.util.customException.ResourceConflictException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.nashorn.internal.parser.Token;

import java.io.IOException;
import java.util.List;

public class UsersServlet extends HttpServlet {
    @Inject
    private final ObjectMapper mapper;
    private final UsersService userService;
    private final UserRolesService userRolesService;
    private final TokenService tokenService;
    @Inject
    public UsersServlet(ObjectMapper mapper, UsersService userService, UserRolesService userRolesService, TokenService tokenService) {
        this.mapper = mapper;
        this.userService = userService;
        this.userRolesService = userRolesService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            NewUserRequest userRequest = mapper.readValue(req.getInputStream(), NewUserRequest.class);
            String[] uris = req.getRequestURI().split("/");

            if (uris.length == 4 && uris[3].equals("username")) {
                Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

                if (requester == null) {
                    resp.setStatus(401); // UNAUTHORIZED
                    return;
                }

                if (!requester.getRole().equals("ADMIN")) {
                    resp.setStatus(403); // FORBIDDEN
                    return;
                }

                if (userRequest.getUsername().equals("")) {
                    resp.setStatus(404);
                    return;
                }

                Users user = userService.getRowByColumnValue("username", userRequest.getUsername());
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(user));
                return;
            }


            //NewUserRequest request = mapper.readValue(req.getInputStream(), NewUserRequest.class);
            Users createdUser = userService.register(userRequest);
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


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

            if (requester == null) {
                resp.setStatus(401); // UNAUTHORIZED
                return;
            }

            if (!userRolesService.getById(requester.getRole()).getRole().equals("ADMIN")) {
                resp.setStatus(403); // FORBIDDEN
                return;
            }

            List<Users> users = userService.getAllUsers();
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(users));
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
