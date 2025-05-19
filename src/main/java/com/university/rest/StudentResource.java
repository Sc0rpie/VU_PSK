package com.university.rest;

import com.university.entity.Student;
import com.university.service.StudentService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/students")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentResource {
    
    @Inject
    private StudentService studentService;
    
    /**
     * GET /api/students
     * Returns a list of all students
     */
    @GET
    public Response getAllStudents() {
        List<Student> students = studentService.getAllStudentsJpa();
        return Response.ok(students).build();
    }
    
    /**
     * GET /api/students/{id}
     * Returns a specific student by ID
     */
    @GET
    @Path("/{id}")
    public Response getStudentById(@PathParam("id") Long id) {
        Student student = studentService.getStudentByIdJpa(id);
        
        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Student with ID " + id + " not found")
                    .build();
        }
        
        return Response.ok(student).build();
    }
    
    /**
     * POST /api/students
     * Creates a new student
     */
    @POST
    public Response createStudent(Student student) {
        if (student.getId() != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Student ID should not be provided for new student")
                    .build();
        }
        
        try {
            studentService.saveStudentJpa(student);
            return Response.status(Response.Status.CREATED)
                    .entity(student)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating student: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * PUT /api/students/{id}
     * Updates an existing student
     */
    @PUT
    @Path("/{id}")
    public Response updateStudent(@PathParam("id") Long id, Student student) {
        Student existingStudent = studentService.getStudentByIdJpa(id);
        
        if (existingStudent == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Student with ID " + id + " not found")
                    .build();
        }
        
        // Set the ID from the path parameter
        student.setId(id);
        
        try {
            studentService.saveStudentJpa(student);
            return Response.ok(student).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating student: " + e.getMessage())
                    .build();
        }
    }
}
