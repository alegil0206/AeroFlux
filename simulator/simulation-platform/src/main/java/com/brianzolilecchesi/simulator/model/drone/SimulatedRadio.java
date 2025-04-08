package com.brianzolilecchesi.simulator.model.drone;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.ArrayList;

import com.brianzolilecchesi.drone.domain.component.Radio;
import com.brianzolilecchesi.drone.DroneSystem;

public class SimulatedRadio implements Radio {

    private static final double MAX_TRANSMISSION_DISTANCE = 500.0; // Distanza massima di trasmissione in metri
    private final List<DroneSystem> drones = new CopyOnWriteArrayList<>();
    private final List<String> messageQueue = new ArrayList<>();
    private DroneSystem sender;

    @Override
    public void sendMessage(String message) {
        for (DroneSystem receiver : drones) {
            if (!receiver.getDroneProperties().getId().equals(sender.getDroneProperties().getId()) && isWithinRange(sender, receiver)) {
                ((SimulatedRadio) receiver.getHardwareAbstractionLayer().getRadio()).receiveMessage(message);
            }
        }
    }

    @Override
    public List<String> getReceivedMessages() {
        List<String> receivedMessage = new ArrayList<>(messageQueue);
        messageQueue.clear(); 
        return receivedMessage;
    }

    public void receiveMessage(String message) {
        messageQueue.add(message);
    }

    private boolean isWithinRange(DroneSystem sender, DroneSystem receiver) {
        double distance = sender.getHardwareAbstractionLayer().getGps().getCoordinates().distance(receiver.getHardwareAbstractionLayer().getGps().getCoordinates());
        return distance <= MAX_TRANSMISSION_DISTANCE;
    }

    public void setAvailableDrone(List<DroneSystem> drones) {
        this.drones.addAll(drones);
    }

    public void setSender(DroneSystem sender) {
        this.sender = sender;
    }
}
