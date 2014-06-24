import com.hyvemynd.webserver.FileManager;
import com.hyvemynd.webserver.http.HttpResponse;
import com.hyvemynd.webserver.http.HttpStatusCode;
import com.hyvemynd.webserver.http.MediaType;
import com.hyvemynd.webserver.logging.LoggingManager;
import org.apache.log4j.Level;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by andresmonroy on 6/24/14.
 */
public class HttpResponseTest {

	@BeforeClass
	public static void init(){
		LoggingManager.initConsoleLogging(Level.DEBUG);
	}

	@Test
	public void test200(){
		HttpResponse res = HttpResponse.get200(new FileManager("/wwwroot", "main.html"));
		Assert.assertEquals(HttpStatusCode.OK, res.getStatus());
		Assert.assertNotEquals(0, res.getHeader("Content-length"));
		Assert.assertEquals(MediaType.HTML, res.getHeader("Content-type"));
	}

	@Test
	public void test400(){
		HttpResponse res = HttpResponse.get400();
		Assert.assertEquals(HttpStatusCode.BAD_REQUEST, res.getStatus());
	}

	@Test
	public void test404(){
		HttpResponse res = HttpResponse.get404();
		Assert.assertEquals(HttpStatusCode.NOT_FOUND, res.getStatus());
	}

	@Test
	public void test501(){
		HttpResponse res = HttpResponse.get501();
		Assert.assertEquals(HttpStatusCode.NOT_IMPLEMENTED, res.getStatus());
	}
}
