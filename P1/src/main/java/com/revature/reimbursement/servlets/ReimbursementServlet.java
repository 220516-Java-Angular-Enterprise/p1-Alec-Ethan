package com.revature.reimbursement.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimbursement.dtos.requests.NewReimbursementRequest;
import com.revature.reimbursement.dtos.requests.NewUserRequest;
import com.revature.reimbursement.dtos.requests.SortRequest;
import com.revature.reimbursement.dtos.requests.StatusChangeRequest;
import com.revature.reimbursement.dtos.responses.Principal;
import com.revature.reimbursement.models.Reimbursements;
import com.revature.reimbursement.models.Users;
import com.revature.reimbursement.services.*;
import com.revature.reimbursement.util.annotations.Inject;
import com.revature.reimbursement.util.customException.InvalidRequestException;
import com.revature.reimbursement.util.customException.ResourceConflictException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReimbursementServlet extends HttpServlet {

    @Inject
    private final ObjectMapper mapper;
    private final UsersService usersService;
    private final UserRolesService userRolesService;
    private final TokenService tokenService;
    private final ReimbursementsService reimbursementsService;
    private final ReimbursementStatusService reimbursementStatusService;
    private final ReimbursementTypesService reimbursementTypesService;

    @Inject
    public ReimbursementServlet(ObjectMapper mapper, UsersService usersService, UserRolesService userRolesService, TokenService tokenService, ReimbursementsService reimbursementsService, ReimbursementStatusService reimbursementStatusService, ReimbursementTypesService reimbursementTypesService) {
        this.mapper = mapper;
        this.usersService = usersService;
        this.userRolesService = userRolesService;
        this.tokenService = tokenService;
        this.reimbursementsService = reimbursementsService;
        this.reimbursementStatusService = reimbursementStatusService;
        this.reimbursementTypesService = reimbursementTypesService;
    }

    //I want to create a doGet and this will show different things depending on the user role
    //EMPLOYEE: Display all reimbursements THEY CREATED
    //FINANCE MANAGER: Display all reimbursements
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
            String role = userRolesService.getById(requester.getRole()).getRole();
            String[] uris = req.getRequestURI().split("/");
            if (requester == null) {
                resp.setStatus(401); // UNAUTHORIZED
                return;
            }

            if (uris.length == 4 && uris[3].equals("sort")) {
                //Filter the Reimbursement: (FINANCE MANAGER)
                if (role.equals("FINANCE MANAGER")) {
                    SortRequest sortRequest = mapper.readValue(req.getInputStream(), SortRequest.class);

                    // Filter By Type:
                    if (sortRequest.getSortType().equals("Type")) {
                        List<Reimbursements> rems = reimbursementsService.getAll();
                        rems = rems.stream().sorted(Comparator.comparing(Reimbursements::getType_id)).collect(Collectors.toList());
                        resp.setContentType("application/json");
                        resp.getWriter().write(mapper.writeValueAsString(rems));
                    }

                    // Filter By Status:
                    else if (sortRequest.getSortType().equals("PENDING")) {
                        //Display all Reimbursements
                        List<Reimbursements> rems = reimbursementsService.getAllRowsByColumnValue("status_id", "'" + 0 + "'");
                        //rems = rems.stream().sorted(Comparator.comparing(Reimbursements::getStatus_id)).collect(Collectors.toList());
                        resp.setContentType("application/json");
                        resp.getWriter().write(mapper.writeValueAsString(rems));
                    } else if (sortRequest.getSortType().equals("COMPLETED")) {
                        List<Reimbursements> rems = reimbursementsService.getAllRowsByColumnValue("resolver_id", "'" + requester.getId() + "'");
                        rems = rems.stream().sorted(Comparator.comparing(Reimbursements::getStatus_id)).collect(Collectors.toList());
                        resp.setContentType("application/json");
                        resp.getWriter().write(mapper.writeValueAsString(rems));
                    }
                }
                //Sort Employee Reimbursements:
                else if (role.equals("EMPLOYEE")) {
                    SortRequest sortRequest = mapper.readValue(req.getInputStream(), SortRequest.class);
                    List<Reimbursements> rems = reimbursementsService.getAllRowsByColumnValue("author_id", "'" + requester.getId() + "'");
                    //Sort By Status
                    if (sortRequest.getSortType().equals("Status")) {
                        rems = rems.stream().sorted(Comparator.comparing(Reimbursements::getStatus_id)).collect(Collectors.toList());
                        resp.setContentType("application/json");
                        resp.getWriter().write(mapper.writeValueAsString(rems));
                    }
                    //Sort By Type
                    else if (sortRequest.getSortType().equals("Type")) {
                        rems = rems.stream().sorted(Comparator.comparing(Reimbursements::getStatus_id)).collect(Collectors.toList());
                        resp.setContentType("application/json");
                        resp.getWriter().write(mapper.writeValueAsString(rems));
                    }
                    //Sort By ID
                    else if (!sortRequest.getSortType().equals(null)) {
                        Reimbursements rem = reimbursementsService.getById(sortRequest.getSortType());
                        resp.setContentType("application/json");
                        resp.getWriter().write(mapper.writeValueAsString(rem));
                    }
                }
            }
            else if (role.equals("FINANCE MANAGER")) {
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
            //Only Employees should be able to create a new reimbursement
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
            String role = userRolesService.getById(requester.getRole()).getRole();
            String[] uris = req.getRequestURI().split("/");

                //Create a Reimbursement: (EMPLOYEE)
            if (role.equals("EMPLOYEE")) {
                // Read the Reimbursement JSON Values and change some values
                NewReimbursementRequest reimbursementRequest = mapper.readValue(req.getInputStream(), NewReimbursementRequest.class);
                reimbursementRequest.setSubmitted(Timestamp.from(Instant.now()));
                reimbursementRequest.setAuthor_id(requester.getId());
                reimbursementRequest.setStatus_id("0"); // Sets status to PENDING
                reimbursementRequest.setType(reimbursementTypesService.getIdByType(reimbursementRequest.getType()));
                // Add the New Reimbursement:
                Reimbursements createdRem = reimbursementsService.saveReimbursement(reimbursementRequest);
                resp.setStatus(201); // CREATED
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(createdRem.getId()));
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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //Only Employees should be able to create a new reimbursement
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
            String role = userRolesService.getById(requester.getRole()).getRole();
            String[] uris = req.getRequestURI().split("/");

            //Checks the URI to see what page they are on
            //Modify the Reimbursement:
            //Employees can update if their reimbursement is still PENDING.
            //FINANCE MANAGERS can change the reimbursement status
            if (uris.length == 4 && uris[3].equals("update")) {
                if (role.equals("FINANCE MANAGER")) {
                    // Change Reimbursement Status
                    // The Finance Manager should only change the status of the reimbursement
                    // The Resolver_ID should be updated.
                    StatusChangeRequest statusChangeRequest = mapper.readValue(req.getInputStream(), StatusChangeRequest.class);
                    Reimbursements rem = reimbursementsService.getById(statusChangeRequest.getRem_id());
                    rem.setStatus_id(reimbursementStatusService.getIdByStatus(statusChangeRequest.getStatus()));
                    rem.setResolver_id(requester.getId());
                    rem.setResolved(Timestamp.from(Instant.now()));
                    reimbursementsService.updateReimbursement(rem);

                } else if (role.equals("EMPLOYEE")) {
                    // Update Reimbursement
                    // Should I have the user just create a brand new Rem or have them only fill in
                    // the vars that they want to alter from their original reimbursement?
                    NewReimbursementRequest reimbursementUpdateRequest = mapper.readValue(req.getInputStream(), NewReimbursementRequest.class);
                    Reimbursements rem = reimbursementsService.getById(reimbursementUpdateRequest.getId());
                    rem.setSubmitted(Timestamp.from(Instant.now()));
                    if (reimbursementUpdateRequest.getAmount() != null)
                        rem.setAmount(reimbursementUpdateRequest.getAmount());
                    if (reimbursementUpdateRequest.getDescription() != null)
                        rem.setDescription(reimbursementUpdateRequest.getDescription());
                    if (reimbursementUpdateRequest.getType() != null)
                        rem.setType_id(reimbursementTypesService.getIdByType(reimbursementUpdateRequest.getType()));

                    reimbursementsService.updateReimbursement(rem);
                }
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
}
