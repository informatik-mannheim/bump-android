package hs_mannheim.bump;

import junit.framework.TestCase;

public class DeltaTests extends TestCase {

    public void testExceedsThreshold() {
        Threshold zero = Threshold.ZERO;
        Threshold low = Threshold.LOW;
        Threshold medium = Threshold.MEDIUM;
        Threshold high = Threshold.HIGH;

        assertFalse(new Delta(zero.getX(), zero.getY(), zero.getZ()).exceedsThreshold(zero));
        assertFalse(new Delta(zero.getX()-1, zero.getY()-1, zero.getZ()-1).exceedsThreshold(zero));
        assertTrue(new Delta(zero.getX()+1, zero.getY()+1, zero.getZ()+1).exceedsThreshold(zero));

        assertFalse(new Delta(low.getX(), low.getY(), low.getZ()).exceedsThreshold(low));
        assertFalse(new Delta(low.getX()-1, low.getY()-1, low.getZ()-1).exceedsThreshold(low));
        assertTrue(new Delta(low.getX() + 1, low.getY() + 1, low.getZ() + 1).exceedsThreshold(low));

        assertFalse(new Delta(medium.getX(), medium.getY(), medium.getZ()).exceedsThreshold(Threshold.MEDIUM));
        assertFalse(new Delta(medium.getX()-1, medium.getY()-1, medium.getZ()-1).exceedsThreshold(Threshold.MEDIUM));
        assertTrue(new Delta(medium.getX()+1, medium.getY()+1, medium.getZ()+1).exceedsThreshold(Threshold.MEDIUM));

        assertFalse(new Delta(high.getX(), high.getY(), high.getZ()).exceedsThreshold(Threshold.HIGH));
        assertFalse(new Delta(high.getX()-1, high.getY()-1, high.getZ()-1).exceedsThreshold(Threshold.HIGH));
        assertTrue(new Delta(high.getX()+1, high.getY()+1, high.getZ()+1).exceedsThreshold(Threshold.HIGH));
    }

}
