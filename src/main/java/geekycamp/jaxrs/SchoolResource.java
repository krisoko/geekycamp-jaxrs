package geekycamp.jaxrs;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;


@Path("/school")
@Produces("application/json")
@Consumes("application/json")
public class SchoolResource {

	private static Map<String, Student> studentsByName = new ConcurrentHashMap<String, Student>();
	
	public SchoolResource() {
		
		Student s = new Student();
		s.setName("Pesho");
		s.setPhone("0888654654");
		
		studentsByName.put(s.getName(),s);
	}
	
	@GET
	@Path("students/{name}")
	public Student getStudent(@PathParam("name") final String name) {
		
		Student s = studentsByName.get(name);
		if(s == null){
			throw new NotFoundException(String.format("Student with name %s not found",name));
		}
		
		return s;
	}
	
	@GET
	@Path("students/{name}")
	@Produces({"text/plain", "text/html"})
	public String getStudentAsText(@PathParam("name") final String name,	@Context HttpServletRequest request) {
		
		Student s = studentsByName.get(name);
		if(s == null){
			throw new NotFoundException(String.format("Student with name %s not found",name));
		}
		return s.toString();
	}
	
	@POST
	@Path("students")
	public Response addStudent(Student s, @Context UriInfo uriInfo) throws URISyntaxException{
		
		Student existingStudent = studentsByName.get(s.getName());
		if(existingStudent != null){
			throw new ClientErrorException(String.format("Student with this name %s already exists",s.getName()),409);
		}
		
		studentsByName.put(s.getName(), s);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(s.getName());
		
		return Response.created(builder.build()).build();
	}
	
	@DELETE
	@Path("students/{name}")
	public Response deleteStudent(@PathParam("name") final String name) {
		
		Student s = studentsByName.get(name);
		if(s == null){
			throw new NotFoundException(String.format("Student with name %s not found", name));
		}
		
		studentsByName.remove(name);
		
		return Response.noContent().build();
	}
	
	@PUT
	@Path("students/{name}")
	public Response updateStudent(Student s, @PathParam("name") final String name) {

		Student existingStudent = studentsByName.get(s.getName());
		if(existingStudent == null){
			throw new NotFoundException(String.format("Student with name %s not found", name));
		}
		
		if(!name.equals(s.getName())){
			
			studentsByName.remove(name);
		}
		studentsByName.put(s.getName(), s);
		
		return Response.noContent().build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GET
	@Path("studentss/{name}")
	public Response getStudentWithCacheExpiration(@PathParam("name") final String name) {
		
		
		Student s = studentsByName.get(name);
		if(s == null){
			throw new NotFoundException(String.format("Student with name %s not found",name));
		}
		
		CacheControl cache = new CacheControl();
		cache.setMaxAge(86400);
		cache.setPrivate(true);
		
		return Response.ok(s).cacheControl(cache).build();
	}
	
	@GET
	@Path("studentsss/{name}")
	public Response getStudentWithCacheValidation(@PathParam("name") final String name,	@Context Request request) {
		
		
		Student s = studentsByName.get(name);
		if(s == null){
			throw new NotFoundException(String.format("Student with name %s not found",name));
		}
		
		CacheControl cache = new CacheControl();
		cache.setMaxAge(86400);
		cache.setPrivate(true);
		
		EntityTag etag = new EntityTag(Integer.toString(s.hashCode()));
		ResponseBuilder rb = request.evaluatePreconditions(etag);
		if(rb == null){
			rb = Response.ok(s);
			rb.tag(etag);
	    }
		rb.cacheControl(cache);
	    return rb.build();
	}
}
