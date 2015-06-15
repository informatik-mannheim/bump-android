package hs_mannheim.bump;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;
import hs_mannheim.bump.Application.Delta;
import hs_mannheim.bump.Application.Sample;

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

    public void testConstructorWithTimestamp() {
        // Arrange
        double x = 0.0;
        double y = -20.0;
        double z = Double.MAX_VALUE;
        long time = 123;

        // SUT
        Sample sample = new Sample(x, y, z, 123);

        // Assert
        assertEquals(sample.x, x);
        assertEquals(sample.y, y);
        assertEquals(sample.z, z);
        assertEquals(sample.timestamp, time);
    }


    public void testOtherConstructorWithTimestamp() {
        // Arrange
        float[] floats = {-10f, 0f, Float.MAX_VALUE};
        long time = 123;
        // SUT
        Sample sample = new Sample(floats, 123);

        // Assert
        assertEquals(sample.x, new Double(floats[0]));
        assertEquals(sample.y, new Double(floats[1]));
        assertEquals(sample.z, new Double(floats[2]));
        assertEquals(sample.timestamp, time);
    }

    public void testOtherConstructorWithLessThanThreeValues() {
        // Arrange
        float[] floats = {};

        // Assert
        try {
            new Sample(floats, 123);
        } catch (Exception e) {
            assertEquals(e.getClass(), IllegalArgumentException.class);
        }
    }

    public void testOtherConstructorWithMoreThanThreeValues() {
        // Arrange
        float[] floats = {0f, 1f, 2f, 3f};

        // Assert
        try {
            new Sample(floats, 123);
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

    @SmallTest
    /**
     * A class is equal to itself.
     */
    public void testEqual_ToSelf() {

        Sample sample = new Sample(1,1,1);

        assertTrue("Class equal to itself.", sample.equals(sample));
    }

    /**
     * equals(WrongType) must return false;
     *
     */
    @SmallTest
    public void testPassIncompatibleType_isFalse() {

        Sample sample = new Sample(1,1,1);

        assertFalse("Passing incompatible object to equals should return false", sample.equals("string"));
    }

    /**
     * equals(null) must return false;
     *
     */
    @SmallTest
    public void testNullReference_isFalse() {

        Sample sample = new Sample(1,1,1);

        assertFalse("Passing null to equals should return false", sample.equals(null));
    }

    /**
     * 1. x, x.equals(x) must return true.
     * 2. x and y, x.equals(y) must return true if and only if y.equals(x) returns true.
     */
    @SmallTest
    public void testEquals_isReflexive_isSymmetric() {

        Sample sampleX = new Sample(1,1,1);
        Sample sampleY = new Sample(1,1,1);


        assertTrue("Reflexive test fail x,y", sampleX.equals(sampleY));
        assertTrue("Symmetric test fail y", sampleY.equals(sampleX));

    }

    /**
     * 1. x.equals(y) returns true
     * 2. y.equals(z) returns true
     * 3. x.equals(z) must return true
     */
    @SmallTest
    public void testEquals_isTransitive() {

        Sample sampleX = new Sample(1,1,1);
        Sample sampleY = new Sample(1,1,1);
        Sample sampleZ = new Sample(1,1,1);

        assertTrue("Transitive test fails x,y", sampleX.equals(sampleY));
        assertTrue("Transitive test fails y,z", sampleY.equals(sampleZ));
        assertTrue("Transitive test fails x,z", sampleX.equals(sampleZ));
    }

    /**
     * Repeated calls to equals consistently return true or false.
     */
    @SmallTest
    public void testEquals_isConsistent() {

        Sample sampleX = new Sample(1,1,1);
        Sample sampleY = new Sample(1,1,1);
        Sample sampleNotX = new Sample(0,0,0);

        assertTrue("Consistent test fail x,y", sampleX.equals(sampleY));
        assertTrue("Consistent test fail x,y", sampleX.equals(sampleY));
        assertFalse(sampleNotX.equals(sampleX));
        assertFalse(sampleNotX.equals(sampleX));

    }

    /**
     * Repeated calls to hashcode should consistently return the same integer.
     */
    @SmallTest
    public void testHashcode_isConsistent() {

        Sample sampleX = new Sample(1,1,1);

        int initial_hashcode = sampleX.hashCode();

        assertEquals("Consistent hashcode test fails", initial_hashcode, sampleX.hashCode());
        assertEquals("Consistent hashcode test fails", initial_hashcode, sampleX.hashCode());
    }

    /**
     * Objects that are equal using the equals method should return the same integer.
     */
    @SmallTest
    public void testHashcode_twoEqualsObjects_produceSameNumber() {

        Sample sampleX = new Sample(1,1,1);
        Sample sampleY = new Sample(1,1,1);

        int xhashcode = sampleX.hashCode();
        int yhashcode = sampleY.hashCode();

        assertEquals("Equal object, return equal hashcode test fails", xhashcode, yhashcode);
    }

    /**
     * A more optimal implementation of hashcode ensures
     * that if the objects are unequal different integers are produced.
     *
     */
    @SmallTest
    public void testHashcode_twoUnEqualObjects_produceDifferentNumber() {

        Sample sampleX = new Sample(1,1,1);
        Sample sampleNotX = new Sample(0,0,0);

        int xhashcode = sampleX.hashCode();
        int notxHashcode = sampleNotX.hashCode();

        assertTrue("Equal object, return unequal hashcode test fails", !(xhashcode == notxHashcode));
    }
}


