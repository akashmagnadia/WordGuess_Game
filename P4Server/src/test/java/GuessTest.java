import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ServerGuessTest {

	Server s;
	ClientSideGameInfo csgi;

	@BeforeEach
	void init(){
		csgi = new ClientSideGameInfo();
	}

	@Test
	void testObjectCreated() {
		assertNotNull(s, "No object was created for the server");
	}

	@Test
	void testPortAssignment() {
		assertEquals(5555, s.server.port,  "Port was not correctly assigned");
	}

	@Test
	void testServerInit() {
		assertEquals("Sever", s.getClass().getName(),  "Wrong class created");
	}

	@Test
	void testCSGIInit() {
		assertEquals("ClientSideGameInfo", s.getClass().getName(),  "Wrong class created");
	}

	@Test
	void testPlayingAnimalCatg() {
		assertFalse(true, "Animals category is wrongly on");
	}

	@Test
	void testPlayingFoodsCatg() {
		assertFalse(true, "Foods category is wrongly on");
	}

	@Test
	void testPlayingStatesCatg() {
		assertFalse(true, "States category is wrongly on");
	}

	@Test
	void testSolvedAnimalCatg() {
		assertFalse(true, "Animals category is wrongly solved");
	}

	@Test
	void testSolvedFoodCatg() {
		assertFalse(true, "Foods category is wrongly solved");
	}

	@Test
	void testSolvedStatesCatg() {
		assertFalse(true, "States category is wrongly solved");
	}
}