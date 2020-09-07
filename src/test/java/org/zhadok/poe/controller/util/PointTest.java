package org.zhadok.poe.controller.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PointTest {

	private static Stream<Arguments> provideArgumentsForMapSquareToCircle() {
	    return Stream.of(
	    		Arguments.of(0, 0, 0, 0), // middle
	    		Arguments.of(-1, 0, -1, 0), // left
	    		Arguments.of(1, 0, 1, 0), // right
	    		Arguments.of(0, -1, 0, -1), // top
	    		Arguments.of(0, 1, 0, 1), // bottom
	    		Arguments.of(-1, -1, -Math.sqrt(0.5), -Math.sqrt(0.5)), // top left
	    		Arguments.of(1, -1, Math.sqrt(0.5), -Math.sqrt(0.5)), // top right
	    		Arguments.of(1, 1, Math.sqrt(0.5), Math.sqrt(0.5)), // bottom left
	    		Arguments.of(-1, 1, -Math.sqrt(0.5), Math.sqrt(0.5)) // bottom right
	      );
	}
	
	@ParameterizedTest
	@MethodSource("provideArgumentsForMapSquareToCircle")
	void mapSquareToCircle(double xSquare, double ySquare, double xCircleExpected, double yCircleExpected) {
		Point pointOnSquare = new Point(xSquare, ySquare); 
		Point pointOnCircle = pointOnSquare.mapFromCircleToSquare(); 
		assertEquals(xCircleExpected, pointOnCircle.x, 1e-8); 
		assertEquals(yCircleExpected, pointOnCircle.y, 1e-8); 
	}
	
}
