package org.HospitalSystem.Classes;

public final class Room {
    public int id;
    public String type;
    public int occupant_id;
    public int price;
    public Room(int id, String type, int price){
        this.id = id;
        this.type = type;
        this.price = price;
        this.occupant_id = 0;
    }
    public Room emptyRoom() {
        this.occupant_id = 0;
        return this;
    }
    public Room setOccupant(int occupant_id) {
        this.occupant_id = occupant_id;
        return this;
    }
    public Room setOccupant(Patient occupant) {
        this.occupant_id = occupant.id;
        return this;
    }
}
