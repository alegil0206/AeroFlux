package com.aeroflux.simulator.model.drone;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.ArrayList;

import com.aeroflux.drone.domain.component.Radio;
import com.aeroflux.drone.domain.dto.RadioMessageDTO;
import com.aeroflux.drone.DroneSystem;

public class SimulatedRadio implements Radio {

    private static final double MAX_TRANSMISSION_DISTANCE = 500.0; // Distanza massima di trasmissione in metri
    private static final List<PendingMessage> pendingMessages = new ArrayList<>(); // buffer globale

    private final List<DroneSystem> drones = new CopyOnWriteArrayList<>();
    private final List<RadioMessageDTO> messageQueue = new ArrayList<>();
    private DroneSystem sender;

    @Override
    public void sendMessage(RadioMessageDTO message) {
        for (DroneSystem receiver : drones) {
            if (!receiver.getDroneProperties().getId().equals(sender.getDroneProperties().getId()) && isWithinRange(sender, receiver)) {
                pendingMessages.add(new PendingMessage(receiver, message));
            }
        }
    }

    @Override
    public List<RadioMessageDTO> getReceivedMessages() {
        List<RadioMessageDTO> receivedMessage = new ArrayList<>(messageQueue);
        messageQueue.clear(); 
        return receivedMessage;
    }

    public void receiveMessage(RadioMessageDTO message) {
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

    public static void deliverMessages() {
        for (PendingMessage pm : pendingMessages) {
            SimulatedRadio radio = (SimulatedRadio) pm.receiver.getHardwareAbstractionLayer().getRadio();
            radio.receiveMessage(pm.message);
        }
        pendingMessages.clear();
    }

    private static class PendingMessage {
        DroneSystem receiver;
        RadioMessageDTO message;

        public PendingMessage(DroneSystem receiver, RadioMessageDTO message) {
            this.receiver = receiver;
            this.message = message;
        }
    }
}
