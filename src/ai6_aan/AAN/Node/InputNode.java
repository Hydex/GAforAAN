/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai6_aan.AAN.Node;

/**
 *
 * @author david
 */
public class InputNode extends Node {

    private double outputValue;

    public InputNode() {
        outputValue = 0.0;
    }

    @Override
    public double getOutputValue() {
        return outputValue;
    }

    @Override
    public void setOutputValue(double outputValue) {
        if (this.outputValue == 0.0) {
            this.outputValue = outputValue;
        }
    }

    @Override
    public void resetOutputValue() {
        // do nothing
    }

}
