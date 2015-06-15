package hs_mannheim.bump;

import java.sql.Timestamp;
import java.util.List;

import hs_mannheim.bump.Sample;
import hs_mannheim.bump.Threshold;

/**
 * Created by benjamin on 13/05/15.
 */
public class ExperimentData {

    private Integer id;
    private Timestamp timestamp;
    private ExperimentType experimentType;
    private Threshold threshold;
    private String name;
    private List<Sample> samples;

    public ExperimentData() {

    }

    public ExperimentData(Integer id, Timestamp timestamp, Threshold threshold, ExperimentType experimentType, String name, List<Sample> samples) {
        this.id = id;
        this.timestamp = timestamp;
        this.threshold = threshold;
        this.experimentType = experimentType;
        this.samples = samples;
        this.name = name;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getID() {
        return this.id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public Threshold getThreshold() {
        return this.threshold;
    }

    public void setThreshold(Threshold threshold) {
        this.threshold = threshold;
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    public ExperimentType getExperimentType() {
        return experimentType;
    }

    public void setExperimentType(ExperimentType experimentType) {
        this.experimentType = experimentType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other){
        if ( this == other ) return true;
        if ( !(other instanceof ExperimentData) ) return false;
        ExperimentData otherData = (ExperimentData)other;
        return
                this.id == id && this.experimentType == otherData.experimentType && this.threshold == otherData.threshold && this.samples.equals(otherData.samples) && this.name.equals(otherData.name);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + this.id.hashCode();
        hash = 31 * hash + this.experimentType.hashCode();
        hash = 31 * hash + this.threshold.hashCode();
        hash = 31 * hash + this.name.hashCode();
        for( Sample sample : this.samples )
        {
            hash = 31 * hash + sample.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        String samples = "";
        for (Sample sample: this.samples) {
            samples += "\n" + sample.toString();
        }
        return "ID: " + id +  ", TYPE: " + this.experimentType + ", FORCE: " + this.threshold + ", NAME: " + this.name + ", SAMPLES: [" + samples + "])";
    }
}
