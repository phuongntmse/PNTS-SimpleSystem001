package com.api.demorestfull;
import com.db.DummyDB;
import com.model.Employee;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

    @Path("/employees")
    public class EmployeeResource {

        private DummyDB employeeRepository = new DummyDB();

        @GET
        @Path("/all")
        @Produces(MediaType.APPLICATION_JSON)
        public List<Employee> getAll() {
            return employeeRepository.getAll();
        }

        @GET
        @Path("/{id}")
        @Produces(MediaType.APPLICATION_JSON)
        public Employee getById(@PathParam("id") Long id) {
            return employeeRepository.getById(id);
        }

        @PUT
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response insert(Employee employee) {
            Employee rs = employeeRepository.insert(employee);
            if (rs!=null) {
                return Response.ok("Successfully add employee via PUT request").build();
            }
            else return Response.status(Response.Status.BAD_REQUEST).entity("Failed to add employee via PUT request").build();
        }

        @POST
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response update(Employee employee) {
            if (!employeeRepository.exists(employee.getId())) {
                return Response.status(Response.Status.BAD_REQUEST).entity(employee.getId() + "Doesn't exist").build();
            }
            Employee employee1 = employeeRepository.update(employee);
            return Response.ok().entity(employee1).build();
        }

        @Path("/{id}")
        @DELETE
        @Produces(MediaType.APPLICATION_JSON)
        public Response delete(@PathParam("id") Long id) {
            if (id == 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID 0").build();
            }
            employeeRepository.delete(id);
            return Response.ok().entity("Employee has been deleted successfully.").build();
        }

    }