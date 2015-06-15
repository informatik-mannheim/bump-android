package hs_mannheim.bump;

import java.util.List;

public interface BumpEventListener {
    void onBump(List<Sample> samples);
}
