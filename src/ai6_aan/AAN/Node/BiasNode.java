package ai6_aan.AAN.Node;

/**
 *
 * @author david
 */
public class BiasNode extends Node{
    private final static double outputValue = 1;

    @Override
    public double getOutputValue() {
        return outputValue;
    }

    @Override
    public void setOutputValue(double outputValue) {
        // Do nothing
    }

    @Override
    public void resetOutputValue() {
        // Do nothing
    }
    
    
}
