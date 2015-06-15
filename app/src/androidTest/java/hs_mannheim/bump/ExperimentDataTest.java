package hs_mannheim.bump;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import hs_mannheim.bump.Application.Sample;
import hs_mannheim.bump.Application.Threshold;
import hs_mannheim.bump.Experiments.ExperimentData;
import hs_mannheim.bump.Experiments.ExperimentType;


/**
 * Created by benjamin on 14/05/15.
 */
public class ExperimentDataTest extends TestCase {

    @SmallTest
    /**
     * A class is equal to itself.
     */
    public void testEqual_ToSelf() {

        List<Sample> samples = new ArrayList<Sample>();
        samples.add(new Sample(1, 1, 1));

        ExperimentData data = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);

        assertTrue("Class equal to itself.", data.equals(data));
    }

    /**
     * equals(WrongType) must return false;
     */
    @SmallTest
    public void testPassIncompatibleType_isFalse() {

        List<Sample> samples = new ArrayList<Sample>();
        samples.add(new Sample(1, 1, 1));

        ExperimentData data = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);

        assertFalse("Passing incompatible object to equals should return false", data.equals("string"));
    }

    /**
     * equals(null) must return false;
     */
    @SmallTest
    public void testNullReference_isFalse() {

        List<Sample> samples = new ArrayList<Sample>();
        samples.add(new Sample(1, 1, 1));

        ExperimentData data = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);

        assertFalse("Passing null to equals should return false", data.equals(null));
    }

    /**
     * 1. x, x.equals(x) must return true.
     * 2. x and y, x.equals(y) must return true if and only if y.equals(x) returns true.
     */
    @SmallTest
    public void testEquals_isReflexive_isSymmetric() {

        List<Sample> samples = new ArrayList<Sample>();
        samples.add(new Sample(1, 1, 1));

        ExperimentData dataX = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);
        ExperimentData dataY = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);

        assertTrue("Reflexive test fail x,y", dataX.equals(dataY));
        assertTrue("Symmetric test fail y", dataY.equals(dataX));

    }

    /**
     * 1. x.equals(y) returns true
     * 2. y.equals(z) returns true
     * 3. x.equals(z) must return true
     */
    @SmallTest
    public void testEquals_isTransitive() {

        List<Sample> samples = new ArrayList<Sample>();
        samples.add(new Sample(1, 1, 1));

        ExperimentData dataX = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);
        ExperimentData dataY = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);
        ExperimentData dataZ = new ExperimentData(1,new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);

        assertTrue("Transitive test fails x,y", dataX.equals(dataY));
        assertTrue("Transitive test fails y,z", dataY.equals(dataZ));
        assertTrue("Transitive test fails x,z", dataX.equals(dataZ));
    }

    /**
     * Repeated calls to equals consistently return true or false.
     */
    @SmallTest
    public void testEquals_isConsistent() {

        List<Sample> samples = new ArrayList<Sample>();
        samples.add(new Sample(1, 1, 1));

        ExperimentData dataX = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);
        ExperimentData dataY = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);
        ExperimentData dataNotX = new ExperimentData(1, new Timestamp(234), Threshold.HIGH, ExperimentType.FIRST, "name", samples);

        assertTrue("Consistent test fail x,y", dataX.equals(dataY));
        assertTrue("Consistent test fail x,y", dataX.equals(dataY));
        assertFalse(dataNotX.equals(dataX));
        assertFalse(dataNotX.equals(dataX));

    }

    /**
     * Repeated calls to hashcode should consistently return the same integer.
     */
    @SmallTest
    public void testHashcode_isConsistent() {

        List<Sample> samples = new ArrayList<Sample>();
        samples.add(new Sample(1, 1, 1));

        ExperimentData dataX = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);
        int initial_hashcode = dataX.hashCode();

        assertEquals("Consistent hashcode test fails", initial_hashcode, dataX.hashCode());
        assertEquals("Consistent hashcode test fails", initial_hashcode, dataX.hashCode());
    }

    /**
     * Objects that are equal using the equals method should return the same integer.
     */
    @SmallTest
    public void testHashcode_twoEqualsObjects_produceSameNumber() {

        List<Sample> samples = new ArrayList<Sample>();
        samples.add(new Sample(1, 1, 1));

        ExperimentData dataX = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);
        ExperimentData dataY = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);

        int xhashcode = dataX.hashCode();
        int yhashcode = dataY.hashCode();

        assertEquals("Equal object, return equal hashcode test fails", xhashcode, yhashcode);
    }

    /**
     * A more optimal implementation of hashcode ensures
     * that if the objects are unequal different integers are produced.
     */
    @SmallTest
    public void testHashcode_twoUnEqualObjects_produceDifferentNumber() {

        List<Sample> samples = new ArrayList<Sample>();
        samples.add(new Sample(1, 1, 1));

        ExperimentData dataX = new ExperimentData(1, new Timestamp(123), Threshold.MEDIUM, ExperimentType.FIRST, "name", samples);
        ExperimentData dataNotX = new ExperimentData(1, new Timestamp(234), Threshold.HIGH, ExperimentType.FIRST, "name", samples);

        int xhashcode = dataX.hashCode();
        int notxHashcode = dataNotX.hashCode();

        assertTrue("Equal object, return unequal hashcode test fails", !(xhashcode == notxHashcode));
    }

}
