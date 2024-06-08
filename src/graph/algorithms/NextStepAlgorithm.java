package graph.algorithms;

import main.View;

import java.util.List;

public class NextStepAlgorithm extends Algorithm {

    public NextStepAlgorithm(View view) {
        super(view);
    }

    @Override
    protected Void doInBackground() throws Exception {
        if (latch != null)
            latch.countDown();
        view.repaint();
        return null;
    }
}
