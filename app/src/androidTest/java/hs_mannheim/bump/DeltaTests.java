package hs_mannheim.bump;

import junit.framework.TestCase;

public class DeltaTests extends TestCase {
    public void testExceedsThreshold() {
        assertFalse(new Delta(0.0, 0.0, 0.0).exceedsThreshold());
        assertFalse(new Delta(-10.0, -20.0, -100.0).exceedsThreshold());
        assertFalse(new Delta(0.3, 0.5, 0.1).exceedsThreshold());
        assertTrue(new Delta(0.301, 0.51, 0.1000001).exceedsThreshold());
    }
}
