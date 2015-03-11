package hs_mannheim.bump;

import junit.framework.TestCase;

public class SampleTest extends TestCase {
    public void testConstructor() {
        // Arrange
        double x = 0.0;
        double y = -20.0;
        double z = Double.MAX_VALUE;

        // SUT
        Sample sample = new Sample(x, y, z);

        // Assert
        assertEquals(sample.x, x);
        assertEquals(sample.y, y);
        assertEquals(sample.z, z);
    }

    public void testOtherConstructor() {
        // Arrange
        float[] floats = {-10f, 0f, Float.MAX_VALUE};
        double adjustmentFactor = 0.4;

        // SUT
        Sample sample = new Sample(floats, adjustmentFactor);

        // Assert
        assertEquals(sample.x, floats[0] * adjustmentFactor);
        assertEquals(sample.y, floats[1] * adjustmentFactor);
        assertEquals(sample.z, floats[2] * adjustmentFactor);
    }

    public void testOtherConstructorWithLessThanThreeValues() {
        // Arrange
        float[] floats = {};
        double adjustmentFactor = 0.4;

        // Assert
        try {
            new Sample(floats, adjustmentFactor);
        } catch (Exception e) {
            assertEquals(e.getClass(), IllegalArgumentException.class);
        }
    }

    public void testOtherConstructorWithMoreThanThreeValues() {
        // Arrange
        float[] floats = {0f, 1f, 2f, 3f};
        double adjustmentFactor = 0.4;

        // Assert
        try {
            new Sample(floats, adjustmentFactor);
        } catch (Exception e) {
            assertEquals(e.getClass(), IllegalArgumentException.class);
        }
    }

    public void testCalculateDeltaBetweenSamples() {
        // Arrange / SUT
        Sample a = new Sample(0.0, 1.0, -2.0);
        Sample b = new Sample(1.0, -1.0, -2.0);

        // Act
        Delta delta = a.delta(b);

        // Assert
        assertEquals(delta.x, 1.0);
        assertEquals(delta.y, 2.0);
        assertEquals(delta.z, 4.0);
    }

    public void testCloneSamples() {
        // SUT
        Sample sample = new Sample(10, 20, 30);

        // Act
        Sample clone = sample.clone();

        // Assert
        assertEquals(sample.x, clone.x);
        assertEquals(sample.y, clone.y);
        assertEquals(sample.z, clone.z);

        assertNotSame(sample, clone);
    }
}
