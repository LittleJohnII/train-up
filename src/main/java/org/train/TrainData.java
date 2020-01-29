package org.train;


public class TrainData {
    private String trainID;
    private int timestamp;
    private int data;

    public String getTrainID() {
        return trainID;
    }

    public TrainData setTrainID(String trainID) {
        this.trainID = trainID;
        return this;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public TrainData setTimestamp(int timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public int getData() {
        return data;
    }

    public TrainData setData(int data) {
        this.data = data;
        return this;
    }
}
