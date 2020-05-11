import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.function.Consumer;

class GuessTest {

	Client a;
	private final Consumer<Serializable> callBack;

	GuessTest(Consumer<Serializable> callBack) {
		this.callBack = callBack;
	}

	@BeforeEach
	void init() throws UnknownHostException {
		a = new Client(callBack, 5555, "127.0.0.1");
	}

	@Test
	void testObject() {
		//clientConnection.myPlayerInfo.playingStatesCategory = true
		assertNotNull(a, "No object was created on the server");
	}
}