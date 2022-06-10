package com.revature.reimbursement.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimbursement.dtos.responses.Principal;
import com.revature.reimbursement.models.Reimbursements;
import com.revature.reimbursement.services.ReimbursementsService;
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
import java.util.List;

public class ReimbursementServlet extends HttpServlet {

    @Inject
    private final ObjectMapper mapper;
    private final UsersService usersService;
    private final UserRolesService userRolesService;
    private final TokenService tokenService;
    private final ReimbursementsService reimbursementsService;

    @Inject
    public ReimbursementServlet(ObjectMapper mapper, UsersService usersService, UserRolesService userRolesService, TokenService tokenService, ReimbursementsService reimbursementsService) {
        this.mapper = mapper;
        this.usersService = usersService;
        this.userRolesService = userRolesService;
        this.tokenService = tokenService;
        this.reimbursementsService = reimbursementsService;
    }

    //I want to create a doGet and this will show different things depending on the user role
    //EMPLOYEE: Display all reimbursements THEY CREATED
    //FINANCE MANAGER: Display all reimbursements
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
            if (requester == null) {
                resp.setStatus(401); // UNAUTHORIZED
                return;
            }
            String role = userRolesService.getById(requester.getRole()).getRole();
            if (role.equals("FINANCE MANAGER")) {
                //Display all Reimbursements
                List<Reimbursements> rems = reimbursementsService.getAll();
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(rems));
            }
            else if (role.equals("EMPLOYEE")) {
                //Display all Reimbursements by Author ID
                List<Reimbursements> rems = reimbursementsService.getAllByAuthorID(requester.getId());
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(rems));
            }

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

    //EMPLOYEE: Create a new Reimbursement to the Database
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {


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
