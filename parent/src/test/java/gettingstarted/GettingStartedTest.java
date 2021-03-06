package gettingstarted;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;

import child.File;
import child.FileContentStore;
import child.FileRepository;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jSpringRunner;
import com.jayway.restassured.RestAssured;

@RunWith(Ginkgo4jSpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GettingStartedTest {

	@Autowired private FileRepository fileRepo;
	@Autowired private FileContentStore fileContentStore;
	
    @Value("${local.server.port}") private int port;

    private File file;
    
    {
        Describe("File Tests", () -> {
        	BeforeEach(() -> {
        		RestAssured.port = port;
        	});
        	
        	Context("Given a File Entity", () -> {
        		BeforeEach(() -> {
            		File f = new File();
            		f.setName("child-file");
            		f.setMimeType("text/plain");
            		f.setSummary("child file summary");
            		file = fileRepo.save(f);
        		});
        		
        		It("should be able to associate content with the Entity", () -> {
        	    	given()
        	    		.multiPart("file", "file", new ByteArrayInputStream("This is plain text content!".getBytes()), "text/plain")
        		    .when()
        		        .put("/files/" + file.getId())
        		    .then()
        		    	.statusCode(HttpStatus.SC_OK);
                	    	
        	    	file = fileRepo.findOne(file.getId());
        	    	assertThat(IOUtils.toString(fileContentStore.getContent(file)), is("This is plain text content!"));
        		});
        		
        		Context("with existing content", () -> {
        			BeforeEach(() -> {
        				fileContentStore.setContent(file, new ByteArrayInputStream("Existing content".getBytes()));
        				fileRepo.save(file);
        			});
        			
        			It("should return the content", () -> {
        		    	given()
        		    		.header("accept", "text/plain")
        		    	.when()
        	    			.get("files/" + file.getId())
        	    		.then()
	        	    		.assertThat()
	        	    			.contentType(Matchers.startsWith("text/plain"))
	        	    			.body(Matchers.equalTo("Existing content"));
        			});
        		});
        	});
        });
    }

    @Test
    public void noop() {}
}
