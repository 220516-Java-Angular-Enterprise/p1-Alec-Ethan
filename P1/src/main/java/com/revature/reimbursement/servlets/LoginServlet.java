package com.revature.reimbursement.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimbursement.dtos.requests.NewLoginRequest;
import com.revature.reimbursement.dtos.requests.NewUserRequest;
import com.revature.reimbursement.dtos.responses.Principal;
import com.revature.reimbursement.models.Users;
import com.revature.reimbursement.services.TokenService;
import com.revature.reimbursement.services.UsersService;
import com.revature.reimbursement.util.annotations.Inject;
import com.revature.reimbursement.util.customException.InvalidRequestException;
import com.revature.reimbursement.util.customException.ResourceConflictException;
import com.revature.reimbursement.util.customException.UnauthorizedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Inject
    private final ObjectMapper mapper;
    private final UsersService userService;
    private final TokenService tokenService;

    @Inject
    public LoginServlet(ObjectMapper mapper, UsersService userService, TokenService tokenService) {
        this.mapper = mapper;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            NewLoginRequest request = mapper.readValue(req.getInputStream(), NewLoginRequest.class);
            Principal principal = new Principal(userService.login(request));

            boolean activeUser = userService.getRowByColumnValue("id", "'" + principal.getId() + "'").isIs_active();
            if (!activeUser){
                throw new UnauthorizedException();
            }


            String token = tokenService.generateToken(principal);
            resp.setHeader("Authorization", token);
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(principal));
        } catch (UnauthorizedException e){
            resp.setStatus(401);
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
