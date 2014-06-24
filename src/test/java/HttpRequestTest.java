import com.hyvemynd.webserver.http.HttpMethods;
import com.hyvemynd.webserver.http.HttpRequest;
import com.hyvemynd.webserver.logging.LoggingManager;
import org.apache.log4j.Level;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;

/**
 * Created by andresmonroy on 6/24/14.
 */
public class HttpRequestTest {

	@BeforeClass
	public static void init(){
		LoggingManager.initConsoleLogging(Level.DEBUG);
	}

	@Test
	public void parseGetRequest(){
		HttpRequest req = new HttpRequest(new ByteArrayInputStream("GET /home HTTP/1.1\n\n".getBytes()));
		Assert.assertTrue(req.parseRequest());
		Assert.assertEquals(req.getMethod(), HttpMethods.GET);
	}

	@Test
	public void parseHeadRequest(){
		HttpRequest req = new HttpRequest(new ByteArrayInputStream("HEAD /home HTTP/1.1\n\n".getBytes()));
		Assert.assertTrue(req.parseRequest());
		Assert.assertEquals(req.getMethod(), HttpMethods.HEAD);
	}

	@Test
	public void parseURI(){
		String uri = "/asd";
		String line = String.format("GET %s HTTP/1.1\n\n", uri);
		HttpRequest req = new HttpRequest(new ByteArrayInputStream(line.getBytes()));
		Assert.assertTrue(req.parseRequest());
		Assert.assertEquals(uri, req.getUri());
	}

	@Test
	public void parseVersion(){
		String version = "HTTP/1.1";
		String line = String.format("GET /asd %s\n\n", version);
		HttpRequest req = new HttpRequest(new ByteArrayInputStream(line.getBytes()));
		Assert.assertTrue(req.parseRequest());
		Assert.assertEquals(version, req.getVersion());
	}

	@Test
	public void catchBadVersion(){
		String version = "asd";
		String line = String.format("GET /asd %s\n\n", version);
		HttpRequest req = new HttpRequest(new ByteArrayInputStream(line.getBytes()));
		Assert.assertFalse(req.parseRequest());
	}

	@Test
	public void catchBadRequest(){
		HttpRequest req = new HttpRequest(new ByteArrayInputStream("get /asd http/1.1 asd\n\n".getBytes()));
		Assert.assertFalse(req.parseRequest());
	}
}
