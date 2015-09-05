package geekycamp.jaxrs;

import javax.ws.rs.NotFoundException;

import org.junit.Test;

public class SchoolResourceTest {
	
	@Test(expected=NotFoundException.class)
	public void test_getStudent_non_Existent(){
		
		//given
		SchoolResource r = new SchoolResource();
		
		//when
		r.getStudent("Ivan");
	}
}
