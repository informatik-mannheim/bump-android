package hs_mannheim.bump;


import junit.framework.TestCase;

import java.util.ArrayList;

import hs_mannheim.bump.Application.Peaks;
import hs_mannheim.bump.Application.Sample;

public class PeaksTest extends TestCase {
    public void testReadPeaks() {
        // Arrange
        ArrayList<Sample> samples = new ArrayList<Sample>();
        samples.add(new Sample(1, 1, 3));
        samples.add(new Sample(2, 5, 3));
        samples.add(new Sample(1, 6, 3));
        samples.add(new Sample(3, 5, 3));
        samples.add(new Sample(4, 1, 3));
        samples.add(new Sample(3, 1, 3));

        // SUT
        Peaks peaks = Peaks.readFrom(samples);

        // Assert
        assertEquals(peaks.x, 3);
        assertEquals(peaks.y, 1);
        assertEquals(peaks.z, 0);
    }

    public void testBetweenPeaks() {
        assertTrue(new Peaks(11, 15, 20).between(1, 100));
        assertTrue(new Peaks(11, 15, 20).between(11, 20));
        assertFalse(new Peaks(11, 15, 20).between(12, 20));
        assertFalse(new Peaks(11, 15, 20).between(11, 19));
    }
}
