package com.revature.reimbursement.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimbursement.daos.UserRolesDAO;
import com.revature.reimbursement.dtos.requests.NewLoginRequest;
import com.revature.reimbursement.dtos.requests.UserModificationRequest;
import com.revature.reimbursement.dtos.responses.Principal;
import com.revature.reimbursement.models.UserRoles;
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

import java.io.IOException;

public class UserModificationServlet extends HttpServlet {
    @Inject
    private final ObjectMapper mapper;
    private final UsersService userService;
    private final UserRolesService userRolesService;
    private final TokenService tokenService;

    @Inject
    public UserModificationServlet(ObjectMapper mapper, UsersService userService, UserRolesService userRolesService, TokenService tokenService) {
        this.mapper = mapper;
        this.userService = userService;
        this.userRolesService = userRolesService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            //<editor-fold desc = "Verify User Authorized">
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

            if (requester == null) {
                resp.setStatus(401); // UNAUTHORIZED
                return;
            }

            if (!userRolesService.getById(requester.getRole()).getRole().equals("ADMIN")) {
                resp.setStatus(403); // FORBIDDEN
                return;
            }

            //</editor-fold desc">

            //Fullfill request:
            UserModificationRequest request = mapper.readValue(req.getInputStream(), UserModificationRequest.class);
            switch(request.getRequestType()){
                case "DELETE":
                    deleteUser(request);
                    break;
                case "UPDATE":
                    updateUser(request);
                    break;
                case "GETINFO":
                    Users user = getUserInfo(request);
                    resp.setContentType("application/json");
                    resp.getWriter().write(mapper.writeValueAsString(user));
                    break;
                default:
                    throw new InvalidRequestException("Invalid user modification type.");
            }

//Respond:
            resp.setStatus(201); // CREATED
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString( request.getRequestType() + "Completed!" ));

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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            //<editor-fold desc = "Verify User Authorized">
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

            if (requester == null) {
                resp.setStatus(401); // UNAUTHORIZED
                return;
            }

            if (!userRolesService.getById(requester.getRole()).getRole().equals("ADMIN")) {
                resp.setStatus(403); // FORBIDDEN
                return;
            }

            //</editor-fold desc">
            UserModificationRequest request = mapper.readValue(req.getInputStream(), UserModificationRequest.class);
            switch(request.getRequestType()){
                case "UPDATE":
                    updateUser(request);
                    break;
                default:
                    throw new InvalidRequestException("Invalid user modification type.");
            }
            //Respond:
            resp.setStatus(201); // CREATED
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString( request.getRequestType() + "Completed!" ));

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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            //<editor-fold desc = "Verify User Authorized">
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

            if (requester == null) {
                resp.setStatus(401); // UNAUTHORIZED
                return;
            }

            if (!userRolesService.getById(requester.getRole()).getRole().equals("ADMIN")) {
                resp.setStatus(403); // FORBIDDEN
                return;
            }

            //</editor-fold desc">
            UserModificationRequest request = mapper.readValue(req.getInputStream(), UserModificationRequest.class);

            switch(request.getRequestType()){
                case "DELETE":
                    deleteUser(request);
                    break;
                default:
                    throw new InvalidRequestException("Invalid user modification type.");
            }
            //Respond:
            resp.setStatus(201); // CREATED
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString( request.getRequestType() + "Completed!" ));
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

    private Users getUserInfo(UserModificationRequest request) {
        return userService.getRowByColumnValue("username", "'" + request.getUsername() + "'");
    }
    private void updateUser(UserModificationRequest request){
        Users currentUser = userService.getRowByColumnValue("username", "'" + request.getUsername() + "'");

        boolean isPasswordChanged = false;

        if (!request.getEmail().equals("")) currentUser.setEmail(request.getEmail());

        if (!request.getPassword().equals("")){
            isPasswordChanged = true;
            currentUser.setPassword(request.getPassword());
        }

        if (!request.getGiven_name().equals("")) currentUser.setGiven_name(request.getGiven_name());
        else System.out.println("The given request: " + request.getGiven_name());

        if (!request.getSurname().equals("")) currentUser.setSurname(request.getSurname());


        if (!request.getRole_id().equals("")){
            boolean roleExists = new UserRolesService(new UserRolesDAO()).getExistsInColumnByStringValue("id", request.getRole_id());
            if (roleExists == false)
                throw new InvalidRequestException("Role type doesn't exist! Canceled modification.");
            else
                currentUser.setRole_id(request.getRole_id());
        }

        currentUser.setIs_active(request.isIs_active());

        userService.updateUser(currentUser, isPasswordChanged);

    }
    private void deleteUser(UserModificationRequest request){
        userService.deleteUserByUsername("'" + request.getUsername() + "'");
    }

}
